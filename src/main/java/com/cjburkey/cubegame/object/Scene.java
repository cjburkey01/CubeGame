package com.cjburkey.cubegame.object;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;
import com.cjburkey.cubegame.event.game.EventGameRender;
import com.cjburkey.cubegame.event.game.EventGameUpdate;

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
	private static void onUpdate(EventGameUpdate e) {
		updateObjects();
		for (GameObject objectInScene : objectsInScene) {
			objectInScene.onUpdate();
		}
	}
	
	@EventHandler
	private static void onRender(EventGameRender e) {
		if (Camera.getMainCamera() == null) {
			return;
		}
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