/**
 * 
 */
package rs.data.file.util;


/**
 * A number generator for int.
 * @author ralph
 *
 */
public class IntKeyGenerator implements IKeyGenerator<Integer> {

	/**
	 * The last generated ID.
	 */
	private int lastKey = 0;
	
	/**
	 * Constructor.
	 */
	public IntKeyGenerator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getNewId() {
		lastKey++;
		return lastKey;
	}

	
}
