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
import com.cjburkey.cubegame.mesh.MeshBuilder;
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
		voxelShader.bind();
		
		/*dumbVoxelShader = new ShaderProgram(true);
		dumbVoxelShader.addShader(GL20.GL_VERTEX_SHADER, FileUtil.readFileText("res/shader/dumbVoxel/dumbVoxelChunkVert.glsl"));
		dumbVoxelShader.addShader(GL20.GL_FRAGMENT_SHADER, FileUtil.readFileText("res/shader/dumbVoxel/dumbVoxelChunkFrag.glsl"));
		dumbVoxelShader.link();
		dumbVoxelShader.addUniform("sampler");
		dumbVoxelShader.setUniform("sampler", 0);
		dumbVoxelShader.bind();*/
		
		World world = new World();
		
		for (int x = -8; x < 9; x ++) {
			for (int z = -8; z < 9; z ++) {
				for (int y = -8; y < 9; y ++) {
					MeshVoxel mesh = new MeshVoxel();
					MeshData meshDat = new MeshData();
					MeshBuilder.greedyMeshChunk(meshDat, world.getOrGenerateChunk(new BlockPos(x, y, z)));
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