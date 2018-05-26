package com.cjburkey.cubegame.thread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import com.cjburkey.cubegame.Debug;

public final class ThreadedPoolWorker<T extends IPoolTask> {
	
	public final int threadCount;
	public final String name;
	
	private final Set<Thread> threads = new HashSet<>();
	private final LinkedBlockingDeque<T> queue = new LinkedBlockingDeque<>();
	private boolean running = false;
	private int finished = 0;
	
	public ThreadedPoolWorker(String name, int threadCount) {
		this.name = name;
		this.threadCount = threadCount;
	}
	
	public int getItemsInQueue() {
		return queue.size();
	}
	
	public void start() {
		running = true;
		for (int i = 0; i < threadCount; i ++) {
			Thread thread = new Thread(() -> threadRun());
			thread.start();
			thread.setName("\"" + name + "\" worker " + i);
			threads.add(thread);
		}
		Debug.log("Started thread pool: {}", name);
	}
	
	public void stop() {
		running = false;
		Debug.log("Stopping thread pool: {}", name);
		
		// Safely clear it in case some threads haven't shut down yet
		while (queue.size() > 0) {
			queue.poll();
		}
	}
	
	private void threadRun() {
		// Setup error handling
		Debug.setDefaultThreadError();
		
		// Pick jobs off the list when necessary
		while (running) {
			T task = queue.poll();
			if (task != null) {
				task.execute();
			}
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				Debug.exception(e);
			}
		}
		
		// Mark this thread as closed
		finished ++;
	}
	
	public void addTask(T task) {
		queue.offer(task);
	}
	
}