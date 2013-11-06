/**
 * 
 */
package rs.baselib.configuration;

import java.util.prefs.BackingStoreException;
import java.util.regex.Pattern;

/**
 * Common functionality of a preferences service.
 * The service organizes all preferences in this scheme:
 * <pre>
 *    /@application1/@USER   - user preferences of application 1
 *    /@application1/@SYSTEM - system preferences of application 1
 *    /@application2/@USER   - user preferences of application 1
 *    /@application2/@SYSTEM - system preferences of application 2
 * </pre>
 * @author ralph
 *
 */
public abstract class AbstractPreferencesService implements IPreferencesService {

	private static Pattern PATTERN          = Pattern.compile("[-A-Za-z._@]+");
	private static String  USER_NODE_NAME   = "@USER";
	private static String  SYSTEM_NODE_NAME = "@SYSTEM";
	
	private IPreferences rootNode;
	private Object SYNCH_OBJECT = new Object();
	
	/**
	 * Constructor.
	 */
	public AbstractPreferencesService(IPreferences rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Returns the application preferences.
	 * @param applicationName name of application
	 * @return the root node for this application
	 * @throws BackingStoreException
	 */
	protected IPreferences getApplicationPreferences(String applicationName) throws BackingStoreException {
		checkNodeName(applicationName);
		IPreferences rc = null;
		if (!rootNode.nodeExists("@"+applicationName)) {
			synchronized(SYNCH_OBJECT) {
				if (!rootNode.nodeExists("@"+applicationName)) {
					rc = rootNode.node("@"+applicationName);
					IPreferences prefs = rc.node(SYSTEM_NODE_NAME);
					loadSystemPreferences(prefs, applicationName);
					prefs = rc.node(USER_NODE_NAME);
					loadUserPreferences(prefs, applicationName);
				}
			}
		}
		return rc;
	}
	
	/**
	 * Loads the user preferences into the given node.
	 * @param node node that shall be populated
	 * @param applicationName name of application to be loaded
	 */
	protected abstract void loadUserPreferences(IPreferences node, String applicationName) throws BackingStoreException;
	
	/**
	 * Loads the system preferences into the given node.
	 * @param node node that shall be populated
	 * @param applicationName name of application to be loaded
	 */
	protected abstract void loadSystemPreferences(IPreferences node, String applicationName) throws BackingStoreException;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPreferences getUserPreferences(String applicationName) throws BackingStoreException {
		IPreferences rc = getApplicationPreferences(applicationName); 
		return rc.node(USER_NODE_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPreferences getSystemPreferences(String applicationName) throws BackingStoreException {
		IPreferences rc = getApplicationPreferences(applicationName); 
		return rc.node(SYSTEM_NODE_NAME);
	}

	/**
	 * Checks the name of the node to be correct.
	 * A node name must not contain whitespaces or special characters other than '@', '-' or '-' or '.'.
	 * @param s string to be checked
	 */
	protected static void checkNodeName(String s) {
		if (!PATTERN.matcher(s).matches()) {
			throw new IllegalArgumentException("Node name is invalid");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void schedule(IPreferences node, Runnable modificationTask) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush(IPreferences node) throws BackingStoreException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sync(IPreferences node) throws BackingStoreException {
		// TODO Auto-generated method stub
		
	}


}
