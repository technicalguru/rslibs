/**
 * 
 */
package rs.data.file.util;

import java.io.Serializable;

/**
 * A generatpor of new keys.
 * @author ralph
 *
 */
public interface IKeyGenerator<K extends Serializable> {

	/**
	 * Returns a new ID.
	 * @return the new id.
	 */
	public K getNewId();
	
}
