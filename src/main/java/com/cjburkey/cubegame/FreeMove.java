package com.cjburkey.cubegame;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubegame.object.Component;

// Allows free movement around the origin of the object using WASD to move and QE to rotate
public class FreeMove extends Component {
	
	public float moveSpeed = 10.0f;
	public float rotationSpeed = 2.75f;
	
	private final Vector3f move = new Vector3f().zero();
	
	public void onUpdate() {
		// Reset movement from last frame
		move.zero();
		
		// Rotation
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_Q)) {
			parent.transform.rotation.rotateAxis(-rotationSpeed * CubeGame.getDeltaTimeF(), new Vector3f(0.0f, 1.0f, 0.0f));
		}
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_E)) {
			parent.transform.rotation.rotateAxis(rotationSpeed * CubeGame.getDeltaTimeF(), new Vector3f(0.0f, 1.0f, 0.0f));
		}
		
		// Translation
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_W)) {
			move.z -= 1.0f;
		}
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_S)) {
			move.z += 1.0f;
		}
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_A)) {
			move.x -= 1.0f;
		}
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_D)) {
			move.x += 1.0f;
		}
		if (!move.equals(new Vector3f().zero())) {
			move.set(parent.transform.transformDirection(move.normalize(), true).mul(CubeGame.getDeltaTimeF() * moveSpeed));
			parent.transform.position.add(move);
		}
	}
	
}