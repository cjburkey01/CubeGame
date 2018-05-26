package com.cjburkey.cubegame.chunk;

import java.util.Arrays;
import com.cjburkey.cubegame.block.Block;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.block.BlockState;
import com.cjburkey.cubegame.world.World;

public class Chunk {
	
	private BlockState[] blocks = new BlockState[4096];
	
	public final World world;
	public final BlockPos chunkPos;

	private boolean generating = false;
	private boolean generated = false;
	
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
	
	public void markGenerating() {
		generating = true;
	}
	
	public void markGenerated() {
		generating = false;
		generated = true;
	}
	
	public boolean getGenerating() {
		return generating;
	}
	
	public boolean getGenerated() {
		return generated;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(blocks);
		result = prime * result + ((chunkPos == null) ? 0 : chunkPos.hashCode());
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
		Chunk other = (Chunk) obj;
		if (!Arrays.equals(blocks, other.blocks))
			return false;
		if (chunkPos == null) {
			if (other.chunkPos != null)
				return false;
		} else if (!chunkPos.equals(other.chunkPos))
			return false;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		return true;
	}
	
}