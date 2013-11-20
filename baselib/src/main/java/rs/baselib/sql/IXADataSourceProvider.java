/**
 * 
 */
package rs.baselib.sql;

import javax.sql.XADataSource;

/**
 * Returns the name of an {@link XADataSource}.
 * @author ralph
 *
 */
public interface IXADataSourceProvider {

	/**
	 * Returns the name of the {@link XADataSource}.
	 * @return the source
	 */
	public String getXADataSource();
}
