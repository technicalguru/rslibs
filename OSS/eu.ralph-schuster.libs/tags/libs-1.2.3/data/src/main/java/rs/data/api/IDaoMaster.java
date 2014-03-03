/*
 * This file is part of RS Library (Data Base Library).
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
package rs.data.api;

/**
 * A general DAO master interface.
 * @author ralph
 *
 */
public interface IDaoMaster {

	/**
	 * Returns the factory that this master is registered for.
	 * @return the factory that this DAO master is assigned to.
	 */
	public IDaoFactory getFactory();
	

	/**
	 * Sets the factory that this master is registered for.
	 * @param factory the factory
	 */
	public void setFactory(IDaoFactory factory);
	
	/**
	 * Return the value of the given property.
	 * @param key key of value
	 * @return value
	 */
	public String getProperty(String key);
	
	/**
	 * Sets a property
	 * @param key key of property
	 * @param value value of property
	 */
	public void setProperty(String key, String value);
}
