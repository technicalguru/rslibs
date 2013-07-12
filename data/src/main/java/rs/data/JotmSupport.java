/**
 * 
 */
package rs.data;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.objectweb.jotm.Jotm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Involves transactions.
 * @author ralph
 *
 */
public class JotmSupport {

	private static Logger log = LoggerFactory.getLogger(JotmSupport.class);
	
	private static Jotm jotm;
	private static TransactionManager txManager;
	
	/**
	 * Starts transaction support.
	 */
	public static synchronized void start() throws NamingException {
		if (txManager == null) {
			jotm = new Jotm(true, false);
			setTransactionManager(jotm.getTransactionManager());
			log.info("JOTM started");
		}
	}

	/**
	 * Stops the transaction support.
	 */
	public static synchronized void stop() {
		if (txManager != null) {
			jotm.stop();
			jotm = null;
			txManager = null;
			log.info("JOTM stopped");
		}
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
		JotmSupport.txManager = txManager;
	}

	/**
	 * Returns the user transaction.
	 * @return the user transaction
	 */
	public static UserTransaction getUserTransaction() {
		return jotm.getUserTransaction();
	}
}
