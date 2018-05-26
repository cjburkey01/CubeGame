package com.cjburkey.cubegame.world;

import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.thread.IPoolTask;

public final class PoolTaskChunkGen implements IPoolTask {
	
	private final IWorldGenerator generator;
	private final Chunk chunk;
	private final boolean render;
	private final Runnable onDone;
	
	public PoolTaskChunkGen(IWorldGenerator generator, Chunk chunk, boolean render, Runnable onDone) {
		this.generator = generator;
		this.chunk = chunk;
		this.render = render;
		this.onDone = onDone;
	}
	
	public void execute() {
		generator.generateChunk(chunk);
		chunk.markGenerated();
		chunk.world.onChunkGenerated(chunk, onDone, render);
	}
	
}