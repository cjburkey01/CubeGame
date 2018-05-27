package com.cjburkey.cubegame;

import java.util.Random;
import com.cjburkey.cubegame.event.EventHandler;
import com.cjburkey.cubegame.event.EventListener;
import com.cjburkey.cubegame.event.game.EventGamePreInit;

@EventListener
public class RandomHandler {
	
	private static RandomHandler mainInstance;
	
	private Random random;
	
	public RandomHandler() {
		mainInstance = this;
		random = new Random();
	}
	
	// Forces this class to be instantiated when the game begins, even though there is no event.
	// Makes sure "mainInstance" is assigned
	@EventHandler
	public void preinit(EventGamePreInit e) {
	}
	
	public Random getRandom() {
		return random;
	}
	
	public int betweenIncI(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}
	
	public int betweenExcI(int min, int max) {
		return random.nextInt(max - min) + min;
	}
	
	public float betweenExcF(float min, float max) {
		return random.nextFloat() * (max - min) + min;
	}
	
	public static RandomHandler getMainInstance() {
		return mainInstance;
	}
	
}