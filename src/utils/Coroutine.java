package utils;
// Coroutine<T> allows me to stop a thread and yield a value.
// I can then later resume the thread for another value.
// Implementing a coroutine using a thread mostly defeats the
// Point of a Coroutine<T> but ¯\_(ツ)_/¯
// ...also i didn't want to make mergesort iterative :/...
abstract public class Coroutine<T> extends Thread {
	// Volatile insures that all threads see a change
	// in these values.
	private volatile T value;
	private volatile boolean running;
	private volatile boolean killed;
	private volatile boolean suppended;
	
	public T getValue()
		{ return value; }
	public boolean isRunning() 
		{ return running; }
	public void setRunning(boolean set)
		{ running = set; }
	public boolean isSuppended() 
		{ return suppended; }
	
	public abstract void run();
	
	public synchronized void yield(T v) throws InterruptedException {
		value = v;
		suppended = true;
		// Wait until notified that something changed
		// it can either be unyielding or killing
		// The derived class must handle the killed state.
		while(suppended && !killed)
			this.wait();
	}
	
	// Safely stops a thread
	public synchronized void kill() {
		killed = true;
		running = false;
		suppended = false;
		this.notify();
	}
	
	// unyield releases a coroutine from it's suspended state
	public synchronized void unyield() {
		suppended = false;
		this.notify();
	}
	
}
