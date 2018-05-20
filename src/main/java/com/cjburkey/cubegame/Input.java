package com.cjburkey.cubegame;

import static org.lwjgl.glfw.GLFW.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;
import com.cjburkey.cubegame.event.game.EventGamePreInit;

@EventListener
public final class Input {
	
	private static final HashMap<Integer, Boolean> keysDown = new HashMap<>();
	private static final List<Integer> up = new ArrayList<>();
	
	@EventHandler
	public void onGameInit(EventGamePreInit e) {
		Debug.log("Registered input handlers");
		glfwSetKeyCallback(CubeGame.getWindow().getIdentifier(), (window, key, scancode, action, mods) -> {
			if (action == GLFW_PRESS) {
				_onKeyPressInternal(key);
			} else if (action == GLFW_RELEASE) {
				_onKeyReleaseInternal(key);
			}
		});
	}
	
	// Checks whether the user has this key currently pressed
	public static boolean getIsKeyPressed(int key) {
		return keysDown.containsKey(key);
	}
	
	// Checks whether the user pressed this key this frame
	public static boolean getOnKeyDown(int key) {
		return getIsKeyPressed(key) && keysDown.get(key);
	}
	
	// Not to be called by the game
	private static void _onKeyPressInternal(int key) {
		keysDown.put(key, true);
	}
	
	// Not to be called by the game
	private static void _onKeyReleaseInternal(int key) {
		up.add(key);
	}
	
	// Not to be called by the game
	public static void _onLateUpdateInternal() {
		// Remove keys that were released at least one frame after they were pressed
		for (int i = 0; i < up.size(); i ++) {
			if (!keysDown.get(up.get(i))) {
				keysDown.remove(up.get(i));
				up.remove(i --);
			}
		}
		
		// Mark all currently pressed keys as no longer just pressed this frame
		for (Entry<Integer, Boolean> keyDown : keysDown.entrySet()) {
			keysDown.put(keyDown.getKey(), false);
		}
	}
	
}