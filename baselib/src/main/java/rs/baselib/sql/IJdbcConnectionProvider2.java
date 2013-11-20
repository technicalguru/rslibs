/**
 * 
 */
package rs.baselib.sql;


/**
 * Enhanced {@link IJdbcConnectionProvider} which allows more
 * control on connection provider settings.
 * @author ralph
 *
 */
public interface IJdbcConnectionProvider2 extends IJdbcConnectionProvider {

	/**
	 * Returns whether host argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @return <code>true</code> when <code>host</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public boolean isHostEnabled();
	
	/**
	 * Sets whether host argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @param b <code>true</code> when <code>host</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public void setHostEnabled(boolean b);
	
	/**
	 * A shortcut method for {@link #setHostEnabled(boolean)} and {@link #setDefaultHost(String)}.
	 * @param enabled whether to enable host argument
	 * @param defaultValue the default value
	 */
	public void setHost(boolean enabled, String defaultValue);
	
	/**
	 * Returns the default host argument to be used when host argument is disabled or actual value is empty.
	 * @return default value
	 */
	public String getDefaultHost();
	
	/**
	 * Sets the default host argument to be used when host argument is disabled or actual value is empty.
	 * @param s default value
	 */
	public void setDefaultHost(String s);
	
	/**
	 * Returns whether port argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @return <code>true</code> when <code>port</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public boolean isPortEnabled();
	
	/**
	 * Sets whether port argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @param b <code>true</code> when <code>port</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public void setPortEnabled(boolean b);
	
	/**
	 * A shortcut method for {@link #setPortEnabled(boolean)} and {@link #setDefaultPort(String)}.
	 * @param enabled whether to enable port argument
	 * @param defaultValue the default value
	 */
	public void setPort(boolean enabled, String defaultValue);
	
	/**
	 * Returns the default port argument to be used when port argument is disabled or actual value is empty.
	 * @return default value
	 */
	public String getDefaultPort();
	
	/**
	 * Sets the default port argument to be used when port argument is disabled or actual value is empty.
	 * @param s default value
	 */
	public void setDefaultPort(String s);
	
	/**
	 * Returns whether DB name argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @return <code>true</code> when <code>dbName</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public boolean isDbNameEnabled();
	
	/**
	 * Sets whether DB name argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @param b <code>true</code> when <code>dbName</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public void setDbNameEnabled(boolean b);
	
	/**
	 * A shortcut method for {@link #setDbNameEnabled(boolean)} and {@link #setDefaultDbName(String)}.
	 * @param enabled whether to enable DB name argument
	 * @param defaultValue the default value
	 */
	public void setDbName(boolean enabled, String defaultValue);
	
	/**
	 * Returns the default DB name argument to be used when DB name argument is disabled or actual value is empty.
	 * @return default value
	 */
	public String getDefaultDbName();
	
	/**
	 * Sets the default DB name argument to be used when DB name argument is disabled or actual value is empty.
	 * @param s default value
	 */
	public void setDefaultDbName(String s);
	
	/**
	 * Returns whether DB login argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @return <code>true</code> when <code>dbLogin</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public boolean isDbLoginEnabled();
	
	/**
	 * Sets whether DB login argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @param b <code>true</code> when <code>dbLogin</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public void setDbLoginEnabled(boolean b);
	
	/**
	 * A shortcut method for {@link #setDbLoginEnabled(boolean)} and {@link #setDefaultDbLogin(String)}.
	 * @param enabled whether to enable DB login argument
	 * @param defaultValue the default value
	 */
	public void setDbLogin(boolean enabled, String defaultValue);
	
	/**
	 * Returns the default DB login argument to be used when DB login argument is disabled or actual value is empty.
	 * @return default value
	 */
	public String getDefaultDbLogin();
	
	/**
	 * Sets the default DB login argument to be used when DB login argument is disabled or actual value is empty.
	 * @param s default value
	 */
	public void setDefaultDbLogin(String s);
	
	/**
	 * Returns whether DB password argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @return <code>true</code> when <code>dbPassword</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public boolean isDbPasswordEnabled();
	
	/**
	 * Sets whether DB password argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @param b <code>true</code> when <code>dbPassword</code> argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public void setDbPasswordEnabled(boolean b);
	
	/**
	 * A shortcut method for {@link #setDbPasswordEnabled(boolean)} and {@link #setDefaultDbPassword(String)}.
	 * @param enabled whether to enable DB password argument
	 * @param defaultValue the default value
	 */
	public void setDbPassword(boolean enabled, String defaultValue);
	
	/**
	 * Returns the default DB password argument to be used when DB password argument is disabled or actual value is empty.
	 * @return default value
	 */
	public String getDefaultDbPassword();
	
	/**
	 * Sets the default DB password argument to be used when DB password argument is disabled or actual value is empty.
	 * @param s default value
	 */
	public void setDefaultDbPassword(String s);

	/**
	 * Returns whether additional argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @param index index of additional argument
	 * @return <code>true</code> when additional argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public boolean isAdditionalArgumentEnabled(int index);
	
	/**
	 * Sets whether additional argument is enabled.
	 * The information can be used for UI developers to control the input flow.
	 * @param index index of additional argument
	 * @param b <code>true</code> when additional argument can be passed to {@link #getConnection(String, String, String, String, String, String...)}
	 */
	public void setAdditionalArgumentEnabled(int index, boolean b);
	
	/**
	 * A shortcut method for {@link #setAdditionalArgumentEnabled(int, boolean)}, {@link #setAdditionalArgumentName(int, String)}
	 * and {@link #setDefaultAdditionalArgument(int, String)}.
	 * @param index index of additional argument
	 * @param enabled whether to enable additional argument
	 * @param name name of additional argument
	 * @param defaultValue the default value
	 */
	public void setAdditionalArgument(int index, String name, boolean enabled, String defaultValue);
	
	/**
	 * Returns the default additional argument to be used when it is disabled or actual value is empty.
	 * @return default value
	 */
	public String getDefaultAdditionalArgument(int index);
	
	/**
	 * Sets the default additional argument to be used when it is disabled or actual value is empty.
	 * @param s default value
	 */
	public void setDefaultAdditionalArgument(int index, String s);

	/**
	 * Returns the name of the additional argument.
	 * The name can be used for labels.
	 * @param index index of argument
	 * @return name of argument
	 */
	public String getAdditionalArgumentName(int index);
	
	/**
	 * Sets the name of the additional argument.
	 * The name can be used for labels.
	 * @param index index of argument
	 * @param s name of argument
	 */
	public void setAdditionalArgumentName(int index, String s);

	/**
	 * Returns the names of additional arguments.
	 * @return array of names in order (array is empty when no additional arguments are used)
	 */
	public String[] getAdditionalArgumentNames();

	/**
	 * Returns the number of additional arguments this provider knows about.
	 * @return number of additional arguments
	 */
	public int getAdditionalArgumentCount();
}
