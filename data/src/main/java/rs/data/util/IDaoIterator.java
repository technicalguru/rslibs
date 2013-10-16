/**
 * 
 */
package rs.data.util;

import java.util.Iterator;

/**
 * An iterator holding DAO resources.
 * @author ralph
 *
 */
public interface IDaoIterator<E> extends Iterator<E> {

	/**
	 * This must always be called at the end.
	 */
	public void close();
}
