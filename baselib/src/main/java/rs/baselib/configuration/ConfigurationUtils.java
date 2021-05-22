/*
 * This file is part of RS Library (Base Library).
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
package rs.baselib.configuration;

import java.io.File;
import java.net.URL;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.XMLBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;

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
		String className = config.getString("[@class]");
		return load(className, config, configure);
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
	 * @param <T> the type of the object to be instantiated
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T load(Class<T> clazz, Configuration config, boolean configure) {
		try {
			Object rc = clazz.getConstructor().newInstance();
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
				SubnodeConfiguration cfg = (SubnodeConfiguration) config.configurationAt("param("+index+")");
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

	/**
	 * Returns the config parameter with given value for attribute name.
	 * @param config config
	 * @param name name of param
	 * @return value of param
	 */
	public static String getParam(HierarchicalConfiguration<ImmutableNode> config, String name) {
		if (config == null) return null;
		
		int index = 0;
		while (true) {
			try {
				HierarchicalConfiguration<ImmutableNode> cfg = config.configurationAt("param("+index+")");
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

	/**
	 * Creates the basic Properties Builder parameters
	 * @param encoding - loading character encoding
	 * @return basic builder params
	 */
	public static PropertiesBuilderParameters createPropertiesBuilderParams(String encoding) {
		Parameters params = new Parameters();
		return params.properties()
		    .setThrowExceptionOnMissing(false)
		    .setEncoding(encoding);
	}
		
	/**
	 * Creates the Properties configuration object based on the given parameters.
	 * @param params - the Properties builder parameters
	 * @return the Properties configuration 
	 * @throws ConfigurationException when an error occurs
	 */
	public static PropertiesConfiguration getPropertiesConfiguration(PropertiesBuilderParameters params) throws ConfigurationException {
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class).configure(params);			
		return builder.getConfiguration();
	}
	
	/**
	 * Create a Properties configuration object with UTF-8.
	 * @param file - file to load from
	 * @return the configuration object
	 * @throws ConfigurationException when an error occurs
	 */
	public static PropertiesConfiguration getPropertiesConfiguration(File file) throws ConfigurationException {
		return getPropertiesConfiguration(file, "UTF-8");
	}
	
	/**
	 * Create a Properties configuration object.
	 * @param file - file to load from
	 * @param encoding - encoding, e.g. UTF-8
	 * @return the configuration object
	 * @throws ConfigurationException when an error occurs
	 */
	public static PropertiesConfiguration getPropertiesConfiguration(File file, String encoding) throws ConfigurationException {
		return getPropertiesConfiguration(createPropertiesBuilderParams(encoding).setFile(file));
	}

	/**
	 * Create a Properties configuration object with UTF-8.
	 * @param url - URL to load from
	 * @return the configuration object
	 * @throws ConfigurationException when an error occurs
	 */
	public static PropertiesConfiguration getPropertiesConfiguration(URL url) throws ConfigurationException {
		return getPropertiesConfiguration(url, "UTF-8");
	}
	
	/**
	 * Create a Properties configuration object.
	 * @param url - URL to load from
	 * @param encoding - encoding, e.g. UTF-8
	 * @return the configuration object
	 * @throws ConfigurationException when an error occurs
	 */
	public static PropertiesConfiguration getPropertiesConfiguration(URL url, String encoding) throws ConfigurationException {
		return getPropertiesConfiguration(createPropertiesBuilderParams(encoding).setURL(url));
	}

	/**
	 * Creates the basic XML Builder parameters
	 * @param encoding - loading character encoding
	 * @return basic builder params
	 */
	public static XMLBuilderParameters createXMLBuilderParams(String encoding) {
		Parameters params = new Parameters();
		return params.xml()
		    .setThrowExceptionOnMissing(false)
		    .setValidating(false)
		    .setEncoding(encoding);
		    //.setExpressionEngine(new XPathExpressionEngine());
	}
	
	/**
	 * Creates the XML configuration object based on the given parameters.
	 * @param params - the XML builder parameters
	 * @return the XML configuration 
	 * @throws ConfigurationException when an error occurs
	 */
	public static XMLConfiguration getXmlConfiguration(XMLBuilderParameters params) throws ConfigurationException {
		FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class).configure(params);			
		return builder.getConfiguration();
	}
	
	/**
	 * Create a XML configuration object with UTF-8.
	 * @param file - file to load from
	 * @return the configuration object
	 * @throws ConfigurationException when an error occurs
	 */
	public static XMLConfiguration getXmlConfiguration(File file) throws ConfigurationException {
		return getXmlConfiguration(file, "UTF-8");
	}
	
	/**
	 * Create a XML configuration object.
	 * @param file - file to load from
	 * @param encoding - encoding, e.g. UTF-8
	 * @return the configuration object
	 * @throws ConfigurationException when an error occurs
	 */
	public static XMLConfiguration getXmlConfiguration(File file, String encoding) throws ConfigurationException {
		return getXmlConfiguration(createXMLBuilderParams(encoding).setFile(file));
	}

	/**
	 * Create a XML configuration object with UTF-8.
	 * @param configUrl - URL to load from
	 * @return the configuration object
	 * @throws ConfigurationException when an error occurs
	 */
	public static XMLConfiguration getXmlConfiguration(URL configUrl) throws ConfigurationException {
		return getXmlConfiguration(configUrl, "UTF-8");
	}
	
	/**
	 * Create a XML configuration object.
	 * @param configUrl - URL to load from
	 * @param encoding - encoding, e.g. UTF-8
	 * @return the configuration object
	 * @throws ConfigurationException when an error occurs
	 */
	public static XMLConfiguration getXmlConfiguration(URL configUrl, String encoding) throws ConfigurationException {
		return getXmlConfiguration(createXMLBuilderParams(encoding).setURL(configUrl));
	}
}
