/**
 * 
 */
package rs.data.event;

/**
 * Listens to events from a DAO factory.
 * @author ralph
 *
 */
public interface IDaoFactoryListener {

	/**
	 * Handle the given DAO factory event.
	 * @param event event to be handled
	 */
	public void handleDaoFactoryEvent(DaoFactoryEvent event);
}
