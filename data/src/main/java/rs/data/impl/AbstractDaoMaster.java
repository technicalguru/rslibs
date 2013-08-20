/**
 * 
 */
package rs.data.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

import rs.baselib.configuration.IConfigurable;
import rs.baselib.io.FileFinder;
import rs.data.api.IDaoFactory;
import rs.data.api.IDaoMaster;
import rs.data.util.IUrlTransformer;

/**
 * Abstract implementation for DAO masters.
 * @author ralph
 *
 */
public abstract class AbstractDaoMaster implements IDaoMaster, IConfigurable {

	private IDaoFactory factory;
	private Properties properties = new Properties();
	
	/**
	 * Constructor.
	 */
	public AbstractDaoMaster() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoFactory getFactory() {
		return factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFactory(IDaoFactory factory) {
		this.factory = factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {
		try {
			properties = new Properties();
			int idx = 0;
			while (idx >= 0) {
				String key = cfg.getString("property("+idx+")[@name]");
				if (key == null) break;
				String value = cfg.getString("property("+idx+")");
				properties.setProperty(key, value);
				idx++;
			}
		} catch (Exception e) {
			// No such key
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Returns the property as a URL
	 * @param key key
	 * @return URL
	 * @throws MalformedURLException when the property was not a URL
	 */
	public URL getPropertyUrl(String key) throws MalformedURLException {
		String value = getProperty(key);
		if (value == null) return null;
		IUrlTransformer transformer = getFactory().getUrlTransformer();
		if (transformer != null) return transformer.toURL(value);
		return FileFinder.find(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	/**
	 * Returns all keys of the properties.
	 * @return key iterator
	 */
	public Iterator<Object> getPropertyKeys() {
		return properties.keySet().iterator();
	}
}
