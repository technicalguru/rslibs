package rs.mail.templates.cache.impl;

/**
 * Class to hold cacheEntryMeta information.
 * 
 * @param <K> the key type of the cache
 * 
 * @author ralph
 */
public class CacheEntryMeta<K> {
	
	private K    key;
	private long creationTime;
	private long useCounter;
	private long lastUseTime;
	
	/**
	 * Constructor.
	 * @param key the key in the cache
	 */
	public CacheEntryMeta(K key) {
		this.key          = key;
		this.creationTime = DefaultCache.CLOCK.millis();
		this.useCounter   = 0;
		this.lastUseTime  = 0;
	}

	
	/**
	 * Returns the cache entry key.
	 * @return the key
	 */
	public K getKey() {
		return key;
	}


	/**
	 * Returns the time when the entry was created in the cache.
	 * @return the creationTime
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * Returns how often the cache entry was used (hit).
	 * @return the useCounter
	 */
	public long getUseCounter() {
		return useCounter;
	}

	/**
	 * Registers a cache hit for this entry.
	 */
	public void registerHit() {
		useCounter++;
		lastUseTime = DefaultCache.CLOCK.millis();
	}
	
	/**
	 * Returns the last time the cache entry was used (hit).
	 * @return the lastUseTime
	 */
	public long getLastUseTime() {
		return lastUseTime;
	}
	
	
}