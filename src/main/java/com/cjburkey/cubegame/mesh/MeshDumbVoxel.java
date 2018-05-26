package com.cjburkey.cubegame.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.cubegame.BufferUtil;
import com.cjburkey.cubegame.ShaderProgram;
import com.cjburkey.cubegame.Texture2D;

@Deprecated
public class MeshDumbVoxel extends Mesh {
	
	private int uvbo;
	private Vector2f[] uvs;
	
	public void setMesh(Vector3f[] verts, int[] tris, Vector2f[] uvs) {
		this.uvs = uvs;
		setMeshInternal(verts, tris);
	}
	
	public void setMesh(MeshData meshData) {
		setMesh(meshData.verts.toArray(new Vector3f[0]), BufferUtil.getIntArray(meshData.inds), meshData.uvs.toArray(new Vector2f[0]));
	}
	
	public void bindShader() {
		//GameHandler.getDumbVoxelShader().bind();
		//GameHandler.texture.bind();
	}
	
	public void unbindShader() {
		Texture2D.unbind();
		ShaderProgram.unbind();
	}
	
	protected void generateBuffers() {
		uvbo = glGenBuffers();
	}
	
	protected void bufferAndInitData() {
		glBindBuffer(GL_ARRAY_BUFFER, uvbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.getVec2fBuffer(uvs), GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	protected void customPreRender() {
		glEnableVertexAttribArray(1);
	}
	
	protected void customPostRender() {
		glDisableVertexAttribArray(1);
	}
	
}