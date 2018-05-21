package com.cjburkey.cubegame;

public final class Mathf {
	
	public static final float pi = (float) Math.PI;
	
	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
	
	public static int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}
	
	public static float degToRad(float deg) {
		return deg / 180.0f * pi;
	}
	
	public static float radToDeg(float rad) {
		return rad / pi * 180.0f;
	}
	
}