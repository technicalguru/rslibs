/**
 * 
 */
package rs.baselib.sql;

/**
 * Connection provider for HyperSQL (HSQLDB) remote server databases.
 * The URL template used is {@value #URL_TEMPLATE}.
 * @author ralph
 *
 */
public class HyperSqlServerJdbcConnectionProvider extends AbstractJdbcConnectionProvider {

	/** The driver class name */
	public static final String DB_DRIVER_CLASS_NAME = "org.hsqldb.jdbc.JDBCDriver";
	/** The driver name */
	public static final String DISPLAY = "HyperSQL (Server Mode)";
	/** The URL Template */
	public static final String URL_TEMPLATE = "jdbc:hsqldb:hsql://{0}:{1}/{2}";
	/** The default port */
	public static final String DEFAULT_PORT = "9001";
	/** The XA Data Source */
	public static final String XA_DATA_SOURCE_NAME = "org.hsqldb.jdbc.pool.JDBCXADataSource";
	/** The Hibernate dialect */
	public static final String HIBERNATE_DIALECT = "org.hibernate.dialect.HSQLDialect";
	
	/**
	 * Constructor.
	 */
	public HyperSqlServerJdbcConnectionProvider() {
		super(DISPLAY, DB_DRIVER_CLASS_NAME, URL_TEMPLATE);
		setXaDataSource(XA_DATA_SOURCE_NAME);
		setHibernateDialect(HIBERNATE_DIALECT);
		
		setHost(true, "localhost");
		setPort(true, DEFAULT_PORT);
	}

}
