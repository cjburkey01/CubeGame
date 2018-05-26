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
	
	@EventHandler
	public void preinit(EventGamePreInit e) {
	}
	
	public Random getRandom() {
		return random;
	}
	
	public int betweenInc(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}
	
	public static RandomHandler getMainInstance() {
		return mainInstance;
	}
	
}