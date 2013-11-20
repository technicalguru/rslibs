/**
 * 
 */
package rs.baselib.sql;

/**
 * Connection provider for MySQL.
 * The URL template used is {@value #URL_TEMPLATE}.
 * @author ralph
 *
 */
public class MySql5JdbcConnectionProvider extends AbstractJdbcConnectionProvider {

	/** The driver class name */
	public static final String DB_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	/** The driver name */
	public static final String DISPLAY = "MySQL 5.x";
	/** The URL Template */
	public static final String URL_TEMPLATE = "jdbc:mysql://{0}:{1}/{2}";
	/** The default port */
	public static final String DEFAULT_PORT = "3306";
	/** The XA Data Source */
	public static final String XA_DATA_SOURCE_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
	
	/**
	 * Constructor.
	 */
	public MySql5JdbcConnectionProvider() {
		super(DISPLAY, DB_DRIVER_CLASS_NAME, URL_TEMPLATE);
		setXaDataSource(XA_DATA_SOURCE_NAME);
		
		setHost(true, "localhost");
		setPort(true, DEFAULT_PORT);
	}

	
}
