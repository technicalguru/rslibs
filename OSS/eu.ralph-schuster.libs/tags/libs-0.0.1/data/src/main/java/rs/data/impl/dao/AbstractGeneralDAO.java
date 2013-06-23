/**
 * 
 */
package rs.data.impl.dao;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.data.api.DaoFactory;
import rs.data.api.DaoMaster;
import rs.data.api.bo.GeneralBO;
import rs.data.api.dao.GeneralDAO;
import rs.data.event.DaoEvent;
import rs.data.event.DaoEvent.Type;
import rs.data.event.DaoListener;
import rs.data.impl.bo.AbstractGeneralBO;
import rs.data.util.CID;
import rs.data.util.DaoIterator;
import rsbaselib.configuration.Configurable;
import rsbaselib.lang.LangUtils;
import rsbaselib.util.RsDate;

/**
 * Abstract Implementation for Data Access Objects.
 * @param <K> type of primary key
 * @param <T> type of Transfer Object
 * @param <B> type of Business Object Implementation
 * @param <C> type of Business Object Interface
 * @author ralph
 *
 */
public abstract class AbstractGeneralDAO<K extends Serializable, B extends AbstractGeneralBO<K>, C extends GeneralBO<K>> implements GeneralDAO<K, C>, Configurable {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/** The persistent class to manage */
	private Class<K> keyClass;
	private Class<B> boImplementationClass;
	private Class<C> boInterfaceClass;
	
	private DaoFactory factory;
	private DaoMaster daoMaster;
	private Map<CID,WeakReference<B>> cache = new WeakHashMap<CID,WeakReference<B>>();
	private Set<DaoListener> listeners = new HashSet<DaoListener>();
	
	/**
	 * Constructor.
	 */
	public AbstractGeneralDAO() {
		init();
	}

	/**
	 * Initializes / called from constructor.
	 */
	@SuppressWarnings("unchecked")
	protected void init() {
		List<Class<?>> classes = LangUtils.getTypeArguments(AbstractGeneralDAO.class, getClass());
		this.keyClass = (Class<K>) classes.get(0);
		this.boImplementationClass = (Class<B>) classes.get(1);		
		this.boInterfaceClass = (Class<C>) classes.get(2);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void configure(Configuration cfg) throws ConfigurationException {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeConfiguration() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterConfiguration() {
	}

	/**
	 * Returns the log.
	 * @return the log
	 */
	protected Logger getLog() {
		return log;
	}

	/**
	 * Returns the keyClass.
	 * @return the keyClass
	 */
	public Class<K> getKeyClass() {
		return keyClass;
	}

	/**
	 * Returns the boImplementationClass.
	 * @return the boImplementationClass
	 */
	protected Class<B> getBoImplementationClass() {
		return boImplementationClass;
	}

	/**
	 * Returns the boInterfaceClass.
	 * @return the boInterfaceClass
	 */
	protected Class<C> getBoInterfaceClass() {
		return boInterfaceClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DaoFactory getFactory() {
		return factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFactory(DaoFactory factory) {
		this.factory = factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DaoMaster getDaoMaster() {
		return daoMaster;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDaoMaster(DaoMaster daoMaster) {
		this.daoMaster = daoMaster;
	}
	
	/************************* INSTANTIATION ************************/
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public C newInstance() {
		try {
			B rc = getBoImplementationClass().newInstance();
			rc.setDao(this);
			afterNewInstance((C)rc);
			return (C)rc;
		} catch (IllegalAccessException e) {
			log.error("Error creating new object: ", e);
		} catch (InstantiationException e) {
			log.error("Error creating new object: ", e);
		}
		return null;
	}

	/**
	 * Called after a new instance was created.
	 * @param object object being created
	 */
	protected void afterNewInstance(C object) {		
	}
	
	/**
	 * Add the given object to the cache.
	 * @param object object to add
	 */
	protected void addCached(B object) {
		// It is important to have the CID held by the BO to avoid losing the cache
		cache.put(object.getCID(), new WeakReference<B>(object));
	}
	
	/**
	 * Returns an object from the cache.
	 * @param cid CID of object
	 */
	protected B getCached(CID cid) {
		// It is important to have the CID held by the BO to avoid losing the cache
		WeakReference<B> ref = cache.get(cid);
		if (ref != null) return ref.get();
		return null;
	}
	
	/************************* CREATION ************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createObject(Object object) {
		createObject(object, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void createObject(Object object, boolean setCreationDate) {
		if (getBoInterfaceClass().isInstance(object)) create((C)object, setCreationDate);
		else throw new RuntimeException("Cannot create "+object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(C object) {
		create(object, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void create(C object, boolean setCreationDate) {
		B b = (B)object;
		
		if (b.getId() != null) throw new RuntimeException("Object already exists: "+object);
		
		if (setCreationDate) {
			b.setCreationDate(new RsDate());
			b.setChangeDate(new RsDate());
		}
		
		beforeCreate(object);
		_create(b);
		
		// Add the object to the cache
		addCached(b);
		
		afterCreate(object);
		
		fireObjectCreated(object);
	}

	/**
	 * Creates the object.
	 * This method assumes that the object does not exist. 
	 * @param object BO to be saved
	 * @see #create(Object)
	 */
	protected abstract void _create(B object);

	/**
	 * Called immediately before creation.
	 * This method does nothing.
	 * @param object object to be created
	 */
	protected void beforeCreate(C object) {		
	}
	
	/**
	 * Called immediately after creation.
	 * This method does nothing.
	 * @param object object created
	 */
	protected void afterCreate(C object) {
		object.setChanged(false);
	}
	
	/************************* FINDING ************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findAll() {
		return findAll(-1, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findDefaultAll() {
		return findDefaultAll(-1, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DaoIterator<C> iterateAll() {
		return iterateAll(-1, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DaoIterator<C> iterateDefaultAll() {
		return iterateDefaultAll(-1, -1);
	}

	/********************** UPDATING ********************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveObject(Object object) {
		saveObject(object, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveObject(Object object, boolean setChangeDate) {
		if (getBoInterfaceClass().isInstance(object)) save((C)object, setChangeDate);
		else throw new RuntimeException("Cannot save "+object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(C object) {
		save(object, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void save(C object, boolean setChangeDate) {
		if (object == null) return;
		if (object.isInvalid()) throw new RuntimeException("Cannot save an invalid object");
		
		B b = (B)object;
		
		if (b.getId() == null) throw new IllegalStateException("Object was not created yet: "+b);
		if (setChangeDate) b.setChangeDate(new RsDate());
		
		beforeSave(object);
		_save(b);
		afterSave(object);
		
		fireObjectUpdated(object);
	}

	/**
	 * Called immediately before saving.
	 * This method does nothing.
	 * @param object object to be saved
	 */
	protected void beforeSave(C object) {
	}
	
	/**
	 * Called immediately after saving.
	 * This method does nothing.
	 * @param object object saved
	 */
	protected void afterSave(C object) {
		object.setChanged(false);
	}

	/**
	 * Saves the object.
	 * This method assumes that the object already exists. 
	 * @param object BO to be saved
	 * @param setChangeDate whether changeDate will be set
	 * @see #create(Object)
	 */
	protected abstract void _save(B object);

	/********************** DELETING ********************/
	
	@SuppressWarnings("unchecked")
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteObject(Object object) {
		if (getBoInterfaceClass().isInstance(object)) delete((C)object);
		else throw new RuntimeException("Cannot delete "+object);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void delete(C object) {
		B b = (B)object;
		
		beforeDelete(object);
		_delete(b);
		afterDelete(object);
		
		fireObjectDeleted(object);
	}

	/**
	 * Called immediately before deletion.
	 * This method does nothing.
	 * @param object object to be deleted
	 */
	protected void beforeDelete(C object) {
	}
	
	/**
	 * Called immediately after deletion.
	 * This method does nothing.
	 * @param object object deleted
	 */
	protected void afterDelete(C object) {
	}
	
	/**
	 * Deletes the object.
	 * This method assumes that the object existed before.
	 * @param object DTO to be deleted.
	 */
	protected abstract void _delete(B object);
	
	/****************************** LISTENER SUPPORT *****************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDaoListener(DaoListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeDaoListener(DaoListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Fires a DAO create event.
	 * @param object object created
	 */
	protected void fireObjectCreated(C object) {
		fireDaoEvent(new DaoEvent(this, Type.OBJECT_CREATED, object));
	}

	/**
	 * Fires a DAO update event.
	 * @param object object created
	 */
	protected void fireObjectUpdated(C object) {
		fireDaoEvent(new DaoEvent(this, Type.OBJECT_UPDATED, object));
	}

	/**
	 * Fires a DAO delete event.
	 * @param object object created
	 */
	protected void fireObjectDeleted(C object) {
		fireDaoEvent(new DaoEvent(this, Type.OBJECT_DELETED, object));
	}

	/**
	 * Fires a DAO all-delete event.
	 */
	protected void fireAllDeleted() {
		fireDaoEvent(new DaoEvent(this, DaoEvent.Type.ALL_DELETED));
	}

	/**
	 * Fires a DAO all-delete event.
	 */
	protected void fireAllDefaultDeleted() {
		fireDaoEvent(new DaoEvent(this, DaoEvent.Type.ALL_DEFAULT_DELETED));
	}

	/**
	 * Fires a DAO delete event.
	 * @param object object created
	 */
	protected void fireDaoEvent(DaoEvent event) {
		for (DaoListener l : listeners) l.handleDaoEvent(event);
	}

}
