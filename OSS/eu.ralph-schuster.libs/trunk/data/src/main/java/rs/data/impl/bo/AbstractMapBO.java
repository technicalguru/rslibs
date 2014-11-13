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
package rs.data.impl.bo;

import java.io.Serializable;

import rs.data.impl.dto.GeneralDTO;

/**
 * A simplified BO storing its data in a HashMap.
 * <p>The save/load mechanism of this BO implementation is externalized and used e.g. for
 * file-based storage.</p> 
 * @author ralph
 * @deprecated As of release 1.2.9, functionality is integrated in {@link AbstractBO}.
 */
@Deprecated
public abstract class AbstractMapBO<K extends Serializable> extends AbstractBO<K, GeneralDTO<K>> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 */
	public AbstractMapBO() {
		this (new GeneralDTO<K>());
	}

	/**
	 * Constructor.
	 */
	public AbstractMapBO(GeneralDTO<K> transferObject) {
		super(transferObject);
	}

	/**
	 * Returns the data from the map.
	 * @param key key of data
	 * @return data value
	 */
	protected Object getData(String key) {
		return getProperty(key);
	}
	
	/**
	 * Sets the data value in the map and fires the property change.
	 * @param key key of data
	 * @param value value of data
	 */
	protected void setData(String key, Object value) {
		setProperty(key, value);
	}
	
}
