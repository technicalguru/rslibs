/**
 * 
 */
package rs.data.event;

/**
 * Listens to events from a DAO.
 * @author ralph
 *
 */
public interface IDaoListener {

	/**
	 * Handle the given DAO event.
	 * @param event event to be handled
	 */
	public void handleDaoEvent(DaoEvent event);
}
