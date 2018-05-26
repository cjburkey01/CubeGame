package com.cjburkey.cubegame.world;

import org.joml.Vector3i;
import com.cjburkey.cubegame.Mathf;
import com.cjburkey.cubegame.RandomHandler;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.Blocks;
import com.cjburkey.cubegame.chunk.Chunk;

public final class ChunkGeneratorDefault implements IChunkGenerator {
	
	// The base noise level, where a block is located n the y-axis when the noise function produces 0.0
	public static final double defaultGeneratorYBase = 5.0d;
	public static final int layersOfDirt = 3;
	
	// The different noise function values to be combined into one for a complex terrain
	public static final GeneratorOctave[] defaultGeneratorOctaves = new GeneratorOctave[] {
		new GeneratorOctave(1.0d / 150.0d, 35.0d),
		new GeneratorOctave(1.0d / 300.0d, 70.0d),
		new GeneratorOctave(1.0d / 6000.0d, 140.0d),
	};
	
	public void generateChunk(Chunk chunk) {
		Vector3i chunkAt = World.getBlockFromChunk(chunk.chunkPos).getPos();
		for (int z = 0; z < World.BLOCKS_PER_CHUNK; z ++) {
			for (int x = 0; x < World.BLOCKS_PER_CHUNK; x ++) {
				int noiseY = Mathf.floor(Noiser.getNoise(chunkAt.x + x, chunkAt.z + z, defaultGeneratorYBase, defaultGeneratorOctaves));
				for (int y = 0; y < World.BLOCKS_PER_CHUNK; y ++) {
					if (y + chunkAt.y > noiseY) {
						continue;
					}
					if (y + chunkAt.y > noiseY - 1) {
						chunk.setBlock(new BlockPos(x, y, z), Blocks.blockGrass);
						continue;
					}
					int dirt = RandomHandler.getMainInstance().betweenInc(layersOfDirt - 1, layersOfDirt + 1);
					if (y + chunkAt.y > noiseY - 1 - dirt) {
						chunk.setBlock(new BlockPos(x, y, z), Blocks.blockDirt);
						continue;
					}
					chunk.setBlock(new BlockPos(x, y, z), Blocks.blockStone);
				}
			}
		}
	}
	
}