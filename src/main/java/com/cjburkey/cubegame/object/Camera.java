package com.cjburkey.cubegame.object;

import org.joml.Matrix4f;

public final class Camera extends Component {
	
	public float fovDegrees = 90.0f;
	public float nearPlane = 0.1f;
	public float farPlane = 1000.0f;
	
	private final Matrix4f projectionMatrix = new Matrix4f().identity();
	private final Matrix4f viewMatrix = new Matrix4f().identity();
	private final Matrix4f modelMatrix = new Matrix4f().identity();
	
	public Matrix4f getProjectionMatrix() {
		return new Matrix4f(projectionMatrix);
	}
	
	public Matrix4f getViewMatrix() {
		return new Matrix4f(viewMatrix);
	}
	
	public Matrix4f getModelMatrix() {
		return new Matrix4f(modelMatrix);
	}
	
	public Matrix4f getModelViewProjectionMatrix() {
		return getModelMatrix().mul(getViewMatrix().mul(getProjectionMatrix()));
	}
	
}