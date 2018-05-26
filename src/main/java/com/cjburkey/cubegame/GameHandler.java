package com.cjburkey.cubegame;

import org.joml.Vector3f;
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
import com.cjburkey.cubegame.mesh.DirectionalLight;
import com.cjburkey.cubegame.object.Camera;
import com.cjburkey.cubegame.object.GameObject;
import com.cjburkey.cubegame.object.Scene;
import com.cjburkey.cubegame.world.World;

@EventListener
public class GameHandler {
	
	public static final int testRadius = 8;
	
	private static ShaderProgram voxelShader;
	//private static ShaderProgram dumbVoxelShader;
	
	private boolean wireFrame = false;
	private boolean drawDoubleFace = false;
	private FreeMoveController camController;
	private World world;
	
	public static final DirectionalLight sun = new DirectionalLight(new Vector3f(0.5f, 0.5f, 0.5f), /*new Vector3f(1.0f, -5.0f, 2.0f).normalize()*/new Vector3f(0.0f, -1.0f, 0.0f), new Vector3f(0.8f, 0.8f, 0.8f), 0.85f, 0.0f, 0.0f);
	
	@EventHandler
	private void preinit(EventGamePreInit e) {
		Debug.log("Launching CubeGame");
		
		// Add a camera to the scene and make it the main camera; also, add the movement handler component to it
		GameObject camObj = Scene.createObject();
		Camera.setMainCamera(camObj.addComponent(new Camera()));
		Camera.getMainCamera().setFovDegrees(90.0f);
		camObj.transform.position.set(0.0f, 50.0f, 0.0f);
		camController = camObj.addComponent(new FreeMoveController());
		
		voxelShader = new ShaderProgram(true);
		voxelShader.addShader(GL20.GL_VERTEX_SHADER, FileUtil.readFileText("res/shader/voxel/voxelChunkVert.glsl"));
		voxelShader.addShader(GL20.GL_FRAGMENT_SHADER, FileUtil.readFileText("res/shader/voxel/voxelChunkFrag.glsl"));
		voxelShader.link();
		voxelShader.addUniform("ambientLight");
		voxelShader.addUniform("dirLightDirection");
		voxelShader.addUniform("dirLightColor");
		voxelShader.addUniform("dirLightIntensity");
		voxelShader.addUniform("specularPower");
		voxelShader.addUniform("reflectance");
		voxelShader.bind();
		
		world = new World();
		
		for (int x = -testRadius; x <= testRadius; x ++) {
			for (int z = -testRadius; z <= testRadius; z ++) {
				for (int y = -testRadius; y <= testRadius; y ++) {
					// Create chunk mesh
					world.getChunkOrCreateScheduleGenerate(new BlockPos(x, y, z), true);
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
		Debug.log("    - !!! Press z to toggle double face mode");
		Debug.log(" -- END CONTROLS --");
	}
	
	@EventHandler
	private void update(EventGameUpdate e) {
		// Sun lighting
		voxelShader.setUniform("ambientLight", sun.ambientLight);
		voxelShader.setUniform("dirLightDirection", sun.direction);
		voxelShader.setUniform("dirLightColor", sun.color);
		voxelShader.setUniform("dirLightIntensity", sun.intensity);
		voxelShader.setUniform("specularPower", sun.specularPower);
		voxelShader.setUniform("reflectance", sun.reflectance);
		
		if (Input.getOnKeyDown(GLFW.GLFW_KEY_X)) {
			Debug.log("{} wireframe", ((wireFrame) ? "Disabling" : "Enabling"));
			CubeGame.getWindow().setWireframe(wireFrame = !wireFrame);
		}
		if (Input.getOnKeyDown(GLFW.GLFW_KEY_Z)) {
			Debug.log("{} double face mode", ((drawDoubleFace) ? "Disabling" : "Enabling"));
			CubeGame.getWindow().setDrawBothFaces(drawDoubleFace = !drawDoubleFace);
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
		
		voxelShader.destroy();
	}
	
	public static ShaderProgram getVoxelShader() {
		return voxelShader;
	}
	
}