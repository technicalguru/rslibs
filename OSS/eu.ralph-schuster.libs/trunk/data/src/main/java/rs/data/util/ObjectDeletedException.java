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

import rs.data.api.bo.IGeneralBO;

/**
 * Informs when an object was exceptionally deleted from
 * persistence store. 
 * @author ralph
 * @since 1.2.9
 *
 */
public class ObjectDeletedException extends RuntimeException {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/** Object that was deleted */
	private IGeneralBO<?> objectDeleted;
	
    /** Constructs a new exception.
	 * @param objectDeleted the object that was deleted and causing this exception.
     */
	public ObjectDeletedException(IGeneralBO<?> objectDeleted) {
		super("Object was deleted: "+objectDeleted.getId());
		this.objectDeleted = objectDeleted;
	}

	/**
	 * Returns the object that was deleted and causing this exception.
	 * @return the object deleted
	 */
	public IGeneralBO<?> getObjectDeleted() {
		return objectDeleted;
	}

}
