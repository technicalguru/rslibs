/**
 * 
 */
package rs.mail.templates.cache;

import rs.mail.templates.cache.impl.AbstractCacheManager;
import rs.mail.templates.cache.impl.DefaultCache;
import rs.mail.templates.cache.impl.FifoCacheManager;
import rs.mail.templates.cache.impl.LfuCacheManager;
import rs.mail.templates.cache.impl.LruCacheManager;

/**
 * Create caches.
 * 
 * @author ralph
 *
 */
public class CacheFactory {

	/**
	 * Returns a new cache builder.
	 * @param <K> key type of cache
	 * @param <V> value type of cache
	 * @param keyClass class of the key type
	 * @param valueClass class of the value type
	 * @return a cache builder for the given type of cache
	 */
	public static <K,V> CacheBuilder<K,V> newBuilder(Class<K> keyClass, Class<V> valueClass) {
		return new CacheBuilder<K,V>();
	}
	
	/**
	 * Cache builder implementation.
	 * 
	 * @author ralph
	 *
	 * @param <K> key type of cache
	 * @param <V> value type of cache
	 */
	public static class CacheBuilder<K,V> {
		private CacheStrategy strategy;
		private CacheManager  manager;
		private Integer       cleanupThreshold;
		private Integer       minSizeThreshold;
		private Long          minCleanupLapseMs;
		
		/**
		 * Constructor.
		 */
		public CacheBuilder() {
			this.strategy         = CacheStrategy.LRU;
			this.cleanupThreshold = 30;
		}
		
		/**
		 * Use the strategy for the cache.
		 * @param strategy the strategy to be used.
		 * @return the cache builder itself for method chaining
		 */
		public CacheBuilder<K,V> with(CacheStrategy strategy) {
			this.strategy = strategy;
			return this;
		}
		
		/**
		 * Build the cache using this cache manager implementation.
		 * @param manager the cache manager implementation to be used
		 * @return the cache builder itself for method chaining
		 */
		public CacheBuilder<K,V> with(CacheManager manager) {
			this.manager = manager;
			return this;
		}
		
		/**
		 * Set the cleanup threshold for the cache.
		 * @param cleanupThreshold the cleanup threshold
		 * @return the cache builder itself for method chaining
		 */
		public CacheBuilder<K,V> withCleanupThreshold(int cleanupThreshold) {
			this.cleanupThreshold = cleanupThreshold;
			return this;
		}
		
		/**
		 * Sets the size of tha cache after a cleanup.
		 * @param minSizeThreshold the size after cleanup finished
		 * @return the cache builder itself for method chaining
		 */
		public CacheBuilder<K,V> withMinSizeThreshold(int minSizeThreshold) {
			this.minSizeThreshold = minSizeThreshold;
			return this;
		}
		
		/**
		 * Sets the time in ms that must lapse between to cleanup tasks.
		 * @param minCleanupLapseMs the lapse time in ms
		 * @return the cache builder itself for method chaining
		 */
		public CacheBuilder<K,V> withMinCleanupLapse(long minCleanupLapseMs) {
			this.minCleanupLapseMs = minCleanupLapseMs;
			return this;
		}
		
		/**
		 * Builds the cache.
		 * @return the cache implementation according to the settings
		 */
		public Cache<K,V> build() {
			if (manager == null) {
				switch (strategy) {
				case FIFO: manager = new FifoCacheManager(); break;
				case LFU:  manager = new LfuCacheManager();  break;
				case LRU:  manager = new LruCacheManager();  break;
				}
			}
			
			if (manager instanceof AbstractCacheManager) {
				AbstractCacheManager mgr = (AbstractCacheManager)manager;
				if (cleanupThreshold  != null) mgr.setCleanupThreshold(cleanupThreshold);
				if (minSizeThreshold  != null) mgr.setMinSizeThreshold(minSizeThreshold);
				if (minCleanupLapseMs != null) mgr.setMinCleanupLapse(minCleanupLapseMs);
			}
			
			return new DefaultCache<K,V>(manager);
		}
	}
}
