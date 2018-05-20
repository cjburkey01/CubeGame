package com.cjburkey.cubegame;

public final class CubeGame {
	
	private static boolean running;
	private static double deltaTime = 0.0d;
	private static long fps = 0L;
	private static float timer = 0.1f;
	
	private static Window window;
	private static GameHandler gameHandler = new GameHandler();
	
	// Called when the game is launched
	// Be sure to have the VM arg "-XstartOnFirstThread" set for macOS or else GLFW will throw a fit (it's safest to use it on all platforms: Windows, Linux, etc, and not only macOS)
	public static void main(String[] args) {
		Debug.setDefaultThreadError();	// Sets the default error handling procedure for unhandled exceptions
		startGameLoop();
	}
	
	// Starts the game loop and takes over control of the main game handling
	private static void startGameLoop() {
		long lastFrame = System.nanoTime();
		long thisFrame;
		boolean firstTick = true;
		running = true;
		preinit();
		while (running) {
			if (firstTick) {
				init();
				firstTick = false;
			}
			thisFrame = System.nanoTime();
			deltaTime = (thisFrame - lastFrame) / 1000000000.0d;
			fps = (long) (1.0d / deltaTime);
			lastFrame = thisFrame;
			update();
			render();
			if (deltaTime <= 0.000001d) {	// Throttle FPS to reduce CPU time consumption
				try {
					Thread.sleep(1L);
				} catch (Exception e) {
					Debug.exception(e);
				}
			}
		}
		exit();
	}
	
	// Stops the game loop after the current frame finishes
	public static void stopGameLoop() {
		running = false;
	}
	
	// Called before the game loop is begun
	private static void preinit() {
		Debug.log("Launching CubeGame");
		window = new Window("Loading...", 1, 1, true);
		if (!window.isValidWindow()) {
			Debug.error("Window did not initialize");
			stopGameLoop();
			return;
		}
		
		gameHandler.preinit();
	}
	
	// Called on the first frame of the game loop before update() and render()
	private static void init() {
		Debug.log("Initializing CubeGame");
		window.setClearColor(1.0f, 1.0f, 1.0f);
		window.setSizeToHalfMonitor();
		window.center();
		window.show();
		gameHandler.init();
	}
	
	// Called once every frame, used to handle physics and non-rendering functions
	private static void update() {
		Input._onLateUpdateInternal();
		window.pollEvents();
		
		// Show the Delta time and FPS in the window title and update it every 1/10th of a second
		timer += getDeltaTimeF();
		if (timer >= 1.0f) {
			timer -= 0.1f;
			window.setName("CubeGame 0.0.1 | Delta Time: " + Debug.formatDecimal(deltaTime, 6) + " | Estimated FPS: " + Debug.formatDecimal(fps, 0));
		}
		
		gameHandler.update();
		
		// Check whether the user pressed the X button on the window, and if so, begin the exit process after this frame finishes
		if (window.getClosing()) {
			stopGameLoop();
		}
	}
	
	// Called once every frame following update(), used to create meshes and handle shaders
	private static void render() {
		// Clear the current buffer
		window.clearBuffer();
		
		gameHandler.render();
		
		// Show the current buffer
		window.swapBuffers();
	}
	
	// Called after the last frame is updated and rendered when the game is closing
	private static void exit() {
		Debug.log("Exiting CubeGame");
		gameHandler.exit();
		window.destroy();
	}
	
	// Returns how long in seconds the period of time between this frame and the last frame was (in double format)
	public static double getDeltaTime() {
		return deltaTime;
	}
	
	// Returns how long in seconds the period of time between this frame and the last frame was (in float format)
	public static float getDeltaTimeF() {
		return (float) deltaTime;
	}
	
	// Gets the predicted number of frames per second based on the delta time (in long format)
	public static long getFps() {
		return fps;
	}
	
	// Gets the predicted number of frames per second based on the delta time (in int format)	
	public static int getFpsI() {
		return (int) fps;
	}
	
	// Gets the window for the game
	public static Window getWindow() {
		return window;
	}
	
	public static GameHandler getGameHandler() {
		return gameHandler;
	}
	
}