package com.cjburkey.cubegame.world;

import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.thread.IPoolTask;

public final class PoolTaskChunkGen implements IPoolTask {
	
	private final IChunkGenerator generator;
	private final Chunk chunk;
	private final Runnable onDone;
	
	public PoolTaskChunkGen(IChunkGenerator generator, Chunk chunk, Runnable onDone) {
		this.generator = generator;
		this.chunk = chunk;
		this.onDone = onDone;
	}
	
	public void execute() {
		if (chunk.getGenerated()) {
			return;
		}
		generator.generateChunk(chunk);
		chunk.markGenerated();
		chunk.world.onChunkGenerated(chunk, onDone);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chunk == null) ? 0 : chunk.hashCode());
		result = prime * result + ((generator == null) ? 0 : generator.hashCode());
		result = prime * result + ((onDone == null) ? 0 : onDone.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PoolTaskChunkGen other = (PoolTaskChunkGen) obj;
		if (chunk == null) {
			if (other.chunk != null)
				return false;
		} else if (!chunk.equals(other.chunk))
			return false;
		if (generator == null) {
			if (other.generator != null)
				return false;
		} else if (!generator.equals(other.generator))
			return false;
		if (onDone == null) {
			if (other.onDone != null)
				return false;
		} else if (!onDone.equals(other.onDone))
			return false;
		return true;
	}
	
}