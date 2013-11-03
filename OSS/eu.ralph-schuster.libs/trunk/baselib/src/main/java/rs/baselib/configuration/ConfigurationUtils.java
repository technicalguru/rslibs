/**
 * 
 */
package rs.baselib.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.SubnodeConfiguration;

import rs.baselib.lang.LangUtils;

/**
 * Useful methods for configuration issues.
 * @author ralph
 *
 */
public class ConfigurationUtils {

	/**
	 * Loads an object from a configuration.
	 * The object is configured if it is an instance of {@link IConfigurable}.
	 * The class will be taken from attribute <code>[@class]</code>.
	 * @param config the configuration to apply
	 * @param configure whether the object shall be configured (if it is a {@link IConfigurable}).
	 * @return the object
	 */
	public static Object load(Configuration config, boolean configure) {
		try {
			String className = config.getString("[@class]");
			return load(className, config, configure);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException)e;
			}
			throw new RuntimeException("Cannot load class from configuration", e);
		}
	}

	/**
	 * Loads an object from a configuration.
	 * The object is configured if it is an instance of {@link IConfigurable}.
	 * @param className the name of class to be instantiated
	 * @param config the configuration to apply
	 * @param configure whether the object shall be configured (if it is a {@link IConfigurable}).
	 * @return the object
	 */
	public static Object load(String className, Configuration config, boolean configure) {
		try {
			Class<?> clazz = LangUtils.forName(className);
			return load(clazz, config, configure);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot load class from configuration", e);
		}
	}

	/**
	 * Loads an object from a configuration.
	 * The object is configured if it is an instance of {@link IConfigurable}.
	 * @param clazz the class to be instantiated
	 * @param config the configuration to apply
	 * @param configure whether the object shall be configured (if it is a {@link IConfigurable}).
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T load(Class<T> clazz, Configuration config, boolean configure) {
		try {
			Object rc = clazz.newInstance();
			if (configure && (rc instanceof IConfigurable)) {
				configure((IConfigurable)rc, config);
			}
			return (T)rc;
		} catch (Exception e) {
			throw new RuntimeException("Cannot load class from configuration", e);
		}
	}
	
	/**
	 * Configure the object accordingly.
	 * @param configurable the object to be configured
	 * @param config the configuration to apply
	 * @throws ConfigurationException when a problem occurs
	 */
	public static void configure(IConfigurable configurable, Configuration config) throws ConfigurationException {
		configurable.configure(config);
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
