/**
 * 
 */
package rs.data.impl.dao;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.configuration.IConfigurable;
import rs.baselib.lang.LangUtils;
import rs.baselib.util.RsDate;
import rs.data.api.IDaoFactory;
import rs.data.api.IDaoMaster;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.event.DaoEvent;
import rs.data.event.DaoEvent.Type;
import rs.data.event.IDaoListener;
import rs.data.util.CID;
import rs.data.util.IDaoIterator;

/**
 * Abstract Implementation for Data Access Objects.
 * This implementation makes no restriction on the type of BO implementation.
 * @param <K> type of primary key
 * @param <C> type of Business Object Interface
 * @author ralph
 *
 */
public abstract class AbstractGeneralDAO<K extends Serializable, C extends IGeneralBO<K>> implements IGeneralDAO<K, C>, IConfigurable {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	/** The persistent class to manage */
	private Class<K> keyClass;
	private Class<C> boInterfaceClass;

	private IDaoFactory factory;
	private IDaoMaster daoMaster;
	private Map<CID,WeakReference<C>> cache = new WeakHashMap<CID,WeakReference<C>>();
	private Set<IDaoListener> listeners = new HashSet<IDaoListener>();

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
		this.boInterfaceClass = (Class<C>) classes.get(1);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void configure(Configuration cfg) throws ConfigurationException {

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
	public IDaoFactory getFactory() {
		return factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFactory(IDaoFactory factory) {
		this.factory = factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoMaster getDaoMaster() {
		return daoMaster;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDaoMaster(IDaoMaster daoMaster) {
		this.daoMaster = daoMaster;
	}

	/************************* INSTANTIATION ************************/

	/**
	 * Add the given object to the cache.
	 * @param object object to add
	 */
	protected void addCached(C object) {
		// It is important to have the CID held by the BO to avoid losing the cache
		cache.put(object.getCID(), new WeakReference<C>(object));
	}

	/**
	 * Returns an object from the cache.
	 * @param cid CID of object
	 */
	protected C getCached(CID cid) {
		// It is important to have the CID held by the BO to avoid losing the cache
		WeakReference<C> ref = cache.get(cid);
		if (ref != null) return ref.get();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearCache() {
		cache.clear();
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
	@Override
	public void create(C object, boolean setCreationDate) {
		if (object.getId() != null) throw new RuntimeException("Object already exists: "+object);

		if (setCreationDate) {
			object.setCreationDate(new RsDate());
			object.setChangeDate(new RsDate());
		}

		beforeCreate(object);
		_create(object);

		// Add the object to the cache
		addCached(object);

		afterCreate(object);

		fireObjectCreated(object);
	}

	/**
	 * Creates the object.
	 * This method assumes that the object does not exist. 
	 * @param object BO to be saved
	 * @see #createObject(Object)
	 */
	protected abstract void _create(C object);

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
	 * <p>
	 * This implementation calls {@link #findBy(Serializable)} for each key.
	 * Descendants shall override when there are more efficient ways for
	 * finding multiple objects via their ID.
	 */
	@Override
	public List<C> findBy(Collection<K> ids) {
		List<C> rc = new ArrayList<C>();
		for (K id : ids) {
			C c = findBy(id);
			if (c != null) {
				rc.add(c);
			}
		}
		return rc;
	}


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
	public IDaoIterator<C> iterateAll() {
		return iterateAll(-1, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateDefaultAll() {
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
	@Override
	public void save(C object, boolean setChangeDate) {
		if (object == null) return;
		if (object.isInvalid()) throw new RuntimeException("Cannot save an invalid object");

		if (object.getId() == null) throw new IllegalStateException("Object was not created yet: "+object);
		if (setChangeDate) object.setChangeDate(new RsDate());

		beforeSave(object);
		_save(object);
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
	 * @see #create(IGeneralBO, boolean)
	 */
	protected abstract void _save(C object);

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
	@Override
	public void delete(C... objects) {
		for (C c : objects) {
			if (c != null) {

				beforeDelete(c);
				_delete(c);
				afterDelete(c);

				fireObjectDeleted(c);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void delete(Collection<C> objects) {
		delete(objects.toArray((C[])Array.newInstance(getBoInterfaceClass(), objects.size())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteByKeys(K... ids) {
		List<K> l = new ArrayList<K>();
		for (K id : ids) {
			l.add(id);
		}
		deleteByKeys(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteByKeys(Collection<K> ids) {
		delete(findBy(ids));
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
	protected abstract void _delete(C object);

	/****************************** LISTENER SUPPORT *****************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDaoListener(IDaoListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeDaoListener(IDaoListener listener) {
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
	 * @param event event to be fired
	 */
	protected void fireDaoEvent(DaoEvent event) {
		for (IDaoListener l : listeners) l.handleDaoEvent(event);
	}

}
