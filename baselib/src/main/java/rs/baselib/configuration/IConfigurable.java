/**
 * 
 */
package rs.baselib.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Marks an object as self-configurable.
 * The object can be called with a configuration and it will
 * configure itself from it.
 * @author ralph
 *
 */
public interface IConfigurable {

	/**
	 * Called before configuration starts.
	 */
	public void beforeConfiguration();
	
	/**
	 * Configure using the given configuration
	 * @param cfg configuration
	 * @throws ConfigurationException if a problem occurs
	 */
	public void configure(Configuration cfg) throws ConfigurationException;
	
	/**
	 * Called after configuration finished.
	 */
	public void afterConfiguration();

}
