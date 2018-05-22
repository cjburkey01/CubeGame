package com.cjburkey.cubegame.world;

import org.joml.Vector3i;
import com.cjburkey.cubegame.SimplexNoise;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.Blocks;
import com.cjburkey.cubegame.chunk.Chunk;

public class WorldGeneratorDefault implements IWorldGenerator {
	
	public static final float frequency = 1.0f / 10.0f;
	public static final float scale = 5.0f;
	public static final float yBase = 0.0f;
	
	public void generateChunk(Chunk chunk) {
		Vector3i pos = World.getBlockFromChunk(chunk.chunkPos).getPos();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
				double noiseY = (SimplexNoise.noise(pos.x * frequency, pos.z * frequency) * scale) + yBase;
				for (int y = 0; y < World.BLOCKS_PER_CHUNK; y ++) {
					if (pos.y < noiseY) {
						chunk.setBlock(new BlockPos(pos), Blocks.blockStone);
					}
					pos.y ++;
				}
				pos.x ++;
			}
			pos.z ++;
		}
	}
	
}