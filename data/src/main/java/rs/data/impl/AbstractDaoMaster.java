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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.LoggerFactory;

import rs.baselib.configuration.IConfigurable;
import rs.baselib.io.FileFinder;
import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;
import rs.baselib.util.IUriProvider;
import rs.baselib.util.IUrlProvider;
import rs.baselib.util.UriProviderWrapper;
import rs.baselib.util.UrlProviderWrapper;
import rs.data.api.IDaoFactory;
import rs.data.api.IDaoMaster;
import rs.baselib.util.IUrlTransformer;

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
	public void shutdown() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProperty(String key) {
		return CommonUtils.replaceVariables(properties.getProperty(key));
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
		IUrlTransformer transformer = null;
		if (value.startsWith("class:")) {
			try {
				Object obj = LangUtils.newInstance(value.substring(6));
				if (obj instanceof IUrlTransformer) {
					transformer = (IUrlTransformer)obj;
				} else if (obj instanceof IUrlProvider) {
					transformer = new UrlProviderWrapper((IUrlProvider)obj);
				} else if (obj instanceof IUriProvider) {
					transformer = new UriProviderWrapper((IUriProvider)obj);
				}
			} catch (Exception e) {
				LoggerFactory.getLogger(getClass()).error("Cannot create UrlTransformer from "+value, e);
			}
		} else if (value.toLowerCase().startsWith("env:")) {
			value = System.getenv(value.substring(4));
		}
		if (transformer == null) getFactory().getUrlTransformer();
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
