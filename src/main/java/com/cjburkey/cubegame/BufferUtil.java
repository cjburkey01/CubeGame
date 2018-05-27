package com.cjburkey.cubegame;

import java.util.Collection;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class BufferUtil {
	
	public static float[] getVec2fBuffer(Vector2f[] vecs) {
		float[] vecfs = new float[vecs.length * 2];
		for (int i = 0; i < vecs.length; i ++) {
			vecfs[2 * i] = vecs[i].x;
			vecfs[2 * i + 1] = vecs[i].y;
		}
		return vecfs;
	}
	
	public static float[] getVec3fBuffer(Vector3f[] vecs) {
		float[] vecfs = new float[vecs.length * 3];
		for (int i = 0; i < vecs.length; i ++) {
			vecfs[3 * i] = vecs[i].x;
			vecfs[3 * i + 1] = vecs[i].y;
			vecfs[3 * i + 2] = vecs[i].z;
		}
		return vecfs;
	}
	
	public static int[] getIntArray(Collection<Integer> collection) {
		int[] out = new int[collection.size()];
		int index = 0;
		for (int i : collection) {
			out[index ++] = i;
		}
		return out;
	}
	
	public static float[] getFloatArray(Collection<Float> collection) {
		float[] out = new float[collection.size()];
		int index = 0;
		for (float i : collection) {
			out[index ++] = i;
		}
		return out;
	}
	
}