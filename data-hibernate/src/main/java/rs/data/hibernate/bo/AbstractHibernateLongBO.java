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
package rs.data.hibernate.bo;

import rs.data.impl.dto.GeneralDTO;

/**
 * Hibernate BO for Long based keys.
 * @author ralph
 *
 */
public abstract class AbstractHibernateLongBO<T extends GeneralDTO<Long>> extends AbstractHibernateBO<Long,T> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -7115538745412431772L;

	/**
	 * Constructor.
	 */
	public AbstractHibernateLongBO() {
	}

	/**
	 * Constructor.
	 * @param transferObject - the transfer object to be used
	 */
	public AbstractHibernateLongBO(T transferObject) {
		super(transferObject);
	}

}
