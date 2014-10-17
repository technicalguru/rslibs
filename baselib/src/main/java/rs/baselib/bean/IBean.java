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
package rs.baselib.bean;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import javax.persistence.Transient;

import rs.baselib.util.IDirtyable;


/**
 * A general interface for bean support.
 * @author ralph
 *
 */
public interface IBean extends IPropertyChangeProvider, IDirtyable {

	/**
	 * Returns the property names of this bean.
	 * @return the property names
	 */
	@Transient
	public Iterable<String> getPropertyNames();
	
	/**
	 * Set the property with given name to the value
	 * @param name property name
	 * @param value value
	 */
	public void set(String name, Object value);

	/**
	 * Gets the property with given name
	 * @param name property name
	 */
	public Object get(String name);

	/**
	 * Returns the list of changes that this bean has performed since loading.
	 * @return the list of changes so far
	 */
	@Transient
	public Collection<PropertyChangeEvent> getChanges();

	/**
	 * Copies all properties to the given object.
	 * @param destination destination object
	 */
	public void copyTo(Object destination);

	/**
	 * Reset all changes.
	 */
	public void reset();
	
}
