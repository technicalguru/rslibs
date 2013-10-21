/**
 * 
 */
package rs.baselib.util;

import java.util.Iterator;

/**
 * Wraps an {@link Iterator}.
 * @author ralph
 *
 */
public class IterableImpl<T> implements Iterable<T> {

	private Iterator<T> iterator;
	/**
	 * Constructor.
	 */
	public IterableImpl(Iterator<T> iterator) {
		this.iterator = iterator;
	}

	@Override
	public Iterator<T> iterator() {
		return iterator;
	}

}
