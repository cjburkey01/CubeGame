package com.cjburkey.cubegame.world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import org.joml.Vector3i;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.BlockState;
import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventSystem;
import com.cjburkey.cubegame.event.game.EventGameExit;
import com.cjburkey.cubegame.event.game.EventGameInit;
import com.cjburkey.cubegame.event.game.EventGameUpdate;
import com.cjburkey.cubegame.mesh.MeshVoxel;
import com.cjburkey.cubegame.object.GameObject;
import com.cjburkey.cubegame.object.MeshFilter;
import com.cjburkey.cubegame.object.Scene;
import com.cjburkey.cubegame.thread.ThreadedPoolWorker;

public final class World {
	
	public static final int BLOCKS_PER_CHUNK = 1 << 4;
	
	private final ThreadedPoolWorker<PoolTaskChunkGen> generationPool = new ThreadedPoolWorker<>("ChunkGen", 4);
	private final ThreadedPoolWorker<PoolTaskGenChunkMesh> meshingPool = new ThreadedPoolWorker<>("ChunkMesh", 4);
	private final Map<BlockPos, Chunk> generatedChunks = new HashMap<>();
	private final Map<BlockPos, RenderedChunk> renderedChunks = new HashMap<>();
	private final LinkedBlockingDeque<BlockPos> chunksToMesh = new LinkedBlockingDeque<>();
	private final LinkedBlockingDeque<PoolTaskGenChunkMesh> chunksDoneMeshing = new LinkedBlockingDeque<>();
	public final IChunkGenerator generator;
	
	public World(IChunkGenerator generator) {
		EventSystem.MAIN_HANDLER.registerHandler(this);
		this.generator = generator;
	}
	
	@EventHandler
	private void init(EventGameInit e) {
		generationPool.start();
		meshingPool.start();
	}
	
	@EventHandler
	private void exit(EventGameExit e) {
		generationPool.stop();
		meshingPool.stop();
	}
	
	@EventHandler
	private void update(EventGameUpdate e) {
		beginMeshingQueuedGeneratedChunks();
		spawnQueuedGeneratedChunks();
	}
	
	private void beginMeshingQueuedGeneratedChunks() {
		Set<BlockPos> chunksToTryMeshNext = new HashSet<>();
		while(chunksToMesh.size() > 0) {
			BlockPos chunk = chunksToMesh.poll();
			if (chunk != null) {
				if (hasRenderedChunk(chunk)) {
					continue;
				}
				boolean tryAgain = false;
				Chunk at = getChunkOrNull(chunk);
				if (at == null) {
					getChunkOrCreateAndScheduleGenerate(chunk);
					tryAgain = true;
				}
				if (tryAgain || !at.getGenerated()) {
					chunksToTryMeshNext.add(chunk);
					continue;
				}
				if (!at.getEmpty()) {
					meshingPool.addTask(new PoolTaskGenChunkMesh(getChunkOrNull(chunk)));
				}
			}
		}
		for (BlockPos pos : chunksToTryMeshNext) {
			chunksToMesh.offer(pos);
		}
	}
	
	private void spawnQueuedGeneratedChunks() {
		while (chunksDoneMeshing.size() > 0) {
			PoolTaskGenChunkMesh task = chunksDoneMeshing.poll();
			if (task != null) {
				if (hasRenderedChunk(task.getChunk().chunkPos)) {
					continue;
				}
				
				// Create and upload the mesh
				MeshVoxel mesh = new MeshVoxel();
				mesh.setMesh(task.getMesh());
				
				// Add it to the chunk object in the world
				GameObject obj = Scene.createObject();
				MeshFilter meshFilter = obj.addComponent(new MeshFilter());
				meshFilter.setMesh(mesh);
				RenderedChunk rchunk = obj.addComponent(new RenderedChunk(task.getChunk()));
				renderedChunks.put(task.getChunk().chunkPos, rchunk);
			}
		}
	}
	
	public boolean hasRenderedChunk(BlockPos chunkPos) {
		return renderedChunks.containsKey(chunkPos);
	}
	
	public RenderedChunk getRenderedChunk(BlockPos chunkPos) {
		if (!hasRenderedChunk(chunkPos)) {
			return null;
		}
		return renderedChunks.get(chunkPos);
	}
	
	public Chunk getChunkOrCreateAndScheduleGenerate(BlockPos chunkPos) {
		return getChunkOrCreateAndScheduleGenerate(chunkPos, () -> {  });
	}
	
	public Chunk getChunkOrCreateAndScheduleGenerate(BlockPos chunkPos, Runnable ifGeneratedOnDone) {
		if (generatedChunks.containsKey(chunkPos)) {
			return generatedChunks.get(chunkPos);
		}
		Chunk chunk = new Chunk(this, chunkPos);
		scheduleChunkGenerate(chunk, ifGeneratedOnDone);
		generatedChunks.put(chunkPos, chunk);
		return chunk;
	}
	
	public Chunk getChunkOrNull(BlockPos chunkPos) {
		if (generatedChunks.containsKey(chunkPos)) {
			return generatedChunks.get(chunkPos);
		}
		return null;
	}
	
	public BlockState getBlock(BlockPos pos) {
		Chunk chunk = getChunkOrNull(getChunkFromBlock(pos));
		if (chunk == null) {
			return null;
		}
		return chunk.getBlockState(getBlockPositionInChunk(pos));
	}
	
	public boolean getIsTransparentAt(BlockPos pos) {
		Chunk chunk = getChunkOrNull(getChunkFromBlock(pos));
		if (chunk == null) {
			return false;
		}
		return chunk.getIsTransparentAt(getBlockPositionInChunk(pos));
	}
	
	public void scheduleChunkGenerate(Chunk chunk, Runnable onDone) {
		if (chunk.getGenerating()) {
			return;
		}
		if (!generatedChunks.containsKey(chunk.chunkPos)) {
			generatedChunks.put(chunk.chunkPos, chunk);
		}
		chunk.markGenerating();
		generationPool.addTask(new PoolTaskChunkGen(generator, chunk, onDone));
	}
	
	public void scheduleChunkMeshing(BlockPos chunk) {
		chunksToMesh.add(chunk);
	}
	
	public void onChunkGenerated(Chunk chunk, Runnable onDone) {
		onDone.run();
	}
	
	public void onFinishMesh(PoolTaskGenChunkMesh task) {
		chunksDoneMeshing.add(task);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chunksDoneMeshing == null) ? 0 : chunksDoneMeshing.hashCode());
		result = prime * result + ((chunksToMesh == null) ? 0 : chunksToMesh.hashCode());
		result = prime * result + ((generatedChunks == null) ? 0 : generatedChunks.hashCode());
		result = prime * result + ((generationPool == null) ? 0 : generationPool.hashCode());
		result = prime * result + ((meshingPool == null) ? 0 : meshingPool.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		World other = (World) obj;
		if (chunksDoneMeshing == null) {
			if (other.chunksDoneMeshing != null)
				return false;
		} else if (!chunksDoneMeshing.equals(other.chunksDoneMeshing))
			return false;
		if (chunksToMesh == null) {
			if (other.chunksToMesh != null)
				return false;
		} else if (!chunksToMesh.equals(other.chunksToMesh))
			return false;
		if (generatedChunks == null) {
			if (other.generatedChunks != null)
				return false;
		} else if (!generatedChunks.equals(other.generatedChunks))
			return false;
		if (generationPool == null) {
			if (other.generationPool != null)
				return false;
		} else if (!generationPool.equals(other.generationPool))
			return false;
		if (meshingPool == null) {
			if (other.meshingPool != null)
				return false;
		} else if (!meshingPool.equals(other.meshingPool))
			return false;
		return true;
	}
	
	// Gets the chunk that contains the provided block
	public static BlockPos getChunkFromBlock(BlockPos block) {
		Vector3i tmp = block.getPos();
		tmp.x >>= 4;
		tmp.y >>= 4;
		tmp.z >>= 4;
		return new BlockPos(tmp);
	}
	
	// Gets the "world" position of the provided chunk
	public static BlockPos getBlockFromChunk(BlockPos chunk) {
		Vector3i tmp = chunk.getPos();
		tmp.x <<= 4;
		tmp.y <<= 4;
		tmp.z <<= 4;
		return new BlockPos(tmp);
	}
	
	// Finds the position of the provided block in a chunk
	public static BlockPos getBlockPositionInChunk(BlockPos block) {
		return block.sub(getBlockFromChunk(getChunkFromBlock(block)));
	}
	
}