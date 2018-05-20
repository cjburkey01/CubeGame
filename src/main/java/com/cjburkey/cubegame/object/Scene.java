package com.cjburkey.cubegame.object;

import java.util.ArrayList;
import java.util.List;

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
	
	// Not to be called by the game
	public static void _onUpdateInternal() {
		updateObjects();
		for (GameObject objectInScene : objectsInScene) {
			objectInScene.onUpdate();
		}
	}
	
	// Not to be called by the game
	public static void _onRenderInternal() {
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