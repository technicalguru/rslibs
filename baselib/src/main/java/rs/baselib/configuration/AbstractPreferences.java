/**
 * 
 */
package rs.baselib.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;

import org.apache.commons.codec.binary.Base64;

import rs.baselib.bean.AbstractBean;
import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;

/**
 * Abstract implementation of a preference node.
 * @author ralph
 *
 */
public abstract class AbstractPreferences extends AbstractBean implements IPreferences {

	/** The parent node */
	private AbstractPreferences parent;
	/** Name of this child (null if root node) */
	private String name;
	/** All children */
	private Map<String, AbstractPreferences> children;
	/** All values */
	private Map<String, String> values;
	/** cache of absolute path */
	private String absolutePath = null;
	/**
	 * Constructor.
	 * Creates a root node.
	 */
	public AbstractPreferences() {
		this(null,null);
	}

	/**
	 * Constructor.
	 * @param parent the parent of this node
	 */
	public AbstractPreferences(AbstractPreferences parent, String name) {
		this.parent = parent;
		this.name = name;
		children = new HashMap<String, AbstractPreferences>();
		values = new HashMap<String, String>();
	}

	/**
	 * Subclasses must implement this to return a new child node.
	 * @param parent the parent to be used
	 * @param name the name to be used
	 * @return the new node
	 */
	protected abstract AbstractPreferences createNode(AbstractPreferences parent, String name);

	/**
	 * Adds the given node.
	 * The scheduling policy will define whether this task is executed immediately or
	 * performed by a background task (which requires a {@link #flush()} call to access
	 * the node later).
	 * @param child child to be added
	 */
	protected void addNode(AbstractPreferences child) {
		schedule(new NodeAdder(child));
	}

	/**
	 * Removes the given node.
	 * The scheduling policy will define whether this task is executed immediately or
	 * performed by a background task (which requires a {@link #flush()} to see the result).
	 * @param child child to be removed
	 */
	protected void removeNode(AbstractPreferences child) {
		schedule(new NodeRemover(child));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void put(String key, String value) {
		schedule(new ValueAdder(key, value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String get(String key, String def) {
		String rc = values.get(key);
		return rc != null ? rc : def;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(String key) {
		schedule(new ValueRemover(key));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() throws BackingStoreException {
		schedule(new Clearer());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putInt(String key, int value) {
		schedule(new ValueAdder(key, Integer.toString(value)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(String key, int def) {
		return LangUtils.getInt(values.get(key), def);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putLong(String key, long value) {
		schedule(new ValueAdder(key, Long.toString(value)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(String key, long def) {
		return LangUtils.getLong(values.get(key), def);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putBoolean(String key, boolean value) {
		schedule(new ValueAdder(key, Boolean.toString(value)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(String key, boolean def) {
		String s = get(key, null);
		if (s != null) return LangUtils.getBoolean(s);
		return def;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putFloat(String key, float value) {
		schedule(new ValueAdder(key, Float.toString(value)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(String key, float def) {
		return LangUtils.getFloat(values.get(key), def);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putDouble(String key, double value) {
		schedule(new ValueAdder(key, Double.toString(value)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(String key, double def) {
		return LangUtils.getDouble(values.get(key), def);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putByteArray(String key, byte[] value) {
		schedule(new ValueAdder(key, Base64.encodeBase64String(value)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getByteArray(String key, byte[] def) {
		String s = get(key, null);
		if (s != null) return Base64.decodeBase64(s);
		return def;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] keys() throws BackingStoreException {
		return values.keySet().toArray(new String[values.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] childrenNames() throws BackingStoreException {
		return children.keySet().toArray(new String[values.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPreferences parent() {
		return parent;
	}

	/**
	 * Returns true when the given pathname is relative to this node.
	 * This means the path does not start with a slash ('/').
	 * @param pathName name to be checked
	 * @return <code>true</code> when path name is relative to this node
	 */
	protected boolean isRelative(String pathName) {
		return !isAbsolute(pathName);
	}

	/**
	 * Returns true when the given pathname is absolute to root node of this node.
	 * This means the path starts with a slash ('/').
	 * @param pathName name to be checked
	 * @return <code>true</code> when path name is absolute to root node of this node
	 */
	protected boolean isAbsolute(String pathName) {
		return pathName.startsWith("/");
	}

	/**
	 * Returns true when this is a root node.
	 * @return <code>true</code> when this is a root node
	 */
	protected boolean isRootNode() {
		return parent() == null;
	}
	/**
	 * Returns the root node of this node.
	 * @return root node
	 */
	protected AbstractPreferences getRootNode() {
		if (isRootNode()) return this;
		return ((AbstractPreferences)parent()).getRootNode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPreferences node(String pathName) {
		IPreferences rc = null;
		if (isAbsolute(pathName) && !isRootNode()) {
			rc = getRootNode().node(pathName);
		} else {
			// Make name relative
			if (pathName.startsWith("/")) {
				if (pathName.length() > 1) {
					pathName = pathName.substring(1);
				} else {
					// Name is empty. That's this node
					return this;
				}
			}
			String names[] = pathName.split("\\/");
			AbstractPreferences child = children.get(names[0]);
			if (child == null) {
				child = createNode(this, names[0]);
				addNode(child);
			}
			if (names.length > 1) {
				pathName = CommonUtils.join("/", names, 1);
				rc = child.node(pathName);
			} else {
				rc = child;
			}
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean nodeExists(String pathName) throws BackingStoreException {
		if (isAbsolute(pathName) && !isRootNode()) {
			return getRootNode().nodeExists(pathName);
		} else {
			// Make name relative
			if (pathName.startsWith("/")) {
				if (pathName.length() > 1) {
					pathName = pathName.substring(1);
				} else {
					// Name is empty. That's this node
					return true;
				}
			}
			String names[] = pathName.split("\\/");
			AbstractPreferences child = children.get(names[0]);
			if (child == null) {
				return false;
			}
			if (names.length > 1) {
				pathName = CommonUtils.join("/", names, 1);
				return child.nodeExists(pathName);
			}
			// Last child found
			return true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNode() throws BackingStoreException {
		if (parent() != null) {
			((AbstractPreferences)parent()).removeNode(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String absolutePath() {
		if (absolutePath == null) {
			if (isRootNode()) {
				absolutePath = "/";
			} else {
				StringBuilder s = new StringBuilder();
				s.append(parent.absolutePath());
				s.append("/");
				s.append(name());
				s.deleteCharAt(0);
				absolutePath = s.toString();
			}
		}
		return absolutePath;
	}

	/**
	 * Returns the preferences service.
	 * @return the preferences service
	 */
	protected abstract IPreferencesService getPreferencesService();

	/**
	 * Schedules the task for execution.
	 * @param task
	 */
	protected void schedule(Runnable task) {
		IPreferencesService svc = getPreferencesService();
		if (svc != null) {
			svc.schedule(this, task);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() throws BackingStoreException {
		IPreferencesService svc = getPreferencesService();
		if (svc != null) {
			svc.flush(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sync() throws BackingStoreException {
		IPreferencesService svc = getPreferencesService();
		if (svc != null) {
			svc.sync(this);
		}
	}

	/**
	 * The runnable to be executed for adding a new node.
	 * @author ralph
	 *
	 */
	protected class NodeAdder implements Runnable {

		private AbstractPreferences child;

		/**
		 * Constructor.
		 * @param child child to be registered
		 */
		public NodeAdder(AbstractPreferences child) {
			this.child = child;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			String n = child.name();
			if (n == null) n = "";
			children.put(n, child);
			firePropertyChange(CHILD_ADDED, null, child);
		}

	}

	/**
	 * The runnable to be executed for adding a new value.
	 * @author ralph
	 *
	 */
	protected class ValueAdder implements Runnable {

		private String key;
		private String value;

		/**
		 * Constructor.
		 * @param key key to be registered
		 * @param value value to be registered
		 */
		public ValueAdder(String key, String value) {
			this.key   = key;
			this.value = value;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			String oldValue = values.put(key, value);
			firePropertyChange(key, oldValue, value);
		}

	}

	/**
	 * The runnable to be executed for removing a value.
	 * @author ralph
	 *
	 */
	protected class ValueRemover implements Runnable {

		private String key;

		/**
		 * Constructor.
		 * @param key key to be removed
		 */
		public ValueRemover(String key) {
			this.key   = key;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			String oldValue = values.remove(key);
			firePropertyChange(key, oldValue, null);
		}

	}

	/**
	 * The runnable to be executed for removing a node.
	 * @author ralph
	 *
	 */
	protected class NodeRemover implements Runnable {

		private AbstractPreferences child;

		/**
		 * Constructor.
		 * @param key key to be removed
		 */
		public NodeRemover(AbstractPreferences child) {
			this.child = child;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			if (parent() != null) {
				children.remove(child.name());
				firePropertyChange(CHILD_REMOVED, child, null);
			}
		}

	}

	/**
	 * The runnable to be executed for removing all key-value pairs.
	 * @author ralph
	 *
	 */
	protected class Clearer implements Runnable {

		/**
		 * Constructor.
		 * @param key key to be removed
		 */
		public Clearer() {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			values.clear();
			firePropertyChange(VALUES_CLEARED, null, this);
		}

	}


}
