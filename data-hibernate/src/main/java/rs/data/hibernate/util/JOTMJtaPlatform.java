/**
 * 
 */
package rs.data.hibernate.util;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.HibernateException;
import org.hibernate.service.jta.platform.internal.AbstractJtaPlatform;

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
