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
package rs.baselib.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Extends functionality of {@link ThreadLocal} by allowing to access
 * values from other threads.
 * <p>The need comes from threads that leave behind resources, however cannot
 * detect the problem themselfs.</p>
 * @author ralph
 * @since 1.2.9
 *
 */
public class AdvancedThreadLocal<T> {

	private volatile Map<Thread,T> map = new HashMap<Thread, T>();
	private ReadWriteLock rwLock;
	private Lock readLock;
	private Lock writeLock;

	/**
	 * Constructor.
	 */
	public AdvancedThreadLocal() {
		rwLock    = new ReentrantReadWriteLock();
		readLock  = rwLock.readLock();
		writeLock = rwLock.writeLock();
	}

	/**
	 * Returns the current thread's "initial value" for this
	 * thread-local variable.  This method will be invoked the first
	 * time a thread accesses the variable with the {@link #get}
	 * method, unless the thread previously invoked the {@link #set}
	 * method, in which case the <tt>initialValue</tt> method will not
	 * be invoked for the thread.  Normally, this method is invoked at
	 * most once per thread, but it may be invoked again in case of
	 * subsequent invocations of {@link #remove} followed by {@link #get}.
	 *
	 * <p>This implementation simply returns <tt>null</tt>; if the
	 * programmer desires thread-local variables to have an initial
	 * value other than <tt>null</tt>, <tt>ThreadLocal</tt> must be
	 * subclassed, and this method overridden.  Typically, an
	 * anonymous inner class will be used.
	 *
	 * @return the initial value for this thread-local
	 */
	protected T initialValue() {
		return null;
	}

	/**
	 * Returns the value in the current thread's copy of this thread-local variable. If the variable 
	 * has no value for the current thread, it is first initialized to the value returned by an 
	 * invocation of the {@link #initialValue()} method.
	 * @return the initial value for this thread-local
	 */
	public T get() {
		T rc = null;
		try {
			readLock.lock();
			if (map.containsKey(Thread.currentThread())) {
				rc = map.get(Thread.currentThread());
			} else {
				rc = initialValue();
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
	 * Sets the current thread's copy of this thread-local variable to the specified value. 
	 * Most subclasses will have no need to override this method, relying solely on the 
	 * {@link ThreadLocal#initialValue()} method to set the values of thread-locals.
	 * @param value the value to be stored in the current thread's copy of this thread-local.
	 * @see java.lang.ThreadLocal#set(java.lang.Object)
	 */
	public T set(T value) {
		try {
			writeLock.lock();
			return map.put(Thread.currentThread(), value);
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * Removes the current thread's value for this thread-local variable. If this thread-local 
	 * variable is subsequently read by the current thread, its value will be reinitialized by 
	 * invoking its initialValue method, unless its value is set by the current thread in the 
	 * interim. This may result in multiple invocations of the initialValue method in the 
	 * current thread.
	 * @see java.lang.ThreadLocal#remove()
	 */
	public T remove() {
		try {
			writeLock.lock();
			return map.remove(Thread.currentThread());
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * Returns the current values of this set.
	 * @return the current values
	 */
	public Collection<Map.Entry<Thread, T>> getEntries() {
		try {
			readLock.lock();
			return new HashSet<Map.Entry<Thread, T>>(map.entrySet());
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * Removes all values where the thried died meanwhile.
	 */
	public void verifyThreads() {
		try {
			writeLock.lock();
			Set<Thread> keys = map.keySet();
			Set<Thread> remove = new HashSet<Thread>();
			for (Thread t : keys) {
				if (!t.isAlive() || t.isInterrupted()) {
					remove.add(t);
				}
			}
			for (Thread t : remove) {
				map.remove(t);
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
			map.clear();
		} finally {
			writeLock.unlock();
		}
	}

}
