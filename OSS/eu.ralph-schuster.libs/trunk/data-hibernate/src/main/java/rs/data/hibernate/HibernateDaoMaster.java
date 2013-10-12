/**
 * 
 */
package rs.data.hibernate;

import java.io.File;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import rs.data.hibernate.util.DataSourceConnectionProvider;
import rs.data.impl.AbstractDaoMaster;

/**
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
		"hibernate.dialect"
	};
	private static Logger log = LoggerFactory.getLogger(HibernateDaoMaster.class);
	private SessionFactory factory;
	
	/**
	 * Constructor.
	 */
	public HibernateDaoMaster() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {
		super.configure(cfg);
		
		// Where is the config?
		URL configFile = toURL("hbmconfig-file");
		URL dbconfigFile = toURL("dbconfig-file");
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
						String value = dbconfig.getString("property("+idx+")");
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
		factory = createSessionFactory(configFile, overriddenProperties);
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
	 * @param dbconfig
	 */
	@SuppressWarnings("unchecked")
	protected void loadDataSource(SubnodeConfiguration dbconfig) {
		try {
			Class<? extends DataSource> clazz = (Class<? extends DataSource>)Class.forName(dbconfig.getString("[@class]"));
			DataSource datasource = clazz.newInstance();
			int idx=0;
			while (true) {
				String name = dbconfig.getString("property("+idx+")[@name]");
				if (name == null) break;
				String value = dbconfig.getString("property("+idx+")");
				PropertyUtils.setProperty(datasource, name, value);
				if (name.equals("url")) log.debug("Using Database:   "+value);
				else if (name.equals("user")) log.debug("Using Login Name: "+value);
				idx++;
			}
			DataSourceConnectionProvider.setMyDatasource(datasource);
		} catch (Exception e) {
			throw new RuntimeException("Cannot prepare data source:", e);
		}
	}
	/**
	 * Returns the Hibernate session. This method will start a new transaction
	 * if required.
	 * 
	 * @return hibernate session.
	 */
	public Session getSession() {
		Session rc = factory.getCurrentSession();
		/*
		if (rc != null) {
			// Some check on valid sessions
			if (!rc.isOpen()) {
				log.debug("Session is not open!");
				rc = null;
			}
		}
		
		if (rc == null) {
			log.debug("Creating session!");
			rc = factory.openSession();
		}
		*/
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
