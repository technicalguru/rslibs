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
package rs.data.event;

import java.util.EventObject;

import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;

/**
 * Event that will be fired from a DAO.
 * @author ralph
 *
 */
public class DaoEvent extends EventObject {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -2121712297338985716L;

	/**
	 * Type of event information.
	 * @author ralph
	 *
	 */
	public static enum Type {
		/** An object was created by the DAO. */
		OBJECT_CREATED,
		/** An object was updated by the DAO. */
		OBJECT_UPDATED,
		/** An object was deleted by the DAO. */
		OBJECT_DELETED,
		/** All default objects were deleted by the DAO. */
		ALL_DEFAULT_DELETED,
		/** All objects were deleted by the DAO. */
		ALL_DELETED
	};
	
	private Type type;
	private  IGeneralBO<?> object;
	
	/**
	 * Constructor for DELETE_ALL events.
	 * @param source - the source of the event
	 * @param type - the type of event that is happening
	 */
	public DaoEvent(IGeneralDAO<?, ?> source, Type type) {
		this(source, type, null);
	}

	/**
	 * Constructor.
	 * @param source - the source of the event
	 * @param type - the type of event that is happening
	 * @param object - the obbject that was affected
	 */
	public DaoEvent(IGeneralDAO<?, ?> source, Type type, IGeneralBO<?> object) {
		super(source);
		this.type = type;
		this.object = object;
	}

	/**
	 * Returns the type.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns the object.
	 * @return the object
	 */
	public IGeneralBO<?> getObject() {
		return object;
	}

	
}
