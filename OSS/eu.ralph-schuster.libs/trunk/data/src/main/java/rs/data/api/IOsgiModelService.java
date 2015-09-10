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
package rs.data.api;

import javax.transaction.TransactionManager;

import org.apache.commons.configuration.HierarchicalConfiguration;


/**
 * OSGI model service.
 * @author ralph
 *
 */
public interface IOsgiModelService {

	/** Default factory name ("default") */
	public static final String DEFAULT_NAME = "default";
	
	/**
	 * Sets the configuration with all {@link IDaoFactory} settings.
	 * @param config the config to bet set
	 */
	public void setConfiguration(HierarchicalConfiguration config);
	
	/**
	 * Returns the configuration with all {@link IDaoFactory} settings.
	 * @return the main configuration
	 */
	public HierarchicalConfiguration getConfiguration();

	/**
	 * Returns the {@link TransactionManager}.
	 * @return the {@link TransactionManager}
	 */
	public TransactionManager getTransactionManager();

	/**
	 * Sets the {@link TransactionManager}.
	 * @param txManager the {@link TransactionManager} to set
	 */
	public void setTransactionManager(TransactionManager txManager);

	/**
	 * Returns the default factory.
	 * @see #DEFAULT_NAME
	 * @return factory
	 */
	public IDaoFactory getFactory();
	
	/**
	 * Returns the factory with given name.
	 * @param name name of factory
	 * @return factory
	 */
	public IDaoFactory getFactory(String name);
	
	/**
	 * Returns the factory of the given type.
	 * @param clazz clazz of factory to be returned
	 * @return the factory
	 */
	public <T extends IDaoFactory> T getFactory(Class<T> clazz);
	
	/**
	 * Registers a new factory.
	 * @param name name of factory
	 * @param factory the factory to register
	 */
	public void registerFactory(String name, IDaoFactory factory);
	
	/**
	 * Shutdown all data factories.
	 * @return true whenn all factories could be shut down.
	 */
	public boolean stop();
	
	/**
	 * Returns whether the model service has started.
	 * @return {@code true} when service is up and running
	 * @since 1.3.0
	 */
	public boolean isRunning();
}
