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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.configuration.ConfigurationUtils;
import rs.baselib.io.FileFinder;
import rs.data.JotmSupport;
import rs.data.api.IDaoFactory;
import rs.data.api.IOsgiModelService;

/**
 * Singleton service for models.
 * @author ralph
 *
 */
public class OsgiModelServiceImpl implements IOsgiModelService {

	private static Logger log = LoggerFactory.getLogger(OsgiModelServiceImpl.class);

	/** Non-OSGI environment */
	private static volatile IOsgiModelService modelService = null;

	/**
	 * Returns the model service in a non-OSGI environment.
	 * @return the model service singleton
	 */
	public static IOsgiModelService getModelService() {
		if (modelService == null) {
			synchronized (log) {
				if (modelService == null) {
					modelService = new OsgiModelServiceImpl();
				}
			}
		}
		return modelService;
	}

	/** All factories */
	private Map<String,IDaoFactory> factories = new HashMap<String,IDaoFactory>();
	/** The DAO configurations */
	private HierarchicalConfiguration daoConfig;
	/** TX Manager */
	private TransactionManager txManager;
	/** Already loaded? */
	private boolean factoriesLoaded = false;

	/**
	 * Constructor.
	 */
	public OsgiModelServiceImpl() {
		modelService = this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConfiguration(HierarchicalConfiguration config) {
		this.daoConfig = config;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public HierarchicalConfiguration getConfiguration() {
		if (this.daoConfig == null) {
			try {
				URL url = FileFinder.find(getClass(), "dao-config.xml");
				if (url == null) throw new NullPointerException("Cannot find dao-config.xml");
				daoConfig = new XMLConfiguration(url);
			} catch (Exception e) {
				throw new RuntimeException("Cannot setup default configuration", e);
			}
		}
		return this.daoConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransactionManager getTransactionManager() {
		if (txManager == null) {
			try {
				// Create JOTM TX Manager
				JotmSupport.start();
				setTransactionManager(JotmSupport.getTransactionManager());
			} catch (Exception e) {
				throw new RuntimeException("Cannot setup Transaction Manager", e);
			}			
		}
		return txManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTransactionManager(TransactionManager txManager) {
		this.txManager = txManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoFactory getFactory() {
		return getFactory((String)null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoFactory getFactory(String name) {
		if (!factoriesLoaded) loadFactories();
		if (name == null) name = DEFAULT_NAME;
		return factories.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IDaoFactory> T getFactory(Class<T> clazz) {
		if (!factoriesLoaded) loadFactories();
		for (Map.Entry<String, IDaoFactory> entry : factories.entrySet()) {
			if (clazz.isInstance(entry.getValue())) return (T)entry.getValue();
		}
		return null;
	}

	/**
	 * Loads the factories.
	 */
	protected void loadFactories() {
		if (factoriesLoaded) return;

		synchronized (factories) {
			if (factoriesLoaded) return;
			int i=0;
			try {
				HierarchicalConfiguration daoConfig = getConfiguration();
				while (true) {
					HierarchicalConfiguration subConfig = daoConfig.configurationAt("DaoFactory("+i+")");
					String s = subConfig.getString("[@name]");
					if (s == null) s = DEFAULT_NAME;

					// Create the DAO factory
					try {
						IDaoFactory factory = (IDaoFactory)ConfigurationUtils.load(subConfig, true);
						factory.setTransactionManager(txManager);
						registerFactory(s, factory);
					} catch (RuntimeException e) {
						if (ClassNotFoundException.class.equals(e.getCause())) {
							log.error("Cannot load class: "+s, e);
						}
					}
					i++;
				}
			} catch (Exception e) {
				// End of config
			}
			factoriesLoaded = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void registerFactory(String name, IDaoFactory factory) {
		if (factories.get(name) != null) {
			throw new IllegalArgumentException("Factory already registered: "+name);
		}
		factories.put(name, factory);
	}

}
