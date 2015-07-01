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

import java.beans.PropertyChangeEvent;
import java.io.Serializable;

import org.apache.commons.beanutils.PropertyUtils;

import rs.baselib.bean.NamedObject;
import rs.baselib.util.RsDate;
import rs.data.impl.dto.GeneralDTO;

/**
 * Abstract implementation for BOs that are transfer objects itself.
 * @param <K> type of primary key
 * @author ralph
 */
public abstract class AbstractPlainBO<K extends Serializable> extends AbstractGeneralBO<K> {

	private K id;
	private String name;
	private RsDate creationDate;
	private RsDate changeDate;
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public AbstractPlainBO() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public K getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(K id) {
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getCreationDate() {
		return creationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationDate(RsDate creationDate) {
		RsDate oldValue = getCreationDate();
		this.creationDate = creationDate;
		firePropertyChange(CREATION_DATE, oldValue, creationDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getChangeDate() {
		return changeDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChangeDate(RsDate changeDate) {
		RsDate oldValue = getChangeDate();
		this.changeDate = changeDate;
		firePropertyChange(CHANGE_DATE, oldValue, changeDate);
	}
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		if (!(this instanceof NamedObject)) throw new IllegalArgumentException("This is not a NamedObject");
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		String oldValue = getName();
		this.name = name;
		firePropertyChange(NamedObject.NAME, oldValue, name);
	}

	/**
	 * Returns the property of given name.
	 * @param name name of property
	 * @return value of property
	 * @since 1.2.9
	 */
	@SuppressWarnings("unchecked")
	public <X> X getProperty(String name) {
		try {
			return (X)PropertyUtils.getProperty(this, name);
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (Exception e) {
			throw new RuntimeException("Cannot get property: "+name, e);
		}
		return null;
	}
	
	/**
	 * Standard implementation of setter method.
	 * <p>The method retrieves the old value via {@link #getProperty(String)} and sets
	 * the new value through {@link GeneralDTO#setProperty(String, Object)}. Afterwards
	 * it fires a {@link PropertyChangeEvent} for this property name.</p>
	 * @param name name of DTO property and name to be used in {@link PropertyChangeEvent}
	 * @param value value of property
	 * @since 1.2.9
	 */
	public <X> void setProperty(String name, X value) {
		setProperty(name, name, value);
	}
	
	/**
	 * Standard implementation of setter method.
	 * <p>The method retrieves the old value via {@link #getProperty(String)} and sets
	 * the new value through {@link GeneralDTO#setProperty(String, Object)}. Afterwards
	 * it fires a {@link PropertyChangeEvent} with parameter firePropertyName.</p>
	 * @param name name of DTO property 
	 * @param firePropertyName name to be used in {@link PropertyChangeEvent}
	 * @param value new value of property
	 * @since 1.2.9
	 */
	public <X> void setProperty(String name, String firePropertyName, X value) {
		X oldValue = getProperty(name);
		try {
			PropertyUtils.setProperty(this, name, value);
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (Exception e) {
			throw new RuntimeException("Cannot set property: "+name+"="+value, e);
		}
		firePropertyChange(firePropertyName, oldValue, value);
	}
	

}
