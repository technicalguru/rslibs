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
	public static final String DATA_SOURCE_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
	/** The Hibernate dialect */
	public static final String HIBERNATE_DIALECT = "org.hibernate.dialect.MySQLDialect";
	
	/**
	 * Constructor.
	 */
	public MySql5JdbcConnectionProvider() {
		super(DISPLAY, DB_DRIVER_CLASS_NAME, URL_TEMPLATE);
		setDataSource(DATA_SOURCE_NAME);
		setHibernateDialect(HIBERNATE_DIALECT);
		setHost(true, "localhost");
		setPort(true, DEFAULT_PORT);
	}

	
}
