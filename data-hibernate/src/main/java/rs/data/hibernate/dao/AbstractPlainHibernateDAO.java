/*
 * This file is part of RS Library (Data Hibernate Library).
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
package rs.data.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import rs.data.api.bo.IGeneralBO;
import rs.data.hibernate.HibernateDaoMaster;
import rs.data.hibernate.bo.AbstractPlainHibernateBO;
import rs.data.impl.dao.AbstractPlainDAO;
import rs.data.util.IDaoIterator;
import rs.data.util.ObjectDeletedException;

/**
 * Abstract implementation of DAO using {@link AbstractPlainBO}s as transfer objects
 * between HMB store and application.
 * <p>Please notice that caching mechanism is disabled for this DAO and cannot be enabled.</p>
 * @author ralph
 *
 */
public abstract class AbstractPlainHibernateDAO<K extends Serializable, B extends AbstractPlainHibernateBO<K>, C extends IGeneralBO<K>> extends AbstractPlainDAO<K, C, B> {

	/**
	 * Constructor.
	 */
	public AbstractPlainHibernateDAO() {
		super.setCacheEnabled(false);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected C _newInstance() {
		C rc = super._newInstance();
		prepareObject(rc, false);
		return rc;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCacheEnabled(boolean enabled) {
		// NULL method. Never enable cache!
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getObjectCount() {
		return getRowCount(buildCriteria());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDefaultObjectCount() {
		return getRowCount(getDefaultCriteria());
	}

	/*************************** FIND / ITERATE **********************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public C findBy(K id) {
		C rc = (C) getSession().get(getBoImplementationClass(), id);
		prepareObject(rc, true);
		return rc;
	}

	/**
	 * Prepares an data object for usage.
	 * @param obj object to be prepared
	 * @param persisted whether the object was loaded from DB or not
	 * @return the object
	 */
	protected C prepareObject(C obj, boolean persisted) {
		if (obj != null) {
			obj.set("dao", this);
			obj.set("fromDb", persisted);
			afterNewInstance(obj, persisted);
		}
		return obj;
	}
	
	/**
	 * Prepares all objects in list for usage.
	 * @param objects the list of objects
	 * @param persisted whether objects were loaded from DB
	 * @return the list
	 */
	protected List<C> prepareObjects(List<C> objects, boolean persisted) {
		if (objects != null) {
			for (C obj : objects) {
				prepareObject(obj, persisted);
			}
		}
		return objects;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findAll(int firstResult, int maxResults) {
		Criteria criteria = buildCriteria(firstResult, maxResults);
		return findByCriteria(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findDefaultAll(int firstResult, int maxResults) {
		Criteria criteria = getDefaultCriteria();
		if (criteria == null) criteria = buildCriteria();
		return findByCriteria(filterResult(criteria, firstResult, maxResults));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateAll(int firstResult, int maxResults) {
		return iterateByCriteria(null, null, firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateDefaultAll(int firstResult, int maxResults) {
		Criterion criterions[] = getDefaultCriterions();
		return iterateByCriteria(criterions, null, firstResult, maxResults);
	}

	/**************************** CREATE ***********************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _create(C object) {
		// Just save
		getSession().save(object);
	}

	/**************************** UPDATE ***********************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _save(C object) {
		Session session = getSession();
		session.merge(object);
	}

	/**************************** DELETE ***********************/

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void _delete(C object) {
		object = (C)getSession().merge(object);
		getSession().delete(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteAll() {
		return deleteByCriteria(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteDefaultAll() {
		return deleteByCriteria(getDefaultCriterions());
	}


	/************************************ Criteria stuff ******************************************/
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
	protected C findSingleByCriteria(Criteria criteria) {
		criteria.setMaxResults(1);
		List<C> l = findByCriteria(criteria);
		if (l.size() > 0) return l.get(0);
		return null;
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
		return buildCustomCriteria(getBoImplementationClass(), firstResult, maxResults, criterion);
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
	 * Finds all BOs matching the Hibernate criteria.
	 * @param crit Hibernate criteria
	 * @return the list of objects matching the criteria
	 */
	@SuppressWarnings("unchecked")
	protected List<C> findByCriteria(Criteria crit) {
		if (crit == null) crit = buildCriteria();
		List<C> rc = prepareObjects(crit.list(), true);
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
	protected IDaoIterator<C> iterateByCriteria(Criterion criterions[]) {
		return iterateByCriteria(criterions, null, -1, -1);
	}

	/**
	 * Creates an iterator object for the Hibernate criteria.
	 * Use this inside subclasses as a convenience method.
	 * @param criterions Hibernate criterions
	 * @param orders Hibernate orders
	 * @return iterator object for the criteria
	 */
	protected IDaoIterator<C> iterateByCriteria(Criterion criterions[], Order orders[]) {
		return iterateByCriteria(criterions, orders, -1, -1);
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
	protected IDaoIterator<C> iterateByCriteria(Criterion criterions[], Order orders[], int firstResult, int maxResults) {
		return new IteratorImpl(criterions, orders, firstResult, maxResults);
	}

	/**
	 * Deletes all objects matching this criteria.
	 * @param criterions criterions for deletion
	 * @return number of objects deleted
	 */
	protected int deleteByCriteria(Criterion criterions[]) {
		int rc = 0;
		IDaoIterator<C> i = iterateByCriteria(criterions, null);
		while (i.hasNext()) {
			_delete(i.next());
			rc++;
		}
		i.close();
		return rc;
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

	/******************************** HIBERNATE STUFF ***********************/

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
	 * {@inheritDoc}
	 */
	@Override
	public void refresh(C object) {
		try {
			getFactory().begin();
			getSession().refresh(object, LockOptions.NONE);
			getFactory().commit();
		} catch (ObjectDeletedException e) {
			// Ignore all subsequent exceptions and re-throw
			try {
				getFactory().commit();
			} catch (Exception e2) { 
				try { getFactory().rollback(); } catch (Exception e3) {	}				
			}
			throw e;
		} catch (Exception e) {
			try {
				getFactory().rollback();
			} catch (Exception e2) {
				throw new RuntimeException("Cannot rollback exception", e2);
			}
			throw new RuntimeException("Cannot initialize DTO", e);
		}
	}

	/**
	 * Iterator for objects.
	 * Funny thing: it can be reused at any time!
	 * @author ralph
	 *
	 */
	protected class IteratorImpl implements IDaoIterator<C> {

		private ScrollableResults scrollableResult = null;
		private C nextObject = null;

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
		public C next() {
			if (nextObject == null) retrieveNext();
			C rc = nextObject;
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
				nextObject = prepareObject((C)scrollableResult.get(0), true);
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
				try {
					getFactory().commit();
				} catch (Exception e) {
					getLog().error("Cannot commit TX", e);
					try {
						getFactory().rollback();
					} catch (Exception e2) {
						getLog().error("Cannot rollback TX", e);
					}
				}
			}
		}

		/*
		 * Finalizes this object by calling close().
		 * @see java.lang.Object#finalize()
		 * Deprecated
		@Override
		protected void finalize() throws Throwable {
			close();
			super.finalize();
		}
		*/
	}		

}
