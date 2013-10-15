/**
 * 
 */
package rs.data.file.storage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import rs.data.api.bo.IGeneralBO;


/**
 * Interface for storing objects.
 * @param <S> type of specifier
 * @author ralph
 *
 */
public interface IStorageStrategy<K extends Serializable, T extends IGeneralBO<K>, S> {

	/**
	 * Populate the given business object using the specifier.
	 * @param bo the Business Object to be loaded
	 * @param specifier a specifier specific to the strategy
	 */
	public void load(T bo, K id, S specifier) throws IOException;
	
	/**
	 * Save the given business object using the specifier.
	 * @param bo the Business Object to be loaded
	 * @param specifier a specifier specific to the strategy
	 */
	public void save(T bo, S specifier) throws IOException;
	
	/**
	 * Refresh the given business object.
	 * @param bo business object t be refreshed
	 * @param specifier specifier for the strategy
	 */
	public void refresh(T bo, S specifier) throws IOException;
	
	/**
	 * Returns the number of objects defined by given list of specifiers.
	 * @param specifiers number of specifiers
	 * @return number of objects that those specifiers define
	 */
	public int getObjectCount(Collection<S> specifiers) throws IOException;
	
	/**
	 * Returns the number of default objects defined by given list of specifiers.
	 * @param specifiers number of specifiers
	 * @return number of default objects that those specifiers define
	 */
	public int getDefaultObjectCount(Collection<S> specifiers) throws IOException;
	
	/**
	 * Returns the IDs for each specifier defined by given list of specifiers.
	 * @param specifiers number of specifiers
	 * @return map of keys (of all objects) with assigned specifier
	 */
	public Map<K, S> getList(Collection<S> specifiers) throws IOException;
	
	/**
	 * Returns the default objects' IDs for each specifier defined by given list of specifiers.
	 * @param specifiers number of specifiers
	 * @return map of keys (of all default objects) with assigned specifier
	 */
	public Map<K, S> getDefaultList(Collection<S> specifiers) throws IOException;
}
