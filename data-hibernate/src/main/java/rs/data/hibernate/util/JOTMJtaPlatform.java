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
package rs.data.hibernate.util;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.HibernateException;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import rs.data.JotmSupport;

/**
 * Extends the standard JTA implementation by getting the TX
 * from the TX manager.
 * @author ralph
 *
 */
public class JOTMJtaPlatform extends AbstractJtaPlatform {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public JOTMJtaPlatform() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TransactionManager locateTransactionManager() {
		return JotmSupport.getTransactionManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UserTransaction locateUserTransaction() {
		UserTransaction rc = JotmSupport.getUserTransaction();
		if (rc == null) throw new HibernateException("No user transaction started");
		return rc;
	}

	
}
