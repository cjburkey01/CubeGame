package com.cjburkey.cubegame.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Vector3i;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.BlockState;
import com.cjburkey.cubegame.block.Direction;
import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventSystem;
import com.cjburkey.cubegame.event.game.EventGameExit;
import com.cjburkey.cubegame.event.game.EventGameInit;
import com.cjburkey.cubegame.event.game.EventGameUpdate;
import com.cjburkey.cubegame.mesh.MeshVoxel;
import com.cjburkey.cubegame.object.MeshFilter;
import com.cjburkey.cubegame.object.Scene;
import com.cjburkey.cubegame.thread.ThreadedPoolWorker;

public final class World {
	
	public static final int BLOCKS_PER_CHUNK = 1 << 4;
	public static final IWorldGenerator generator = new WorldGeneratorDefault();
	private final ThreadedPoolWorker<PoolTaskChunkGen> generationPool = new ThreadedPoolWorker<>("ChunkGen", 128);
	private final ThreadedPoolWorker<PoolTaskGenChunkMesh> meshingPool = new ThreadedPoolWorker<>("ChunkMesh", 4);
	
	private final Map<BlockPos, Chunk> generatedChunks = new HashMap<>();
	private final Queue<BlockPos> chunksToMesh = new ConcurrentLinkedQueue<>();
	private final Queue<PoolTaskGenChunkMesh> chunksDoneMeshing = new ConcurrentLinkedQueue<>();
	
	public World() {
		EventSystem.MAIN_HANDLER.registerHandler(this);
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
		checkChunkMeshingProcess();
		checkChunkGenerationProcess();
	}
	
	private void checkChunkMeshingProcess() {
		Queue<BlockPos> chunksToTryMeshNext = new ConcurrentLinkedQueue<>();
		while(chunksToMesh.size() > 0) {
			BlockPos chunk = chunksToMesh.poll();
			if (chunk != null) {
				boolean tryAgain = false;
				Chunk at = getChunkOrNull(chunk);
				Chunk atN = getChunkOrNull(Direction.NORTH.add(chunk));
				Chunk atS = getChunkOrNull(Direction.SOUTH.add(chunk));
				Chunk atE = getChunkOrNull(Direction.EAST.add(chunk));
				Chunk atW = getChunkOrNull(Direction.WEST.add(chunk));
				Chunk atU = getChunkOrNull(Direction.UP.add(chunk));
				Chunk atD = getChunkOrNull(Direction.DOWN.add(chunk));
				if (at == null) {
					getChunkOrCreateScheduleGenerate(chunk, false);
					tryAgain = true;
				}
				if (atN == null) {
					getChunkOrCreateScheduleGenerate(Direction.NORTH.add(chunk), false);
					tryAgain = true;
				}
				if (atS == null) {
					getChunkOrCreateScheduleGenerate(Direction.SOUTH.add(chunk), false);
					tryAgain = true;
				}
				if (atE == null) {
					getChunkOrCreateScheduleGenerate(Direction.EAST.add(chunk), false);
					tryAgain = true;
				}
				if (atW == null) {
					getChunkOrCreateScheduleGenerate(Direction.WEST.add(chunk), false);
					tryAgain = true;
				}
				if (atU == null) {
					getChunkOrCreateScheduleGenerate(Direction.UP.add(chunk), false);
					tryAgain = true;
				}
				if (atD == null) {
					getChunkOrCreateScheduleGenerate(Direction.DOWN.add(chunk), false);
					tryAgain = true;
				}
				if (tryAgain || !at.getGenerated() || !atN.getGenerated() || !atS.getGenerated() || !atE.getGenerated() || !atW.getGenerated() || !atU.getGenerated() || !atD.getGenerated()) {
					chunksToTryMeshNext.offer(chunk);
					continue;
				}
				meshingPool.addTask(new PoolTaskGenChunkMesh(getChunkOrNull(chunk)));
			}
		}
		for (BlockPos pos : chunksToTryMeshNext) {
			chunksToMesh.offer(pos);
		}
	}
	
	private void checkChunkGenerationProcess() {
		while (chunksDoneMeshing.size() > 0) {
			PoolTaskGenChunkMesh task = chunksDoneMeshing.poll();
			if (task != null) {
				MeshVoxel mesh = new MeshVoxel();
				mesh.setMesh(task.getMesh());
				MeshFilter meshFilter = Scene.createObject().addComponent(new MeshFilter());
				meshFilter.setMesh(mesh);
			}
		}
	}
	
	public Chunk getChunkOrCreateScheduleGenerate(BlockPos chunkPos, boolean render) {
		if (generatedChunks.containsKey(chunkPos)) {
			return generatedChunks.get(chunkPos);
		}
		Chunk chunk = new Chunk(this, chunkPos);
		//generator.generateChunk(chunk);
		scheduleChunkGenerate(chunk, render, () -> {  });
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
			return true;
		}
		return chunk.getIsTransparentAt(getBlockPositionInChunk(pos));
	}
	
	public void scheduleChunkGenerate(Chunk chunk, boolean render, Runnable onDone) {
		if (chunk.getGenerating()) {
			return;
		}
		if (!generatedChunks.containsKey(chunk.chunkPos)) {
			generatedChunks.put(chunk.chunkPos, chunk);
		}
		chunk.markGenerating();
		generationPool.addTask(new PoolTaskChunkGen(generator, chunk, render, onDone));
	}
	
	public void scheduleChunkMeshing(Chunk chunk) {
		//meshingPool.addTask(new PoolTaskGenChunkMesh(chunk));
		chunksToMesh.add(chunk.chunkPos);
	}
	
	public void onChunkGenerated(Chunk chunk, Runnable onDone, boolean render) {
		onDone.run();
		if (render) {
			scheduleChunkMeshing(chunk);
		}
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