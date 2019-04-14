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
package rs.data.api;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import rs.baselib.util.IUrlTransformer;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.event.IDaoFactoryListener;
import rs.data.impl.dto.GeneralDTO;

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
	 * @throws MalformedURLException when the parameter URL cannot be created
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
	 * Enable or disable caching for all registered DAOs.
	 * @param cacheEnabled {@code true} when cache shall be enabled
	 * @see IGeneralDAO#setCacheEnabled(boolean)
	 */
	public void setCacheEnabled(boolean cacheEnabled);
	
	/**
	 * Returns the correct DAO for the given transfer object.
	 * @param o the object to look for
	 * @param <K> the key class
	 * @param<T> the transfer class
	 * @return DAO or null if not found
	 */
	public <K extends Serializable, T extends GeneralDTO<K>> IGeneralDAO<K,?> getDaoFor(T o);
	
	/**
	 * Returns the correct DAO for the given business object.
	 * @param o the object to look for
	 * @param <B> the BO class
	 * @return DAO or null if not found
	 */
	public <B extends IGeneralBO<?>> IGeneralDAO<?,B> getDaoFor(B o);
	
	/**
	 * Returns the correct DAO for the given business object class.
	 * @param clazz the BO clazz to look for
	 * @param <B> the BO class
	 * @return DAO or null if not found
	 */
	public <B extends IGeneralBO<?>> IGeneralDAO<?,B> getDaoFor(Class<B> clazz);
	
	/**
	 * Registers a DAO with this factory (under its class name).
	 * @param dao the DAO to be registered.
	 */
	public void registerDao(IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> dao);

	/**
	 * Registers a DAO with this factory.
	 * @param name name of registration
	 * @param dao the DAO to be registered.
	 * @since 1.2.7
	 */
	public void registerDao(String name, IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> dao);
	
	/**
	 * Returns a registered DAO of given class (registered under its class name).
	 * <p>The method will try to return the DAO registered under the class name. If that cannopt be found then the
	 * first object found being an instance of the class will be returned.</p>
	 * @param clazz the class of the DAO to be returned
	 * @param <X> the DAO class
	 * @return the DAO or null if not registered
	 */
	public <X extends IGeneralDAO<?, ?>> X getDao(Class<X> clazz);

	/**
	 * Returns a registered DAO of given class.
	 * @param name the name of the registration
	 * @param clazz the class of the DAO to be returned
	 * @param <X> the DAO class
	 * @return the DAO or null if not registered
	 * @throws ClassCastException when the registered object is not an instance of the given class.
	 * @since 1.2.7
	 */
	public <X extends IGeneralDAO<?, ?>> X getDao(String name, Class<X> clazz);

	/**
	 * Return an iterable of the registered DAOs.
	 * @return the iterable
	 */
	public Iterable<IGeneralDAO<?, ?>> getDaos();
	
	/**
	 * Clear cache of all DAOs.
	 * @since 1.2.8
	 */
	public void clearCache();
	
	/**
	 * Shutdown the factory.
	 */
	public void shutdown();
	
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
	 * Returns the default timeout for transactions.
	 * @return timeout in ms
	 * @since 1.2.9
	 */
	public long getDefaultTransactionTimeout();
	
	/**
	 * Sets the default timeout for transactions.
	 * @param defaultTimeout timeout in ms, {@link #DEFAULT_TX_TIMEOUT} will be used when value is 0.
	 * @since 1.2.9
	 */
	public void setDefaultTransactionTimeout(long defaultTimeout);

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
	
	/**
	 * Returns the start time of the current or last transaction in ms.
	 * @return the start time in ms
	 */
	public long getTransactionStartTime();

	/**
	 * Returns the end time of the current or last transaction in ms.
	 * @return the end time in ms
	 */
	public long getTransactionEndTime();

	/**
	 * Returns the duration of the current or last transaction in ms.
	 * @return the duration in ms
	 */
	public long getTransactionDuration();

	/**
	 * Ensures that the current thread has no left-overs from last TX activity.
	 * @since 1.2.9
	 */
	public void cleanTransactionContext();
	
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
