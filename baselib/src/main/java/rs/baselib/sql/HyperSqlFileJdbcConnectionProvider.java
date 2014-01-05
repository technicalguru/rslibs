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
 * Connection provider for HyperSQL (HSQLDB) file databases.
 * The URL template used is {@value #URL_TEMPLATE}. That means that host, port and DB name are not relevant
 * but instead argument 5 must be the path to the file.
 * @author ralph
 *
 */
public class HyperSqlFileJdbcConnectionProvider extends AbstractJdbcConnectionProvider {

	/** The driver class name */
	public static final String DB_DRIVER_CLASS_NAME = "org.hsqldb.jdbc.JDBCDriver";
	/** The driver name */
	public static final String DISPLAY = "HyperSQL (File Mode)";
	/** The URL Template */
	public static final String URL_TEMPLATE = "jdbc:hsqldb:file:{5};shutdown=true";
	/** The Data Source */
	public static final String DATA_SOURCE_NAME = "org.hsqldb.jdbc.JDBCDataSource";
	/** The Hibernate dialect */
	public static final String HIBERNATE_DIALECT = "org.hibernate.dialect.HSQLDialect";
	
	/**
	 * Constructor.
	 */
	public HyperSqlFileJdbcConnectionProvider() {
		super(DISPLAY, DB_DRIVER_CLASS_NAME, URL_TEMPLATE);
		setDataSource(DATA_SOURCE_NAME);
		setHibernateDialect(HIBERNATE_DIALECT);
		
		setHost(false, null);
		setPort(false, null);
		setDbName(false, null);
		setDbLogin(false, "SA");
		setDbPassword(false, "");
		setAdditionalArgument(0, "path", true, "");
	}

}
