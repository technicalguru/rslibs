/**
 * 
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
	 * Returns the factory with given name.
	 * @param name name of factory
	 * @return factory
	 */
	public IDaoFactory getFactory(String name);
	
	/**
	 * Registers a new factory.
	 * @param name name of factory
	 * @param factory the factory to register
	 */
	public void registerFactory(String name, IDaoFactory factory);
	
}
