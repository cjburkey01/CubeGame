package com.cjburkey.cubegame;

public final class Mathf {
	
	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
	
	public static int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}
	
}