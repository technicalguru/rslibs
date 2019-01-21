/*
 * This file is part of RS Library (Data Hibernate Library).
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
package rs.data.hibernate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import rs.baselib.lang.LangUtils;
import rs.baselib.lang.ReflectionUtils;
import rs.baselib.util.CommonUtils;
import rs.data.hibernate.util.DataSourceConnectionProvider;
import rs.data.impl.AbstractDaoMaster;

/**
 * The DAO Master holding information about the DB connection and Hibernate instance.
 * @author ralph
 *
 */
public class HibernateDaoMaster extends AbstractDaoMaster {

	public static final String DEFAULT_CONFIG_FILE = "config/hbm/hibernate.cfg.xml";
	public static final String FACTORY_KEY = "hibernateDaoMaster";
	
	private static final String DB_CONFIG_KEYS[] = new String[] {
		"hibernate.connection.driver_class",
		"hibernate.connection.url",
		"hibernate.connection.username",
		"hibernate.connection.password",
		"hibernate.dialect",
		"hibernate.hbm2ddl.auto"
	};
	private static Logger log = LoggerFactory.getLogger(HibernateDaoMaster.class);
	private SessionFactory sessionFactory;
	private DataSource datasource;
	
	/**
	 * Constructor.
	 */
	public HibernateDaoMaster() {
	}
	
	/**
	 * {@inheritDoc}
	 * <p>{@code hbmconfig-file} and {@code dbconfig-file} properties will support the {@code env:} syntax for
	 * evaluating environment values, and {@code class:} for programmatic configuration.</p>
	 * <p>db config file supports the {@code $ENV{name}} syntax in its properties.
	 */
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {
		super.configure(cfg);
		
		// Where is the config?
		URL configFile = toURL("hbmconfig-file");
		URL dbconfigFile = toURL("dbconfig-file");
		if (configFile == null) throw new ConfigurationException("hbmconfig-file must not be NULL");
		if (dbconfigFile == null) throw new ConfigurationException("dbconfig-file must not be NULL");
		log.info("Database Configuration: "+dbconfigFile.toString());

		// Load all properties for hibernate now
		Properties overriddenProperties = new Properties();
		Iterator<Object> keys = getPropertyKeys();
		while (keys.hasNext()) {
			String key = (String)keys.next();
			if (key.startsWith("hibernate.")) overriddenProperties.setProperty(key, getProperty(key));
		}

		// Overload the properties from dbconfig
		XMLConfiguration dbconfig = new XMLConfiguration(dbconfigFile);
		for (String key : DB_CONFIG_KEYS) {
			try {
				int idx = 0;
				while (idx >= 0) {
					String k = dbconfig.getString("property("+idx+")[@name]");
					if (k == null) break;
					if (k.equals(key)) {
						String value = CommonUtils.replaceVariables(dbconfig.getString("property("+idx+")"));
						overriddenProperties.setProperty(key, value);
						break;
					} else idx++;
				}
			} catch (Exception e) {
				// No such key
			}
		}

		// Load the datasource
		loadDataSource(dbconfig.configurationAt("datasource(0)"));

		// Create the session factory now
		setSessionFactory(createSessionFactory(configFile, overriddenProperties));
	}

	/**
	 * Tries to get the URL from the factory.
	 * If the URL cannot be constructed by the factory then a File URL will be assumed.
	 * @param key parameter key in factory
	 * @return URL
	 * @throws ConfigurationException if the transformation fails
	 */
	protected URL toURL(String key) throws ConfigurationException {
		try {
			return getPropertyUrl(key);
		} catch (MalformedURLException e) {
			try {
				return new File(getProperty(key)).toURI().toURL();
			} catch (MalformedURLException e2) {
				throw new ConfigurationException("Cannot create a URL from: "+getProperty(key), e2);
			}
		}
	}
	
	/**
	 * Load the data source configuration and inject it for hibernate.
	 * @param dbconfig - the subnode configuration for the db configuration
	 */
	@SuppressWarnings("unchecked")
	protected void loadDataSource(SubnodeConfiguration dbconfig) {
		try {
			Class<? extends DataSource> clazz = (Class<? extends DataSource>)LangUtils.forName(dbconfig.getString("[@class]"));
			datasource = clazz.getConstructor().newInstance();
			setProperty("datasource.class", datasource.getClass().getName());
			int idx=0;
			while (true) {
				String name = dbconfig.getString("property("+idx+")[@name]");
				if (name == null) break;
				Object value = CommonUtils.replaceVariables(dbconfig.getString("property("+idx+")"));
				if ((value == null) && (name.equals("user") || name.equals("password"))) value = "";
				if (value != null) {
					if ("true".equalsIgnoreCase(value.toString())) {
						value = Boolean.TRUE;
					} else if ("false".equalsIgnoreCase(value.toString())) {
						value = Boolean.FALSE;
					} else if (LangUtils.isNumber(value.toString())) {
						value = Integer.parseInt(value.toString());
					} 
					PropertyUtils.setProperty(datasource, name, value);
					setProperty("datasource."+name, ""+value);
				}
				if (name.toLowerCase().contains("url")) log.debug("Using Database:   "+value);
				else if (name.equals("user")) log.debug("Using Login Name: "+value);
				idx++;
			}
			DataSourceConnectionProvider.setMyDatasource(datasource);
		} catch (Exception e) {
			throw new RuntimeException("Cannot prepare data source:", e);
		}
	}
	
	/**
	 * Returns the datasource.
	 * @return the datasource
	 */
	public DataSource getDatasource() {
		return datasource;
	}

	/**
	 * Returns the sessionFactory.
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Sets the sessionFactory.
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		if (this.sessionFactory != null) {
			this.sessionFactory.close();
		}
		this.sessionFactory = sessionFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() {
		super.shutdown();
		if (this.sessionFactory != null) {
			if (this.sessionFactory instanceof SessionFactoryImpl) {
				SessionFactoryImpl sf = (SessionFactoryImpl)sessionFactory;
				ConnectionProvider conn = sf.getConnectionProvider();
				if(ReflectionUtils.isInstanceOf(conn, "org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider")) {
					try {
						conn.getClass().getMethod("close").invoke(conn);
					} catch (InvocationTargetException e) {
						log.error("Cannot close C3P0ConnectionProvider: ", e);
					} catch (Throwable t) {
						// Ignore, there is no such class
					}
				}
			}
			this.sessionFactory.close();
		}
		this.sessionFactory = null;
		DataSource ds = getDatasource();
		if (ReflectionUtils.isInstanceOf(ds, "com.mchange.v2.c3p0.PooledDataSource")) try {
			ds.getClass().getMethod("hardReset").invoke(ds);
		} catch (Exception e) {
			// Do not log
		}

	}

	/**
	 * Returns the Hibernate session. This method will start a new transaction
	 * if required.
	 * 
	 * @return hibernate session.
	 */
	public Session getSession() {
		Session rc = getSessionFactory().getCurrentSession();
		return rc;
	}

	/**
	 * Returns a new session factory from default Hibernate config file.
	 * @return session factory
	 */
	public static SessionFactory createSessionFactory() {
		return createSessionFactory(DEFAULT_CONFIG_FILE, null);
	}
	
	/**
	 * Returns a new session factory from given Hibernate config file.
	 * @param configFile - Hibernate configuration file
	 * @return session factory
	 */
	public static SessionFactory createSessionFactory(String configFile) {
		return createSessionFactory(configFile, null);
	}
	
	/**
	 * Returns a new session factory from given Hibernate config file.
	 * @param configFile - Hibernate configuration file
	 * @return session factory
	 */
	public static SessionFactory createSessionFactory(URL configFile) {
		return createSessionFactory(configFile, null);
	}
	
	/**
	 * Returns a new session factory from given Hibernate config file.
	 * @param configFile - Hibernate configuration file
	 * @return session factory
	 */
	public static SessionFactory createSessionFactory(File configFile) {
		return createSessionFactory(configFile, null);
	}
	
	/**
	 * Returns a new session factory from default Hibernate config file.
	 * @param overriddenProperties - Properties that must be overridden on Hibernate's config file
	 * @return session factory
	 */
	public static SessionFactory createSessionFactory(Properties overriddenProperties) {
		return createSessionFactory((String)null, overriddenProperties);
	}
	
	/**
	 * Returns a new session factory.
	 * @param configFile - Hibernate configuration file
	 * @param overriddenProperties - Properties that must be overridden on Hibernate's config file
	 * @return session factory
	 */
	public static SessionFactory createSessionFactory(String configFile, Properties overriddenProperties) {
		return createSessionFactory(createConfiguration(configFile, overriddenProperties));
	}
	
	/**
	 * Returns a new session factory.
	 * @param configFile - Hibernate configuration file
	 * @param overriddenProperties - Properties that must be overridden on Hibernate's config file
	 * @return session factory
	 */
	public static SessionFactory createSessionFactory(URL configFile, Properties overriddenProperties) {
		return createSessionFactory(createConfiguration(configFile, overriddenProperties));
	}
	
	/**
	 * Returns a new session factory.
	 * @param configFile - Hibernate configuration file
	 * @param overriddenProperties - Properties that must be overridden on Hibernate's config file
	 * @return session factory
	 */
	public static SessionFactory createSessionFactory(File configFile, Properties overriddenProperties) {
		return createSessionFactory(createConfiguration(configFile, overriddenProperties));
	}
	
	/**
	 * Returns a new session factory.
	 * @param config - Hibernate configuration
	 * @return session factory
	 */
	@SuppressWarnings("deprecation")
	public static SessionFactory createSessionFactory(org.hibernate.cfg.Configuration config) {
		return config.buildSessionFactory();
	}
	
	/**
	 * Creates the Hibernate configuration.
	 * @param configFile - Hibernate configuration file
	 * @param overriddenProperties - Properties that must be overridden on Hibernate's config file
	 * @return configuration object
	 */
	public static org.hibernate.cfg.Configuration createConfiguration(String configFile, Properties overriddenProperties) {
		if (configFile == null) configFile = DEFAULT_CONFIG_FILE;
		URL url = FileFinder.find(configFile);
		if (url == null) {
			if (log.isDebugEnabled()) log.debug("Config File: "+configFile);
			return createConfiguration(new File(configFile), overriddenProperties);
		}
		return createConfiguration(url, overriddenProperties);
	}
	
	/**
	 * Creates the Hibernate configuration.
	 * @param file - Hibernate configuration file
	 * @param overriddenProperties - Properties that must be overridden on Hibernate's config file
	 * @return configuration object
	 */
	public static org.hibernate.cfg.Configuration createConfiguration(File file, Properties overriddenProperties) {
		if (log.isDebugEnabled()) log.debug("Config File: "+file.getAbsolutePath());
		return adaptConfiguration(new org.hibernate.cfg.Configuration().configure(file), overriddenProperties);
	}
	
	/**
	 * Creates the Hibernate configuration.
	 * @param url - URL of Hibernate configuration file
	 * @param overriddenProperties - Properties that must be overridden on Hibernate's config file
	 * @return configuration object
	 */
	public static org.hibernate.cfg.Configuration createConfiguration(URL url, Properties overriddenProperties) {
		if (log.isDebugEnabled()) log.debug("Config URL: "+url.toString());
		return adaptConfiguration(new org.hibernate.cfg.Configuration().configure(url), overriddenProperties);
	}
	
	/**
	 * Adapts the configuration
	 * @param config configuration to adapt
	 * @param overriddenProperties adapted properties
	 * @return the configuration object for method chaining
	 */
	private static org.hibernate.cfg.Configuration adaptConfiguration(org.hibernate.cfg.Configuration config, Properties overriddenProperties) {
		if (overriddenProperties != null) {
			Iterator<Object> i = overriddenProperties.keySet().iterator();
			while (i.hasNext()) {
				String key = (String)i.next();
				String value = overriddenProperties.getProperty(key);
				config = config.setProperty(key, value);
				if (log.isDebugEnabled()) {
					if (key.equals("hibernate.connection.password")) value = "<hidden>";
					log.debug("Customized: "+key+"="+value);
				}
			}
		}
		return config;
	}
}
