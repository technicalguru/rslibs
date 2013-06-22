/**
 * 
 */
package rs.data.api.bo;

import java.io.Serializable;

import rs.baselib.bean.IBean;
import rs.baselib.util.RsDate;
import rs.data.api.dao.IGeneralDAO;
import rs.data.util.LockInformation;

/**
 * Interface for Business Objects.
 * @param <K> type of primary key
 * @author ralph
 *
 */
public interface IGeneralBO<K extends Serializable> extends Serializable, IBean {
	
	public static final String PROPERTY_CREATION_DATE = "creationDate";
	public static final String PROPERTY_CHANGE_DATE = "changeDate";
	
	/**
	 * Returns the responsible DAO.
	 * @return the DAO that this business object was created by
	 */
	public IGeneralDAO<K, ? extends IGeneralBO<K>> getDao();
	
	/**
	 * Returns the id.
	 * @return the id
	 */
	public K getId();

	/**
	 * Returns true when this object has not yet been created.
	 * @return true when object is not persisted yet
	 */
	public boolean isNew();
	
	/**
	 * Returns whether this object has changed.
	 * @return true or false
	 */
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
	public boolean isInvalid();
	
	/**
	 * Invalidates all cached data and orders BO to reload
	 * its data from underlying persistence store.
	 */
	public void invalidate();
	
	/**
	 * Immediately refresh all values from persistence store.
	 */
	public void refresh();
	
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
	public LockInformation getLockInformation();
	
	/**
	 * Returns the creationDate.
	 * @return the creationDate
	 */
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
	public RsDate getChangeDate();

	/**
	 * Sets the changeDate.
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(RsDate changeDate);

}
