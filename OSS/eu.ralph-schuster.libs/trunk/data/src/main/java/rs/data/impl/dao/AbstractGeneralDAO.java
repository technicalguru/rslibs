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
package rs.data.impl.dao;

import java.io.Serializable;
import java.util.List;

import rs.baselib.lang.ReflectionUtils;
import rs.data.api.bo.IGeneralBO;
import rs.data.impl.bo.AbstractGeneralBO;

/**
 * Abstract Implementation for Data Access Objects that can create BO instances.
 * This implementation assumes that the BO implementation derives from {@link AbstractGeneralBO}.
 * @param <K> type of primary key
 * @param <B> type of Business Object Implementation
 * @param <C> type of Business Object Interface
 * @author ralph
 *
 */
public abstract class AbstractGeneralDAO<K extends Serializable, B extends AbstractGeneralBO<K>, C extends IGeneralBO<K>> extends AbstractBasicDAO<K, C> {

	/** The persistent class to manage */
	private Class<B> boImplementationClass;

	/**
	 * Constructor.
	 */
	public AbstractGeneralDAO() {
		init();
	}

	/**
	 * Initializes / called from constructor.
	 */
	@SuppressWarnings("unchecked")
	protected void init() {
		super.init();
		List<Class<?>> classes = ReflectionUtils.getTypeArguments(AbstractGeneralDAO.class, getClass());
		this.boImplementationClass = (Class<B>) classes.get(1);		
	}

	/**
	 * Returns the boImplementationClass.
	 * @return the boImplementationClass
	 */
	protected Class<B> getBoImplementationClass() {
		return boImplementationClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected C _newInstance() {
		try {
			return (C)getBoImplementationClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create Business Object", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void afterCreate(C object) {
		super.afterCreate(object);
		((B)object).setFromDb(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void afterSave(C object) {
		super.afterSave(object);
		((B)object).setFromDb(true);
	}

	
}
