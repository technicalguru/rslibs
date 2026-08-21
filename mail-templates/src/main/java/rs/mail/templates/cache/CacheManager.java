/**
 * 
 */
package rs.mail.templates.cache;

import java.util.Map;

import rs.mail.templates.cache.impl.CacheEntryMeta;

/**
 * A cache manager is responsible to cleanup a cache to prevent from consuming to much memory.
 * 
 * @author ralph
 *
 */
public interface CacheManager {

	/**
	 * Return the size of cache that triggers a cleanup.
	 * @return the size of cache that triggers a cleanup
	 */
	public int getCleanupThreshold();

	/**
	 * Returns the time in ms between two cleanup tasks.
	 * @return the time in ms between two cleanup tasks
	 */
	public long getMinCleanupLapse();
	
	/**
	 * Performs a cleanup task.
	 * @param <K> the key type of the cache
	 * @param <V> the value type of the cache
	 * @param cache the cache that requires the cleanup
	 * @param meta the meta data of the cache
	 */
	public <K,V> void cleanup(Cache<K,V> cache, Map<K,CacheEntryMeta<K>> meta);
	
}
