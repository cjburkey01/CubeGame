package com.cjburkey.cubegame.event;

public abstract class Event {
	
	protected boolean cancellable = false;
	protected boolean cancelled = false;
	
	public boolean getIsCancellable() {
		return cancellable;
	}
	
	public boolean getIsCancelled() {
		return cancelled;
	}
	
	public void cancel() {
		if (cancellable) {
			cancelled = true;
		}
	}
	
}