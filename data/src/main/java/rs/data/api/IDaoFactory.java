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

import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.event.IDaoFactoryListener;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.IUrlTransformer;

/**
 * A general interface describing a DAO factory.
 * @author ralph
 *
 */
public interface IDaoFactory {

	/**
	 * Default timeout of transactions.
	 */
	public static final long DEFAULT_TX_TIMEOUT = 30000L;
	
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
	public IUrlTransformer getUrlTransformer();

	/**
	 * Sets the urlTransformer.
	 * @param urlTransformer the urlTransformer to set
	 */
	public void setUrlTransformer(IUrlTransformer urlTransformer);

	/**
	 * Returns the DAO master with given ID.
	 * @param id ID of master
	 * @return the master or null
	 */
	public IDaoMaster getDaoMaster(String id);
	
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
	public <K extends Serializable, T extends GeneralDTO<K>> IGeneralDAO<K,?> getDaoFor(T o);
	
	/**
	 * Registers a DAO with this factory.
	 * @param dao the DAO to be registered.
	 */
	public void registerDao(IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> dao);

	/**
	 * Returns a registered DAO of given class
	 * @param clazz the class of the DAO to be returned
	 * @return the DAO or null if not registered
	 */
	public <X extends IGeneralDAO<?, ?>> X getDao(Class<X> clazz);

	/**
	 * Return an iterable of the registered DAOs.
	 * @return the iterable
	 */
	public Iterable<IGeneralDAO<?, ?>> getDaos();
	
	/**
	 * Clear cache of all DAOs.
	 */
	public void clearCache();
	
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
	 * Begins a transaction.
	 * This method does nothing if the thread is already within a transaction.
	 * @param timeout timeout of transaction in ms
	 */
	public void begin(long timeout);
	
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
	public void addDaoFactoryListener(IDaoFactoryListener listener);
	
	/**
	 * Removes a factory listener.
	 * @param listener listener
	 */
	public void removeDaoFactoryListener(IDaoFactoryListener listener);
	
}
