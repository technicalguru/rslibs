/**
 * 
 */
package rs.baselib.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Implements a thread-safe synchronized object queue that also wakes up waiting threads.
 * @author ralph
 *
 */
public class SynchronizedQueue<E> {

	/** Default number of objects to hold in this queue */
	public static final int MAX_CAPACITY = 100;
	
	/** The object queue */
	protected List<E> available = new LinkedList<>();
	
	/** The maximum number of objects to be held in this queue instance */
	protected int capacity = MAX_CAPACITY;
	
	/**
	 * Constructor.
	 * <p>Will create the queue using {@link SynchronizedQueue#MAX_CAPACITY} as capacity.</p>
	 */
	public SynchronizedQueue() {
		this(MAX_CAPACITY);
	}

	/**
	 * Constructor.
	 * @param capacity - capacity of this queue
	 */
	public SynchronizedQueue(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Delivers the next object from the queue.
	 * <p>The method will block when no object is available.</p>
	 * <p>Used by the reader/consumer thread.</p>
	 * @return next object in queue
	 */
	public synchronized E poll() {
		E rc;
	 
		while (available.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) { }
		}
		rc = available.remove(0);
	 
		notify();
	 
		return rc;
	}

	/**
	 * Adds a new object to the list of available objects.
	 * <p>The method will block when the queue is full.</p>
	 * <p>Used by the writer/producer thread.</p>
	 * @param o - the object to be queued
	 */
	public synchronized void push(E o) {
		while (available.size() >= capacity) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		available.add(o);
	 
		notify();
	}
}
