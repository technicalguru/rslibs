/**
 * 
 */
package rs.data.impl;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
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
import rs.data.api.IDaoFactory;
import rs.data.api.IDaoMaster;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.event.DaoEvent;
import rs.data.event.DaoFactoryEvent;
import rs.data.event.DaoFactoryEvent.Type;
import rs.data.event.IDaoFactoryListener;
import rs.data.event.IDaoListener;
import rs.data.impl.dao.AbstractDAO;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.IUrlTransformer;

/**
 * The basic implementation of a DAO factory.
 * @author ralph
 *
 */
public abstract class AbstractDaoFactory implements IDaoFactory, IConfigurable {

	// TX Management	
	private Logger log = LoggerFactory.getLogger(getClass());

	private ThreadLocal<TransactionContext> txContext = new ThreadLocal<TransactionContext>();
	private Set<IDaoFactoryListener> listeners = new HashSet<IDaoFactoryListener>();
	private IDaoListener daoListener = new MyDaoListener();
	private Map<String, Object> properties = new HashMap<String, Object>();
	private Map<String, String> params = new HashMap<String, String>();
	private TransactionManager txManager;
	private IUrlTransformer urlTransformer;
	private Map<String, IDaoMaster> daoMasters = new HashMap<String, IDaoMaster>();
	private Map<String,IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>>> daos =
			new HashMap<String, IGeneralDAO<? extends Serializable,? extends IGeneralBO<? extends Serializable>>>();

	/**
	 * Constructor.
	 */
	public AbstractDaoFactory() {
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
					getLog().error("Cannit load DaoMaster: ", e);
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
			Class<?> clazz = Class.forName(className);
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

			// Add the factory as a listener
			rc.addDaoListener(daoListener);

			// Add the DAO to our list
			registerDao(rc);
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
		this.daos.put(dao.getClass().getName(), dao);
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
			setTransactionManager(createTransactionManager());
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
	public void begin() {
		TransactionContext context = txContext.get();
		if (context == null) {
			context = new TransactionContext();
			txContext.set(context);
		}

		context.begin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		txContext.get().commit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		txContext.get().rollback();
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
	 * Keeps track of transaction creation.
	 * @author ralph
	 *
	 */
	protected class TransactionContext {

		private int beginCount;
		private boolean modelChanged;

		public TransactionContext() {
			this.beginCount = 0;
			this.modelChanged = false;
		}

		/**
		 * Starts the transaction (or increments the internal counter).
		 * If there is already a transaction started then nothing will be performed.
		 */
		public void begin() {
			try {
				boolean startTx = true;

				// Do we have an active transaction?

				Transaction tx = getTransaction();
				if (tx != null) startTx = (tx.getStatus() != Status.STATUS_ACTIVE);

				// Start the TX if required
				if (startTx) {
					getTransactionManager().begin();
					beginCount = 1;
					modelChanged = false;
					fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_STARTED));
				} else {
					beginCount++;
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
		public void commit() {
			try {
				if (beginCount == 1) {
					if (isModelChanged()) fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.MODEL_CHANGED));
					fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_COMMITTING));
					getTransaction().commit();
					fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_COMMITTED));

				}
				beginCount--;
			} catch (Exception e) {
				throw new RuntimeException("Cannot commit transaction: ", e);
			}
			if (beginCount < 0) {
				throw new RuntimeException("No active transaction found");
			}
		}

		/**
		 * Rolls back a transaction.
		 * If the last call to {@link #begin()} did not start a new TX then
		 * this method will just mark the transaction for rollback only.
		 */
		public void rollback() {
			try {
				beginCount--;
				if (beginCount == 0) {
					fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_ROLLING_BACK));
					getTransaction().rollback();
					fireDaoFactoryEvent(new DaoFactoryEvent(AbstractDaoFactory.this, Type.TRANSACTION_ROLLED_BACK));
				} else if (beginCount > 0) {
					getTransaction().setRollbackOnly();
				}
			} catch (Exception e) {
				throw new RuntimeException("Cannot rollback transaction: ", e);
			}
			if (beginCount < 0) {
				throw new RuntimeException("No active transaction found");
			}
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
