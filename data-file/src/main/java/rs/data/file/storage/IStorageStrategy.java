/*
 * This file is part of RS Library (Data File Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.data.file.storage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import rs.data.api.bo.IGeneralBO;


/**
 * Interface for storing objects.
 * @param <K> type of ID for Business Objects to be managed
 * @param <T> type of Business Object the strategy manages
 * @param <S> type of specifier, e.g. a file
 * @author ralph
 *
 */
public interface IStorageStrategy<K extends Serializable, T extends IGeneralBO<K>, S> {

	/**
	 * Populate the given business object using the specifier.
	 * @param bo the Business Object to be loaded
	 * @param id ID of object
	 * @param specifier a specifier specific to the strategy
	 * @throws IOException when a storage exception occurs
	 */
	public void load(T bo, K id, S specifier) throws IOException;
	
	/**
	 * Save the given business object using the specifier.
	 * @param bo the Business Object to be loaded
	 * @param specifier a specifier specific to the strategy
	 * @throws IOException when a storage exception occurs
	 */
	public void save(T bo, S specifier) throws IOException;
	
	/**
	 * Refresh the given business object.
	 * @param bo business object t be refreshed
	 * @param specifier specifier for the strategy
	 * @throws IOException when a storage exception occurs
	 */
	public void refresh(T bo, S specifier) throws IOException;
	
	/**
	 * Returns the number of objects defined by given list of specifiers.
	 * @param specifiers number of specifiers
	 * @return number of objects that those specifiers define
	 * @throws IOException when a storage exception occurs
	 */
	public int getObjectCount(Collection<S> specifiers) throws IOException;
	
	/**
	 * Returns the number of default objects defined by given list of specifiers.
	 * @param specifiers number of specifiers
	 * @return number of default objects that those specifiers define
	 * @throws IOException when a storage exception occurs
	 */
	public int getDefaultObjectCount(Collection<S> specifiers) throws IOException;
	
	/**
	 * Returns the IDs for each specifier defined by given list of specifiers.
	 * @param specifiers number of specifiers
	 * @return map of keys (of all objects) with assigned specifier
	 * @throws IOException when a storage exception occurs
	 */
	public Map<K, S> getList(Collection<S> specifiers) throws IOException;
	
	/**
	 * Returns the default objects' IDs for each specifier defined by given list of specifiers.
	 * @param specifiers number of specifiers
	 * @return map of keys (of all default objects) with assigned specifier
	 * @throws IOException when a storage exception occurs
	 */
	public Map<K, S> getDefaultList(Collection<S> specifiers) throws IOException;
}
