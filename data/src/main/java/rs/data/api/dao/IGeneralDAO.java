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
package rs.data.api.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import rs.data.api.IDaoFactory;
import rs.data.api.IDaoMaster;
import rs.data.api.bo.IGeneralBO;
import rs.data.event.IDaoListener;
import rs.data.util.IDaoIterator;

/**
 * Interface for Data Access Objects.
 * @param <K> type of primary key
 * @param <B> type of Business Object
 * @author ralph
 *
 */
public interface IGeneralDAO<K extends Serializable, B extends IGeneralBO<K>> {

	/**
	 * Returns the DAO factory.
	 * @return the DAO factory this DAO is assigned to
	 */
	public IDaoFactory getFactory();
	
	/**
	 * Sets the factory.
	 * @param factory - the factory this DAO belongs to
	 */
	public void setFactory(IDaoFactory factory);
	
	/**
	 * Returns the DAO master.
	 * @return the DAO master
	 */
	public IDaoMaster getDaoMaster();
	
	/**
	 * Sets the DAO master.
	 * @param daoMaster the master
	 */
	public void setDaoMaster(IDaoMaster daoMaster);
	
	// CREATE
	/**
	 * Create a new domain class instance.
	 * This method will set the creation date.
	 * @return new instance
	 */
	public B newInstance();
	
	/**
	 * Shutdown the DAO.
	 * @since 1.2.8
	 */
	public void shutdown();

	/********************* CACHING ******************/
	
	/**
	 * Enables or disables the caching mechanism.
	 * @param enabled {@code true} when cache shall be enabled.
	 */
	public void setCacheEnabled(boolean enabled);
	
	/**
	 * Returns status of caching mechanism.
	 * @return {@code true} when cache shall be enabled.
	 */
	public boolean isCacheEnabled();
	
	/********************* CREATION ******************/
	
	/**
	 * Inserts the object into the database if it does not exist.
	 * This method will set the creation date.
	 * @param object domain class instance
	 */
	public void createObject(Object object);
	
	/**
	 * Creates the object.
	 * @param object object to be saved
	 * @param setCreationDate whether creationDate will be set
	 * @see #create(IGeneralBO, boolean)
	 */
	public void createObject(Object object, boolean setCreationDate);

	/**
	 * Inserts the object into the database if it does not exist.
	 * This method will set the creation date.
	 * @param object domain class instance
	 */
	public void create(B object);
	
	/**
	 * Inserts the object into the database if it does not exist.
	 * @param object domain class instance
	 * @param setCreationDate whether creation date shall be set
	 */
	public void create(B object, boolean setCreationDate);
	
	/********************** FINDING ********************/
	
	/**
	 * Returns the number of domain objects.
	 * @return number of objects.
	 */
	public int getObjectCount();
	
	/**
	 * Returns the number of objects with default criteria matched.
	 * This method shall be used if you want to count only non-deleted objects.
	 * @return number of objects
	 */
	public int getDefaultObjectCount();
	
	/**
	 * Find the given object in model.
	 * @param id id of object
	 * @return object
	 */
	public B findBy(K id);
	
	/**
	 * Find the given object in model.
	 * This method is an alternative in case you cannot specify the type of ID at compile time.
	 * @param id id of object
	 * @return object
	 */
	public B findById(Object id);
	
	/**
	 * Refresh the given object.
	 * <p>The method will throw an {@link rs.data.util.ObjectDeletedException ObjectDeletedException} when the underlying
	 * persistence store does not have this object anymore.</p>
	 * @param object object to be refreshed
	 */
	public void refresh(B object);
	
	/**
	 * Find the given objects in model.
	 * @param ids ids of objects
	 * @return object
	 */
	public List<B> findBy(Collection<K> ids);
	
	/**
	 * Returns all domain objects.
	 * @return list of all objects
	 */
	public List<B> findAll();
	
	/**
	 * Returns a subset of all objects.
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return list of objects
	 */
	public List<B> findAll(int firstResult, int maxResults);

	/**
	 * Returns domain objects with default criteria.
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects)
	 * @return list of all objects
	 */
	public List<B> findDefaultAll();

	/**
	 * Returns domain objects with default criteria.
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects).
	 * @param firstResult - the first result to be returned
	 * @param maxResults - the number of results returned at most
	 * @return list of all objects
	 */
	public List<B> findDefaultAll(int firstResult, int maxResults);
	
	/**
	 * Returns all domain objects.
	 * @return list of all objects
	 */
	public IDaoIterator<B> iterateAll();
	
	/**
	 * Returns a subset of all objects
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return iterator of BO
	 */
	public IDaoIterator<B> iterateAll(int firstResult, int maxResults);
	
	/**
	 * Returns domain objects with default criteria.
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects)
	 * @return list of all objects
	 */
	public IDaoIterator<B> iterateDefaultAll();
	
	/**
	 * Returns a subset of domain objects with default criteria.
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects)
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return list of all objects
	 */
	public IDaoIterator<B> iterateDefaultAll(int firstResult, int maxResults);
	
	/********************** UPDATING ********************/
	
	/**
	 * Saves the object.
	 * This method assumes that the object already exists. Also, this method
	 * will set the change date.
	 * @param object object to be saved
	 */
	public void saveObject(Object object);
	
	/**
	 * Saves the object.
	 * This method assumes that the object already exists. 
	 * @param object object to be saved
	 * @param setChangeDate whether changeDate will be set
	 */
	public void saveObject(Object object, boolean setChangeDate);
	
	/**
	 * Saves the object.
	 * This method assumes that the object already exists. Also, this method
	 * will set the change date.
	 * @param object object to be saved
	 */
	public void save(B object);
	
	/**
	 * Saves the object.
	 * This method assumes that the object already exists. 
	 * @param object object to be saved
	 * @param setChangeDate whether changeDate will be set
	 */
	public void save(B object, boolean setChangeDate);
	
	/********************** DELETING ********************/
	
	/**
	 * Deletes the object.
	 * @param object object to be saved
	 */
	public void deleteObject(Object object);
	
	/**
	 * Deletes the object.
	 * This method assumes that the object existed before.
	 * @param object object to be deleted.
	 */
	public void delete(B object);
	
	/**
	 * Deletes the objects.
	 * This method assumes that the objects existed before.
	 * @param objects objects to be deleted.
	 */
	public void delete(Collection<B> objects);
	
	/**
	 * Deletes the object of given key.
	 * This method assumes that the object existed before.
	 * @param id id of object to be deleted.
	 */
	public void deleteByKey(K id);
	
	/**
	 * Deletes the objects.
	 * This method assumes that the object existed before.
	 * @param ids id of objects to be deleted.
	 */
	public void deleteByKeys(Collection<K> ids);
	
	/**
	 * Deletes all objects.
	 * @return number of object deleted
	 */
	public int deleteAll();
	
	/**
	 * Deletes all objects.
	 * @return number of object deleted
	 */
	public int deleteDefaultAll();
	
	/**************************************** PROPERTY CHANGE SUPPORT *****************************/
	
	/**
	 * Adds a DAO event listener.
	 * @param listener listener
	 */
	public void addDaoListener(IDaoListener listener);

	/**
	 * Removes a DAO event listener.
	 * @param listener listener
	 */
	public void removeDaoListener(IDaoListener listener);
	
	/**
	 * Clears the cache.
	 */
	public void clearCache();
}
