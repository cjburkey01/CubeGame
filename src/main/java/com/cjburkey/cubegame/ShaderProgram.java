package com.cjburkey.cubegame;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.cubegame.object.Camera;

public final class ShaderProgram {
	
	private static ShaderProgram currentShader;
	private static final List<ShaderProgram> linkedShaders = new ArrayList<>();
	
	private int program = 0;
	private final Map<Integer, Integer> shaders = new HashMap<>();	// Store shaders in a map to prevent multiple shaders of the same type (<Type, Shader>)
	private final Map<String, Integer> uniforms = new HashMap<>();
	private boolean valid = false;
	
	public final boolean transforms;
	
	public ShaderProgram(boolean transforms) {
		this.transforms = transforms;
		
		// Create the shader program
		program = glCreateProgram();
		if (program == NULL) {
			Debug.error("Failed to create shader program");
			return;
		}
		valid = true;
	}
	
	public void addUniform(String name) {
		if (!valid) {
			Debug.error("Cannot add uniform to an invalid shader program ");
			return;
		}
		if (uniforms.containsKey(name)) {
			return;
		}
		int loc = glGetUniformLocation(program, name);
		if (loc < 0) {
			Debug.warn("Shader uniform not found: " + name);
			return;
		}
		uniforms.put(name, loc);
	}
	
	public void setUniform(String name, int value) {
		if (uniforms.containsKey(name)) {
			glUniform1i(uniforms.get(name), value);
			return;
		}
		Debug.warn("Shader uniform not found: " + name);
	}
	
	public void setUniform(String name, float value) {
		if (uniforms.containsKey(name)) {
			glUniform1f(uniforms.get(name), value);
			return;
		}
		Debug.warn("Shader uniform not found: " + name);
	}
	
	public void setUniform(String name, Vector4f value) {
		if (uniforms.containsKey(name)) {
			glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
			return;
		}
		Debug.warn("Shader uniform not found: " + name);
	}
	
	public void setUniform(String name, Matrix4f value) {
		if (uniforms.containsKey(name)) {
			FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
			glUniformMatrix4fv(uniforms.get(name), false, value.get(floatBuffer));
			MemoryUtil.memFree(floatBuffer);
			return;
		}
		Debug.warn("Shader uniform not found: " + name);
	}
	
	public void addShader(int type, String source) {
		if (!valid) {
			Debug.error("Cannot add shader to invalid shader program");
			return;
		}
		
		// Create the shader
		int shader = glCreateShader(type);
		if (shader == NULL) {
			Debug.error("Failed to create shader of type: " + type);
			return;
		}
		
		// Add and compile this shader's source code
		glShaderSource(shader, source);
		glCompileShader(shader);
		String info = glGetShaderInfoLog(shader);
		if (info != null && !(info = info.trim()).isEmpty()) {
			// If there was an error while compiling, show the error and cancel
			Debug.error("Failed to compile shader: " + info);
			return;
		}
		
		// Add shader to program
		shaders.put(type, shader);
	}
	
	public void link() {
		if (!valid) {
			Debug.error("Cannot link invalid shader program");
			return;
		}
		
		// Attach shaders to shader program
		for (Entry<Integer, Integer> shader : shaders.entrySet()) {
			glAttachShader(program, shader.getValue());
		}
		
		// Link program and and retrieve errors
		glLinkProgram(program);
		String info = glGetProgramInfoLog(program);
		if (info != null && !(info = info.trim()).isEmpty()) {
			Debug.error("Could not link shader program: " + info);
			return;
		}
		
		// Detach shaders because once the program is linked, shaders can be discarded
		for (Entry<Integer, Integer> shader : shaders.entrySet()) {
			glDetachShader(program, shader.getValue());
			glDeleteShader(shader.getValue());
		}
		shaders.clear();
		
		linkedShaders.add(this);
		if (transforms) {
			addUniform("projectionMatrix");
			addUniform("modelViewMatrix");
		}
	}
	
	public void destroy() {
		unbind();
		
		// Destroy the program
		glDeleteProgram(program);
	}
	
	public void bind() {
		glUseProgram(program);
		currentShader = this;
		if (Camera.getMainCamera() != null) {
			setUniform("projectionMatrix", Camera.getMainCamera().getProjectionMatrix());
		}
	}
	
	public static void unbind() {
		glUseProgram(0);
		currentShader = null;
	}
	
	public static ShaderProgram getCurrentShader() {
		return currentShader;
	}
	
	public static ShaderProgram[] getLinkedShaders() {
		return linkedShaders.toArray(new ShaderProgram[0]);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + program;
		result = prime * result + ((shaders == null) ? 0 : shaders.hashCode());
		result = prime * result + (valid ? 1231 : 1237);
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
		ShaderProgram other = (ShaderProgram) obj;
		if (program != other.program) {
			return false;
		}
		if (shaders == null) {
			if (other.shaders != null) {
				return false;
			}
		} else if (!shaders.equals(other.shaders)) {
			return false;
		}
		if (valid != other.valid) {
			return false;
		}
		return true;
	}
	
}