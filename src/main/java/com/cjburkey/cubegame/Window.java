package com.cjburkey.cubegame;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.joml.Vector2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

// Wrapper for GLFW windows
public final class Window {
	
	private long window = NULL;
	private String name = "";
	private Vector2i size = new Vector2i().zero();
	private GLCapabilities caps;
	private boolean valid = false;
	
	public Window(String name, int width, int height, boolean vsync) {
		this.name = name;
		size.set(width, height);
		
		// Set default error handling and try to initialize GLFW
		GLFWErrorCallback.createPrint(System.out).set();
		if (!glfwInit()) {
			Debug.error("Failed to initialize GLFW");
			return;
		}
		
		// Use latest possible OpenGL version above 3.3 (with the core compatibility profile)
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		
		// Window should be resizable, but not shown by default
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		
		// Create the window
		window = glfwCreateWindow(width, height, name, NULL, NULL);
		if (window == NULL) {
			Debug.error("Failed to create GLFW window");
			return;
		}
		
		// Initialize OpenGL in this GLFW window
		glfwMakeContextCurrent(window);
		caps = GL.createCapabilities();
		if (caps == null) {
			Debug.error("Failed to create GL capabilities");
			return;
		}
		
		// Enable or disable vertical sync
		glfwSwapInterval((vsync) ? 1 : 0);
		
		// When resized, fit GL to window and update local window size
		glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
			size.set(w, h);
			glViewport(0, 0, w, h);
		});
		
		// Handle input with the "Input" class
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (action == GLFW_PRESS) {
				Input._onKeyPressInternal(key);
			} else if (action == GLFW_RELEASE) {
				Input._onKeyReleaseInternal(key);
			}
		});
		
		setWireframe(false);
		
		// Make this window functional
		valid = true;
	}
	
	// Checks whether this window/GLFW has been fully initialized
	public boolean isValidWindow() {
		return valid;
	}
	
	public void setWireframe(boolean wireframe) {
		glPolygonMode(GL_FRONT_AND_BACK, (wireframe) ? GL_LINE : GL_FILL);
	}
	
	// Shows the window
	public void show() {
		if (!valid) {
			Debug.error("Cannot show an invalid window");
			return;
		}
		glfwShowWindow(window);
	}
	
	// Hides the window
	public void hide() {
		if (!valid) {
			Debug.error("Cannot hide an invalid window");
			return;
		}
		glfwHideWindow(window);
	}
	
	// Destroys the window and terminates GLFW after cleaning up
	public void destroy() {
		if (!valid) {
			Debug.error("Cannot destroy an invalid window");
			return;
		}
		hide();
		glfwDestroyWindow(window);
		
		// Clean up after GLFW
		Callbacks.glfwFreeCallbacks(window);
		glfwSetErrorCallback(null).free();
		glfwTerminate();
	}
	
	// Checks for new events like mouse clicks, keyboard presses, etc
	public void pollEvents() {
		if (!valid) {
			Debug.error("Cannot poll events of an invalid window");
			return;
		}
		glfwPollEvents();
	}
	
	// Switches the back buffer with the front buffer, allowing a new frame to be drawn
	public void swapBuffers() {
		if (!valid) {
			Debug.error("Cannot swap buffers of an invalid window");
			return;
		}
		glfwSwapBuffers(window);
	}
	
	// Sets the color that the window should be after clearBuffer() is called
	public void setClearColor(float r, float g, float b) {
		glClearColor(Mathf.clamp(r, 0.0f, 1.0f), Mathf.clamp(g, 0.0f, 1.0f), Mathf.clamp(b, 0.0f, 1.0f), 1.0f);
	}
	
	// Clears the buffer so there is a "clean slate"
	public void clearBuffer() {
		if (!valid) {
			Debug.error("Cannot clear buffers of an invalid window");
			return;
		}
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	// Centers the window on the primary monitor
	public void center() {
		if (!valid) {
			Debug.error("Cannot center an invalid window");
			return;
		}
		Vector2i mon = getMonitorSize();
		glfwSetWindowPos(window, (mon.x - size.x) / 2, (mon.y - size.y) / 2);
	}
	
	// Sets the window size to half of the monitors width and half of the monitors height
	public void setSizeToHalfMonitor() {
		if (!valid) {
			Debug.error("Cannot set position of an invalid window");
			return;
		}
		Vector2i mon = getMonitorSize();
		setSize(mon.x / 2, mon.y / 2);
	}
	
	// Checks whether the user is trying to close the window
	public boolean getClosing() {
		if (!valid) {
			Debug.error("Cannot check closing state of invalid window");
			return false;
		}
		return glfwWindowShouldClose(window);
	}
	
	// Sets the title of the window
	public void setName(String name) {
		if (!valid) {
			Debug.error("Cannot set name of invalid window");
			return;
		}
		this.name = name;
		glfwSetWindowTitle(window, name);
	}
	
	// Gets the title of the window
	public String getName() {
		if (!valid) {
			Debug.error("Cannot get name of invalid window");
			return null;
		}
		return name;
	}
	
	// Sets the size of the window
	public void setSize(int width, int height) {
		if (!valid) {
			Debug.error("Cannot set size of invalid window");
			return;
		}
		size.set(width, height);
		glfwSetWindowSize(window, width, height);
	}
	
	// Sets the size of the window (using a duple)
	public void setSize(Vector2i size) {
		setSize(size.x, size.y);
	}
	
	// Gets the size of the window (in a duple)
	public Vector2i getSize() {
		if (!valid) {
			Debug.error("Cannot get size of invalid window");
			return null;
		}
		return new Vector2i(size);
	}
	
	// Gets the size of the primary monitor (in a duple)
	public Vector2i getMonitorSize() {
		return getMonitorSize(glfwGetPrimaryMonitor());
	}
	
	// Gets the size of the specified monitor (in a duple)
	public Vector2i getMonitorSize(long monitor) {
		if (!valid) {
			Debug.error("Cannot get size of monitor with invalid window");
			return null;
		}
		GLFWVidMode vidMode = glfwGetVideoMode(monitor);
		return new Vector2i(vidMode.width(), vidMode.height());
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + (valid ? 1231 : 1237);
		result = prime * result + (int) (window ^ (window >>> 32));
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
		Window other = (Window) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (size == null) {
			if (other.size != null) {
				return false;
			}
		} else if (!size.equals(other.size)) {
			return false;
		}
		if (valid != other.valid) {
			return false;
		}
		if (window != other.window) {
			return false;
		}
		return true;
	}
	
}