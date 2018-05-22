package com.cjburkey.cubegame;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public final class Texture2D {
	
	private int textureId;
	
	// Protected because this should not be called outside of this class
	private Texture2D(ByteBuffer imageBytes, int width, int height) {
		// Initialize texture in OpenGL
		glActiveTexture(GL_TEXTURE0);
		textureId = glGenTextures();
		
		// Load texture with OpenGL
		bind();
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);	// 1 byte per component
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);	// Pixel perfect (with mipmapping)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBytes);
		glGenerateMipmap(GL_TEXTURE_2D);
		unbind();
		
		// Cleanup the image buffer
		MemoryUtil.memFree(imageBytes);
	}
	
	public void destroy() {
		glDeleteTextures(textureId);
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE0, textureId);
	}
	
	public static void unbind() {
		glBindTexture(GL_TEXTURE0, 0);
	}
	
	// Static initialization methods
	public static Texture2D createTextureFromFile(String path) {
		return createTextureFromStream(FileUtil.getStreamFromFile(path));
	}
	
	public static Texture2D createTextureFromStream(InputStream inputStream) {
		try {
			if (inputStream == null) {
				throw new FileNotFoundException("Stream was null");
			}
			
			// Load file bytes into an array
			byte[] fileBytes = IOUtils.toByteArray(inputStream);
			if (fileBytes == null) {
				throw new Exception("Failed to load bytes from file");
			}
			
			// Create image bytes
			int[] w = new int[1];
			int[] h = new int[1];
			int[] comp = new int[1];
			ByteBuffer image = BufferUtils.createByteBuffer(fileBytes.length);
			image.put(fileBytes).flip();
			ByteBuffer buff = stbi_load_from_memory(image, w, h, comp, 0);
			
			// Return the texture object
			return new Texture2D(buff, w[0], h[0]);
		} catch (Exception e) {
			Debug.error("Failed to initialize texture");
			Debug.exception(e);
		}
		return null;
	}
	
}