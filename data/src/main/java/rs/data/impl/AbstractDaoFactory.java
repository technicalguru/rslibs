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
package rs.data.impl;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.configuration.ConfigurationUtils;
import rs.baselib.configuration.IConfigurable;
import rs.baselib.io.FileFinder;
import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;
import rs.baselib.util.IUrlTransformer;
import rs.data.api.IDaoFactory;
import rs.data.api.IDaoMaster;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.event.DaoEvent;
import rs.data.event.DaoFactoryEvent;
import rs.data.event.DaoFactoryEvent.Type;
import rs.data.event.IDaoFactoryListener;
import rs.data.event.IDaoListener;
import rs.data.impl.dao.AbstractBasicDAO;
import rs.data.impl.dao.AbstractDAO;
import rs.data.impl.dto.GeneralDTO;

/**
 * The basic implementation of a DAO factory.
 * @author ralph
 *
 */
public abstract class AbstractDaoFactory implements IDaoFactory, IConfigurable {

	private Logger log = LoggerFactory.getLogger(getClass());

	// TX Management	
	private ThreadLocal<TransactionContext> txContext = new ThreadLocal<TransactionContext>();
	private Set<IDaoFactoryListener> listeners = new HashSet<IDaoFactoryListener>();
	private boolean debugTransactions = false;
	private boolean traceTransactions = false;


	private IDaoListener daoListener = new MyDaoListener();
	private Map<String, Object> properties = new HashMap<String, Object>();
	private Map<String, String> params = new HashMap<String, String>();
	private long defaultTimeout = DEFAULT_TX_TIMEOUT;
	private TransactionManager txManager;
	private IUrlTransformer urlTransformer;
	private Map<String, IDaoMaster> daoMasters = new HashMap<String, IDaoMaster>();
	private Map<String,IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>>> daos =
			new HashMap<String, IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>>>();

	/**
	 * Constructor.
	 */
	public AbstractDaoFactory() {
		setDebugTransactions(LangUtils.getBoolean(System.getProperty("transaction.debug")));
		setTraceTransactions(LangUtils.getBoolean(System.getProperty("transaction.trace")));
	}

	/**
	 * Returns the logger object.
	 * @return the logger object
	 */
	protected Logger getLog() {
		return log;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {
		{
			// Read all parameters first
			int index = 0;
			while (index >= 0) {
				try {
					String name = cfg.getString("property("+index+")[@name]");
					if (name == null) break;
					String value = cfg.getString("property("+index+")");
					index++;
					params.put(name, value);
					log.debug(name+"="+value);
				} catch (Exception e) {
					index = -1;
				}
			}
		}

		// Load the URL transformer, if it exists:
		try {
			SubnodeConfiguration tCfg = ((HierarchicalConfiguration)cfg).configurationAt("UrlTransformer(0)");
			setUrlTransformer((IUrlTransformer)ConfigurationUtils.load(tCfg, true));
		} catch (Exception e) {
			log.info("No URL Transformer loaded");
		}

		{
			// Load DAO masters
			int index = 0;
			while (index >= 0) {
				try {
					SubnodeConfiguration tCfg = ((HierarchicalConfiguration)cfg).configurationAt("DaoMaster("+index+")");
					IDaoMaster daoMaster = loadDaoMaster(tCfg);
					String id = cfg.getString("DaoMaster("+index+")[@name]");
					if (id == null) id = "default";
					getLog().debug("DAO Master \""+id+"\": "+daoMaster.getClass().getName());
					setDaoMaster(id, daoMaster);
					index++;
				} catch (IllegalArgumentException e) {
					index = -1;
				} catch (Exception e) {
					getLog().error("Cannot load DaoMaster: ", e);
					index = -1;
				}
			}

		}

		{
			// Load DAOs
			int index = 0;
			while (index >= 0) {
				try {
					SubnodeConfiguration dCfg = ((HierarchicalConfiguration)cfg).configurationAt("Dao("+index+")");
					IGeneralDAO<?, ?> dao = loadDao(dCfg);
					getLog().debug("DAO: "+dao.getClass().getName());
					index++;
				} catch (IllegalArgumentException e) {
					index = -1;
				} catch (Exception e) {
					getLog().error("Cannot load DAO: ", e);
					index = -1;
				}
			}

		}
	}

	/**
	 * Loads an object from a configuration.
	 * The object is configured if it is an instance of {@link IConfigurable}.
	 * @param config configuration
	 * @return the object
	 */
	public IDaoMaster loadDaoMaster(SubnodeConfiguration config) {
		// Tweak the class loader to avoid some unexpected RCP problems
		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(this.getClass().getClassLoader());

		try {
			String className = config.getString("[@class]");
			Class<?> clazz = LangUtils.forName(className);
			IDaoMaster rc = (IDaoMaster)clazz.newInstance();
			rc.setFactory(this);
			if (rc instanceof IConfigurable) {
				((IConfigurable)rc).configure(config);
			}
			return rc;
		} catch (Exception e) {
			throw new RuntimeException("Cannot load class from configuration", e);
		} finally {
			// Restore the class loader
			thread.setContextClassLoader(loader);
		}

	}

	/**
	 * Sets the DAO master.
	 * @param id id of master
	 * @param daoMaster master to bet set
	 */
	public void setDaoMaster(String id, IDaoMaster daoMaster) {
		if (daoMasters.containsKey(id)) throw new RuntimeException("DAO Master already exists: "+id);
		daoMasters.put(id, daoMaster);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoMaster getDaoMaster(String id) {
		return daoMasters.get(id);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() {
		// Shutdown all DAOs
		for (IGeneralDAO<?, ?> dao : daos.values()) {
			try {
				dao.shutdown();
			} catch (Throwable t) {
				getLog().error("Cannot shutdown DAO:", t);
			}
		}
		// Shutdown all DAO Masters
		for (IDaoMaster master : daoMasters.values()) {
			try {
				master.shutdown();
			} catch (Throwable t) {
				getLog().error("Cannot shutdown DAO Master:", t);
			}
		}
		// Check the TX contexts
	}

	/**
	 * Loads an object from a configuration.
	 * The object is configured if it is an instance of {@link IConfigurable}.
	 * @param config configuration
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> loadDao(HierarchicalConfiguration config) {
		// Tweak the class loader to avoid some unexpected RCP problems
		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(this.getClass().getClassLoader());

		IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> rc = null;
		try {

			// Create the class
			rc = (IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>>) ConfigurationUtils.load(config, false);

			// Set the factory
			rc.setFactory(this);

			// Set the DAO Master
			String masterId = config.getString("[@daoMaster]");
			if (masterId == null) masterId = "default";
			rc.setDaoMaster(getDaoMaster(masterId));

			// Configure it
			if (rc instanceof IConfigurable) ConfigurationUtils.configure((IConfigurable)rc, config);

			// Add the DAO to our list
			String name = config.getString("[@name]");
			registerDao(name, rc);
		} catch (Exception e) {
			throw new RuntimeException("Cannot load class from configuration", e);
		} finally {
			// Restore the class loader
			thread.setContextClassLoader(loader);
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getProperty(String name) {
		return properties.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getParameter(String name) {
		return params.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<String> getParameterKeys() {
		return params.keySet().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getParameterUrl(String name) throws MalformedURLException {
		String value = getParameter(name);
		if (value == null) return null;
		IUrlTransformer transformer = getUrlTransformer();
		if (transformer != null) return transformer.toURL(value);
		return FileFinder.find(value);
	}


	/********************* DAO Handling ************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerDao(IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> dao) {
		registerDao(dao.getClass().getName(), dao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerDao(String name, IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> dao) {
		if (name == null) name = dao.getClass().getName();

		dao.setFactory(this); // Just in case

		// Add the factory as a listener
		dao.addDaoListener(daoListener);

		this.daos.put(name, dao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <X extends IGeneralDAO<?, ?>> X getDao(Class<X> clazz) {
		X rc = null;
		rc = (X) daos.get(clazz.getName());
		if (rc == null) {
			for (IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> dao : daos.values()) {
				if (clazz.isInstance(dao)) {
					rc = (X)dao;
					break;
				}
			}
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <X extends IGeneralDAO<?, ?>> X getDao(String name, Class<X> clazz) {
		return (X) daos.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <K extends Serializable, T extends GeneralDTO<K>> IGeneralDAO<K, ?> getDaoFor(T o) {
		for (IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> dao : daos.values()) {
			if (dao instanceof AbstractDAO) {
				if (((AbstractDAO<?,?,?,?>)dao).getTransferClass().isInstance(o)) {
					return (IGeneralDAO<K, ?>) dao;
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <B extends IGeneralBO<?>> IGeneralDAO<?, B> getDaoFor(B o) {
		return (IGeneralDAO<?, B>) getDaoFor(o.getClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <B extends IGeneralBO<?>> IGeneralDAO<?,B> getDaoFor(Class<B> clazz) {
		for (IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>> dao : daos.values()) {
			if (dao instanceof AbstractBasicDAO) {
				if (((AbstractBasicDAO<?,?>)dao).getBoInterfaceClass().equals(clazz)) {
					return (IGeneralDAO<?, B>) dao;
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<IGeneralDAO<?, ?>> getDaos() {
		return new ArrayList<IGeneralDAO<?,?>>(daos.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearCache() {
		for (IGeneralDAO<?, ?> dao : getDaos()) {
			dao.clearCache();
		}
	}

	/********************* TRANSACTIONS ************************/

	/**
	 * Returns the urlTransformer.
	 * @return the urlTransformer
	 */
	public IUrlTransformer getUrlTransformer() {
		return urlTransformer;
	}

	/**
	 * Sets the urlTransformer.
	 * @param urlTransformer the urlTransformer to set
	 */
	public void setUrlTransformer(IUrlTransformer urlTransformer) {
		this.urlTransformer = urlTransformer;
	}

	/**
	 * Returns the TX manager used.
	 * If there was no TX manager set before, this method will create a TX manager.
	 * @return the transaction manager
	 */
	@Override
	public TransactionManager getTransactionManager() {
		if (txManager == null) {
			synchronized (this) {
				if (txManager == null) {
					setTransactionManager(createTransactionManager());
				}
			}
		}
		return txManager;
	}

	/**
	 * Creates a new transaction manager.
	 * @return a new transaction manager
	 */
	protected TransactionManager createTransactionManager() {
		try {
			return OsgiModelServiceImpl.getModelService().getTransactionManager();
		} catch (Throwable t) {
			throw new RuntimeException("Cannot create Transaction Manager", t);
		}
	}

	/**
	 * Sets the given TX manager to be used.
	 * This method will throw an exception if there is already a TX manager active.
	 * @param txManager the TX manager.
	 */
	@Override
	public void setTransactionManager(TransactionManager txManager) {
		if (this.txManager != null) throw new RuntimeException("Transaction Manager already set");
		this.txManager = txManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getDefaultTransactionTimeout() {
		return defaultTimeout;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultTransactionTimeout(long defaultTimeout) {
		if (defaultTimeout == 0) defaultTimeout = DEFAULT_TX_TIMEOUT;
		this.defaultTimeout = defaultTimeout;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void begin() {
		begin(0L);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void begin(long timeout) {
		TransactionContext context = txContext.get();
		if (context == null) {
			context = new TransactionContext();
			txContext.set(context);
		}

		context.begin(timeout);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		if (txContext.get() != null) {
			if (txContext.get().commit()) txContext.remove();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		if (txContext.get() != null) {
			if (txContext.get().rollback()) txContext.remove();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transaction getTransaction() throws SystemException {
		return getTransactionManager().getTransaction();
	}

	/********************* PROPERTY CHANGES **********************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDaoFactoryListener(IDaoFactoryListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeDaoFactoryListener(IDaoFactoryListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fires a factory event.
	 * @param event event to be fired
	 */
	protected void fireDaoFactoryEvent(DaoFactoryEvent event) {
		for (IDaoFactoryListener l : listeners) l.handleDaoFactoryEvent(event);
	}

	/**
	 * Registers the current context that the model changed underneath.
	 */
	public void modelChanged() {
		TransactionContext context = txContext.get();
		if (context != null) context.setModelChanged(true);
	}

	/**
	 * Returns whether debugging of transaction demarcations.
	 * @return <code>true</code> when debugging shall be enabled (via SLF4J)
	 */
	public boolean isDebugTransactions() {
		return this.debugTransactions;
	}

	/**
	 * Sets debugging of transaction demarcations.
	 * @param debug <code>true</code> when debugging shall be enabled (via SLF4J)
	 */
	public void setDebugTransactions(boolean debug) {
		this.debugTransactions = debug;
		if (debugTransactions) log.info("Enabling transaction demarcation log");
	}

	/**
	 * Returns whether stacktracing is enabled with {@link #isDebugTransactions() transaction demarcation debugging} option.
	 * @return <code>true</code> when stacktrace shall be enabled (via SLF4J)
	 */
	public boolean isTraceTransactions() {
		return this.traceTransactions;
	}

	/**
	 * Sets whether stacktracing is enabled with {@link #isDebugTransactions() transaction demarcation debugging} option.
	 * @param trace <code>true</code> when stacktrace shall be enabled (via SLF4J)
	 */
	public void setTraceTransactions(boolean trace) {
		this.traceTransactions = trace;
		if (debugTransactions && traceTransactions) log.info("Enabling stacktrace for transaction demarcation");
	}

	/**
	 * Keeps track of transaction creation.
	 * @author ralph
	 *
	 */
	public class TransactionContext {

		private int beginCount;
		private boolean modelChanged;
		private Logger log = LoggerFactory.getLogger(TransactionContext.class);

		public TransactionContext() {
			this.beginCount = 0;
			this.modelChanged = false;
		}

		/**
		 * Starts the transaction (or increments the internal counter).
		 * If there is already a transaction started then nothing will be performed.
		 */
		public void begin() {
			begin(0L);
		}

		/**
		 * Starts the transaction (or increments the internal counter).
		 * If there is already a transaction started then nothing will be performed.
		 * @param timeout in ms
		 */
		public void begin(long timeout) {
			try {
				boolean startTx = true;

				// Do we have an active transaction?

				Transaction tx = getTransaction();
				if (tx != null) {
					int txStatus = tx.getStatus();
					//log.debug("TX status: "+txStatus+ " count="+beginCount);
					startTx = (txStatus != Status.STATUS_ACTIVE) && (txStatus != Status.STATUS_MARKED_ROLLBACK) && (txStatus != Status.STATUS_ROLLEDBACK);
				}

				// Start the TX if required
				if (startTx) {
					int seconds = (int)(timeout > 0 ? timeout/1000L : getDefaultTransactionTimeout()/1000L); 
					getTransactionManager().setTransactionTimeout(seconds);
					getTransactionManager().begin();
					beginCount = 1;
					modelChanged = false;
					if (debugTransactions) {
						log.debug("Transaction started: TX-"+Thread.currentThread().getId()+" ("+seconds+"sec timeout)");
						if (traceTransactions) CommonUtils.debugStackTrace(log);
					}
					fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_STARTED));
				} else {
					beginCount++;
					if (debugTransactions) {
						log.debug("Transaction usage increased to "+beginCount+": TX-"+Thread.currentThread().getId());
						if (traceTransactions) CommonUtils.debugStackTrace(log);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Cannot start TX:", e);
			}
		}

		/**
		 * Commits a transaction.
		 * If the last call to {@link #begin()} did not start a new TX then
		 * this method does nothing.
		 */
		public boolean commit() {
			boolean rc = false;
			try {
				//log.debug("commit: count="+beginCount+": TX-"+Thread.currentThread().getId());
				if (beginCount == 1) {
					rc = true;
					boolean doRollback = false;
					Transaction tx = getTransaction();
					if (tx != null) {
						int txStatus = tx.getStatus();
						doRollback = (txStatus == Status.STATUS_MARKED_ROLLBACK) || (txStatus == Status.STATUS_ROLLEDBACK);
					}

					if (!doRollback) {
						if (isModelChanged()) fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.MODEL_CHANGED));
						fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_COMMITTING));
						getTransaction().commit();
						if (debugTransactions) {
							log.debug("Transaction committed: TX-"+Thread.currentThread().getId());
							if (traceTransactions) CommonUtils.debugStackTrace(log);
						}
						fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_COMMITTED));
					} else {
						fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_ROLLING_BACK));
						rollback();
						if (debugTransactions) {
							log.debug("Transaction rolled back: TX-"+Thread.currentThread().getId());
							if (traceTransactions) CommonUtils.debugStackTrace(log);
						}
						fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_ROLLED_BACK));
						beginCount++; // Has been decreased in rollback call
					}

				} else {
					if (debugTransactions) {
						log.debug("Transaction usage decreased to "+(beginCount-1)+": TX-"+Thread.currentThread().getId());
						if (traceTransactions) CommonUtils.debugStackTrace(log);
					}

				}
				beginCount--;
			} catch (Exception e) {
				throw new RuntimeException("Cannot commit transaction: ", e);
			}
			if (beginCount < 0) {
				throw new RuntimeException("No active transaction found");
			}
			return rc;
		}

		/**
		 * Rolls back a transaction.
		 * If the last call to {@link #begin()} did not start a new TX then
		 * this method will just mark the transaction for rollback only.
		 */
		public boolean rollback() {
			boolean rc = false;
			try {
				//log.debug("rollback: count="+beginCount+": TX-"+Thread.currentThread().getId());
				beginCount--;
				if (beginCount == 0) {
					fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_ROLLING_BACK));
					rc = true;
					Transaction tx = getTransaction();
					if (tx != null) {
						tx.rollback();
					} else {
						log.warn("No transaction found for rollback");
					}
					if (debugTransactions) {
						log.debug("Transaction rolled back: TX-"+Thread.currentThread().getId());
						if (traceTransactions) CommonUtils.debugStackTrace(log);
					}
					fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_ROLLED_BACK));
				} else if (beginCount > 0) {
					getTransaction().setRollbackOnly();
					if (debugTransactions) {
						log.debug("Transaction marked as roll-back only: TX-"+Thread.currentThread().getId());
						if (traceTransactions) CommonUtils.debugStackTrace(log);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Cannot rollback transaction: ", e);
			}
			if (beginCount < 0) {
				throw new RuntimeException("No active transaction found");
			}
			return rc;
		}

		/**
		 * Returns the modelChanged.
		 * @return the modelChanged
		 */
		public boolean isModelChanged() {
			return modelChanged;
		}

		/**
		 * Sets the modelChanged.
		 * @param modelChanged the modelChanged to set
		 */
		public void setModelChanged(boolean modelChanged) {
			this.modelChanged = modelChanged;
		}	
	}

	/**
	 * Listener for DAO events.
	 * Mainly just forwards events.
	 * @author ralph
	 *
	 */
	protected class MyDaoListener implements IDaoListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void handleDaoEvent(DaoEvent event) {
			modelChanged();
		}

	}


}
