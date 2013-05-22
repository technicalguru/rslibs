/**
 * 
 */
package rs.data.util;

/**
 * Different types of lock state.
 * @author ralph
 *
 */
public enum LockState {

	/** The object is not locked */
	UNLOCKED,
	/** The lock could be obtained */
	LOCK_AQUIRED,
	/** The object is locked by someone */
	LOCKED;
	
}
