package com.cjburkey.cubegame.mesh;

import org.joml.Vector3f;

public class MeshBasic extends Mesh {
	
	public void setMesh(Vector3f[] verts, int[] tris) {
		setMeshInternal(verts, tris);
	}
	
}