package com.cjburkey.cubegame;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class ShaderProgram {
	
	private int program = 0;
	private final Map<Integer, Integer> shaders = new HashMap<>();	// Store shaders in a map to prevent multiple shaders of the same type (<Type, Shader>)
	private boolean valid = false;
	
	public ShaderProgram() {
		// Create the shader program
		program = glCreateProgram();
		if (program == NULL) {
			Debug.error("Failed to create shader program");
			return;
		}
		valid = true;
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
	}
	
	public void destroy() {
		// Destroy the program
		glDeleteProgram(program);
	}
	
	public void bind() {
		glUseProgram(program);
	}
	
	public static void unbind() {
		glUseProgram(0);
	}
	
}