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

import java.io.Serializable;

import org.hibernate.Session;

import rs.data.hibernate.HibernateDaoMaster;
import rs.data.impl.bo.AbstractPlainBO;

/**
 * Abstract BO implementation for Hibernate where the BO itself is used
 * as transfer object in Hibernate.
 * @author ralph
 *
 */
public class AbstractPlainHibernateBO<K extends Serializable> extends AbstractPlainBO<K> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public AbstractPlainHibernateBO() {
	}

	/**
	 * Returns the DAO master.
	 * @return the DAO master
	 */
	protected HibernateDaoMaster getDaoMaster() {
		return (HibernateDaoMaster)getDao().getDaoMaster();
	}

	/**
	 * Returns a Hibernate session.
	 * @return the session
	 */
	protected Session getSession() {
		return getDaoMaster().getSession();
	}

	
}
