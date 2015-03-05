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

import java.beans.PropertyChangeListener;

/**
 * An interface telling that the object informs about property changes.
 * @author ralph
 *
 */
public interface IPropertyChangeProvider {

	/**
	 * Add a change listener.
	 * @param listener the listener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Remove a change listener.
	 * @param listener the listener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Add a change listener.
	 * @param propertyName the property name the listener will be registered for
	 * @param listener the listener to be added
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	/**
	 * Remove a change listener.
	 * @param propertyName the property name the listener will not listen for anymore
	 * @param listener the listener to be removed
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
	
}
