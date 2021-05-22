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

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.bean.AbstractBean;
import rs.baselib.lang.HashCodeUtil;
import rs.baselib.lang.ReflectionUtils;
import rs.data.api.IDaoFactory;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.util.CID;
import rs.data.util.LockInformation;

/**
 * Abstract Implementation for Business Objects that do not require a Transfer Object.
 * @param <K> type of primary key
 * @author ralph
 *
 */
public abstract class AbstractGeneralBO<K extends Serializable> extends AbstractBean implements IGeneralBO<K> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/** The persistent classes to manage */
	private Class<K> keyClass;
	/** The interface class of this BO. */
	private Class<? extends IGeneralBO<K>> interfaceClass;
	private Logger log = null; // Will be created upon request only
	private boolean invalid;
	private boolean fromDb = false;
	private CID cid;
	private IGeneralDAO<K, ? extends IGeneralBO<K>> dao;
	
	/**
	 * Constructor.
	 */
	public AbstractGeneralBO() {
		init();
	}

	/**
	 * Initializes this BO.
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		List<Class<?>> classes = ReflectionUtils.getTypeArguments(AbstractGeneralBO.class, getClass());
		this.keyClass = (Class<K>) classes.get(0);
		
		for (Class<?> clazz : getClass().getInterfaces()) {
			if (IGeneralBO.class.isAssignableFrom(clazz)) {
				this.interfaceClass = (Class<? extends IGeneralBO<K>>)clazz;
			}
		}
		
		invalid = false;
	}
	
	/**
	 * Returns the DAO factory.
	 * @return the DAO factory
	 */
	protected IDaoFactory getFactory() {
		return getDao().getFactory();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transient
	public IGeneralDAO<K, ? extends IGeneralBO<K>> getDao() {
		return dao;
	}

	/**
	 * Sets the dao.
	 * @param dao the dao to set
	 */
	public void setDao(IGeneralDAO<K, ? extends IGeneralBO<K>> dao) {
		this.dao = dao;
	}

	/**
	 * Returns the keyClass.
	 * @return the keyClass
	 */
	@Transient
	public Class<K> getKeyClass() {
		return keyClass;
	}

	/**
	 * Sets the id of this object.
	 * @param id id of object
	 */
	public abstract void setId(K id);
	
	/**
	 * Returns the cid.
	 * @return the cid
	 */
	@Transient
	public CID getCID() {
		if (cid == null) {
			K id = getId();
			if (id != null) setCID(new CID(getInterfaceClass(), id));
		}
		return cid;
	}

	/**
	 * Returns the interface class of this BO.
	 * @return the BO interface class
	 */
	@Transient
	public Class<? extends IGeneralBO<K>> getInterfaceClass() {
		return interfaceClass;
	}
	
	/**
	 * Sets the cid.
	 * @param cid the cid to set
	 */
	public void setCID(CID cid) {
		this.cid = cid;
	}

	/**
	 * Returns a logger.
	 * @return the logger
	 */
	protected Logger getLog() {
		if (log == null) {
			log = LoggerFactory.getLogger(getClass());
		}
		return log;
	}

	/**
	 * Begins a TX.
	 */
	protected void beginTx() {
		getFactory().begin();
	}
	
	/**
	 * Commits a TX.
	 */
	protected void commitTx() {
		getFactory().commit();
	}
	
	/**
	 * Rolls back the TX.
	 */
	protected void rollbackTx() {
		getFactory().rollback();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transient
	public boolean isChanged() {
		return isDirty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChanged(boolean changed) {
		setDirty(changed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNew() {
		return !fromDb;
	}

	/** 
	 * Marks this object as been loaded from database.
	 * @param fromDb {@code true} when loaded or saved from/to DB
	 */
	@Transient
	public void setFromDb(boolean fromDb) {
		this.fromDb = fromDb;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidate() {
		invalid = true;
		setChanged(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockInformation lock(int timeout) {
		return new LockInformation(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockInformation release() {
		return new LockInformation(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockInformation getLockInformation() {
		return new LockInformation(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInvalid() {
		return invalid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getClass().getSimpleName()+"[id="+getId()+"]";
	}

	/**
	 * {@inheritDoc}
	 * Hash code combining class and id.
	 */
	@Override
	public int hashCode() {
		int rc = HashCodeUtil.SEED;
		rc = HashCodeUtil.hash(rc, getClass());
		rc = HashCodeUtil.hash(rc, getId());
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (this == o) return true;
		if (!getClass().equals(o.getClass())) return false;
		K otherId = ((IGeneralBO<K>)o).getId();
		K thisId = getId();
		if ((otherId == null) || (thisId == null)) return false;
		return thisId.equals(otherId);
	}


}
