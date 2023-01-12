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
package rs.baselib.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;

/**
 * Abstract implementation for JdbcConnectionProviders.
 * The implementation already takes care of most purposes. A common usage scenario is to
 * provide a URL template as follows:
 * <pre>
 *    jdbc:mysql://{0}:{1}/{2}
 * </pre>
 * The URL template is a format as for {@link MessageFormat} with following arguments:
 * <ul>
 * <li>{0} - DB Host</li>
 * <li>{1} - DB Port</li>
 * <li>{2} - DB Name</li>
 * <li>{3} - DB Login</li>
 * <li>{4} - DB Password</li>
 * <li>{5}...{n} - Additional arguments (addOnArgs)</li>
 * </ul>
 * <p>
 * Developers can override {@link #getDriverUrl(String, String, String, String, String, String...)} to accommodate
 * for more sophisticated URL templates.
 * </p>
 * <p>
 * Clients and developers can control whether each of the arguments is enabled and the default value of an argument.
 * The default implementation will automatically use the default value for an argument when the argument is not
 * enabled or is empty.
 * </p>
 * @author ralph
 *
 */
public abstract class AbstractJdbcConnectionProvider implements IJdbcConnectionProvider2, IDataSourceProvider, IHibernateDialectProvider {

	/** driver class name */
	private String dbDriverClassName;
	/** URL Template for connection in {@link MessageFormat} format */
	private String urlTemplate;
	/** readable name of this driver */
	private String display;
	/** The dialect class */
	private String hibernateDialect;
	/** The Data Source name */
	private String dataSource;
	/** Whether a host customization is possible */
	private boolean hostEnabled = true;
	/** The default host when given host is empty or host customization is not allowed */
	private String defaultHost = null;
	/** Whether a host customization is possible */
	private boolean portEnabled = true;
	/** The default host when given host is empty or host customization is not allowed */
	private String defaultPort = null;
	/** Whether a host customization is possible */
	private boolean dbNameEnabled = true;
	/** The default host when given host is empty or host customization is not allowed */
	private String defaultDbName = null;
	/** Whether a host customization is possible */
	private boolean dbLoginEnabled = true;
	/** The default host when given host is empty or host customization is not allowed */
	private String defaultDbLogin = null;
	/** Whether a host customization is possible */
	private boolean dbPasswordEnabled = true;
	/** The default host when given host is empty or host customization is not allowed */
	private String defaultDbPassword = null;
	/** number of additional arguments */
	private int maxAdditionalArgumentIndex = -1;
	/** Name or label of additional argument */
	private Map<Integer,String> additionalArgumentNames = new HashMap<Integer,String>();
	/** Set of enabled additional arguments */
	private Set<Integer> enabledAdditionalArguments = new HashSet<Integer>();
	/** Map of default additional arguments */
	private Map<Integer,String> defaultAdditionalArguments = new HashMap<Integer,String>();
	/** Driver parameters */
	private Map<String,String> driverParameters = new HashMap<String, String>();
	
	/**
	 * Constructor.
	 * Descendants must override {@link #getDbDriverClassName()} and {@link #getDriverUrl(String, String, String, String, String, String...)}
	 * for a class.
	 * @param display a name to be displayed to users 
	 */
	public AbstractJdbcConnectionProvider(String display) {
		this(display, null, null);
	}

	/**
	 * Constructor.
	 * Descendants must override {@link #getDbDriverClassName()} for a working class.
	 * @param display a name to be displayed to users 
	 * @param urlTemplate the URL template (see class description for details)
	 */
	public AbstractJdbcConnectionProvider(String display, String urlTemplate) {
		this(display, null, urlTemplate);
	}

	/**
	 * Constructor.
	 * @param display a name to be displayed to users 
	 * @param dbDriverClassName the name of the DB driver (can be <code>null</code> if you override {@link #getDbDriverClassName()})
	 * @param urlTemplate the URL template (see class description for details)
	 */
	public AbstractJdbcConnectionProvider(String display, String dbDriverClassName, String urlTemplate) {
		this.display = display;
		this.dbDriverClassName = dbDriverClassName;
		this.urlTemplate = urlTemplate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection getConnection(String host, String port, String dbName, String dbLogin, String dbPassword, String... addOnArgs) throws SQLException {
		String driverName = getDbDriverClassName();
		if (driverName == null) throw new SQLException("No JDBC driver specified");
		
		try {
			LangUtils.forName(driverName);
		} catch (ClassNotFoundException e) {
			throw new SQLException("JDBC driver \""+driverName+"\" not found", e);
		}
		
		// Construct the URL
		String url = null;
		try {
			url = getDriverUrl(host, port, dbName, dbLogin, dbPassword, addOnArgs);
		} catch (Throwable t) {
			throw new SQLException("Cannot construct driver URL: "+t.getLocalizedMessage(), t);
		}
		if (url == null) throw new SQLException("Cannot construct driver URL");
		
		// Connect
		return DriverManager.getConnection(url, dbLogin, dbPassword);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDbDriverClassName() {
		return dbDriverClassName;
	}

	/**
	 * Returns the {@link #urlTemplate}.
	 * @return the urlTemplate
	 */
	public String getUrlTemplate() {
		return urlTemplate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDriverUrl(String host, String port, String dbName, String dbLogin, String dbPassword, String... addOnArgs) {
		if (urlTemplate == null) return null;
		if (CommonUtils.isEmpty(port, true)) port = getDefaultPort();
		// Construct the arguments array
		Object args[] = new Object[maxAdditionalArgumentIndex+6];
		args[0] = getHost(host);
		args[1] = getPort(port);
		args[2] = getDbName(dbName);
		args[3] = getDbLogin(dbLogin);
		args[4] = getDbPassword(dbPassword);
		for (int i=0; i<=maxAdditionalArgumentIndex; i++) args[i+5] = getAdditionalArgument(i, i<addOnArgs.length ? addOnArgs[i] : null);
		return MessageFormat.format(getUrlTemplate(), args)+getDriverParametersString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the {@link #dataSource}.
	 * @param dataSource the dataSource to set
	 */
	protected void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHibernateDialect() {
		return hibernateDialect;
	}

	/**
	 * Sets the {@link #hibernateDialect}.
	 * @param hibernateDialect the hibernateDialect to set
	 */
	public void setHibernateDialect(String hibernateDialect) {
		this.hibernateDialect = hibernateDialect;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHost(String s) {
		if (!isHostEnabled() || CommonUtils.isEmpty(s)) return getDefaultHost();
		return s.trim();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHostEnabled() {
		return hostEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHostEnabled(boolean b) {
		this.hostEnabled = b;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHost(boolean enabled, String defaultValue) {
		setHostEnabled(enabled);
		setDefaultHost(defaultValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultHost() {
		return defaultHost;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultHost(String s) {
		this.defaultHost = s;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPort(String s) {
		if (!isPortEnabled() || CommonUtils.isEmpty(s)) return getDefaultPort();
		return s.trim();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPortEnabled() {
		return portEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPortEnabled(boolean b) {
		this.portEnabled = b;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPort(boolean enabled, String defaultValue) {
		setPortEnabled(enabled);
		setDefaultPort(defaultValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultPort() {
		return defaultPort;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultPort(String s) {
		this.defaultPort = s;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDbName(String s) {
		if (!isDbNameEnabled() || CommonUtils.isEmpty(s)) return getDefaultDbName();
		return s.trim();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDbNameEnabled() {
		return dbNameEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDbNameEnabled(boolean b) {
		this.dbNameEnabled = b;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDbName(boolean enabled, String defaultValue) {
		setDbNameEnabled(enabled);
		setDefaultDbName(defaultValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultDbName() {
		return defaultDbName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultDbName(String s) {
		this.defaultDbName = s;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDbLogin(String s) {
		if (!isDbLoginEnabled() || CommonUtils.isEmpty(s)) return getDefaultDbLogin();
		return s.trim();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDbLoginEnabled() {
		return dbLoginEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDbLoginEnabled(boolean b) {
		this.dbLoginEnabled = b;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDbLogin(boolean enabled, String defaultValue) {
		setDbLoginEnabled(enabled);
		setDefaultDbLogin(defaultValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultDbLogin() {
		return defaultDbLogin;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultDbLogin(String s) {
		this.defaultDbLogin = s;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDbPassword(String s) {
		if (!isDbPasswordEnabled() || CommonUtils.isEmpty(s)) return getDefaultDbPassword();
		return s.trim();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDbPasswordEnabled() {
		return dbPasswordEnabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDbPasswordEnabled(boolean b) {
		this.dbPasswordEnabled = b;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDbPassword(boolean enabled, String defaultValue) {
		setDbPasswordEnabled(enabled);
		setDefaultDbPassword(defaultValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultDbPassword() {
		return defaultDbPassword;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultDbPassword(String s) {
		this.defaultDbPassword = s;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAdditionalArgument(int index, String s) {
		if (!isAdditionalArgumentEnabled(index) || CommonUtils.isEmpty(s)) return getDefaultAdditionalArgument(index);
		return s.trim();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAdditionalArgumentEnabled(int index) {
		return enabledAdditionalArguments.contains(Integer.valueOf(index));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdditionalArgumentEnabled(int index, boolean b) {
		if (b) {
			enabledAdditionalArguments.add(Integer.valueOf(index));
		} else {
			enabledAdditionalArguments.remove(Integer.valueOf(index));
		}
		if (index > maxAdditionalArgumentIndex) maxAdditionalArgumentIndex = index;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdditionalArgument(int index, String name, boolean enabled, String defaultValue) {
		setAdditionalArgumentEnabled(index, enabled);
		setDefaultAdditionalArgument(index, defaultValue);
		setAdditionalArgumentName(index, name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultAdditionalArgument(int index) {
		return defaultAdditionalArguments.get(Integer.valueOf(index));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultAdditionalArgument(int index, String s) {
		defaultAdditionalArguments.put(Integer.valueOf(index), s);
		if (index > maxAdditionalArgumentIndex) maxAdditionalArgumentIndex = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAdditionalArgumentName(int index) {
		return additionalArgumentNames.get(Integer.valueOf(index));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdditionalArgumentName(int index, String s) {
		additionalArgumentNames.put(Integer.valueOf(index), s);
		if (index > maxAdditionalArgumentIndex) maxAdditionalArgumentIndex = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getAdditionalArgumentNames() {
		String rc[] = new String[maxAdditionalArgumentIndex+1];
		for (int i=0; i<rc.length; i++) {
			rc[i] = getAdditionalArgumentName(i);
		}
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAdditionalArgumentCount() {
		return maxAdditionalArgumentIndex+1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDriverParameterCount() {
		return driverParameters.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDriverParameter(String name, String value) {
		if (value != null) {
			driverParameters.put(name, value);
		} else {
			driverParameters.remove(name);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDriverParameter(String name) {
		return driverParameters.get(name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDriverParametersString() {
		if (driverParameters.isEmpty()) return "";
		StringBuilder rc = new StringBuilder();
		for (Map.Entry<String, String> entry : driverParameters.entrySet()) {
			if (rc.length() == 0) rc.append('?');
			else rc.append('&');
			rc.append(entry.getKey());
			rc.append('=');
			rc.append(entry.getValue());
		}
		return rc.toString();
	}
	
	
	/**
	 * Returns the display.
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getDisplay();
	}
}
