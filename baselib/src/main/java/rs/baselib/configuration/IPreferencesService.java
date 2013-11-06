/**
 * 
 */
package rs.baselib.configuration;

import java.util.concurrent.locks.Lock;
import java.util.prefs.BackingStoreException;

/**
 * The preference service.
 * 
 * @author ralph
 *
 */
public interface IPreferencesService {

	/**
	 * Returns the user preferences for the given application.
	 * @param applicationName name of application
	 * @throws BackingStoreException
	 */
	public IPreferences getUserPreferences(String applicationName) throws BackingStoreException;

	/**
	 * Returns the system preferences for the given application.
	 * @param applicationName name of application
	 * @throws BackingStoreException
	 */
	public IPreferences getSystemPreferences(String applicationName) throws BackingStoreException;
	
	/**
	 * Flushes any changes made in this node.
	 * The method does not return before the flush has been completed.
	 * @param node the node to be flushed
	 * @throws BackingStoreException when there has been a communication problem with the backing store.
	 */
	public void flush(IPreferences node) throws BackingStoreException;
	
	/**
	 * Wait for any modifications to be flushed.
	 * The method does not return before the next flush has been completed (in case modifications are pending).
	 * @param node the node to be flushed
	 * @throws BackingStoreException when there has been a communication problem with the backing store.
	 */
	public void sync(IPreferences node) throws BackingStoreException;
	
	/**
	 * Returns the read lock object for the given node.
	 * @param node the node that requires a read lock
	 * @return the responsible {@link Lock} object
	 */
	public Lock getReadLock(IPreferences node);
	
	/**
	 * Returns the write lock object for the given node.
	 * @param node the node that requires a write lock
	 * @return the responsible {@link Lock} object
	 */
	public Lock getWriteLock(IPreferences node);

}
