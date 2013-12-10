/**
 * 
 */
package rs.data.file.util;


/**
 * A number generator for long.
 * @author ralph
 *
 */
public class LongKeyGenerator implements IKeyGenerator<Long> {

	/**
	 * The last generated ID.
	 */
	private long lastKey = 0L;
	
	/**
	 * Constructor.
	 */
	public LongKeyGenerator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getNewId() {
		lastKey++;
		return lastKey;
	}

	
}
