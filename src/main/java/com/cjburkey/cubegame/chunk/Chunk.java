package com.cjburkey.cubegame.chunk;

import com.cjburkey.cubegame.block.Block;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.BlockState;
import com.cjburkey.cubegame.world.World;

public class Chunk {
	
	private BlockState[] blocks = new BlockState[4096];
	
	public final World world;
	public final BlockPos chunkPos;
	
	public Chunk(World world, BlockPos chunkPos) {
		this.world = world;
		this.chunkPos = chunkPos;
	}
	
	public void setBlock(BlockPos blockPos, Block block) {
		if (!verifyPos(blockPos)) {
			return;
		}
		blocks[getIndex(blockPos)] = (block == null) ? null : new BlockState(world, block, this, blockPos);
	}
	
	public void removeBlock(BlockPos blockPos) {
		setBlock(blockPos, null);
	}
	
	public boolean getIsTransparentAt(BlockPos blockPos) {
		BlockState state = getBlockState(blockPos);
		return state == null || state.block == null || !state.block.getIsFullBlock(state);
	}
	
	public BlockState getBlockState(BlockPos blockPos) {
		if (!verifyPos(blockPos)) {
			return null;
		}
		return blocks[getIndex(blockPos)];
	}
	
	private boolean verifyPos(BlockPos pos) {
		return pos.getX() >= 0 && pos.getX() < World.BLOCKS_PER_CHUNK && pos.getY() >= 0 && pos.getY() < World.BLOCKS_PER_CHUNK && pos.getZ() >= 0 && pos.getZ() < World.BLOCKS_PER_CHUNK;
	}
	
	private int getIndex(BlockPos blockPos) {
		return blockPos.getZ() * World.BLOCKS_PER_CHUNK * World.BLOCKS_PER_CHUNK + blockPos.getY() * World.BLOCKS_PER_CHUNK + blockPos.getX();
	}
	
}