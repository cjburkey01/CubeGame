package com.cjburkey.cubegame.event.window;

import com.cjburkey.cubegame.Window;

public class EventWindowResize {
	
	public final Window window;
	public final int width;
	public final int height;
	
	public EventWindowResize(Window window, int width, int height) {
		this.window = window;
		this.width = width;
		this.height = height;
	}
	
}