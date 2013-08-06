/**
 * 
 */
package rs.data.impl.dao;

import java.io.File;
import java.io.Serializable;

import rs.data.api.bo.IGeneralBO;
import rs.data.impl.bo.AbstractFileBO;
import rs.data.impl.dto.GeneralDTO;

/**
 * Abstract implementation of file-based DAOs.
 * @author ralph
 *
 */
public abstract class AbstractFileDAO<K extends Serializable, T extends GeneralDTO<K>, B extends AbstractFileBO<K, T>, C extends IGeneralBO<K>> extends AbstractDAO<K, T, B, C> {

	/**
	 * Constructor.
	 */
	public AbstractFileDAO() {
	}

	/**
	 * Returns the file that shall have the object with given key.
	 * @param key key of business object
	 * @return the file for this object
	 */
	protected abstract File getFile(K key);
	
	
}
