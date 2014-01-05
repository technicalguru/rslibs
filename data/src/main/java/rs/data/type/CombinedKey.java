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
package rs.data.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rs.baselib.util.CommonUtils;

/**
 * A key combining multiple other serializables.
 * @author ralph
 *
 */
public abstract class CombinedKey implements Serializable {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/** The keys */
	private List<Serializable> keys = new ArrayList<Serializable>();
	/** The type of keys */
	private List<Class<? extends Serializable>> keyClasses = new ArrayList<Class<? extends Serializable>>();

	/**
	 * Constructor.
	 */
	public CombinedKey(Serializable ...keys) {
		setKeys(keys);
	}

	/**
	 * Constructor.
	 */
	public void setKeys(Serializable ...keys) {
		this.keys.clear();
		this.keyClasses.clear();
		for (Serializable key : keys) {
			addKey(key);
		}
	}

	/**
	 * Adds the given key to the end of keys.
	 * @param key key to be added
	 */
	private void addKey(Serializable key) {
		addKey(getSize(), key);
	}

	/**
	 * Adds the given key at given index
	 * @param index index at which to be inserted
	 * @param key key to be added
	 */
	private void addKey(int index, Serializable key) {
		this.keys.add(index, key);
		this.keyClasses.add(index, key.getClass());
	}
	
	/**
	 * Sets the given key at given index.
	 * @param index index to be set
	 * @param key key to be set
	 */
	protected void setKey(int index, Serializable key) {
		this.keys.add(index, key);
		this.keyClasses.add(index, key.getClass());
	}
	
	/**
	 * Returns the number of keys.
	 * @return number of keys
	 */
	protected int getSize() {
		return keys.size();
	}
	
	/**
	 * Returns the key at given index
	 * @param index the inndex
	 * @return the key
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Serializable> T getKey(int index) {
		return (T)keys.get(index);
	}
	
	/**
	 * Returns the key at given index
	 * @param index the inndex
	 * @return the key
	 */
	protected Class<? extends Serializable> getKeyClass(int index) {
		return keyClasses.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int rc = 1;
		for (Serializable k : keys) {
			rc = prime * rc + k.hashCode();
		}
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (obj.getClass().equals(getClass())) {
			CombinedKey other = (CombinedKey)obj;
			if (other.getSize() != getSize()) return false;
			for (int i=0; i<getSize(); i++) {
				if (!CommonUtils.equals(getKey(i), other.getKey(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder rc = new StringBuilder();
		rc.append(getClass().getSimpleName());
		rc.append("[");
		boolean first = true;
		for (Serializable key : keys) {
			if (!first) rc.append(";"); 
			rc.append(key.toString());
			first = false;
		}
		rc.append("]");
		return rc.toString();
	}

}
