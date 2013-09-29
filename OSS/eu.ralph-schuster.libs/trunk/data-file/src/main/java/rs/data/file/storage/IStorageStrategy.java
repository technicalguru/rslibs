/**
 * 
 */
package rs.data.file.storage;

import java.io.IOException;

import rs.data.api.bo.IGeneralBO;


/**
 * Interface for storing objects.
 * @param <S> type of specifier
 * @author ralph
 *
 */
public interface IStorageStrategy<S> {

	/**
	 * Populate the given business object using the specifier.
	 * @param bo the Business Object to be loaded
	 * @param specifier a specifier specific to the strategy
	 */
	public <T extends IGeneralBO<?>> void load(T bo, S specifier) throws IOException;
	
	/**
	 * Save the given business object using the specifier.
	 * @param bo the Business Object to be loaded
	 * @param specifier a specifier specific to the strategy
	 */
	public <T extends IGeneralBO<?>> void save(T bo, S specifier) throws IOException;
	
	/**
	 * Refresh the given business object.
	 * @param bo business object t be refreshed
	 * @param specifier specifier for the strategy
	 */
	public <T extends IGeneralBO<?>> void refresh(T bo, S specifier) throws IOException;
}
