/**
 * 
 */
package rs.data.util;

import java.util.Iterator;

/**
 * An iterator implementation wrapping another iterator.
 * @author ralph
 *
 */
public class DaoIteratorImpl<E> implements IDaoIterator<E> {

	private Iterator<E> iterator;
	
	/**
	 * Constructor.
	 */
	public DaoIteratorImpl(Iterator<E> iterator) {
		this.iterator = iterator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() {
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		iterator.remove();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		if (iterator instanceof IDaoIterator) {
			((IDaoIterator<E>)iterator).close();
		}
	}

	
}
