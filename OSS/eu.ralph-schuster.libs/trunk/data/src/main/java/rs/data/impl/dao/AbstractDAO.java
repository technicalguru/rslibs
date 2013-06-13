/**
 * 
 */
package rs.data.impl.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import rs.baselib.lang.LangUtils;
import rs.data.api.bo.GeneralBO;
import rs.data.api.dao.GeneralDAO;
import rs.data.impl.bo.AbstractBO;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.CID;
import rs.data.util.DaoIterator;

/**
 * Abstract Implementation for Data Access Objects.
 * @param <K> type of primary key
 * @param <T> type of Transfer Object
 * @param <B> type of Business Object Implementation
 * @param <C> type of Business Object Interface
 * @author ralph
 *
 */
public abstract class AbstractDAO<K extends Serializable, T extends GeneralDTO<K>, B extends AbstractBO<K, T>, C extends GeneralBO<K>> extends AbstractGeneralDAO<K, B, C> implements GeneralDAO<K, C> {

	/** The persistent class to manage */
	private Class<T> transferClass;
	
	/**
	 * Constructor.
	 */
	public AbstractDAO() {
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void init() {
		super.init();
		List<Class<?>> classes = LangUtils.getTypeArguments(AbstractDAO.class, getClass());
		this.transferClass = (Class<T>) classes.get(1);		
	}

	/**
	 * Returns the dtoInterfaceClass.
	 * @return the dtoInterfaceClass
	 */
	protected Class<T> getTransferClass() {
		return transferClass;
	}

	/************************* INSTANTIATION ************************/
	
	/**
	 * Returns the business objects for the transfer object.
	 */
	@SuppressWarnings("unchecked")
	public C getBusinessObject(T object) {
		if (object == null) return null;
		try {
			// Return the cached BO if it exists
			CID cid = new CID(getBoImplementationClass(), object.getId());
			B cached = getCached(cid);
			if (cached != null) return (C)cached;
			
			// Create the BO
			B rc = getBoImplementationClass().newInstance();
			rc.setDao(this);
			afterNewInstance((C)rc);
			rc.setTransferObject(object);
			
			// Add it to our cache
			addCached(rc);
			
			return (C)rc;
		} catch (IllegalAccessException e) {
			log.error("Error creating new object: ", e);
		} catch (InstantiationException e) {
			log.error("Error creating new object: ", e);
		}
		return null;
	}

	/**
	 * Returns the CID for this object.
	 * @param object object
	 * @return CID
	 */
	protected CID getCID(T object) {
		return new CID(getBoImplementationClass(), object.getId());
	}
	
	/**
	 * Wraps the collection into business objects.
	 * @param rc the collection where to store the BO.
	 * @param collection collection of DTO to be wrapped.
	 */
	protected void wrap(Collection<C> rc, Collection<T> collection) {
		for (T t : collection) {
			rc.add(getBusinessObject(t));
		}
	}
	
	/**
	 * Returns the wrapper for DTO iterators.
	 * @param i DTO iterator to be wrapped
	 * @return iterator on BOs
	 */
	protected DaoIterator<C> wrap(Iterator<T> i) {
		return new BusinessIterator(i);
	}
	
	/************************* CREATION ************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _create(B object) {
		T t = object.getTransferObject();
		_create(t);
	}

	/**
	 * Creates the object.
	 * This method assumes that the object already exists. 
	 * @param object DTO to be saved
	 * @see #create(GeneralBO, boolean)
	 */
	protected abstract void _create(T object);
	
	/************************* CREATION ************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C findById(K id) {
		return getBusinessObject(_findById(id));
	}

	/**
	 * Find the object in underlying store.
	 * @param id id of object
	 * @return DTO
	 */
	protected abstract T _findById(K id);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findAll(int firstResult, int maxResults) {
		List<T> result = _findAll(firstResult, maxResults);
		List<C> rc = new ArrayList<C>();
		wrap(rc, result);
		return rc;
	}

	/**
	 * Returns a subset of all objects.
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return list of DTO
	 */
	protected abstract List<T> _findAll(int firstResult, int maxResults);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findDefaultAll(int firstResult, int maxResults) {
		List<T> result = _findDefaultAll(firstResult, maxResults);
		List<C> rc = new ArrayList<C>();
		wrap(rc, result);
		return rc;
	}

	/**
	 * Returns a subset of domain objects with default criteria.
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects)
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return list of all DTO
	 */
	protected abstract List<T> _findDefaultAll(int firstResult, int maxResults);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DaoIterator<C> iterateAll(int firstResult, int maxResults) {
		return wrap(_iterateAll(firstResult, maxResults));
	}

	/**
	 * Returns a subset of all objects
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return iterator of DTO
	 */
	protected abstract Iterator<T> _iterateAll(int firstResult, int maxResults);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DaoIterator<C> iterateDefaultAll(int firstResult, int maxResults) {
		return wrap(_iterateDefaultAll(firstResult, maxResults));
	}
	
	/**
	 * Returns a subset of domain objects with default criteria.
	 * A default criteria could hide objects not needed regulary (e.g. deleted objects)
	 * @param firstResult index of first result
	 * @param maxResults maximum number of results to return
	 * @return list of all objects
	 */
	protected abstract Iterator<T> _iterateDefaultAll(int firstResult, int maxResults);
	
	/********************** UPDATING ********************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _save(B object) {
		T t = object.getTransferObject();
		_save(t);
	}

	/**
	 * Saves the object.
	 * This method assumes that the object already exists. 
	 * @param object DTO to be saved
	 * @see #save(GeneralBO, boolean)
	 */
	protected abstract void _save(T object);

	/********************** DELETING ********************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _delete(B object) {
		T t = object.getTransferObject();
		_delete(t);
	}

	/**
	 * Deletes the object.
	 * This method assumes that the object existed before.
	 * @param object DTO to be deleted.
	 */
	protected abstract void _delete(T object);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteAll() {
		int rc = _deleteAll();
		fireAllDeleted();
		return rc;
	}

	/**
	 * Deletes all objects.
	 * @return number of object deleted
	 */
	protected abstract int _deleteAll();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteDefaultAll() {
		int rc = _deleteDefaultAll();
		fireAllDefaultDeleted();
		return rc;
	}

	/**
	 * Deletes all objects.
	 * @return number of object deleted
	 */
	protected abstract int _deleteDefaultAll();
	
	/**
	 * Wrapping all DTOs into BOs when returning.
	 * @author ralph
	 *
	 */
	protected class BusinessIterator implements DaoIterator<C> {
		
		private Iterator<T> iterator;
		
		/**
		 * Constructor.
		 * @param iterator DTO iterator to be wrapped.
		 */
		public BusinessIterator(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			if (iterator == null) return false;
			return iterator.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public C next() {
			if (iterator == null) throw new RuntimeException("Empty iterator!");
			return getBusinessObject(iterator.next());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			iterator.remove();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() {
			if (iterator instanceof DaoIterator) ((DaoIterator<?>)iterator).close();
		}
	}
}
