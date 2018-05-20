package com.cjburkey.cubegame.object;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform extends Component {
	
	public final Vector3f position = new Vector3f().zero();
	public final Quaternionf rotation = new Quaternionf().identity();
	public final Vector3f scale = new Vector3f().zero();
	
}