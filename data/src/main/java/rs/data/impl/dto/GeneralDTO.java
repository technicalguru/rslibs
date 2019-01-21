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
package rs.data.impl.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rs.baselib.bean.NamedObject;
import rs.baselib.type.IIdObject;
import rs.baselib.util.RsDate;
import rs.data.api.bo.IGeneralBO;

/**
 * Abstract Implementation for Data Transfer Objects.
 * @param <K> type of primary key
 * @author ralph
 *
 */
public class GeneralDTO<K extends Serializable> implements Serializable {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> properties = new HashMap<String, Object>();
	
	/**
	 * Constructor.
	 */
	public GeneralDTO() {
	}

	/**
	 * Returns the ID.
	 * @return the ID
	 */
	public K getId() {
		return getProperty(IIdObject.ID);
	}

	/**
	 * Sets the ID.
	 * @param id the ID to set
	 */
	public void setId(K id) {
		setProperty(IGeneralBO.ID, id);
	}

	/**
	 * Returns the creation date.
	 * @return the creation date
	 */
	public RsDate getCreationDate() {
		return getProperty(IGeneralBO.CREATION_DATE);
	}

	/**
	 * Sets the creation date.
	 * @param creationDate the creation date to set
	 */
	public void setCreationDate(RsDate creationDate) {
		setProperty(IGeneralBO.CREATION_DATE, creationDate);
	}

	/**
	 * Returns the change date.
	 * @return the change date
	 */
	public RsDate getChangeDate() {
		return getProperty(IGeneralBO.CHANGE_DATE);
	}

	/**
	 * Sets the change date.
	 * @param changeDate the change date to set
	 */
	public void setChangeDate(RsDate changeDate) {
		setProperty(IGeneralBO.CHANGE_DATE, changeDate);
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return getProperty(NamedObject.NAME);
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		setProperty(NamedObject.NAME, name);
	}

	/**
	 * Returns the property value.
	 * @param name name of property
	 * @param <T> the type of the value
	 * @return the value of the property
	 */
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String name) {
		return (T)properties.get(name);
	}
	
	/**
	 * Sets the property with given name.
	 * @param name name of property
	 * @param value new value of property
	 */
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}
	
	/**
	 * Returns the {@link #properties}.
	 * <p>This map can be incomplete as it doesn't contain properties that are handled through
	 * custom fields of the object.</p>
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

}
