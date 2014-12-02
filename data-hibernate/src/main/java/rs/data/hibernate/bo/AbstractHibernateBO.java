/*
 * This file is part of RS Library (Data Hibernate Library).
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
package rs.data.hibernate.bo;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import rs.data.hibernate.HibernateDaoMaster;
import rs.data.impl.bo.AbstractBO;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.ObjectDeletedException;

/**
 * Abstract implementation for hibernate BO.
 * @author ralph
 *
 */
public abstract class AbstractHibernateBO<K extends Serializable, T extends GeneralDTO<K>> extends AbstractBO<K, T> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -248221407299395538L;
	private boolean isInitialized = false;

	/**
	 * Constructor.
	 */
	public AbstractHibernateBO() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public AbstractHibernateBO(T transferObject) {
		super(transferObject);
	}

	/**
	 * Returns the DAO master.
	 * @return the DAO master
	 */
	protected HibernateDaoMaster getDaoMaster() {
		return (HibernateDaoMaster)getDao().getDaoMaster();
	}

	/**
	 * Returns a Hibernate session.
	 * @return the session
	 */
	protected Session getSession() {
		return getDaoMaster().getSession();
	}

	/**
	 * Returns the transferObject.
	 * @return the transferObject
	 */
	public T getTransferObject() {
		T t = super.getTransferObject();
		if (!isInitialized) {
			synchronized (this) {
				if (!isInitialized) {
					boolean needsInit = false;
					if (t instanceof HibernateProxy) {
						LazyInitializer initializer = ((HibernateProxy)t).getHibernateLazyInitializer();
						if (initializer != null) needsInit = initializer.isUninitialized();
					}
					if (needsInit) t = initialize();
					isInitialized = true;
				}
			}
		}
		return t;
	}

	/**
	 * Returns the session-attached transferObject.
	 * @return the session attached transferObject
	 */
	@SuppressWarnings("unchecked")
	public T getAttachedTransferObject() {
		// First check whether T is still attached
		T t = super.getTransferObject();
		if (t instanceof HibernateProxy) {
			LazyInitializer initializer = ((HibernateProxy)t).getHibernateLazyInitializer();
			if (initializer != null) {
				if (!initializer.getSession().isOpen()) {
					t = (T)getSession().get(getTransferClass(), getId());
					setTransferObject(t);
				}
			}
			return t;
		}
		T dbObject = (T)getSession().get(getTransferClass(), getId());
		if (dbObject != null) {
			setTransferObject(dbObject);
			return dbObject;
		}
		throw new ObjectDeletedException(this);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public K getId() {
		// Do not initialize transfer object
		return super.getTransferObject().getId();
	}

	/**
	 * Initializes the DTO.
	 */
	@SuppressWarnings("unchecked")
	protected T initialize() {
		T t = super.getTransferObject();
		try {
			beginTx();
			if (t instanceof HibernateProxy) {
				LazyInitializer initializer = ((HibernateProxy)t).getHibernateLazyInitializer();
				if (initializer != null) {
					if (initializer.getSession() == null || !initializer.getSession().isOpen()) {
						initializer.setSession((SessionImplementor)getSession());
					}
					t = (T)initializer.getImplementation();
					super.setTransferObject(t);
				}
			}
			commitTx();
		} catch (Exception e) {
			try {
				rollbackTx();
			} catch (Exception e2) {
				throw new RuntimeException("Cannot initialize DTO", e2);
			}
			throw new RuntimeException("Cannot initialize DTO", e);
		}
		return t;
	}


}
