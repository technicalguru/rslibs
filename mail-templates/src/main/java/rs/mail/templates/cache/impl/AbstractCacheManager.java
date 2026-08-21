/**
 * 
 */
package rs.mail.templates.cache.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import rs.mail.templates.cache.Cache;
import rs.mail.templates.cache.CacheManager;

/**
 * Abstract implementation of manager properties.
 * 
 * @author ralph
 *
 */
public abstract class AbstractCacheManager implements CacheManager {

	/** Default threshold of cache entries before cleaning up */
	public static final int  DEFAULT_CLEANUP_THRESHOLD  = 30;
	/** number of cache entries after cleanup */
	public static final int  DEFAULT_MIN_SIZE_THRESHOLD = 20;
	/** Milliseconds before next cache cleanup will run */
	public static final long DEFAULT_CLEANUP_LAPSE      = 600000;
	
	private int  cleanupThreshold;
	private int  minSizeThreshold;
	private long minCleanupLapse;
	
	/**
	 * Constructor.
	 * @see #DEFAULT_CLEANUP_THRESHOLD
	 * @see #DEFAULT_MIN_SIZE_THRESHOLD
	 * @see #DEFAULT_CLEANUP_LAPSE
	 */
	protected AbstractCacheManager() { 
		this(DEFAULT_CLEANUP_THRESHOLD, DEFAULT_MIN_SIZE_THRESHOLD, DEFAULT_CLEANUP_LAPSE);
	}
	
	/**
	 * Constructor.
	 * @param cleanupThreshold - size of cache that triggers a cleanup
	 * @param minSizeThreshold - size of cache after cleanup
	 * @param minCleanupLapse - time in ms between two cleanup tasks
	 */
	public AbstractCacheManager(int cleanupThreshold, int minSizeThreshold, long minCleanupLapse) {
		this.cleanupThreshold = cleanupThreshold;
		this.minSizeThreshold = minSizeThreshold;
		this.minCleanupLapse = minCleanupLapse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCleanupThreshold() {
		return cleanupThreshold;
	}
	
	/**
	 * Sets the size of cache that triggers a cleanup.
	 * @param cleanupThreshold the size of cache that triggers a cleanup
	 */
	public void setCleanupThreshold(int cleanupThreshold) {
		this.cleanupThreshold = cleanupThreshold;
	}
	
	/**
	 * Returns the size of cache after cleanup.
	 * @return the size of cache after cleanup
	 */
	public int getMinSizeThreshold() {
		return minSizeThreshold;
	}
	
	/**
	 * Sets the size of cache after cleanup.
	 * @param minSizeThreshold the size of cache after cleanup
	 */
	public void setMinSizeThreshold(int minSizeThreshold) {
		this.minSizeThreshold = minSizeThreshold;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMinCleanupLapse() {
		return minCleanupLapse;
	}
	
	/**
	 * Sets the time in ms between two cleanup tasks.
	 * @param minCleanupLapse the time in ms between two cleanup tasks
	 */
	public void setMinCleanupLapse(long minCleanupLapse) {
		this.minCleanupLapse = minCleanupLapse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K,V> void cleanup(Cache<K, V> cache, Map<K,CacheEntryMeta<K>> meta) {
		for (K key : getCleanupPriority(meta)) {
			if (cache.size() <= getMinSizeThreshold()) break;
			cache.remove(key);
		}
	}

	/**
	 * Returns the cache entries in order of priority for cleanup.
	 * @param <K> the key type of the cache
	 * @param <V> the value type of the cache
	 * @param meta the meta data of the cache entries 
	 * @return the priority list of keys to be removed in a cleanup
	 */
	protected <K, V> List<K> getCleanupPriority(Map<K,CacheEntryMeta<K>> meta) {
		List<CacheEntryMeta<K>> sorted = new ArrayList<>(meta.values());
		sorted.sort(getMetaComparator());
		return sorted.stream().map(e -> e.getKey()).collect(Collectors.toList());
	}
	
	/**
	 * A comparator that can sort the cache's meta data in order of priority.
	 * <p>Descendants only need to implement this method in order to achieve a cleanup.</p>
	 * @param <K> the key type of the cache
	 * @return the comparator to be used to sort the meta data
	 */
	protected abstract <K> Comparator<CacheEntryMeta<K>> getMetaComparator();
}
