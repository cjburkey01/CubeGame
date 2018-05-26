package com.cjburkey.cubegame.world;

import com.cjburkey.cubegame.chunk.Chunk;

public interface IChunkGenerator {
	
	void generateChunk(Chunk chunk);
	
}