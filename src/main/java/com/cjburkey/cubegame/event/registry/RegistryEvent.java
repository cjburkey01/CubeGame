package com.cjburkey.cubegame.event.registry;

import com.cjburkey.cubegame.event.Event;
import com.cjburkey.cubegame.registry.IRegistryObject;
import com.cjburkey.cubegame.registry.Registry;

public class RegistryEvent<T extends IRegistryObject> extends Event {
	
	public final Registry<T> registry;
	
	public RegistryEvent(Registry<T> registry) {
		this.registry = registry;
	}
	
}