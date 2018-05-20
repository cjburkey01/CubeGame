package com.cjburkey.cubegame;

import org.joml.Vector3f;

public class BufferUtil {
	
	public static float[] getVec3fBuffer(Vector3f[] vecs) {
		float[] vecfs = new float[vecs.length * 3];
		for (int i = 0; i < vecs.length; i ++) {
			vecfs[3 * i] = vecs[i].x;
			vecfs[3 * i + 1] = vecs[i].y;
			vecfs[3 * i + 2] = vecs[i].z;
		}
		return vecfs;
	}
	
}