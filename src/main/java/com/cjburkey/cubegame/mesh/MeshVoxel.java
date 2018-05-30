package com.cjburkey.cubegame.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import org.joml.Vector3f;
import com.cjburkey.cubegame.BufferUtil;
import com.cjburkey.cubegame.GameHandler;
import com.cjburkey.cubegame.ShaderProgram;

public class MeshVoxel extends Mesh {
	
	private int cbo;
	private int nbo;
	private int crbo;
	private Vector3f[] cs;
	private Vector3f[] ns;
	private float[] crs;
	
	public void setMesh(Vector3f[] verts, int[] tris, Vector3f[] cs, Vector3f[] ns, float[] crs) {
		this.cs = cs;
		this.ns = ns;
		this.crs = crs;
		setMeshInternal(verts, tris);
	}
	
	public void setMesh(MeshData meshData) {
		setMesh(meshData.verts.toArray(new Vector3f[0]), BufferUtil.getIntArray(meshData.inds), meshData.colors.toArray(new Vector3f[0]), meshData.normals.toArray(new Vector3f[0]), BufferUtil.getFloatArray(meshData.colorVariability));
	}
	
	public void bindShader() {
		GameHandler.getVoxelShader().bind();
	}
	
	public void unbindShader() {
		ShaderProgram.unbind();
	}
	
	protected void generateBuffers() {
		cbo = glGenBuffers();
		nbo = glGenBuffers();
		crbo = glGenBuffers();
	}
	
	protected void bufferAndInitData() {
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.getVec3fBuffer(cs), GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, nbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.getVec3fBuffer(ns), GL_STATIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, crbo);
		glBufferData(GL_ARRAY_BUFFER, crs, GL_STATIC_DRAW);
		glVertexAttribPointer(3, 1, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(3);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	protected void customPreRender() {
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
	}
	
	protected void customPostRender() {
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(1);
	}
	
	protected void customDestroy() {
		glDeleteBuffers(cbo);
		glDeleteBuffers(nbo);
		glDeleteBuffers(crbo);
	}
	
}