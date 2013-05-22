/**
 * 
 */
package rs.data.event;

/**
 * Listens to events from a DAO.
 * @author ralph
 *
 */
public interface DaoListener {

	/**
	 * Handle the given DAO event.
	 * @param event event to be handled
	 */
	public void handleDaoEvent(DaoEvent event);
}
