package com.cjburkey.cubegame.block;

import com.cjburkey.cubegame.Resource;

public class BlockBasic extends Block {

	public BlockBasic(Resource resourcePath) {
		super(resourcePath);
	}
	
	public BlockBasic(String domain, String path) {
		this(new Resource(domain, path));
	}
	
}