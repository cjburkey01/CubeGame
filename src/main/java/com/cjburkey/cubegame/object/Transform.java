package com.cjburkey.cubegame.object;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform extends Component {
	
	public final Vector3f position = new Vector3f().zero();
	public final Quaternionf rotation = new Quaternionf().identity();
	public final Vector3f scale = new Vector3f().zero();
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((rotation == null) ? 0 : rotation.hashCode());
		result = prime * result + ((scale == null) ? 0 : scale.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Transform other = (Transform) obj;
		if (position == null) {
			if (other.position != null) {
				return false;
			}
		} else if (!position.equals(other.position)) {
			return false;
		}
		if (rotation == null) {
			if (other.rotation != null) {
				return false;
			}
		} else if (!rotation.equals(other.rotation)) {
			return false;
		}
		if (scale == null) {
			if (other.scale != null) {
				return false;
			}
		} else if (!scale.equals(other.scale)) {
			return false;
		}
		return true;
	}
	
}