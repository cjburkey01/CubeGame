package com.cjburkey.cubegame.registry;

import java.util.regex.Pattern;

public class RegistryName {
	
	public final String domain;
	public final String path;
	
	public RegistryName(String domain, String path) {
		this.domain = domain;
		
		path = path.trim().replaceAll(Pattern.quote("\\"), "/");
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		while (path.endsWith("/")) {
			path = path.substring(0, path.length() - 2);
		}
		this.path = path;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistryName other = (RegistryName) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	public String toString() {
		return domain + ":" + path;
	}
	
}