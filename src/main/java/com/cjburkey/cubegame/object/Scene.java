package com.cjburkey.cubegame.object;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.cubegame.event.EventGameRender;
import com.cjburkey.cubegame.event.EventGameUpdate;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;

@EventListener
public final class Scene {
	
	private static final List<GameObject> objectsInScene = new ArrayList<>();
	private static final List<GameObject> objectsToAdd = new ArrayList<>();
	private static final List<GameObject> objectsToDestroy = new ArrayList<>();
	
	public static GameObject createObject() {
		GameObject obj = new GameObject();
		objectsToAdd.add(obj);
		return obj;
	}
	
	public static void destroyGameObject(GameObject gameObject) {
		objectsToDestroy.add(gameObject);
	}
	
	@EventHandler
	private static void _onUpdateInternal(EventGameUpdate e) {
		updateObjects();
		for (GameObject objectInScene : objectsInScene) {
			objectInScene.onUpdate();
		}
	}
	
	@EventHandler
	private static void _onRenderInternal(EventGameRender e) {
		for (GameObject objectInScene : objectsInScene) {
			objectInScene.onRender();
		}
	}
	
	private static void updateObjects() {
		// Add all queued objects
		for (GameObject objectToAdd : objectsToAdd) {
			objectsInScene.add(objectToAdd);
		}
		objectsToAdd.clear();

		// Remove all queued objects
		for (GameObject objectToDestroy : objectsToDestroy) {
			objectToDestroy.onDestroy();
			objectsInScene.remove(objectToDestroy);
		}
		objectsToDestroy.clear();
	}
	
}