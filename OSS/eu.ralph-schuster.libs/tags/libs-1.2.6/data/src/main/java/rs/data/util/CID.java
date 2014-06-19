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
package rs.data.util;

import java.io.Serializable;

import rs.baselib.lang.HashCodeUtil;

/**
 * The Class ID for caching mechanism.
 * @author ralph
 *
 */
public class CID implements Serializable {
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -9209131720989751209L;
	private Class<?> clazz;
	private Object id;
	
	/**
	 * Constructor.
	 * @param clazz the class.
	 * @param id the id of the object
	 */
	public CID(Class<?> clazz, Object id) {
		this.clazz = clazz;
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int rc = HashCodeUtil.SEED;
		rc = HashCodeUtil.hash(rc, clazz);
		rc = HashCodeUtil.hash(rc, id);
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof CID)) return false;
		CID cid = (CID)o;
		if (!clazz.equals(cid.clazz)) return false;
		if ((id == null) || (cid.id == null)) return false;
		return cid.id.equals(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "CID["+clazz.getSimpleName()+";"+id+"]";
	}
}

