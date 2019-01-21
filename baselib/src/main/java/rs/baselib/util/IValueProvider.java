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
package rs.baselib.util;

/**
 * Interface for an object that can retrieve a certain
 * property from an object. ValueProviders serve as supporting
 * objects e.g. when comparing objects or providing
 * cell labels in a table.
 * @author ralph
 *
 */
public interface IValueProvider {

	/**
	 * Return the value for the given object.
	 * @param o the object
	 * @return the value from the object.
	 */
	public Object getValue(Object o);
}
