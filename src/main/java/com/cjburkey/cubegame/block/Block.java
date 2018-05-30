package com.cjburkey.cubegame.block;

import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.cubegame.registry.IRegistryObject;
import com.cjburkey.cubegame.registry.RegistryName;

public abstract class Block implements IRegistryObject {
	
	private final RegistryName resourcePath;
	
	protected Block(RegistryName resourcePath) {
		this.resourcePath = resourcePath;
	}
	
	public final RegistryName getRegistryName() {
		return resourcePath;
	}
	
	public abstract Vector2i getTexturePos();
	
	public void onUpdate(BlockState self) {
	}
	
	public void onRender(BlockState self) {
	}
	
	public void onPlace(BlockState self) {
	}
	
	public void onBreak(BlockState self) {
	}
	
	// Also determines if this block must be rendered separately. If this is false, it cannot be rendered by the chunk batcher
	// When this is false, use onRender() to draw the block/object
	public boolean getIsFullBlock(BlockState self) {
		return true;
	}
	
	public Vector3f getRenderColorForFace(BlockState self, Direction dir) {
		return new Vector3f().zero();
	}
	
	public float getRenderColorRandomization(BlockState self) {
		return 0.095f;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resourcePath == null) ? 0 : resourcePath.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Block other = (Block) obj;
		if (resourcePath == null) {
			if (other.resourcePath != null) {
				return false;
			}
		} else if (!resourcePath.equals(other.resourcePath)) {
			return false;
		}
		return true;
	}
	
}