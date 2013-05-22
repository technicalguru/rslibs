/**
 * 
 */
package rs.data.api;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import rs.data.api.bo.GeneralBO;
import rs.data.api.dao.GeneralDAO;
import rs.data.event.DaoFactoryListener;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.URLTransformer;

/**
 * A general interface describing a DAO factory.
 * @author ralph
 *
 */
public interface DaoFactory {

	/**
	 * Returns the factory property.
	 * @param name name of property
	 * @return value or NULL
	 */
	public Object getProperty(String name);

	/**
	 * Sets the property.
	 * @param name name of property
	 * @param value value of property
	 */
	public void setProperty(String name, Object value);

	/**
	 * Returns the factory parameter.
	 * @param name name of parameter
	 * @return value or NULL
	 */
	public String getParameter(String name);

	/**
	 * Returns the factory parameter as a URL.
	 * @param name name of parameter
	 * @return value or NULL
	 */
	public URL getParameterUrl(String name) throws MalformedURLException;

	/**
	 * Returns the urlTransformer.
	 * @return the urlTransformer
	 */
	public URLTransformer getUrlTransformer();

	/**
	 * Sets the urlTransformer.
	 * @param urlTransformer the urlTransformer to set
	 */
	public void setUrlTransformer(URLTransformer urlTransformer);

	/**
	 * Returns the DAO master with given ID.
	 * @param id ID of master
	 * @return the master or null
	 */
	public DaoMaster getDaoMaster(String id);
	
	/**
	 * Returns an iterator of all property keys.
	 * @return iterator
	 */
	public Iterator<String> getParameterKeys();

	/**
	 * Returns the correct DAO for the given transfer object.
	 * @param o the object to look for
	 * @return DAO or null if not found
	 */
	public <K extends Serializable, T extends GeneralDTO<K>> GeneralDAO<K,?> getDaoFor(T o);
	

	/**
	 * Returns the correct DAO for the given model object.
	 * @param o the object to look for
	 * @return DAO or null if not found
	 */
	public <K extends Serializable, B extends GeneralBO<K>> GeneralDAO<K,B> getDaoFor(B o);
	
	/********************* TRANSACTIONS ************************/
	
	/**
	 * Returns the TX manager used.
	 * @return the transaction manager
	 */
	public TransactionManager getTransactionManager();
	
	/**
	 * Sets the given TX manager to be used.
	 * @param txManager the TX manager.
	 */
	public void setTransactionManager(TransactionManager txManager);
	
	/**
	 * Begins a transaction.
	 * This method does nothing if the thread is already within a transaction.
	 */
	public void begin();
	
	/**
	 * Commits a transaction.
	 * If the last call to {@link #begin()} did not start a new TX then
	 * this method does nothing.
	 * 
	 */
	public void commit();
	
	/**
	 * Rolls back a transaction.
	 * If the last call to {@link #begin()} did not start a new TX then
	 * this method does nothing.
	 * 
	 */
	public void rollback();
	
	/**
	 * Returns the current active transaction.
	 * @return current transaction
	 * @throws SystemException if there is a problem
	 */
	public Transaction getTransaction() throws SystemException;
	
	/********************* FACTORY CHANGES **********************************/
	
	/**
	 * Adds a factory listener.
	 * @param listener listener
	 */
	public void addDaoFactoryListener(DaoFactoryListener listener);
	
	/**
	 * Removes a factory listener.
	 * @param listener listener
	 */
	public void removeDaoFactoryListener(DaoFactoryListener listener);
	
}
