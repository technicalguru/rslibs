/*
 * This file is part of RS Library (Data Base Library).
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
package rs.data.util;

import rs.baselib.util.RsDate;
import rs.data.api.bo.IGeneralBO;

/**
 * Tells information about the lock
 * @author ralph
 *
 */
public class LockInformation {

	private IGeneralBO<?> lockObject;
	private LockState lockState = LockState.UNLOCKED;
	private Object lockOwner = null;
	private RsDate expirationDate = null;
	
	/**
	 * Constructor.
	 */
	public LockInformation(IGeneralBO<?> lockObject) {
		this.lockObject = lockObject;
	}

	/**
	 * Returns the lockObject.
	 * @return the lockObject
	 */
	public IGeneralBO<?> getLockObject() {
		return lockObject;
	}

	/**
	 * Sets the lockObject.
	 * @param lockObject the lockObject to set
	 */
	public void setLockObject(IGeneralBO<?> lockObject) {
		this.lockObject = lockObject;
	}

	/**
	 * Returns the lockState.
	 * @return the lockState
	 */
	public LockState getLockState() {
		return lockState;
	}

	/**
	 * Sets the lockState.
	 * @param lockState the lockState to set
	 */
	public void setLockState(LockState lockState) {
		this.lockState = lockState;
	}

	/**
	 * Returns the lockOwner.
	 * @return the lockOwner
	 */
	public Object getLockOwner() {
		return lockOwner;
	}

	/**
	 * Sets the lockOwner.
	 * @param lockOwner the lockOwner to set
	 */
	public void setLockOwner(Object lockOwner) {
		this.lockOwner = lockOwner;
	}

	/**
	 * Returns the expirationDate.
	 * @return the expirationDate
	 */
	public RsDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Sets the expirationDate.
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(RsDate expirationDate) {
		this.expirationDate = expirationDate;
	}

}
