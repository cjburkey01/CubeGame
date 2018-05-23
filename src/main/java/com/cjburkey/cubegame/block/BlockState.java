package com.cjburkey.cubegame.block;

import com.cjburkey.cubegame.chunk.Chunk;
import com.cjburkey.cubegame.world.World;

public final class BlockState {
	
	public final World world;
	public final Block block;
	public final Chunk chunk;
	public final BlockPos posInChunk;
	
	public BlockState(World world, Block block, Chunk chunk, BlockPos posInChunk) {
		this.world = world;
		this.block = block;
		this.chunk = chunk;
		this.posInChunk = posInChunk;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + ((chunk == null) ? 0 : chunk.hashCode());
		result = prime * result + ((posInChunk == null) ? 0 : posInChunk.hashCode());
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockState other = (BlockState) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (chunk == null) {
			if (other.chunk != null)
				return false;
		} else if (!chunk.equals(other.chunk))
			return false;
		if (posInChunk == null) {
			if (other.posInChunk != null)
				return false;
		} else if (!posInChunk.equals(other.posInChunk))
			return false;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		return true;
	}
	
}