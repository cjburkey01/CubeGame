package com.cjburkey.cubegame.world;

import org.joml.Vector3i;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.Blocks;
import com.cjburkey.cubegame.chunk.Chunk;

public final class WorldGeneratorDefault implements IWorldGenerator {
	
	// The base noise level, where a block is located n the y-axis when the noise function produces 0.0
	public static final double defaultGeneratorYBase = 5.0d;
	
	// The different noise function values to be combined into one for a complex terrain
	public static final GeneratorOctave[] defaultGeneratorOctaves = new GeneratorOctave[] {
		new GeneratorOctave(1.0d / 225.0d, 15.0d),
		//new GeneratorOctave(1.0d / 450.0d, 30.0d),
		//new GeneratorOctave(1.0d / 900.0d, 60.0d),
	};
	
	public void generateChunk(Chunk chunk) {
		Vector3i chunkAt = World.getBlockFromChunk(chunk.chunkPos).getPos();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
				double noiseY = Noiser.getNoise(chunkAt.x + x, chunkAt.z + z, defaultGeneratorYBase, defaultGeneratorOctaves);
				for (int y = 0; y < World.BLOCKS_PER_CHUNK; y ++) {
					if (y + chunkAt.y < noiseY) {
						chunk.setBlock(new BlockPos(x, y, z), Blocks.blockStone);
					}
				}
			}
		}
	}
	
}