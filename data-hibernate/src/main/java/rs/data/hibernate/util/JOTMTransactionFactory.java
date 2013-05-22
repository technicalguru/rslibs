/**
 * 
 */
package rs.data.hibernate.util;

import javax.transaction.UserTransaction;

import org.hibernate.HibernateException;
import org.hibernate.transaction.JTATransactionFactory;

import rs.data.TransactionSupport;

/**
 * Extends the standard JTA implementation by getting the TX
 * from the TX manager.
 * @author ralph
 *
 */
public class JOTMTransactionFactory extends JTATransactionFactory {

	/**
	 * Constructor.
	 */
	public JOTMTransactionFactory() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UserTransaction getUserTransaction() {
		UserTransaction rc = TransactionSupport.getUserTransaction();
		if (rc == null) throw new HibernateException("No user transaction started");
		return rc;
	}

	
}
