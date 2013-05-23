/**
 * 
 */
package rs.data.hibernate.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import rs.data.api.bo.GeneralBO;
import rs.data.hibernate.HibernateDaoMaster;
import rs.data.impl.bo.AbstractBO;
import rs.data.impl.dao.AbstractDAO;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.DaoIterator;

/**
 * Implements the Hibernate specific functions.
 * @param <K> type of Key
 * @param <T> type of Transfer Object
 * @param <B> type of Business Object Implementation
 * @param <C> type of Business Object Interface
 * @author ralph
 *
 */
public abstract class AbstractHibernateDAO<K extends Serializable, T extends GeneralDTO<K>, B extends AbstractBO<K, T>, C extends GeneralBO<K>> extends AbstractDAO<K, T, B, C> {

	/**
	 * Constructor.
	 */
	public AbstractHibernateDAO() {
		super();
	}

	/******************************** FINDING ***************************/

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected T _findById(K id) {
		T rc = (T) getSession().get(getTransferClass(), id);
		return rc;
	}

	/**
	 * Find objects by IDs.
	 * @param ids collection of IDs
	 * @return list of objects with specified IDd
	 */
	public List<T> findById(Collection<K> ids) {
		if ((ids == null) || ids.isEmpty()) return new ArrayList<T>();
		return _findByCriteria(buildCriteria(Restrictions.in("id", ids)));
	}

	/**
	 * Returns the number of domain objects.
	 * @return number of objects.
	 */
	public int getObjectCount() {
		return getRowCount(buildCriteria());
	}

	/**
	 * Returns the number of objects with default criteria matched.
	 * This method shall be used if you want to count only non-deleted objects.
	 * @return number of objects
	 */
	public int getDefaultObjectCount() {
		return getRowCount(getDefaultCriteria());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<T> _findAll(int firstResult, int maxResults) {
		Criteria criteria = buildCriteria(firstResult, maxResults);
		return _findByCriteria(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<T> _findDefaultAll(int firstResult, int maxResults) {
		Criteria criteria = getDefaultCriteria();
		if (criteria == null) criteria = buildCriteria();
		return _findByCriteria(filterResult(criteria, firstResult, maxResults));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Iterator<T> _iterateAll(int firstResult, int maxResults) {
		return _iterateByCriteria(null, null, firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Iterator<T> _iterateDefaultAll(int firstResult, int maxResults) {
		Criterion criterions[] = getDefaultCriterions();
		return _iterateByCriteria(criterions, null, firstResult, maxResults);
	}

	/******************************** CREATE ***************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _create(T object) {
		// Just save
		getSession().save(object);
	}

	/******************************** UPDATE ***************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _save(T object) {
		Session session = getSession();
		session.merge(object);
	}

	/******************************** DELETE ***************************/

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void _delete(T object) {
		object = (T)getSession().merge(object);
		getSession().delete(object);
	}

	/**
	 * Deletes all objects that match default criteria.
	 * @return number of object deleted
	 */
	@Override
	protected int _deleteDefaultAll() {
		return deleteByCriteria(getDefaultCriterions());
	}

	/**
	 * Deletes all objects.
	 * @return number of object deleted
	 */
	@Override
	protected int _deleteAll() {
		return deleteByCriteria(null);
	}

	/************************************ Criteria stuff ******************************************/
	/**
	 * Returns the Hibernate session. This method will start a new transaction
	 * if required.
	 * 
	 * @return hibernate session.
	 */
	public Session getSession() {
		return ((HibernateDaoMaster)getDaoMaster()).getSession();
	}

	/**
	 * Returns a default Hibernate criteria for search.
	 * This can be used to simplify queries when tables may contain soft-deleted objects.
	 * This abstract implementation return null.
	 * @return a default criteria to be used
	 */
	protected Criteria getDefaultCriteria() {
		return buildCriteria(getDefaultCriterions());
	}

	/**
	 * Returns the default criterions.
	 * @return the default criterions for a query
	 */
	protected Criterion[] getDefaultCriterions() {
		return null;
	}

	/**
	 * Finds a single object defined by the Hibernate criteria.
	 * Note that the criteria might match many objects. Only the first result will
	 * be returned.
	 * @param criteria criteria for search
	 * @return first object matching the criteria
	 */
	protected T _findSingleByCriteria(Criteria criteria) {
		criteria.setMaxResults(1);
		List<T> l = _findByCriteria(criteria);
		if (l.size() > 0) return l.get(0);
		return null;
	}

	/**
	 * Finds a single object defined by the Hibernate criteria.
	 * Note that the criteria might match many objects. Only the first result will
	 * be returned.
	 * @param criteria criteria for search
	 * @return first object matching the criteria
	 */
	protected C findSingleByCriteria(Criteria criteria) {
		T rc = _findSingleByCriteria(criteria);
		return getBusinessObject(rc);
	}

	/**
	 * Builds an Hibernate criteria with unlimited result count.
	 * @param criterion list of criterions to be added.
	 * @return the Hibernate criteria
	 */
	protected Criteria buildCriteria(Criterion... criterion) {
		return buildCriteria(-1, -1, criterion);
	}

	/**
	 * Builds an Hibernate criteria with unlimited result count.
	 * @param firstResult index of first result returned
	 * @param maxResults maximum number of results returned
	 * @param criterion list of criterions to be added.
	 * @return the Hibernate criteria
	 */
	protected Criteria buildCriteria(int firstResult, int maxResults, Criterion... criterion) {
		return buildCustomCriteria(getTransferClass(), firstResult, maxResults, criterion);
	}

	/**
	 * Builds an Hibernate criteria with unlimited result count.
	 * @param firstResult index of first result returned
	 * @param maxResults maximum number of results returned
	 * @param criterion list of criterions to be added.
	 * @return the Hibernate criteria
	 */
	protected Criteria buildCustomCriteria(Class<?> forClass, int firstResult, int maxResults, Criterion... criterion) {
		if (forClass == null) return null;
		org.hibernate.Criteria crit = getSession().createCriteria(forClass);
		if (criterion != null) {
			for (Criterion c : criterion) {
				crit.add(c);
			}
		}
		return filterResult(crit, firstResult, maxResults);
	}


	/**
	 * Applies the result count limitation to the Hibernate criteria.
	 * @param crit criteria to be limited
	 * @param firstResult index of first result returned
	 * @param maxResults maximum number of results returned
	 * @return limited criteria.
	 */
	protected Criteria filterResult(Criteria crit, int firstResult, int maxResults) {
		if (firstResult > 0) crit.setFirstResult(firstResult);
		if (maxResults > 0) crit.setMaxResults(maxResults);
		return crit;
	}

	/**
	 * Finds all DTOs matching the Hibernate criteria.
	 * @param crit Hibernate criteria
	 * @return the list of objects matching the criteria
	 */
	@SuppressWarnings("unchecked")
	protected List<T> _findByCriteria(Criteria crit) {
		if (crit == null) crit = buildCriteria();
		List<T> rc = crit.list();

		return rc;
	}

	/**
	 * Finds all BOs matching the Hibernate criteria.
	 * @param crit Hibernate criteria
	 * @return the list of objects matching the criteria
	 */
	protected List<C> findByCriteria(Criteria crit) {
		List<T> l = _findByCriteria(crit);
		List<C> rc = new ArrayList<C>();
		wrap(rc, l);
		return rc;
	}

	/**
	 * Execute an unspecific Hibernate criteria.
	 * If the criteria returns T then {@link AbstractHibernateDAO#findByCriteria(org.hibernate.Criteria)}
	 * must be used.
	 * @param crit Hibernate criteria
	 * @return the list of objects matching the criteria
	 */
	protected List<?> executeCriteria(Criteria crit) {
		if (crit == null) crit = buildCriteria();
		return crit.list();
	}

	/**
	 * Creates an iterator object for the Hibernate criteria.
	 * Use this inside subclasses as a convenience method.
	 * @param criterions Hibernate criterions
	 * @return iterator object for the criteria
	 */
	protected DaoIterator<T> _iterateByCriteria(Criterion criterions[]) {
		return _iterateByCriteria(criterions, null, -1, -1);
	}

	/**
	 * Creates an iterator object for the Hibernate criteria.
	 * Use this inside subclasses as a convenience method.
	 * @param criterions Hibernate criterions
	 * @param orders Hibernate orders
	 * @return iterator object for the criteria
	 */
	protected DaoIterator<T> _iterateByCriteria(Criterion criterions[], Order orders[]) {
		return _iterateByCriteria(criterions, orders, -1, -1);
	}

	/**
	 * Creates an iterator object for the Hibernate criteria.
	 * Use this inside subclasses as a convenience method.
	 * @param criterions Hibernate criterions
	 * @param orders Hibernate orders
	 * @param firstResult index of first result to return
	 * @param maxResults maximum number of results to return
	 * @return iterator object for the criteria
	 */
	protected DaoIterator<T> _iterateByCriteria(Criterion criterions[], Order orders[], int firstResult, int maxResults) {
		return new IteratorImpl(criterions, orders, firstResult, maxResults);
	}


	/**
	 * Creates an iterator object for the Hibernate criteria.
	 * Use this inside subclasses as a convenience method.
	 * @param criterions Hibernate criterions
	 * @param orders Hibernate orders
	 * @param firstResult index of first result to return
	 * @param maxResults maximum number of results to return
	 * @return iterator object for the criteria
	 */
	protected DaoIterator<C> iterateBy(Criterion criterions[], Order orders[], int firstResult, int maxResults) {
		return wrap(_iterateByCriteria(criterions, orders, firstResult, maxResults));
	}

	/**
	 * Counts the number of objects returned by this Hibernate criteria.
	 * Use this inside subclasses as a convenience method.
	 * @param crit Hibernate criteria
	 * @return number of objects returned by the criteria
	 */
	protected int getRowCount(Criteria crit) {
		crit = crit.setProjection(Projections.rowCount());
		crit.setReadOnly(true);
		List<?> rc = crit.list();
		long count = (Long)rc.get(0);
		return (int)count;
	}

	/**
	 * Deletes all objects matching this criteria.
	 * @param criterions criterions for deletion
	 * @return number of objects deleted
	 */
	protected int deleteByCriteria(Criterion criterions[]) {
		int rc = 0;
		Iterator<T> i = _iterateByCriteria(criterions, null);
		while (i.hasNext()) {
			_delete(i.next());
			rc++;
		}
		return rc;
	}

	/**
	 * Returns the result count for this criteria. The Criteria object will be changed and cannot 
	 * be used as a result-returning query anymore.
	 * @param criteria Criteria for object (row) count
	 * @return the row count.
	 */
	protected int getObjectCount(Criteria criteria) {
		if (criteria == null) criteria = buildCriteria();
		criteria.setProjection(Projections.rowCount());
		return (Integer) criteria.list().get(0);
	}

	/**
	 * Iterator for DTOs.
	 * Funny thing: it can be reused at any time!
	 * @author ralph
	 *
	 */
	protected class IteratorImpl implements DaoIterator<T> {

		private ScrollableResults scrollableResult = null;
		private T nextObject = null;

		private Criteria criteria; 
		private Criterion criterions[];
		private Order orders[];
		private int firstResult;
		private int maxResults;

		/**
		 * Constructs the iterator.
		 * @param criterions Hibernate criterions
		 * @param orders Hibernate orders
		 * @param firstResult index of first result to return
		 * @param maxResults maximum number of results to return
		 */
		public IteratorImpl(Criterion criterions[], Order orders[], int firstResult, int maxResults) {
			this.criterions = criterions;
			this.orders = orders;
			this.firstResult = firstResult;
			this.maxResults = maxResults;
		}

		/**
		 * Returns true when there is another object to be read.
		 * @return true or false.
		 */
		@Override
		public boolean hasNext() {
			if (nextObject == null) retrieveNext();
			return nextObject != null;
		}

		/**
		 * Returns the next object.
		 * @return next object
		 */
		@Override
		public T next() {
			if (nextObject == null) retrieveNext();
			T rc = nextObject;
			nextObject = null;
			return rc;
		}

		/**
		 * Retrieves the next object from the underlying Hibernate result.
		 */
		@SuppressWarnings("unchecked")
		private void retrieveNext() {
			if (nextObject != null) return;

			// Create scrollable result
			if (scrollableResult == null) { 

				// Create criteria now
				if (criteria == null) {
					getFactory().begin();
					criteria = buildCriteria(firstResult, maxResults, criterions);
					if (orders != null) {
						for (Order order : orders) {
							criteria.addOrder(order);
						}
					}
				}

				//log.debug("Creating Scroller...");
				this.scrollableResult = criteria.scroll(ScrollMode.FORWARD_ONLY);
				//log.debug("Scroller created");
			}

			//log.debug("Asking for next object...");
			if (scrollableResult.next()) {
				//log.debug("   Next object in postSelect...");
				nextObject = (T)scrollableResult.get(0);
			}
			//log.debug("    Next object retrieved");
		}

		/**
		 * Remove is not supported.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove not supported");      
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() {
			if (scrollableResult != null) {
				scrollableResult.close();
				scrollableResult = null;
				criteria = null;
				nextObject = null;
				getFactory().commit();
			}
		}

		/**
		 * Finalizes this object by calling close().
		 * @see java.lang.Object#finalize()
		 */
		@Override
		protected void finalize() throws Throwable {
			close();
			super.finalize();
		}
	}		
}