package com.cjburkey.cubegame.world;

import org.joml.Vector3i;
import com.cjburkey.cubegame.SimplexNoise;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.Blocks;
import com.cjburkey.cubegame.chunk.Chunk;

public class WorldGeneratorDefault implements IWorldGenerator {
	
	public static final float frequency = 1.0f / 50.0f;
	public static final float scale = 13.0f;
	public static final float yBase = 10.0f;
	
	public void generateChunk(Chunk chunk) {
		Vector3i chunkAt = World.getBlockFromChunk(chunk.chunkPos).getPos();
		for (int z = chunkAt.z; z < chunkAt.z + World.BLOCKS_PER_CHUNK; z ++) {
			for (int x = chunkAt.x; x < chunkAt.x + World.BLOCKS_PER_CHUNK; x ++) {
				double noiseY = (SimplexNoise.noise(x * frequency, z * frequency) * scale) + yBase;
				for (int y = 0; y < noiseY + 1; y ++) {
					chunk.setBlock(new BlockPos(x, y, z).add(chunkAt.negate(new Vector3i())), Blocks.blockStone);
				}
			}
		}
	}
	
}