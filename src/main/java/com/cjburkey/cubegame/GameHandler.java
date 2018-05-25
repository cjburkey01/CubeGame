package com.cjburkey.cubegame;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import com.cjburkey.cubegame.block.BlockPos;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;
import com.cjburkey.cubegame.event.game.EventGameExit;
import com.cjburkey.cubegame.event.game.EventGameInit;
import com.cjburkey.cubegame.event.game.EventGamePreInit;
import com.cjburkey.cubegame.event.game.EventGameRender;
import com.cjburkey.cubegame.event.game.EventGameUpdate;
import com.cjburkey.cubegame.mesh.ChunkMeshBuilder;
import com.cjburkey.cubegame.mesh.MeshData;
import com.cjburkey.cubegame.mesh.MeshVoxel;
import com.cjburkey.cubegame.object.Camera;
import com.cjburkey.cubegame.object.GameObject;
import com.cjburkey.cubegame.object.MeshFilter;
import com.cjburkey.cubegame.object.Scene;
import com.cjburkey.cubegame.world.World;

@EventListener
public class GameHandler {
	
	private static ShaderProgram voxelShader;
	//private static ShaderProgram dumbVoxelShader;
	
	private GameObject meshTestObject;
	private boolean wireFrame = false;
	private FreeMoveController camController;
	
	@EventHandler
	private void preinit(EventGamePreInit e) {
		Debug.log("Launching CubeGame");
		
		// Add a camera to the scene and make it the main camera; also, add the movement handler component to it
		GameObject camObj = Scene.createObject();
		Camera.setMainCamera(camObj.addComponent(new Camera()));
		camController = camObj.addComponent(new FreeMoveController());
		
		voxelShader = new ShaderProgram(true);
		voxelShader.addShader(GL20.GL_VERTEX_SHADER, FileUtil.readFileText("res/shader/voxel/voxelChunkVert.glsl"));
		voxelShader.addShader(GL20.GL_FRAGMENT_SHADER, FileUtil.readFileText("res/shader/voxel/voxelChunkFrag.glsl"));
		voxelShader.link();
		voxelShader.bind();
		
		World world = new World();
		
		for (int x = -8; x < 9; x ++) {
			for (int z = -8; z < 9; z ++) {
				for (int y = -8; y < 9; y ++) {
					MeshVoxel mesh = new MeshVoxel();
					MeshData meshDat = new MeshData();
					ChunkMeshBuilder.greedyMeshChunk(meshDat, world.getOrGenerateChunk(new BlockPos(x, y, z)));
					mesh.setMesh(meshDat);
					meshTestObject = Scene.createObject();
					MeshFilter meshFilter = meshTestObject.addComponent(new MeshFilter());
					meshFilter.setMesh(mesh);
					Debug.log("Generated chunk {}, {}, {}", x, y, z);
				}
			}
		}
	}
	
	@EventHandler
	private void init(EventGameInit e) {
		Debug.log("Initializing CubeGame");
		
		Debug.log(" -- CONTROLS --");
		Debug.log("    - !!! Use WASD and mouse to move and look respectively");
		Debug.log("    - !!! Press escape to free cursor");
		Debug.log("    - !!! Press alt+escape to close game (or click the X on the window)");
		Debug.log("    - !!! Press x to toggle wireframe mode");
		Debug.log(" -- END CONTROLS --");
	}
	
	@EventHandler
	private void update(EventGameUpdate e) {
		if (Input.getOnKeyDown(GLFW.GLFW_KEY_X)) {
			Debug.log("{} wireframe", ((wireFrame) ? "Disabling" : "Enabling"));
			CubeGame.getWindow().setWireframe(wireFrame = !wireFrame);
		}
		if (Input.getOnKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			// Alt+Escape closes the game
			if (Input.getIsKeyPressed(GLFW.GLFW_KEY_LEFT_ALT) || Input.getIsKeyPressed(GLFW.GLFW_KEY_RIGHT_ALT)) {
				CubeGame.stopGameLoop();
				return;
			}
			
			// Escape frees cursor
			camController.lockCursor = !camController.lockCursor;
		}
	}
	
	@EventHandler
	public void render(EventGameRender e) {
	}
	
	@EventHandler
	private void exit(EventGameExit e) {
		Debug.log("Exiting CubeGame");
		
		//dumbVoxelShader.destroy();
		voxelShader.destroy();
	}
	
	public static ShaderProgram getVoxelShader() {
		return voxelShader;
	}
	
	//public static ShaderProgram getDumbVoxelShader() {
	//	return dumbVoxelShader;
	//}
	
}