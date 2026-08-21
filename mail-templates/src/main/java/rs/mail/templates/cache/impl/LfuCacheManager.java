/**
 * 
 */
package rs.mail.templates.cache.impl;

import java.util.Comparator;

/**
 * Removes entries from the cache the were least frequently used.
 * 
 * @author ralph
 *
 */
public class LfuCacheManager extends AbstractCacheManager {

	/**
	 * Constructor.
	 * @see AbstractCacheManager#DEFAULT_CLEANUP_THRESHOLD
	 * @see AbstractCacheManager#DEFAULT_MIN_SIZE_THRESHOLD
	 * @see AbstractCacheManager#DEFAULT_CLEANUP_LAPSE
	 */
	public LfuCacheManager() {
		super();
	}

	/**
	 * Constructor.
	 * @param cleanupThreshold - size of cache that triggers a cleanup
	 * @param minSizeThreshold - size of cache after cleanup
	 * @param minCleanupLapse - time in ms between two cleanup tasks
	 */
	public LfuCacheManager(int cleanupThreshold, int minSizeThreshold, long minCleanupLapse) {
		super(cleanupThreshold, minSizeThreshold, minCleanupLapse);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected <K> Comparator<CacheEntryMeta<K>> getMetaComparator() {
		return new Comparator<CacheEntryMeta<K>>() {
			@Override
			public int compare(CacheEntryMeta<K> o1, CacheEntryMeta<K> o2) {
				if (o1.getUseCounter() < o2.getUseCounter()) return -1;
				if (o1.getUseCounter() > o2.getUseCounter()) return 1;
				return 0;
			}
			
		};
	}
}
