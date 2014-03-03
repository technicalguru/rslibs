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

import rs.data.api.IDaoFactory;

/**
 * Event notification from Dao Factory.
 * @author ralph
 *
 */
public class DaoFactoryEvent extends EventObject {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3614048885876975805L;

	/** Type of factory event */
	public static enum Type {
		/** Transaction was started */
		TRANSACTION_STARTED,
		/** Transaction is committing but did not finish yet */
		TRANSACTION_COMMITTING,
		/** Transaction has been committed */
		TRANSACTION_COMMITTED,
		/** Transaction is rolling back but did not finish yet */
		TRANSACTION_ROLLING_BACK,
		/** Transaction has been rolled back */
		TRANSACTION_ROLLED_BACK,
		/** A change was made to the data model. (Details should be caught via {@link DaoEvent}). */
		MODEL_CHANGED
	}
	
	private Type type;
	
	/**
	 * Constructor.
	 * @param source factory
	 */
	public DaoFactoryEvent(IDaoFactory source, Type type) {
		super(source);
		this.type = type;
	}

	/**
	 * Returns the type.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName()+"[source="+getSource().getClass().getSimpleName()+";type="+getType().name()+"]";
	}

	
}
