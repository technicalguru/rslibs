/**
 * 
 */
package rs.data.api.bo;

import java.io.Serializable;

import rs.data.api.dao.GeneralDAO;
import rs.data.util.LockInformation;
import rsbaselib.bean.IBean;
import rsbaselib.util.RsDate;

/**
 * Interface for Business Objects.
 * @param <K> type of primary key
 * @author ralph
 *
 */
public interface GeneralBO<K extends Serializable> extends Serializable, IBean {
	
	public static final String PROPERTY_CREATION_DATE = "creationDate";
	public static final String PROPERTY_CHANGE_DATE = "changeDate";
	
	/**
	 * Returns the responsible DAO.
	 * @return
	 */
	public GeneralDAO<K, ? extends GeneralBO<K>> getDao();
	
	/**
	 * Returns the id.
	 * @return the id
	 */
	public K getId();

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
