package com.cjburkey.cubegame.registry;

import java.util.HashMap;
import java.util.Map;

public final class Registry<T extends IRegistryObject> {
	
	private final Map<RegistryName, T> registry = new HashMap<>();
	
	public final boolean register(T object) {
		if (contains(object.getRegistryName())) {
			return false;
		}
		registry.put(object.getRegistryName(), object);
		return true;
	}
	
	public final boolean contains(RegistryName registryName) {
		return registry.containsKey(registryName);
	}
	
	public final T getObject(RegistryName registryName) {
		if (!contains(registryName)) {
			return null;
		}
		return registry.get(registryName);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((registry == null) ? 0 : registry.hashCode());
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
		Registry<?> other = (Registry<?>) obj;
		if (registry == null) {
			if (other.registry != null) {
				return false;
			}
		} else if (!registry.equals(other.registry)) {
			return false;
		}
		return true;
	}
	
}