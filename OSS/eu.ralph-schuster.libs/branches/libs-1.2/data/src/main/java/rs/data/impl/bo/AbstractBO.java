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
import java.util.List;

import javax.persistence.Transient;

import rs.baselib.bean.NamedObject;
import rs.baselib.bean.NoCopy;
import rs.baselib.lang.ReflectionUtils;
import rs.baselib.util.RsDate;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.impl.dao.AbstractDAO;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.CID;

/**
 * Abstract Implementation for Business Objects that require a Transfer Object.
 * @param <K> type of primary key
 * @param <T> type of Transfer Object Implementation
 * @author ralph
 *
 */
public abstract class AbstractBO<K extends Serializable, T extends GeneralDTO<K>> extends AbstractGeneralBO<K> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** The persistent classes to manage */
	private Class<T> transferClass;
	private T transferObject;
	
	/**
	 * Constructor.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public AbstractBO() {
		this(null);
	}

	/**
	 * Constructor.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public AbstractBO(T transferObject) {
		init();
		if (transferObject == null) try {
			transferObject = getTransferClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create empty instance: ", e);
		}
		setTransferObject(transferObject);
	}

	/**
	 * Initializes this BO.
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		List<Class<?>> classes = ReflectionUtils.getTypeArguments(AbstractBO.class, getClass());
		this.transferClass = (Class<T>) classes.get(1);
	}
	
	/**
	 * Returns the transferClass.
	 * @return the transferClass
	 */
	@Transient
	public Class<T> getTransferClass() {
		return transferClass;
	}

	/**
	 * Returns the Transfer Object.
	 * @return transfer object
	 */
	@Transient
	@NoCopy
	public T getTransferObject() {
		return transferObject;
	}
	
	/**
	 * Sets the transfer object.
	 * @param transferObject the object to be set.
	 */
	public void setTransferObject(T transferObject) {
		this.transferObject = transferObject;
		K id = transferObject.getId();
		if (id != null) setCID(new CID(getInterfaceClass(), id));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public K getId() {
		return getTransferObject().getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(K id) {
		getTransferObject().setId(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getCreationDate() {
		return getTransferObject().getCreationDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationDate(RsDate creationDate) {
		RsDate oldValue = getCreationDate();
		getTransferObject().setCreationDate(creationDate);
		firePropertyChange(CREATION_DATE, oldValue, creationDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getChangeDate() {
		return getTransferObject().getChangeDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChangeDate(RsDate changeDate) {
		RsDate oldValue = getChangeDate();
		getTransferObject().setChangeDate(changeDate);
		firePropertyChange(CHANGE_DATE, oldValue, changeDate);
	}
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		if (!(this instanceof NamedObject)) throw new IllegalArgumentException("This is not a NamedObject");
		return getTransferObject().getName();
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		String oldValue = getName();
		getTransferObject().setName(name);
		firePropertyChange(NamedObject.NAME, oldValue, name);
	}
	
	/**
	 * Fetches the BO from the DAO factory.
	 * @param dto the DTO
	 * @return the BO
	 */
	@SuppressWarnings("unchecked")
	protected <X extends Serializable, Y extends GeneralDTO<X>, Z extends IGeneralBO<X>> Z getBusinessObject(Y dto) {
		if (dto == null) return null;
		IGeneralDAO<X, Z> dao = (IGeneralDAO<X, Z>)getFactory().getDaoFor(dto);
		if ((dao == null) || !(dao instanceof AbstractDAO)) throw new RuntimeException("Cannot find DAO for: "+dto);
		return ((AbstractDAO<X, Y, ?, Z>)dao).getBusinessObject(dto);
	}
	
	/**
	 * Returns the DTO from that BO (or null).
	 * @param o the BO
	 * @return the DTO underneath
	 */
	public <X extends Serializable, Y extends GeneralDTO<X>> Y getTransferObject(AbstractBO<X,Y> o) {
		if (o == null) return null;
		return o.getTransferObject();
	}
	
	/**
	 * Returns the property of given name.
	 * @param name name of property
	 * @return value of property
	 * @since 1.2.9
	 */
	public <X> X getProperty(String name) {
		return getTransferObject().getProperty(name);
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
		getTransferObject().setProperty(name, value);
		firePropertyChange(firePropertyName, oldValue, value);
	}
	
}
