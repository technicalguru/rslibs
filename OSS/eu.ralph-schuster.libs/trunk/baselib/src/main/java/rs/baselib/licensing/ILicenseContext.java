/*
 * This file is part of RS Library (Base Library).
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
package rs.baselib.licensing;

/**
 * A container holding properties for creation and verification of licenses.
 * @author ralph
 *
 */
public interface ILicenseContext {

	/**
	 * Returns the property with given key.
	 * @param key key of property
	 * @return the value or <code>null</code> if the property doesn't exist
	 */
	public Object get(String key);
	
	/**
	 * Returns the property of given class.
	 * The class parameter will be the key of the property. 
	 * @param clazz type of property
	 * @return the value or <code>null</code> if the property doesn't exist
	 */
	public <T> T get(Class<T> clazz);
	
	/**
	 * Returns whether the context has such a value.
	 * @param clazz the clazz to look for (name is key)
	 * @return <code>true</code> when context has this key
	 */
	public boolean hasKey(Class<?> clazz);
	
	/**
	 * Returns whether the context has such a value.
	 * @param key the key to look for
	 * @return <code>true</code> when context has this key
	 */
	public boolean hasKey(String key);
	
	/**
	 * Sets the property.
	 * @param key key of property
	 * @param value value
	 */
	public void set(String key, Object value);
	
	/**
	 * Sets the property.
	 * The property key will be the name of the class parameter.
	 * @param clazz class of property
	 * @param value value
	 */
	public <T> void set(Class<T> clazz, T value);
	
	/**
	 * Removes the property with given key.
	 * @param key key of property
	 * @return the value that was removed
	 */
	public Object remove(String key);
	
	/**
	 * Removes the property of given class.
	 * The key of the removed property is the class parameter name.
	 * @param clazz class of property
	 * @return the value that was removed
	 */
	public <T> T remove(Class<T> clazz);
	
	/**
	 * Returns the property keys.
	 * @return an iterable of all property keys
	 */
	public Iterable<String> getKeys();
}
