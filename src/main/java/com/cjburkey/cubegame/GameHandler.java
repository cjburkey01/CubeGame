package com.cjburkey.cubegame;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import com.cjburkey.cubegame.event.EventGameExit;
import com.cjburkey.cubegame.event.EventGameInit;
import com.cjburkey.cubegame.event.EventGamePreInit;
import com.cjburkey.cubegame.event.EventGameRender;
import com.cjburkey.cubegame.event.EventGameUpdate;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;
import com.cjburkey.cubegame.mesh.MeshBasic;
import com.cjburkey.cubegame.object.GameObject;
import com.cjburkey.cubegame.object.MeshFilter;
import com.cjburkey.cubegame.object.Scene;

@EventListener
public class GameHandler {
	
	private static ShaderProgram voxelShader;
	
	private final Vector3f[] vertsTest = new Vector3f[] {
		new Vector3f(0.5f, 0.5f, 0.0f),
		new Vector3f(-0.5f, 0.5f, 0.0f),
		new Vector3f(-0.5f, -0.5f, 0.0f),
		new Vector3f(0.5f, -0.5f, 0.0f)
	};
	
	private final int[] trisTest = new int[] {
		0, 1, 2,
		0, 2, 3
	};
	
	private MeshBasic mesh = new MeshBasic();
	private GameObject meshTestObject;
	private boolean wireFrame = false;
	
	@EventHandler
	private void preinit(EventGamePreInit e) {
		Debug.log("Launching CubeGame");
		
		voxelShader = new ShaderProgram();
		voxelShader.addShader(GL20.GL_VERTEX_SHADER, FileUtil.readFileText("/res/shader/voxel/voxelChunkVert.glsl"));
		voxelShader.addShader(GL20.GL_FRAGMENT_SHADER, FileUtil.readFileText("/res/shader/voxel/voxelChunkFrag.glsl"));
		voxelShader.link();
		voxelShader.bind();
		
		mesh.setMesh(vertsTest, trisTest);
		meshTestObject = Scene.createObject();
		MeshFilter meshFilter = meshTestObject.addComponent(new MeshFilter());
		meshFilter.setMesh(mesh);
	}
	
	@EventHandler
	private void init(EventGameInit e) {
		Debug.log("Initializing CubeGame");
	}
	
	@EventHandler
	private void update(EventGameUpdate e) {
		if (Input.getOnKeyDown(GLFW.GLFW_KEY_Q)) {
			Debug.log("{} wireframe", ((wireFrame) ? "Disabling" : "Enabling"));
			CubeGame.getWindow().setWireframe(wireFrame = !wireFrame);
		}
	}

	@EventHandler
	public void render(EventGameRender e) {
		
	}

	@EventHandler
	private void exit(EventGameExit e) {
		Debug.log("Exiting CubeGame");
	}
	
}