/**
 * 
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
import rs.baselib.util.IDisplayProvider;

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
public abstract class AbstractJdbcConnectionProvider implements IJdbcConnectionProvider2, IDisplayProvider, IXADataSourceProvider {

	/** driver class name */
	private String dbDriverClassName;
	/** URL Template for connection in {@link MessageFormat} format */
	private String urlTemplate;
	/** readable name of this driver */
	private String display;
	/** The XA Data Source name */
	private String xaDataSource;
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
	
	/**
	 * Constructor.
	 * Descendants must override {@link #getDbDriverClassName()} and {@link #getDriverUrl(String, String, String, String, String, String...)}
	 * for a class.
	 * @param display a name to be displayed to users (see {@link IDisplayProvider})
	 */
	public AbstractJdbcConnectionProvider(String display) {
		this(display, null, null);
	}

	/**
	 * Constructor.
	 * Descendants must override {@link #getDbDriverClassName()} for a working class.
	 * @param display a name to be displayed to users (see {@link IDisplayProvider})
	 * @param urlTemplate the URL template (see class description for details)
	 */
	public AbstractJdbcConnectionProvider(String display, String urlTemplate) {
		this(display, null, urlTemplate);
	}

	/**
	 * Constructor.
	 * @param display a name to be displayed to users (see {@link IDisplayProvider})
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
			url = getDriverUrl(host, port, dbName, dbLogin, dbPassword);
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
	public String getDisplay() {
		return display;
	}

	/**
	 * Returns the {@link #dbDriverClassName}.
	 * The method shall return <code>null</code> in case of any errors.
	 * @return the dbDriverClassName
	 */
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
	 * Constructs the driver's URL from the arguments.
	 * The method shall return <code>null</code> in case of any problems or throw an exception.
	 * @param host the host of the database
	 * @param port the port of the database (some DB use names as port information)
	 * @param dbName name of database
	 * @param dbLogin database login
	 * @param dbPassword database password
	 * @param addOnArgs additional arguments for the driver URL
	 * @return the URL for the driver or <code>null</code>
	 */
	protected String getDriverUrl(String host, String port, String dbName, String dbLogin, String dbPassword, String... addOnArgs) {
		if (urlTemplate == null) return null;
		if (CommonUtils.isEmpty(port, true)) port = getDefaultPort();
		// Construct the arguments array
		Object args[] = new Object[addOnArgs.length+5];
		args[0] = getHost(host);
		args[1] = getPort(port);
		args[2] = getDbName(dbName);
		args[3] = getDbLogin(dbLogin);
		args[4] = getDbPassword(dbPassword);
		for (int i=0; i<addOnArgs.length; i++) args[i+5] = getAdditionalArgument(i, addOnArgs[i]);
		return MessageFormat.format(getUrlTemplate(), args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getXADataSource() {
		return xaDataSource;
	}

	/**
	 * Sets the {@link #xaDataSource}.
	 * @param xaDataSource the xaDataSource to set
	 */
	protected void setXaDataSource(String xaDataSource) {
		this.xaDataSource = xaDataSource;
	}
	
	/**
	 * Returns the host argument to be used for URL construction.
	 * Descendants can override this method when they have more sophisticated algorithms
	 * to determine the host argument. This implementation returns the default host
	 * when the passed string is either empty or host argument was disabled.
	 * @param s the string containing an actual host argument
	 * @return the argument to be used in URL construction
	 * @see #isHostEnabled()
	 * @see #setHostEnabled(boolean)
	 * @see #getDefaultHost()
	 * @see #setDefaultHost(String)
	 */
	protected String getHost(String s) {
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
	 * Returns the port argument to be used for URL construction.
	 * Descendants can override this method when they have more sophisticated algorithms
	 * to determine the port argument. This implementation returns the default port
	 * when the passed string is either empty or port argument was disabled.
	 * @param s the string containing an actual port argument
	 * @return the argument to be used in URL construction
	 * @see #isPortEnabled()
	 * @see #setPortEnabled(boolean)
	 * @see #getDefaultPort()
	 * @see #setDefaultPort(String)
	 */
	protected String getPort(String s) {
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
	 * Returns the DB name argument to be used for URL construction.
	 * Descendants can override this method when they have more sophisticated algorithms
	 * to determine the DB name argument. This implementation returns the default DB name
	 * when the passed string is either empty or DB name argument was disabled.
	 * @param s the string containing an actual DB name argument
	 * @return the argument to be used in URL construction
	 * @see #isDbNameEnabled()
	 * @see #setDbNameEnabled(boolean)
	 * @see #getDefaultDbName()
	 * @see #setDefaultDbName(String)
	 */
	protected String getDbName(String s) {
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
	 * Returns the DB login argument to be used for URL construction.
	 * Descendants can override this method when they have more sophisticated algorithms
	 * to determine the DB login argument. This implementation returns the default DB login
	 * when the passed string is either empty or DB login argument was disabled.
	 * @param s the string containing an actual DB login argument
	 * @return the argument to be used in URL construction
	 * @see #isDbLoginEnabled()
	 * @see #setDbLoginEnabled(boolean)
	 * @see #getDefaultDbLogin()
	 * @see #setDefaultDbLogin(String)
	 */
	protected String getDbLogin(String s) {
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
	 * Returns the DB password argument to be used for URL construction.
	 * Descendants can override this method when they have more sophisticated algorithms
	 * to determine the DB password argument. This implementation returns the default DB password
	 * when the passed string is either empty or DB password argument was disabled.
	 * @param s the string containing an actual DB password argument
	 * @return the argument to be used in URL construction
	 * @see #isDbPasswordEnabled()
	 * @see #setDbPasswordEnabled(boolean)
	 * @see #getDefaultDbPassword()
	 * @see #setDefaultDbPassword(String)
	 */
	protected String getDbPassword(String s) {
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
	 * Returns the additional argument to be used for URL construction.
	 * Descendants can override this method when they have more sophisticated algorithms
	 * to determine the additional argument. This implementation returns the default additional
	 * argument when the passed string is either empty or the additional argument was disabled.
	 * @param index index of additional argument
	 * @param s the string containing an actual additional argument
	 * @return the argument to be used in URL construction
	 * @see #isAdditionalArgumentEnabled(int)
	 * @see #setAdditionalArgumentEnabled(int, boolean)
	 * @see #getDefaultAdditionalArgument(int)
	 * @see #setDefaultAdditionalArgument(int, String)
	 */
	protected String getAdditionalArgument(int index, String s) {
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
}
