package rs.mail.templates.cache;

/**
 * Defines possible Caching strategies.
 * 
 * @author ralph
 *
 */
public enum CacheStrategy {

	/** Least recently used - cleanup entries that are not used for longest time */
	LRU,
	/** Least frequently used - cleanup entries that are used the least */
	LFU,
	/** First In First Out - cleanup entries that are oldest */
	FIFO;
	
}
