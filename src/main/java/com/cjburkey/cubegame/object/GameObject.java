package com.cjburkey.cubegame.object;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.cubegame.Debug;

public final class GameObject {
	
	private final List<Component> components = new ArrayList<>();
	private final List<Component> componentsToAdd = new ArrayList<>();
	private final List<Component> componentsToRemove = new ArrayList<>();
	
	public final Transform transform;
	
	public GameObject() {
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
	
}