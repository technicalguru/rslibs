/**
 * 
 */
package rs.data.util;

import rs.baselib.util.RsDate;
import rs.data.api.bo.GeneralBO;

/**
 * Tells information about the lock
 * @author ralph
 *
 */
public class LockInformation {

	private GeneralBO<?> lockObject;
	private LockState lockState = LockState.UNLOCKED;
	private Object lockOwner = null;
	private RsDate expirationDate = null;
	
	/**
	 * Constructor.
	 */
	public LockInformation(GeneralBO<?> lockObject) {
		this.lockObject = lockObject;
	}

	/**
	 * Returns the lockObject.
	 * @return the lockObject
	 */
	public GeneralBO<?> getLockObject() {
		return lockObject;
	}

	/**
	 * Sets the lockObject.
	 * @param lockObject the lockObject to set
	 */
	public void setLockObject(GeneralBO<?> lockObject) {
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
