package com.cjburkey.cubegame.block;

import org.joml.Vector3f;
import com.cjburkey.cubegame.Resource;

public class BlockBasic extends Block {
	
	private final Vector3f color = new Vector3f();
	
	public BlockBasic(String domain, String path, Vector3f color) {
		this(new Resource(domain, path), color);
	}

	public BlockBasic(Resource resourcePath, Vector3f color) {
		super(resourcePath);
		this.color.set(color);
	}
	
	public Vector3f getRenderColorForFace(BlockState self, Direction dir) {
		return new Vector3f(color);
	}
	
}