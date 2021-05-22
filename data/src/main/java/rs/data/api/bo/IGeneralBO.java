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
package rs.data.api.bo;

import java.beans.Transient;
import java.io.Serializable;

import rs.baselib.bean.IBean;
import rs.baselib.bean.NoCopy;
import rs.baselib.type.IIdObject;
import rs.baselib.util.RsDate;
import rs.data.api.dao.IGeneralDAO;
import rs.data.util.CID;
import rs.data.util.LockInformation;

/**
 * Interface for Business Objects.
 * @param <K> type of primary key
 * @author ralph
 *
 */
public interface IGeneralBO<K extends Serializable> extends Serializable, IBean, IIdObject<K> {
	
	/** 
	 * Property name of creationDate.
	 * @since 1.2.9 
	 */
	public static final String CREATION_DATE = "creationDate";
	/** 
	 * Property name of changeDate.
	 * @since 1.2.9 
	 */
	public static final String CHANGE_DATE = "changeDate";
	/** 
	 * Property name of creationDate.
	 * @deprecated As of release 1.2.9, replaced by {@link #CREATION_DATE}.
	 */
	@Deprecated
	public static final String PROPERTY_CREATION_DATE = CREATION_DATE;
	/** 
	 * Property name of changeDate.
	 * @deprecated As of release 1.2.9, replaced by {@link #CHANGE_DATE}.
	 */
	@Deprecated
	public static final String PROPERTY_CHANGE_DATE = CHANGE_DATE;
	
	/**
	 * Returns the responsible DAO.
	 * @return the DAO that this business object was created by
	 */
	@Transient
	public IGeneralDAO<K, ? extends IGeneralBO<K>> getDao();
	
	/**
	 * Returns the CID.
	 * @return the CID
	 */
	@Transient
	public CID getCID();

	/**
	 * Returns true when this object has not yet been created.
	 * @return true when object is not persisted yet
	 */
	@Transient
	public boolean isNew();
	
	/**
	 * Returns whether this object has changed.
	 * @return true or false
	 */
	@Transient
	public boolean isChanged();
	
	/**
	 * Sets the change status of this object.
	 * @param changed new status
	 */
	public void setChanged(boolean changed);
	
	/**
	 * Returns true when object was invalidated.
	 * @return true or false 
	 */
	@Transient
	public boolean isInvalid();
	
	/**
	 * Invalidates all cached data and orders BO to reload
	 * its data from underlying persistence store.
	 */
	public void invalidate();
	
	/**
	 * Try to lock this object.
	 * @param timeout timeout when trying to lock, 0 for no timeout
	 * @return the new lock information
	 */
	public LockInformation lock(int timeout);
	
	/**
	 * Release the lock.
	 * @return the new lock information.
	 */
	public LockInformation release();
	
	/**
	 * Get the current lock information.
	 * @return the current lock information
	 */
	@Transient
	public LockInformation getLockInformation();
	
	/** Sets the ID. 
	 * @param id the key of the object
	 */
	public void setId(K id);

	/**
	 * Returns the creationDate.
	 * @return the creationDate
	 */
	@NoCopy
	public RsDate getCreationDate();

	/**
	 * Sets the creationDate.
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(RsDate creationDate);

	/**
	 * Returns the changeDate.
	 * @return the changeDate
	 */
	@NoCopy
	public RsDate getChangeDate();

	/**
	 * Sets the changeDate.
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(RsDate changeDate);

}
