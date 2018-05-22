package com.cjburkey.cubegame;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;
import com.cjburkey.cubegame.event.game.EventGameExit;
import com.cjburkey.cubegame.event.game.EventGameInit;
import com.cjburkey.cubegame.event.game.EventGamePreInit;
import com.cjburkey.cubegame.event.game.EventGameRender;
import com.cjburkey.cubegame.event.game.EventGameUpdate;
import com.cjburkey.cubegame.mesh.MeshData;
import com.cjburkey.cubegame.mesh.MeshDumbVoxel;
import com.cjburkey.cubegame.mesh.MeshHelper;
import com.cjburkey.cubegame.object.Camera;
import com.cjburkey.cubegame.object.GameObject;
import com.cjburkey.cubegame.object.MeshFilter;
import com.cjburkey.cubegame.object.Scene;

@EventListener
public class GameHandler {
	
	private static ShaderProgram voxelShader;
	private static ShaderProgram dumbVoxelShader;
	
	// Test mesh thing
	/*private static final Vector3f[] vertsTest = new Vector3f[] {
		new Vector3f(0.5f, 0.0f, 0.5f),		// 0
		new Vector3f(-0.5f, 0.0f, 0.5f),	// 1
		new Vector3f(-0.5f, 0.0f, -0.5f),	// 2
		new Vector3f(0.0f, 0.5f, 0.0f)		// 3
	};
	private static final int[] trisTest = new int[] {
		0, 1, 2,
		3, 1, 0,
		0, 2, 3,
		3, 2, 1
	};*/
	
	private MeshDumbVoxel mesh = new MeshDumbVoxel();
	private GameObject meshTestObject;
	private boolean wireFrame = false;
	
	public static Texture2D texture;	// TODO: Make private
	
	@EventHandler
	private void preinit(EventGamePreInit e) {
		Debug.log("Launching CubeGame");
		
		// Add a camera to the scene and make it the main camera; also, add the movement handler component to it
		GameObject camObj = Scene.createObject();
		Camera.setMainCamera(camObj.addComponent(new Camera()));
		camObj.addComponent(new FreeMoveController());
		
		voxelShader = new ShaderProgram(true);
		voxelShader.addShader(GL20.GL_VERTEX_SHADER, FileUtil.readFileText("res/shader/voxel/voxelChunkVert.glsl"));
		voxelShader.addShader(GL20.GL_FRAGMENT_SHADER, FileUtil.readFileText("res/shader/voxel/voxelChunkFrag.glsl"));
		voxelShader.link();
		voxelShader.addUniform("sampler");
		voxelShader.setUniform("sampler", 0);
		voxelShader.bind();
		
		dumbVoxelShader = new ShaderProgram(true);
		dumbVoxelShader.addShader(GL20.GL_VERTEX_SHADER, FileUtil.readFileText("res/shader/dumbVoxel/dumbVoxelChunkVert.glsl"));
		dumbVoxelShader.addShader(GL20.GL_FRAGMENT_SHADER, FileUtil.readFileText("res/shader/dumbVoxel/dumbVoxelChunkFrag.glsl"));
		dumbVoxelShader.link();
		dumbVoxelShader.addUniform("sampler");
		dumbVoxelShader.setUniform("sampler", 0);
		dumbVoxelShader.bind();
		
		//mesh.setMesh(vertsTest, trisTest);
		MeshData meshDat = new MeshData();
		MeshHelper.addCube(meshDat, new Vector3f(), new Vector2f().zero(), new Vector2f(1.0f, 1.0f));
		mesh.setMesh(meshDat);
		meshTestObject = Scene.createObject();
		meshTestObject.transform.position.z -= 5.0f;
		MeshFilter meshFilter = meshTestObject.addComponent(new MeshFilter());
		meshFilter.setMesh(mesh);
		
		//texture = Texture2D.createTextureFromFile("res/smile.png");
		texture = Texture2D.createTextureFromFile("res/block.png");
	}
	
	@EventHandler
	private void init(EventGameInit e) {
		Debug.log("Initializing CubeGame");
	}
	
	@EventHandler
	private void update(EventGameUpdate e) {
		if (Input.getOnKeyDown(GLFW.GLFW_KEY_X)) {
			Debug.log("{} wireframe", ((wireFrame) ? "Disabling" : "Enabling"));
			CubeGame.getWindow().setWireframe(wireFrame = !wireFrame);
		}
		if (Input.getOnKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			CubeGame.stopGameLoop();
		}
	}
	
	@EventHandler
	public void render(EventGameRender e) {
	}
	
	@EventHandler
	private void exit(EventGameExit e) {
		Debug.log("Exiting CubeGame");
	}
	
	public static ShaderProgram getVoxelShader() {
		return voxelShader;
	}
	
	public static ShaderProgram getDumbVoxelShader() {
		return dumbVoxelShader;
	}
	
}