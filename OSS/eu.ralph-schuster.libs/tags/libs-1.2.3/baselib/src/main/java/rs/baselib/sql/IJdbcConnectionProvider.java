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
import java.sql.SQLException;

/**
 * An interface for generically providing JDBC connections.
 * The intention of this interface is to encapsulate all database
 * specific issues of constructing a URL or the driver name.
 * <p>
 * Clients of <code>IJdbcConnectionProvider</code> simply call
 * {@link #getConnection(String, String, String, String, String, String...)} in order
 * to get a valid driver URL constructed and passed to {@link DriverManager}.
 * </p>
 * @author ralph
 *
 */
public interface IJdbcConnectionProvider {

	/**
	 * Provide and open the connection for the given arguments.
	 * @param host the host of the database
	 * @param port the port of the database (some DB use names as port information)
	 * @param dbName name of database
	 * @param dbLogin database login
	 * @param dbPassword database password
	 * @param addOnArgs additional arguments to be processed by the driver
	 * @return an opened connection if successful
	 * @throws SQLException when the connection cannot be opened
	 */
	public Connection getConnection(String host, String port, String dbName, String dbLogin, String dbPassword, String... addOnArgs) throws SQLException;

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
	public String getDriverUrl(String host, String port, String dbName, String dbLogin, String dbPassword, String... addOnArgs);
}
