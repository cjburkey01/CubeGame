package com.cjburkey.cubegame.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.reflections.Reflections;
import com.cjburkey.cubegame.Debug;

public class EventSystem {
	
	public static final EventSystem MAIN_HANDLER = new EventSystem();
	
	private final Map<Class<?>, Map<Method, Object>> eventListeners = new HashMap<>();
	
	public void _initInternal() {
		Reflections reflections = new Reflections();
		Set<Class<?>> listeners = reflections.getTypesAnnotatedWith(EventListener.class);
		for (Class<?> listener : listeners) {
			try {
				registerHandler(listener.newInstance());
			} catch (Exception e) {
				Debug.error("Failed to find event handling methods inside listener: {}", listener.getSimpleName());
				Debug.exception(e);
			}
		}
	}
	
	public <T> void triggerEvent(T event) {
		if (eventListeners.size() < 1) {
			return;
		}
		Map<Method, Object> listeners = eventListeners.get(event.getClass());
		if (listeners == null || listeners.size() < 1) {
			return;
		}
		for (Entry<Method, Object> listener : listeners.entrySet()) {
			try {
				listener.getKey().invoke(listener.getValue(), event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Debug.error("Failed to invoke event listener for event: {} in listener: {}", event.getClass().getSimpleName(), listener.getValue().getClass().getSimpleName());
				Debug.exception(e.getCause());
			}
		}
	}
	
	public void registerHandler(Object listenerObj) {
		Method[] methods = listenerObj.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(EventHandler.class) && method.getParameterTypes().length == 1) {
				Class<?> eventType = method.getParameterTypes()[0];
				Map<Method, Object> listAt = eventListeners.get(eventType);
				if (listAt == null) {
					listAt = new HashMap<>();
					eventListeners.put(eventType, listAt);
				}
				method.setAccessible(true);
				listAt.put(method, listenerObj);
			}
		}
	}
	
	public int getRegisteredEvents() {
		return eventListeners.size();
	}
	
}