package com.cjburkey.cubegame.mesh;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MeshData {
	
	public final List<Vector3f> verts = new ArrayList<>();
	public final List<Integer> inds = new ArrayList<>();
	public final List<Vector2f> uvs = new ArrayList<>();
	public final List<Vector3f> colors = new ArrayList<>();
	
}