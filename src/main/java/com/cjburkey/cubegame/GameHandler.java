package com.cjburkey.cubegame;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import com.cjburkey.cubegame.mesh.MeshBasic;

public class GameHandler {
	
	private static ShaderProgram voxelShader;
	
	private static final Vector3f[] vertsTest = new Vector3f[] {
		new Vector3f(0.5f, 0.5f, 0.0f),
		new Vector3f(-0.5f, 0.5f, 0.0f),
		new Vector3f(-0.5f, -0.5f, 0.0f),
		new Vector3f(0.5f, -0.5f, 0.0f)
	};
	
	private static final int[] trisTest = new int[] {
		0, 1, 2,
		0, 2, 3
	};
	
	private MeshBasic mesh = new MeshBasic();
	
	public void preinit() {
		voxelShader = new ShaderProgram();
		voxelShader.addShader(GL20.GL_VERTEX_SHADER, FileUtil.readFileText("/res/shader/voxel/voxelChunkVert.glsl"));
		voxelShader.addShader(GL20.GL_FRAGMENT_SHADER, FileUtil.readFileText("/res/shader/voxel/voxelChunkFrag.glsl"));
		voxelShader.link();
		voxelShader.bind();
		
		mesh.setMesh(vertsTest, trisTest);
	}
	
	public void init() {
		
	}
	
	public void update() {
		
	}
	
	public void render() {
		mesh.render();
	}
	
	public void exit() {
		
	}
	
}