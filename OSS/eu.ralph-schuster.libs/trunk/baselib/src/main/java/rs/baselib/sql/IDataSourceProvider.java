/**
 * 
 */
package rs.baselib.sql;

import javax.sql.DataSource;

/**
 * Returns the name of an {@link DataSource}.
 * @author ralph
 *
 */
public interface IDataSourceProvider {

	/**
	 * Returns the name of the {@link DataSource}.
	 * @return the source
	 */
	public String getDataSource();
}
