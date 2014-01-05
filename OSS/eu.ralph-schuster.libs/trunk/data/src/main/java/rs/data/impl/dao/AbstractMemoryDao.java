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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.data.api.bo.IGeneralBO;
import rs.data.impl.bo.AbstractGeneralBO;
import rs.data.util.DaoIteratorImpl;
import rs.data.util.IDaoIterator;

/**
 * A DAO storing its objects in a memory list only.
 * @author ralph
 *
 */
public abstract class AbstractMemoryDao<K extends Serializable, B extends AbstractGeneralBO<K>, C extends IGeneralBO<K>> extends AbstractGeneralDAO<K, B, C> {

	private Map<K, C> objects = new HashMap<K, C>();
	
	/**
	 * Constructor.
	 */
	public AbstractMemoryDao() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getObjectCount() {
		return objects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDefaultObjectCount() {
		return objects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C findBy(K id) {
		return objects.get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh(C object) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findAll(int firstResult, int maxResults) {
		List<C> rc = new ArrayList<C>();
		for (C c : objects.values()) {
			if (firstResult >= 0) {
				firstResult--;
				continue;
			}
			rc.add(c);
			if (maxResults == rc.size()) break;
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findDefaultAll(int firstResult, int maxResults) {
		return findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateAll(int firstResult, int maxResults) {
		return new DaoIteratorImpl<C>(findAll(firstResult, maxResults).iterator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateDefaultAll(int firstResult, int maxResults) {
		return iterateAll(firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteAll() {
		int rc = objects.size();
		objects.clear();
		clearCache();
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteDefaultAll() {
		return deleteAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void _create(C object) {
		K id = getNewId();
		if (id == null) throw new RuntimeException("Invalid key: null");
		if (objects.containsKey(id)) throw new RuntimeException("Duplicate key: "+id);
		((B)object).setId(id);
		objects.put(id, object);
	}

	/**
	 * Deliever a new id.
	 * @return a new id.
	 */
	protected abstract K getNewId();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _save(C object) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _delete(C object) {
		objects.remove(object.getId());
	}

	
}
