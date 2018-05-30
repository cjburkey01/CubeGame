package com.cjburkey.cubegame.block;

import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.cubegame.registry.RegistryName;

public class BlockColored extends Block {
	
	private final Vector3f color = new Vector3f();
	
	public BlockColored(RegistryName resourcePath, Vector3f color) {
		super(resourcePath);
		this.color.set(color);
	}
	
	public Vector3f getRenderColorForFace(BlockState self, Direction dir) {
		return new Vector3f(color);
	}
	
	// Not used, so we won't set one
	public Vector2i getTexturePos() {
		return new Vector2i().zero();
	}
	
}