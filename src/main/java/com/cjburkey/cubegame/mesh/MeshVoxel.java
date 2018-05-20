package com.cjburkey.cubegame.mesh;

import org.joml.Vector3f;
import com.cjburkey.cubegame.GameHandler;
import com.cjburkey.cubegame.ShaderProgram;

public class MeshVoxel extends Mesh {
	
	public void setMesh(Vector3f[] verts, int[] tris) {
		setMeshInternal(verts, tris);
	}
	
	public void bindShader() {
		GameHandler.getVoxelShader().bind();
	}
	
	public void unbindShader() {
		ShaderProgram.unbind();
	}
	
}