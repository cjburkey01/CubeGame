package com.cjburkey.cubegame.object;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.cjburkey.cubegame.Debug;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventSystem;
import com.cjburkey.cubegame.event.window.EventWindowResize;

public final class Camera extends Component {
	
	private static Camera mainCamera = null;
	
	private float aspectRatio = 1.0f;
	private float fovDegrees = 60.0f;
	private float nearPlane = 0.1f;
	private float farPlane = 1000.0f;
	
	public Camera() {
		EventSystem.MAIN_HANDLER.registerHandler(this);
		Debug.log("Created camera");
		if (mainCamera == null) {
			setMainCamera(this);
		}
	}
	
	private final Matrix4f projectionMatrix = new Matrix4f().identity();
	private final Matrix4f viewMatrix = new Matrix4f().identity();
	private final Matrix4f modelMatrix = new Matrix4f().identity();
	
	public Matrix4f getProjectionMatrix() {
		return new Matrix4f(projectionMatrix);
	}
	
	public Matrix4f getViewMatrix() {
		viewMatrix.identity().rotate(parent.transform.rotation).translate(parent.transform.position.negate(new Vector3f()));
		return new Matrix4f(viewMatrix);
	}
	
	public Matrix4f getModelMatrix(Transform object) {
		modelMatrix.identity().translate(object.position).rotate(object.rotation.invert(new Quaternionf())).scale(object.scale);
		return new Matrix4f(modelMatrix);
	}
	
	@EventHandler
	private void updateWindowSize(EventWindowResize e) {
		aspectRatio = (float) e.width / e.height;
		updateProjection();
	}
	
	public void updateProjection() {
		projectionMatrix.identity().perspective((float) Math.toRadians(fovDegrees), aspectRatio, nearPlane, farPlane);
	}
	
	public float getAspectRatio() {
		return fovDegrees;
	}
	
	public float getFovDegrees() {
		return fovDegrees;
	}
	
	public void setFovDegrees(float fovDegrees) {
		this.fovDegrees = fovDegrees;
		updateProjection();
	}
	
	public float getNearPlane() {
		return nearPlane;
	}
	
	public void setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
		updateProjection();
	}
	
	public float getFarPlane() {
		return farPlane;
	}
	
	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
		updateProjection();
	}
	
	public static void setMainCamera(Camera camera) {
		mainCamera = camera;
	}
	
	public static Camera getMainCamera() {
		return mainCamera;
	}
	
}