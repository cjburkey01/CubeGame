package com.cjburkey.cubegame.block;

import com.cjburkey.cubegame.Resource;

public abstract class Block {
	
	public final Resource resourcePath;
	
	protected Block(Resource resourcePath) {
		this.resourcePath = resourcePath;
	}
	
	public void onUpdate(BlockState self) {
	}
	
	public void onRender(BlockState self) {
	}
	
	public void onPlace(BlockState self) {
	}
	
	public void onBreak(BlockState self) {
	}
	
	public boolean getIsFullBlock(BlockState self) {
		return true;
	}
	
}