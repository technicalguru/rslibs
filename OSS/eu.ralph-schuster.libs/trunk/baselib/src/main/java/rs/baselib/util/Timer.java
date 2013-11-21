/**
 * 
 */
package rs.baselib.util;

/**
 * A class executing a separate thread until shutdown was requested.
 * @author ralph
 *
 */
public class Timer extends Thread {
	
	/** The time between two executions of the {@link #runnable} */
	private long sleepTime;
	/** The task to be run */
	private Runnable runnable;
	/** Whether a shutdwon was requested */
	private volatile boolean shutdownRequested = false;
	/** Whether the timer thread is running */
	private volatile boolean running = false;
	/** An internal synch object */
	private Object SYNCH = new Object();
	/** Whether the timer shall abort when an error occurs in the task */
	private boolean stopOnError = false;
	
	/**
	 * Constructor.
	 * @param sleepTime time between two executions
	 * @param runnable the task to be run
	 */
	public Timer(long sleepTime, Runnable runnable) {
		this.sleepTime = sleepTime;
		this.runnable = runnable;
	}
	
	/** Runs the timer */
	public void run() {
		boolean doRun = true;
		synchronized (SYNCH) {
			running = true;
		}
		while (doRun) {
			try {
				runnable.run();
			} catch (Throwable t) {
				if (isStopOnError()) break;
			}
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				// Ignore
			}
			synchronized (SYNCH)  {
				if (shutdownRequested) {
					doRun = false;
				}
			}				
		}
		synchronized (SYNCH) {
			running = false;
		}
	}
	
	/**
	 * Shutsdown the timer.
	 */
	public void shutdown() {
		synchronized (SYNCH) {
			shutdownRequested = true;
		}
		while (isRunning()) {
			try {
				sleep(200);
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	/**
	 * Returns whether the timer is running or not.
	 * @return
	 */
	public boolean isRunning() {
		boolean rc = false;
		synchronized (SYNCH) {
			rc = running;
		}
		return rc;
	}

	/**
	 * Returns the {@link #stopOnError}.
	 * @return the stopOnError
	 */
	public boolean isStopOnError() {
		return stopOnError;
	}

	/**
	 * Sets the {@link #stopOnError}.
	 * @param stopOnError the stopOnError to set
	 */
	public void setStopOnError(boolean stopOnError) {
		this.stopOnError = stopOnError;
	}
	
	
}
