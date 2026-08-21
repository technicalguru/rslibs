package rs.mail.templates.cache.impl;

import java.time.Clock;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rs.mail.templates.cache.Cache;
import rs.mail.templates.cache.CacheManager;

/**
 * Defines a cache using various cache (cleanup) strategies.
 * This class is thread-safe.
 * 
 * @author ralph
 *
	 * @param <K> - key class
	 * @param <V> - value class
 */
public class DefaultCache<K, V> implements Cache<K, V> {

	/** clock implementation for easier time access */
	protected static Clock CLOCK = Clock.systemDefaultZone();
	
	private Map<K, V>    entries;
	private Map<K, CacheEntryMeta<K>> meta;
	private CacheManager cacheManager;
	private int          cleanupThreshold;
	private long         lastCleanupTime;
	private long         minCleanupLapse;
	
	/**
	 * Constructor.
	 * @param cacheManager the cache manager implementation to be used
	 */
	public DefaultCache(CacheManager cacheManager) {
		this.cacheManager     = cacheManager;
		this.entries          = new HashMap<>();
		this.meta             = new HashMap<>();
		this.cleanupThreshold = cacheManager.getCleanupThreshold();
		this.minCleanupLapse  = cacheManager.getMinCleanupLapse();
		this.lastCleanupTime  = 0L;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		synchronized (meta) {
			return entries.size();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		synchronized (meta) {
			return entries.isEmpty();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(K key) {
		synchronized (meta) {
			_cleanup();
			boolean rc = entries.containsKey(key);
			if (rc) {
				meta.get(key).registerHit();
			}
			return rc;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsValue(V value) {
		synchronized (meta) {
			_cleanup();
			for (Map.Entry<K,V> entry : entries.entrySet()) {
				if (entry.getValue().equals(value)) {
					meta.get(entry.getKey()).registerHit();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(K key) {
		synchronized (meta) {
			_cleanup();
			V rc = entries.get(key);
			if (rc != null) meta.get(key).registerHit();
			return rc;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value) {
		synchronized (meta) {
			_cleanup();
			V rc = entries.put(key, value);
			meta.put(key, new CacheEntryMeta<K>(key));
			return rc;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V remove(K key) {
		synchronized (meta) {
			V rc = entries.remove(key);
			meta.remove(key);
			return rc;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		synchronized (meta) {
			entries.clear();
			meta.clear();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<K> keySet() {
		synchronized (meta) {
			_cleanup();
			return entries.keySet();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<V> values() {
		synchronized (meta) {
			return entries.values();
		}
	}

	/**
	 * @return the meta
	 */
	protected Map<K, CacheEntryMeta<K>> getMeta() {
		return meta;
	}

	/**
	 * Internal cleanup method that performs the check on threshold and lapse times.
	 */
	protected void _cleanup() {
		if ((size() > cleanupThreshold) && (CLOCK.millis() - lastCleanupTime > minCleanupLapse)) {
			cleanup();
		}
	}
	
	/**
	 * Performs a cleanup unconditionally.
	 */
	public void cleanup() {
		synchronized (meta) {
			cacheManager.cleanup(this, meta);
			lastCleanupTime = CLOCK.millis();
		}
	}
}
