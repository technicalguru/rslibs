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
package rs.data;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.objectweb.jotm.Jotm;
import org.objectweb.jotm.TimerManager;
import org.objectweb.jotm.rmi.RmiLocalConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.lang.LangUtils;

/**
 * Involves transactions.
 * @author ralph
 *
 */
public class JotmSupport {

	private static Logger log = LoggerFactory.getLogger(JotmSupport.class);
	
	private static Jotm jotm;
	private static TransactionManager txManager;
	private static boolean debugTransactions = false;
	private static boolean traceTransactions = false;
	
	/**
	 * Starts transaction support.
	 */
	public static synchronized void start() throws NamingException {
		setDebugTransactions(LangUtils.getBoolean(System.getProperty("transaction.debug")));
		setTraceTransactions(LangUtils.getBoolean(System.getProperty("transaction.trace")));
		if (txManager == null) {
			jotm = new Jotm(true, false, new RmiLocalConfiguration());
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
			// Stop every thread remaining
			TimerManager.stop(true);
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
		log.debug("TX-"+Thread.currentThread().getId()+" returned");
		return jotm.getUserTransaction();
	}
	
	/**
	 * Returns whether debugging of transaction demarcations.
	 * @return <code>true</code> when debugging shall be enabled (via SLF4J)
	 */
	public static boolean isDebugTransactions() {
		return debugTransactions;
	}
	
	/**
	 * Sets debugging of transaction demarcations.
	 * @param debug <code>true</code> when debugging shall be enabled (via SLF4J)
	 */
	public static void setDebugTransactions(boolean debug) {
		debugTransactions = debug;
		if (debugTransactions) log.info("Enabling transaction demarcation log");
	}
	
	/**
	 * Returns whether stacktracing is enabled with {@link #isDebugTransactions() transaction demarcation debugging} option.
	 * @return <code>true</code> when stacktrace shall be enabled (via SLF4J)
	 */
	public static boolean isTraceTransactions() {
		return traceTransactions;
	}
	
	/**
	 * Sets whether stacktracing is enabled with {@link #isDebugTransactions() transaction demarcation debugging} option.
	 * @param trace <code>true</code> when stacktrace shall be enabled (via SLF4J)
	 */
	public static void setTraceTransactions(boolean trace) {
		traceTransactions = trace;
		if (debugTransactions && traceTransactions) log.info("Enabling stacktrace for transaction demarcation");
	}

}
