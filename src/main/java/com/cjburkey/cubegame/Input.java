package com.cjburkey.cubegame;

import static org.lwjgl.glfw.GLFW.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.joml.Vector2f;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;
import com.cjburkey.cubegame.event.game.EventGamePreInit;

@EventListener
public final class Input {
	
	private static final HashMap<Integer, Boolean> keysDown = new HashMap<>();
	private static final List<Integer> keysUp = new ArrayList<>();
	
	private static final HashMap<Integer, Boolean> mouseDown = new HashMap<>();
	private static final List<Integer> mouseUp = new ArrayList<>();
	
	private static final Vector2f mousePos = new Vector2f().zero();
	private static final Vector2f prevMousePos = new Vector2f().zero();
	private static final Vector2f deltaMouse = new Vector2f().zero();
	
	private static boolean firstMouseMove = true;
	
	@EventHandler
	private void onGameInit(EventGamePreInit e) {
		Debug.log("Registered input handlers");
		
		// Keyboard handler
		glfwSetKeyCallback(CubeGame.getWindow().getIdentifier(), (window, key, scancode, action, mods) -> {
			if (action == GLFW_PRESS) {
				keysDown.put(key, true);
			} else if (action == GLFW_RELEASE) {
				keysUp.add(key);
			}
		});
		
		// Mouse button handler
		glfwSetMouseButtonCallback(CubeGame.getWindow().getIdentifier(), (window, button, action, mods) -> {
			if (action == GLFW_PRESS) {
				mouseDown.put(button, true);
			} else if (action == GLFW_RELEASE) {
				mouseUp.add(button);
			}
		});
		
		// Mouse position handler
		glfwSetCursorPosCallback(CubeGame.getWindow().getIdentifier(), (window, x, y) -> {
			mousePos.set((float) x, (float) y);
			if (firstMouseMove) {
				firstMouseMove = false;
				prevMousePos.set(mousePos);
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
	
	public static boolean getIsMousePressed(int button) {
		return mouseDown.containsKey(button);
	}
	
	// Checks whether the user pressed this key this frame
	public static boolean getOnMouseDown(int button) {
		return getIsMousePressed(button) && mouseDown.get(button);
	}
	
	// Get the current position of the mouse
	public static Vector2f getMousePos() {
		return new Vector2f(mousePos);
	}
	
	// Get the position of the mouse last frame
	public static Vector2f getPreviousMousePos() {
		return new Vector2f(prevMousePos);
	}
	
	// Get the current mouse position minus the previous mouse position
	public static Vector2f getDeltaMousePos() {
		return new Vector2f(deltaMouse);
	}
	
	// Not to be called by the game
	public static void _onEarlyUpdateInternal() {
		// Update previous mouse position and mouse delta position
		deltaMouse.set(mousePos.sub(prevMousePos, new Vector2f()));
		prevMousePos.set(mousePos);
	}
	
	// Not to be called by the game
	public static void _onLateUpdateInternal() {
		// Remove keys that were released at least one frame after they were pressed
		for (int i = 0; i < keysUp.size(); i ++) {
			if (keysDown.get(keysUp.get(i)) == null || !keysDown.get(keysUp.get(i))) {
				keysDown.remove(keysUp.get(i));
				keysUp.remove(i --);
			}
		}
		
		// Mark all currently pressed keys as no longer just pressed this frame
		for (Entry<Integer, Boolean> keyDown : keysDown.entrySet()) {
			keysDown.put(keyDown.getKey(), false);
		}
		
		// Remove mouse buttons that were released at least one frame after they were pressed
		for (int i = 0; i < mouseUp.size(); i ++) {
			if (mouseDown.get(mouseUp.get(i)) == null || !mouseDown.get(mouseUp.get(i))) {
				mouseDown.remove(mouseUp.get(i));
				mouseUp.remove(i --);
			}
		}
		
		// Mark all currently pressed mouse buttons as no longer just pressed this frame
		for (Entry<Integer, Boolean> keyDown : mouseDown.entrySet()) {
			mouseDown.put(keyDown.getKey(), false);
		}
	}
	
}