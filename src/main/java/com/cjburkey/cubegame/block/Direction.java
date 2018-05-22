package com.cjburkey.cubegame.block;

public enum Direction {
	
	SOUTH(new BlockPos(0, 0, 1)),
	NORTH(new BlockPos(0, 0, -1)),
	EAST(new BlockPos(1, 0, 0)),
	WEST(new BlockPos(-1, 0, 0)),
	UP(new BlockPos(0, 1, 0)),
	DOWN(new BlockPos(0, -1, 0)),
	
	;
	
	public final BlockPos unitDir;
	
	private Direction(BlockPos unitDir) {
		this.unitDir = unitDir;
	}
	
}