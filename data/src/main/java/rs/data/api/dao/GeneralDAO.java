/**
 * 
 */
package rs.data.api.dao;

import java.io.Serializable;
import java.util.List;

import rs.data.api.DaoFactory;
import rs.data.api.DaoMaster;
import rs.data.api.bo.GeneralBO;
import rs.data.event.DaoListener;
import rs.data.util.DaoIterator;

/**
 * Interface for Data Access Objects.
 * @param <K> type of primary key
 * @param <B> type of Business Object
 * @author ralph
 *
 */
public interface GeneralDAO<K extends Serializable, B extends GeneralBO<K>> {

	/**
	 * Returns the DAO factory.
	 * @return the DAO factory this DAO is assigned to
	 */
	public DaoFactory getFactory();
	
	/**
	 * Sets the factory.
	 * @param factory
	 */
	public void setFactory(DaoFactory factory);
	
	/**
	 * Returns the DAO master.
	 * @return the DAO master
	 */
	public DaoMaster getDaoMaster();
	
	/**
	 * Sets the DAO master.
	 * @param daoMaster the master
	 */
	public void setDaoMaster(DaoMaster daoMaster);
	
	// CREATE
	/**
	 * Create a new domain class instance.
	 * This method will set the creation date.
	 * @return new instance
	 */
	public B newInstance();
	
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
	 * @see #create(GeneralBO, boolean)
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
	public B findById(K id);
	
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
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects)
	 * @return list of all objects
	 */
	public List<B> findDefaultAll(int firstResult, int maxResults);
	
	/**
	 * Returns all domain objects.
	 * @return list of all objects
	 */
	public DaoIterator<B> iterateAll();
	
	/**
	 * Returns a subset of all objects
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return iterator of BO
	 */
	public DaoIterator<B> iterateAll(int firstResult, int maxResults);
	
	/**
	 * Returns domain objects with default criteria.
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects)
	 * @return list of all objects
	 */
	public DaoIterator<B> iterateDefaultAll();
	
	/**
	 * Returns a subset of domain objects with default criteria.
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects)
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return list of all objects
	 */
	public DaoIterator<B> iterateDefaultAll(int firstResult, int maxResults);
	
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
	public void addDaoListener(DaoListener listener);

	/**
	 * Removes a DAO event listener.
	 * @param listener listener
	 */
	public void removeDaoListener(DaoListener listener);
	
}
