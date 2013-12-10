/**
 * 
 */
package rs.baselib.sql;

/**
 * Provides a name for the Hibernate dialect class
 * @author ralph
 *
 */
public interface IHibernateDialectProvider {

	/**
	 * Returns the dialect to be used by Hibernate.
	 * @return the name of the dialect class.
	 */
	public String getHibernateDialect();
}
