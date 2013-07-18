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
