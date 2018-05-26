package com.cjburkey.cubegame.mesh;

import org.joml.Vector3f;

public class DirectionalLight {
	
	public final Vector3f ambientLight = new Vector3f();
	public final Vector3f direction = new Vector3f();
	public final Vector3f color = new Vector3f();
	public float intensity = 0.0f;
	
	public DirectionalLight(Vector3f ambientLight, Vector3f direction, Vector3f color, float intensity) {
		this.ambientLight.set(ambientLight);
		this.direction.set(direction);
		this.color.set(color);
		this.intensity = intensity;
	}
	
}