/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Alternative implementation of {@link ThreadLocal} that can
 * verify Threads' aliveness and remove those threads from
 * the object.
 * 
 * @author ralph
 *
 */
public class AltThreadLocal<T> {

	private Map<Thread, T> localMap;
	private ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
	private Lock readLock = rwLock.readLock();
	private Lock writeLock = rwLock.writeLock();

	/**
	 * Constructor.
	 */
	public AltThreadLocal() {
		localMap = new HashMap<Thread, T>();
	}

	/**
	 * Returns the object associated with current thread.
	 * <p>If the thread is not yet associated with an object
	 * then {@link #getInitialValue()} will be used to create
	 * a new object for the current thread.</p>
	 * @return the object associated with current thread.
	 */
	public T get() {
		T rc = null;
		try {
			readLock.lock();
			if (localMap.containsKey(Thread.currentThread())) {
				rc = localMap.get(Thread.currentThread());
			} else {
				rc = getInitialValue();
				try {
					readLock.unlock();
					set(rc);
				} finally {
					readLock.lock();
				}
			}
		} finally {
			readLock.unlock();
		}
		return rc;
	}
	
	/**
	 * Sets the object as value for the current thread.
	 * @param t the object to associate.
	 * @return the object previously associated with current thread
	 */
	public T set(T t) {
		try {
			writeLock.lock();
			return localMap.put(Thread.currentThread(), t);
		} finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * Removes the object associated with current thread.
	 * @return the object that was associated previously
	 */
	public T remove() {
		try {
			writeLock.lock();
			return localMap.remove(Thread.currentThread());
		} finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * Returns an initial value when no object has yet been
	 * associated.
	 * <p>This implementation returns {@code null}.</p>
	 * @return initial value
	 * @see #get()
	 */
	protected T getInitialValue() {
		return null;
	}
	
	/**
	 * Verifies all values whether threads have died. If so, those
	 * threads are removed from the map.
	 */
	public void verifyThreads() {
		try {
			writeLock.lock();
			Set<Thread> keys = localMap.keySet();
			for (Thread t : keys) {
				if (!t.isAlive() || t.isInterrupted()) {
					localMap.remove(t);
				}
			}
		} finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * Clears all values on all threads.
	 */
	public void clear() {
		try {
			writeLock.lock();
			localMap.clear();
		} finally {
			writeLock.unlock();
		}
	}
	

}
