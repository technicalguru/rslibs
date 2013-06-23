/**
 * 
 */
package rs.data;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.objectweb.jotm.Jotm;

/**
 * Involves transactions.
 * @author ralph
 *
 */
public class TransactionSupport {

	private static Jotm jotm;
	private static TransactionManager txManager;
	
	/**
	 * Starts transaction support.
	 */
	public static synchronized TransactionManager start() throws NamingException {
		if (txManager != null) throw new RuntimeException("Transaction Manager already started");
		jotm = new Jotm(true, false);
		setTransactionManager(jotm.getTransactionManager());
		return getTransactionManager();
	}

	/**
	 * Stops the transaction support.
	 */
	public static synchronized void stop() {
		if (jotm != null) jotm.stop();
	}
	
	/**
	 * Returns the txManager.
	 * @return the txManager
	 */
	public static TransactionManager getTransactionManager() {
		return txManager;
	}

	/**
	 * Sets the txManager.
	 * @param txManager the txManager to set
	 */
	public static void setTransactionManager(TransactionManager txManager) {
		TransactionSupport.txManager = txManager;
	}

	/**
	 * Returns the user transaction.
	 * @return the user transaction
	 */
	public static UserTransaction getUserTransaction() {
		return jotm.getUserTransaction();
	}
}
