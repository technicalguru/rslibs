/**
 * 
 */
package rs.data.api;

/**
 * A general DAO master interface.
 * @author ralph
 *
 */
public interface DaoMaster {

	/**
	 * Returns the factory that this master is registered for.
	 * @return the factory that this DAO master is assigned to.
	 */
	public DaoFactory getFactory();
	

	/**
	 * Sets the factory that this master is registered for.
	 * @param factory the factory
	 */
	public void setFactory(DaoFactory factory);
}
