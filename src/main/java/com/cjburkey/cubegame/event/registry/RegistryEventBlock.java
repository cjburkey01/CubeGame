package com.cjburkey.cubegame.event.registry;

import com.cjburkey.cubegame.block.Block;
import com.cjburkey.cubegame.registry.Registry;

public class RegistryEventBlock extends RegistryEvent<Block> {
	
	public RegistryEventBlock(Registry<Block> registry) {
		super(registry);
	}
	
}