package com.cjburkey.cubegame.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.cubegame.BufferUtil;
import com.cjburkey.cubegame.GameHandler;
import com.cjburkey.cubegame.ShaderProgram;
import com.cjburkey.cubegame.block.Block;

public class MeshNewVoxel extends Mesh {

	private int nbo;
	private int tpbo;
	private Vector3f[] normals;
	private Vector2i[] blockAtlasPos;
	
	public void setMesh(Vector3f[] vertices, int[] indices, Vector3f[] normals, Block[] blocks) {
		setMeshInternal(vertices, indices);
		this.normals = normals;
		
		// Every four vertices make one quad, which can only be one type of block.
		// We have to expand the blocks into per-vertex data for OpenGL
		blockAtlasPos = new Vector2i[blocks.length * 4];
		for (int i = 0; i < blocks.length; i ++) {
			Vector2i texturePos = new Vector2i(blocks[i].getTexturePos());
			blockAtlasPos[4 * i] = texturePos;
			blockAtlasPos[4 * i + 1] = texturePos;
			blockAtlasPos[4 * i + 2] = texturePos;
			blockAtlasPos[4 * i + 3] = texturePos;
		}
	}
	
	protected void generateBuffers() {
		nbo = glGenBuffers();
		tpbo = glGenBuffers();
	}
	
	protected void bufferAndInitData() {
		glBindBuffer(GL_ARRAY_BUFFER, nbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.getVec3fBuffer(normals), GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, tpbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.getVec2iBuffer(blockAtlasPos), GL_STATIC_DRAW);
		glVertexAttribPointer(2, 2, GL_INT, false, 0, 0);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	protected void customPreRender() {
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
	}
	
	protected void customPostRender() {
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(1);
	}
	
	protected void bindShader() {
		GameHandler.getNewVoxelShader().bind();
	}
	
	protected void unbindShader() {
		ShaderProgram.unbind();
	}
	
	protected void customDestroy() {
		glDeleteBuffers(nbo);
		glDeleteBuffers(tpbo);
	}
	
}