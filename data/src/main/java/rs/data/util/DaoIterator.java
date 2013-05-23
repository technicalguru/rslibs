/**
 * 
 */
package rs.data.util;

import java.util.Iterator;

/**
 * @author ralph
 *
 */
public interface DaoIterator<E> extends Iterator<E> {

	/**
	 * This must always be called at the end.
	 */
	public void close();
}