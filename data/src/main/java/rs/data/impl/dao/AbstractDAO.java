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
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.impl.bo.AbstractBO;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.CID;
import rs.data.util.IDaoIterator;

/**
 * Abstract Implementation for Data Access Objects.
 * This implementation assumes that the BO derives from {@link AbstractBO} (using a {@link GeneralDTO Transfer Object}).
 * @param <K> type of primary key
 * @param <T> type of Transfer Object
 * @param <B> type of Business Object Implementation
 * @param <C> type of Business Object Interface
 * @author ralph
 *
 */
public abstract class AbstractDAO<K extends Serializable, T extends GeneralDTO<K>, B extends AbstractBO<K, T>, C extends IGeneralBO<K>> extends AbstractGeneralDAO<K, B, C> implements IGeneralDAO<K, C> {

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
	public Class<T> getTransferClass() {
		return transferClass;
	}

	/************************* INSTANTIATION ************************/

	/**
	 * {@inheritDoc}
	 * <P>This method overrides the default method by forwarding the call
	 * to {@link #newTransferObject()} and {@link #newInstance(GeneralDTO)}.</p>
	 */
	@Override
	public C newInstance() {
		return newInstance(newTransferObject());
	}
	
	/**
	 * Returns an instance based on the given BO.
	 * <p>Descendants shall override {@link #_newInstance(GeneralDTO)}.</p>
	 * @param transferObject the transfer object
	 * @return the business object
	 */
	protected C newInstance(T transferObject) {
		C rc = _newInstance(transferObject);
		rc.set("dao", this);
		afterNewInstance(rc);
		return rc;
	}

	/**
	 * Create a new instance with given Transfer Object.
	 * @param transferObject the transfer object to be assigned
	 * @return the business object
	 */
	@SuppressWarnings("unchecked")
	protected C _newInstance(T transferObject) {
		try {
			B rc = getBoImplementationClass().newInstance();
			rc.setTransferObject(transferObject);
			return (C)rc;
		} catch (Exception e) {
			throw new RuntimeException("Cannot create Business Object", e);
		}
	}
	
	/**
	 * Returns the business objects for the transfer object.
	 */
	public C getBusinessObject(T object) {
		if (object == null) return null;

		// Return the cached BO if it exists
		CID cid = getCID(object.getId());
		C cached = getCached(cid);
		if (cached != null) return cached;

		// Create the BO
		C rc = newInstance(object);

		// Add it to our cache
		addCached(rc);

		return rc;
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
	protected IDaoIterator<C> wrap(Iterator<T> i) {
		return new BusinessIterator(i);
	}

	/**
	 * Create an instance of the transfer class.
	 * @return a new transfer object
	 */
	protected T newTransferObject() {
		try {
			return getTransferClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create Transfer Object", e);
		}
	}
	
	/************************* CREATION ************************/

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void _create(C object) {
		T t = ((B)object).getTransferObject();
		_create(t);
	}

	/**
	 * Creates the object.
	 * This method assumes that the object already exists. 
	 * @param object DTO to be saved
	 * @see #create(IGeneralBO, boolean)
	 */
	protected abstract void _create(T object);

	/************************* FINDING ************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C findBy(K id) {
		return getBusinessObject(_findBy(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findBy(Collection<K> ids) {
		List<C> rc = new ArrayList<C>();
		wrap(rc, _findBy(ids));
		return rc;
	}

	/**
	 * Find the object in underlying store.
	 * @param id id of object
	 * @return DTO
	 */
	protected abstract T _findBy(K id);

	/**
	 * Find the objects in underlying store.
	 * This implementation calls {@link #_findBy(Serializable)} for each of the given
	 * IDs. Descendants shall override when there are more efficient ways to return the
	 * appropriate list. 
	 * @param ids ids of objects
	 * @return DTOs found
	 */
	protected List<T> _findBy(Collection<K> ids) {
		List<T> rc = new ArrayList<T>();
		for (K id : ids) {
			T t = _findBy(id);
			if (t != null) rc.add(t);
		}
		return rc;
	}

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
	public IDaoIterator<C> iterateAll(int firstResult, int maxResults) {
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
	public IDaoIterator<C> iterateDefaultAll(int firstResult, int maxResults) {
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
	@SuppressWarnings("unchecked")
	@Override
	protected void _save(C object) {
		T t = ((B)object).getTransferObject();
		_save(t);
	}

	/**
	 * Saves the object.
	 * This method assumes that the object already exists. 
	 * @param object DTO to be saved
	 * @see #save(IGeneralBO, boolean)
	 */
	protected abstract void _save(T object);

	/********************** DELETING ********************/

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void _delete(C object) {
		T t = ((B)object).getTransferObject();
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
	protected class BusinessIterator implements IDaoIterator<C> {

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
			if (iterator instanceof IDaoIterator) ((IDaoIterator<?>)iterator).close();
		}
	}
}
