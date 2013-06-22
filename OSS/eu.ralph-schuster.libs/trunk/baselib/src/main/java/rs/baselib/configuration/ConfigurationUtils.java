/**
 * 
 */
package rs.baselib.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.SubnodeConfiguration;

/**
 * Useful methods for configuration issues.
 * @author ralph
 *
 */
public class ConfigurationUtils {

	/**
	 * Loads an object from a configuration.
	 * The object is configured if it is an instance of {@link IConfigurable}.
	 * @param config configuration
	 * @return the object
	 */
	public static Object load(Configuration config, boolean configure) {
		try {
			String className = config.getString("[@class]");
			Class<?> clazz = Class.forName(className);
			Object rc = clazz.newInstance();
			if (configure && (rc instanceof IConfigurable)) {
				configure((IConfigurable)rc, config);
			}
			return rc;
		} catch (Exception e) {
			throw new RuntimeException("Cannot load class from configuration", e);
		}
	}

	/**
	 * Configure the object accordingly.
	 * @param configurable
	 * @param config
	 * @throws ConfigurationException
	 */
	public static void configure(IConfigurable iConfigurable, Configuration config) throws ConfigurationException {
		iConfigurable.beforeConfiguration();
		iConfigurable.configure(config);
		iConfigurable.afterConfiguration();
	}
	
	/**
	 * Returns the config parameter with given value for attribute name.
	 * @param config config
	 * @param name name of param
	 * @return value of param
	 */
	public static String getParam(SubnodeConfiguration config, String name) {
		if (config == null) return null;
		
		int index = 0;
		while (true) {
			try {
				SubnodeConfiguration cfg = config.configurationAt("param("+index+")");
				if (cfg == null) return null;
				String n = cfg.getString("[@name]");
				if (name.equals(n)) return config.getString("param("+index+")");
				index++;
			} catch (Exception e) {
				break;
			}
		}
		return null;
	}

}
