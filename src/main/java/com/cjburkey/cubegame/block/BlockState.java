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
	
}