package com.cjburkey.cubegame.world;

import java.util.HashMap;
import java.util.Map;
import org.joml.Vector3i;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.BlockState;
import com.cjburkey.cubegame.chunk.Chunk;

public final class World {
	
	public static final int BLOCKS_PER_CHUNK = 1 << 4;
	public static final IWorldGenerator generator = new WorldGeneratorDefault();

	private final Map<BlockPos, Chunk> generatedChunks = new HashMap<>();
	
	public Chunk getOrGenerateChunk(BlockPos chunkPos) {
		if (generatedChunks.containsKey(chunkPos)) {
			return generatedChunks.get(chunkPos);
		}
		Chunk chunk = new Chunk(this, chunkPos);
		generator.generateChunk(chunk);
		generatedChunks.put(chunkPos, chunk);
		return chunk;
	}
	
	public BlockState getBlock(BlockPos pos) {
		return getOrGenerateChunk(getChunkFromBlock(pos)).getBlockState(getBlockPositionInChunk(pos));
	}
	
	public boolean getIsTransparentAt(BlockPos pos) {
		return getOrGenerateChunk(getChunkFromBlock(pos)).getIsTransparentAt(getBlockPositionInChunk(pos));
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
		return block.sub(getBlockFromChunk(block));
	}
	
}