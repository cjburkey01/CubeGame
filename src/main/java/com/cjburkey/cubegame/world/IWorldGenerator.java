package com.cjburkey.cubegame.world;

import com.cjburkey.cubegame.chunk.Chunk;

public interface IWorldGenerator {
	
	void generateChunk(Chunk chunk);
	
}