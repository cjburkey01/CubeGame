package com.cjburkey.cubegame.object;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.cjburkey.cubegame.Debug;

public final class GameObject {
	
	private String name = "";
	
	private final List<Component> components = new ArrayList<>();
	private final List<Component> componentsToAdd = new ArrayList<>();
	private final List<Component> componentsToRemove = new ArrayList<>();
	
	public final UUID uuid;
	public final Transform transform;
	
	public GameObject() {
		uuid = UUID.randomUUID();
		setName(uuid.toString());
		transform = addComponent(new Transform());
	}
	
	public void onUpdate() {
		updateComponents();
		for (Component component : components) {
			component.onUpdate();
		}
	}
	
	public void onRender() {
		for (Component component : components) {
			component.onRender();
		}
	}
	
	public void onDestroy() {
		// Remove all components
		for (Component component : components) {
			componentsToRemove.add(component);
		}
		updateComponents();
	}
	
	private void updateComponents() {
		// Add all queued components
		for (Component componentToAdd : componentsToAdd) {
			components.add(componentToAdd);
			// Give the "parent" variable a value in the component
			try {
				Field field = componentToAdd.getClass().getField("parent");
				if (field == null) {
					throw new Exception("Field not found");
				}
				field.setAccessible(true);
				field.set(componentToAdd, this);
			} catch (Exception e) {
				Debug.error("Failed to set parent for component {} on object {}", componentToAdd.getClass().getSimpleName(), getName());
				Debug.exception(e);
				continue;
			}
			componentToAdd.onAdd();
		}
		componentsToAdd.clear();

		// Remove all queued components
		for (Component componentToRemove : componentsToRemove) {
			componentToRemove.onRemove();
			components.remove(componentToRemove);
		}
		componentsToRemove.clear();
	}
	
	public <T extends Component> T addComponent(T component) {
		if (getComponent(component.getClass()) != null) {
			Debug.warn("Cannot add another component of type " + component.getClass().getSimpleName() + " to object");
			return null;
		}
		componentsToAdd.add(component);
		return component;
	}
	
	public <T extends Component> T getComponent(Class<T> componentType) {
		for (Component component : components) {
			if (componentType.isAssignableFrom(component.getClass())) {
				return componentType.cast(component);
			}
		}
		return null;
	}
	
	public Component[] getComponents() {
		return components.toArray(new Component[0]);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((components == null) ? 0 : components.hashCode());
		result = prime * result + ((componentsToAdd == null) ? 0 : componentsToAdd.hashCode());
		result = prime * result + ((componentsToRemove == null) ? 0 : componentsToRemove.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((transform == null) ? 0 : transform.hashCode());
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
		GameObject other = (GameObject) obj;
		if (components == null) {
			if (other.components != null) {
				return false;
			}
		} else if (!components.equals(other.components)) {
			return false;
		}
		if (componentsToAdd == null) {
			if (other.componentsToAdd != null) {
				return false;
			}
		} else if (!componentsToAdd.equals(other.componentsToAdd)) {
			return false;
		}
		if (componentsToRemove == null) {
			if (other.componentsToRemove != null) {
				return false;
			}
		} else if (!componentsToRemove.equals(other.componentsToRemove)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (transform == null) {
			if (other.transform != null) {
				return false;
			}
		} else if (!transform.equals(other.transform)) {
			return false;
		}
		return true;
	}
	
}