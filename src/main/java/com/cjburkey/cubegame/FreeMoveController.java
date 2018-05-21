package com.cjburkey.cubegame;

import static org.lwjgl.glfw.GLFW.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubegame.object.Component;

// Allows free movement around the origin of the object using WASD to move and QE to rotate
public class FreeMoveController extends Component {
	
	public float moveSpeed = 10.0f;
	public float rotationSpeed = 0.225f;
	public boolean lockCursor = true;
	
	private boolean lockedLastFrame = false;
	
	private final Vector3f move = new Vector3f().zero();
	private final Vector2f rotation = new Vector2f().zero();
	private final Vector2f rotationChange = new Vector2f().zero();
	
	public void onUpdate() {
		// Reset movement and rotation from last frame
		rotationChange.zero();
		move.zero();
		
		if (lockedLastFrame != lockCursor) {
			glfwSetInputMode(CubeGame.getWindow().getIdentifier(), GLFW_CURSOR, (lockCursor) ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
			lockedLastFrame = lockCursor;
		}
		
		// Rotation
		rotationChange.set(Input.getDeltaMousePos().y, Input.getDeltaMousePos().x).mul(rotationSpeed);
		rotation.add(rotationChange);
		parent.transform.rotation.rotationXYZ(Mathf.degToRad(rotation.x), Mathf.degToRad(rotation.y), 0.0f);
		
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