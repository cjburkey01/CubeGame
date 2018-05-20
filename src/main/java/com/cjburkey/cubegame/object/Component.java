package com.cjburkey.cubegame.object;

public abstract class Component {
	
	public void onAdd() {
	}
	
	public void onUpdate() {
	}
	
	public void onRender() {
	}
	
	public void onRemove() {
	}
	
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(getClass())) {
			return false;
		}
		return other == this;
	}
	
	public int hashCode() {
		return super.hashCode();
	}
	
}