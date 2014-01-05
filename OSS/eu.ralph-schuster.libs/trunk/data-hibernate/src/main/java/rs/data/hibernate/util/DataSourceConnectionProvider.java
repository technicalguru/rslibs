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
package rs.data.hibernate.util;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider;

/**
 * Inject the data source
 * @author ralph
 *
 */
public class DataSourceConnectionProvider extends InjectedDataSourceConnectionProvider {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;
	
	private static DataSource myDatasource;
	
	/**
	 * Constructor.
	 */
	public DataSourceConnectionProvider() {
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource getDataSource() {
		return getMyDatasource();
	}


	/**
	 * Returns the myDatasource.
	 * @return the myDatasource
	 */
	public static DataSource getMyDatasource() {
		return myDatasource;
	}

	/**
	 * Sets the myDatasource.
	 * @param myDatasource the myDatasource to set
	 */
	public static void setMyDatasource(DataSource myDatasource) {
		DataSourceConnectionProvider.myDatasource = myDatasource;
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void configure(Map configValues) {
		// Do nothing here to avoid runtime configuration.
		configValues.put( Environment.DATASOURCE, getMyDatasource() );
		super.configure(configValues);
	}

	
}
