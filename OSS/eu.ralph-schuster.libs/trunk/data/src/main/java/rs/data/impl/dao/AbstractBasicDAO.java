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
package rs.data.impl.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.configuration.IConfigurable;
import rs.baselib.lang.LangUtils;
import rs.baselib.util.ICache;
import rs.baselib.util.RsDate;
import rs.baselib.util.WeakMapCache;
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
public abstract class AbstractBasicDAO<K extends Serializable, C extends IGeneralBO<K>> implements IGeneralDAO<K, C>, IConfigurable {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	/** The persistent class to manage */
	private Class<K> keyClass;
	private Class<C> boInterfaceClass;

	private IDaoFactory factory;
	private IDaoMaster daoMaster;
	private ICache<CID,C> cache;
	private Set<IDaoListener> listeners = new HashSet<IDaoListener>();

	/**
	 * Constructor.
	 */
	public AbstractBasicDAO() {
		init();
	}

	/**
	 * Initializes / called from constructor.
	 */
	@SuppressWarnings("unchecked")
	protected void init() {
		List<Class<?>> classes = LangUtils.getTypeArguments(AbstractBasicDAO.class, getClass());
		this.keyClass = (Class<K>) classes.get(0);
		this.boInterfaceClass = (Class<C>) classes.get(1);	
		this.cache = createCache();
	}

	/**
	 * Returns a {@link WeakMapCache}.
	 * @return the cache implementation.
	 */
	protected ICache<CID, C> createCache() {
		return new WeakMapCache<CID, C>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() {
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
	public Class<C> getBoInterfaceClass() {
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
	 * Returns the CID for this object.
	 * @param object object
	 * @return CID
	 */
	protected CID getCID(C object) {
		return new CID(getBoInterfaceClass(), object.getId());
	}

	/**
	 * {@inheritDoc}.
	 * <p>This implementation forwards the call to {@link #_newInstance()} which must
	 * be implemented by subclasses. It then calls {@link #afterNewInstance(IGeneralBO, boolean)}.</p>
	 */
	@Override
	public C newInstance() {
		C rc = _newInstance();
		rc.set("dao", this);
		afterNewInstance(rc, false);
		return rc;
	}

	/**
	 * Override here and return a plain, unintitialized BO implementation.
	 * @return a new instance
	 */
	protected abstract C _newInstance();

	/**
	 * Called after a new instance was created.
	 * <p>This method sets {@link IGeneralBO#getCreationDate() creationDate} and {@link IGeneralBO#getChangeDate() changeDate}
	 * when the object was not created from a a persisted object.</p>
	 * @param object object being created
	 * @param persisted <code>true</code> when the object was created from a persisted object
	 */
	protected void afterNewInstance(C object, boolean persisted) {
		if (!persisted) {
			object.setCreationDate(new RsDate());
			object.setChangeDate(new RsDate());
		}
	}

	/**
	 * Add the given object to the cache.
	 * @param object object to add
	 */
	protected void addCached(C object) {
		// It is important to have the CID held by the BO to avoid losing the cache
		cache.put(object.getCID(), object);
	}

	/**
	 * Add the given object to the cache.
	 * @param object object to add
	 */
	protected void removeCached(C object) {
		cache.remove(object.getCID());
	}

	/**
	 * Computes the CID from the id.
	 * @param id id
	 * @return computed CID.
	 */
	public CID getCID(K id) {
		return new CID(getBoInterfaceClass(), id);
	}

	/**
	 * Returns an object from the cache.
	 * @param cid CID of object
	 */
	protected C getCached(CID cid) {
		return cache.get(cid);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearCache() {
		cache.clear();
	}

	/**
	 * Returns the {@link #cache}.
	 * @return the cache
	 */
	protected ICache<CID, C> getCache() {
		return cache;
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
	@SuppressWarnings("unchecked")
	@Override
	public C findById(Object id) {
		return findBy((K)id);
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
	public void delete(C object) {
		if (object != null) {

			beforeDelete(object);
			_delete(object);
			afterDelete(object);
			removeCached(object);
			fireObjectDeleted(object);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Collection<C> objects) {
		for (C object : objects) {
			delete(object);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteByKeys(Collection<K> ids) {
		delete(findBy(ids));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteByKey(K id) {
		delete(findBy(id));
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
