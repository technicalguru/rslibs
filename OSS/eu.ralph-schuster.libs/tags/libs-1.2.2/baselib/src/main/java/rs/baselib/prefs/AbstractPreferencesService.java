/**
 * 
 */
package rs.baselib.prefs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.prefs.BackingStoreException;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

import rs.baselib.lang.LangUtils;

/**
 * Common functionality of a preferences service.
 * The service organizes all preferences in this scheme:
 * <pre>
 *    /@application1/@USER   - user preferences of application 1
 *    /@application1/@SYSTEM - system preferences of application 1
 *    /@application2/@USER   - user preferences of application 2
 *    /@application2/@SYSTEM - system preferences of application 2
 * </pre>
 * @author ralph
 *
 */
public abstract class AbstractPreferencesService implements IPreferencesService {

	private static final Pattern PATTERN          = Pattern.compile("[-A-Za-z._@]+");
	private static final String  USER_NODE_NAME   = "@USER";
	private static final String  SYSTEM_NODE_NAME = "@SYSTEM";
	private static final long FLUSH_DELAY = 500L;
	
	private IPreferences rootNode;
	private Object SYNCH_OBJECT = new Object();
	private Map<IPreferences,ReadWriteLock> locks = new HashMap<IPreferences, ReadWriteLock>();
	private volatile Set<IPreferences> flushableNodes = new HashSet<IPreferences>();
	private volatile Set<String> loadingApplications = new HashSet<String>();
	private volatile long lastModificationTime = 0L;
	private volatile Thread flushingThread = null;

	/**
	 * Constructor.
	 */
	public AbstractPreferencesService() {
	}

	/**
	 * Creates the root node on request.
	 * Bootstrap sequence requires a lazy setup.
	 * @return
	 */
	private IPreferences getRootNode() {
		if (rootNode == null) {
			rootNode = createRootNode();
		}
		return rootNode;
	}
	
	/**
	 * Creates the root node.
	 * @return the root node for the service
	 */
	protected abstract IPreferences createRootNode();
	
	/**
	 * Returns the application preferences.
	 * @param applicationName name of application
	 * @return the root node for this application
	 * @throws BackingStoreException
	 */
	protected IPreferences getApplicationPreferences(String applicationName) throws BackingStoreException {
		checkNodeName(applicationName);
		IPreferences rc = null;
		if (!getRootNode().nodeExists("@"+applicationName)) {
			boolean doLoad = true;
			synchronized(SYNCH_OBJECT) {
				if (loadingApplications.contains(applicationName) || getRootNode().nodeExists("@"+applicationName)) {
					doLoad = false;
				} else {
					loadingApplications.add(applicationName);
				}
			}

			if (doLoad) {
				rc = getRootNode().node("@"+applicationName);
				IPreferences prefs = rc.node(SYSTEM_NODE_NAME);
				locks.put(prefs, createReadWriteLock());
				loadSystemPreferences(prefs, applicationName);
				prefs = rc.node(USER_NODE_NAME);
				locks.put(prefs, createReadWriteLock());
				loadUserPreferences(prefs, applicationName);
				synchronized(SYNCH_OBJECT) {
					loadingApplications.remove(applicationName);
				}
			}
		} else {
			rc = getRootNode().node("@"+applicationName);
		}
		return rc;
	}

	/**
	 * Creates a new {@link ReadWriteLock}.
	 * @return a new {@link ReentrantReadWriteLock}
	 */
	protected ReadWriteLock createReadWriteLock() {
		return new ReentrantReadWriteLock(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public Lock getReadLock(IPreferences node) {
		ReadWriteLock l = getReadWriteLock(node);
		if (l == null) return null;
		return l.readLock();
	}

	/**
	 * {@inheritDoc}
	 */
	public Lock getWriteLock(IPreferences node) {
		ReadWriteLock l = getReadWriteLock(node);
		if (l == null) return null;
		return l.writeLock();
	}

	/**
	 * Returns the correct lock for the given node.
	 * @param node node to get the lock for
	 * @return the {@link ReadWriteLock}
	 */
	protected ReadWriteLock getReadWriteLock(IPreferences node) {
		// Which is the node to be locked?
		node = getLockNode(node);

		// Find and return the lock
		return locks.get(node);
	}

	/**
	 * Returns the node where locks will be placed upon.
	 * @param node the node where a lock is required
	 * @return the node that needs to be locked
	 */
	protected IPreferences getLockNode(IPreferences node) {
		while ((node.name() != null) && !node.name().startsWith("@") && (node.parent() != null)) {
			node = node.parent();
		}
		return node;
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
	 * Flushes the user preferences from the given node.
	 * @param node node that shall be flushed
	 * @param applicationName name of application to be flushed
	 */
	protected abstract void flushUserPreferences(IPreferences node, String applicationName) throws BackingStoreException;

	/**
	 * Flushes the system preferences from the given node.
	 * @param node node that shall be flushed
	 * @param applicationName name of application to be flushed
	 */
	protected abstract void flushSystemPreferences(IPreferences node, String applicationName) throws BackingStoreException;

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
	 * A node changed.
	 * Schedules a flush.
	 * @param node the node that changed
	 */
	public void nodeChanged(IPreferences node) {
		node = getLockNode(node);
		synchronized (SYNCH_OBJECT) {
			if (!loadingApplications.contains(getApplicationName(node))) {
				flushableNodes.add(node);
				lastModificationTime = System.currentTimeMillis();
				if (flushingThread == null) {
					flushingThread = new FlushingThread();
					flushingThread.start();
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush(IPreferences node) throws BackingStoreException {
		Lock lock = getWriteLock(node);
		try {
			if (lock != null) lock.lock();
			IPreferences prefs = getApplicationNode(node);
			if (prefs != null) {
				String applicationName = getApplicationName(prefs);
				flushUserPreferences(prefs.node(USER_NODE_NAME), applicationName);
				flushSystemPreferences(prefs.node(SYSTEM_NODE_NAME), applicationName);
			}
		} finally {
			if (lock != null) lock.unlock();
		}
	}

	/**
	 * Returns the application node rom the given node
	 * @param node child node
	 * @return the application node
	 */
	protected IPreferences getApplicationNode(IPreferences node) {
		while (node != null) {
			if (node.parent() == getRootNode()) return node;
			node = node.parent();
		}
		return null;
	}

	/**
	 * Returns the application name of the given node.
	 * @param node any child node
	 * @return the application name this node belongs to
	 */
	protected String getApplicationName(IPreferences node) {
		node = getApplicationNode(node);
		if (node != null) {
			return node.name().substring(1);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sync(IPreferences node) throws BackingStoreException {
		// Simply check if there is a flushing thread active
		boolean isRunning = true;
		while (isRunning) {
			synchronized (SYNCH_OBJECT) {
				isRunning = flushingThread != null;
			}
		}
	}

	/**
	 * A thread running all flushs.
	 * @author ralph
	 *
	 */
	protected class FlushingThread extends Thread {

		public void run() {
			int toBeFlushed;
			long timeDiff = 0L;
			do {
				// Sleep before start
				LangUtils.sleep(120L);

				// Check the last modification time
				Set<IPreferences> flushingNodes = null;
				synchronized (SYNCH_OBJECT) {
					timeDiff = System.currentTimeMillis() - lastModificationTime;
					// Get the nodes to be flushed in case we have to do something
					toBeFlushed = flushableNodes.size();
					if ((timeDiff > FLUSH_DELAY) && (toBeFlushed > 0)) {
						flushingNodes = flushableNodes;
						flushableNodes = new HashSet<IPreferences>();
					}
				}

				// Only flush if required
				if (flushingNodes != null) {
					for (IPreferences node : flushingNodes) {
						try {
							flush(node);
						} catch (BackingStoreException e) {
							LoggerFactory.getLogger(AbstractPreferencesService.this.getClass()).error("Cannot flush node", e);
						}
					}

					// Recheck if there have been new flushable nodes added
					synchronized (SYNCH_OBJECT) {
						toBeFlushed = flushableNodes.size();
						// End this thread if nothing to do
						if (toBeFlushed == 0) flushingThread = null;
					}
				}

			} while (toBeFlushed > 0);
		}
	}
}
