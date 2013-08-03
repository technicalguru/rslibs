/**
 * 
 */
package rs.data.util;

import java.util.Collection;

/**
 * Performs an iteration on a collection.
 * @author ralph
 *
 */
public class DaoCollectionIterator<E> extends DaoIteratorImpl<E> {

	/**
	 * Constructor.
	 */
	public DaoCollectionIterator(Collection<E> collection) {
		super(collection.iterator());
	}

}
