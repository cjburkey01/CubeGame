package com.cjburkey.cubegame.thread;

@FunctionalInterface
public interface IPoolTask {
	
	void execute();
	boolean equals(Object other);
	
}