package com.cjburkey.cubegame.mesh;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Quad {
	
	public final Vector2i start = new Vector2i();
	public final Vector2i end = new Vector2i();
	public final Vector3f color = new Vector3f();
	public float colorRandomness = 0.0f;
	public int y = 0;
	
	public Quad(Vector2i start, Vector2i end, Vector3f color, int y, float colorRandomness) {
		this.start.set(start);
		this.end.set(end);
		this.color.set(color);
		this.colorRandomness = colorRandomness;
		this.y = y;
	}
	
	public boolean contains(Vector3i point) {
		if (start.x == point.x && start.y == point.z && y == point.y) {
			return true;
		}
		return start.x <= point.x && end.x >= point.x && start.y <= point.z && end.y >= point.z && y == point.y;
	}
	
	public static boolean contains(Iterable<Quad> quads, Vector3i point) {
		for (Quad quad : quads) {
			if (quad.contains(point)) {
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + Float.floatToIntBits(colorRandomness);
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + y;
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quad other = (Quad) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (Float.floatToIntBits(colorRandomness) != Float.floatToIntBits(other.colorRandomness))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public String toString() {
		return "Quad(" + start.x + ", " + start.y + " to " + end.x + ", " + end.y + " at y " + y + ")";
	}
	
}