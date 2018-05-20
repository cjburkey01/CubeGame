package com.cjburkey.cubegame.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.joml.Vector3f;
import com.cjburkey.cubegame.BufferUtil;
import com.cjburkey.cubegame.ShaderProgram;
import com.cjburkey.cubegame.object.Camera;
import com.cjburkey.cubegame.object.Transform;

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
	
	public final void render(Transform transformation) {
		bindShader();
		if (ShaderProgram.getCurrentShader() != null && ShaderProgram.getCurrentShader().transforms && transformation != null && Camera.getMainCamera() != null) {
			ShaderProgram.getCurrentShader().setUniform("modelViewMatrix", Camera.getMainCamera().getModelViewMatrix(transformation));
		}
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		customPreRender();
		customRender();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		unbindShader();
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
	
	protected void bindShader() {
	}
	
	protected void unbindShader() {
	}
	
	public final boolean getHasInit() {
		return hasInit;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ebo;
		result = prime * result + (hasInit ? 1231 : 1237);
		result = prime * result + inds;
		result = prime * result + vao;
		result = prime * result + vbo;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Mesh other = (Mesh) obj;
		if (ebo != other.ebo) {
			return false;
		}
		if (hasInit != other.hasInit) {
			return false;
		}
		if (inds != other.inds) {
			return false;
		}
		if (vao != other.vao) {
			return false;
		}
		if (vbo != other.vbo) {
			return false;
		}
		return true;
	}
	
}