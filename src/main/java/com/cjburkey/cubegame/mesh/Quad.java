package com.cjburkey.cubegame.mesh;

import org.joml.Vector2i;

public class Quad {
	
	public Vector2i start;
	public Vector2i end;
	
	public Quad(Vector2i start, Vector2i end) {
		this.start = new Vector2i(start);
		this.end = new Vector2i(end);
	}
	
	public boolean contains(Vector2i point) {
		return start.x <= point.x && end.x >= point.x && start.y <= point.y && start.y >= point.y;
	}
	
	public static boolean contains(Iterable<Quad> quads, Vector2i point) {
		for (Quad quad : quads) {
			if (quad.contains(point)) {
				return true;
			}
		}
		return false;
	}
	
}