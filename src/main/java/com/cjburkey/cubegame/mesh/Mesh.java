package com.cjburkey.cubegame.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.joml.Vector3f;
import com.cjburkey.cubegame.BufferUtil;

public abstract class Mesh {
	
	private boolean hasInit = false;
	
	private int vao;
	private int vbo;
	private int ebo;
	private int inds;
	
	protected final void init() {
		hasInit = true;
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		ebo = glGenBuffers();
		generateBuffers();
	}
	
	// Call to generate the mesh
	protected final void setMeshInternal(Vector3f[] verts, int[] tris) {
		if (!hasInit) {
			init();
		}
		
		inds = tris.length;
		
		glBindVertexArray(vao);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.getVec3fBuffer(verts), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, tris, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		bufferAndInitData();
		glBindVertexArray(0);
	}
	
	public final void render() {
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		customPreRender();
		customRender();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	// Override if there is a custom rendering behavior
	protected void customRender() {
		glDrawElements(GL_TRIANGLES, inds, GL_UNSIGNED_INT, 0);
	}
	
	// Override if there is a custom rendering behavior that does not interfere with the draw call
	protected void customPreRender() {
	}
	
	// Generate custom buffers
	protected void generateBuffers() {
	}
	
	// Add data to custom buffers
	protected void bufferAndInitData() {
	}
	
}