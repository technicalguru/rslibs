/**
 * 
 */
package rs.data.file.util;

import java.io.IOException;

import rs.data.api.bo.IGeneralBO;


/**
 * Interface for storing objects.
 * @author ralph
 *
 */
public interface IStorageStrategy {

	/**
	 * Populate the given business object using the specifier.
	 * @param bo the Business Object to be loaded
	 * @param specifier a specifier specific to the strategy
	 */
	public <T extends IGeneralBO<?>> void load(T bo, Object specifier) throws IOException;
	
	/**
	 * Save the given business object using the specifier.
	 * @param bo the Business Object to be loaded
	 * @param specifier a specifier specific to the strategy
	 */
	public <T extends IGeneralBO<?>> void save(T bo, Object specifier) throws IOException;
}
