/**
 * 
 */
package rs.baselib.configuration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.prefs.BackingStoreException;

import org.apache.commons.codec.binary.Base64;

import rs.baselib.bean.IPropertyChangeProvider;
import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;

/**
 * Abstract implementation of a preference node.
 * All modifications are performed synchronously.
 * @author ralph
 *
 */
public abstract class AbstractPreferences implements IPreferences, IPropertyChangeProvider {

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
	/** PropertyChangeSupport */
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	/** Our responsible read lock object */
	private Lock readLock;
	/** Our responsible read lock object */
	private Lock writeLock;

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
		readLock = createReadLock();
		writeLock = createWriteLock();
	}

	/**
	 * Creates the appropriate read lock object.
	 * @return the read lock object
	 */
	protected abstract Lock createReadLock();

	/**
	 * Creates the appropriate write lock object.
	 * @return the write lock object
	 */
	protected abstract Lock createWriteLock();

	/**
	 * Returns the readLock.
	 * @return the readLock
	 */
	public Lock getReadLock() {
		return readLock;
	}

	/**
	 * Aquires a read lock.
	 */
	protected void readLock() {
		Lock l = getReadLock();
		if (l != null) l.lock();
	}
	
	/**
	 * Releases a read lock.
	 */
	protected void readUnlock() {
		Lock l = getReadLock();
		if (l != null) l.unlock();
	}
	
	/**
	 * Returns the writeLock.
	 * @return the writeLock
	 */
	public Lock getWriteLock() {
		return writeLock;
	}

	/**
	 * Aquires a write lock.
	 */
	protected void writeLock() {
		Lock l = getWriteLock();
		if (l != null) l.lock();
	}
	
	/**
	 * Releases a write lock.
	 */
	protected void writeUnlock() {
		Lock l = getWriteLock();
		if (l != null) l.unlock();
		// schedule flush
		getPreferencesService().nodeChanged(this);
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
		try {
			writeLock();
			String n = child.name();
			if (n == null) n = "";
			children.put(n, child);
		} finally {
			writeUnlock();
		}
		firePropertyChange(CHILD_ADDED, null, child);
	}

	/**
	 * Removes the given node.
	 * The scheduling policy will define whether this task is executed immediately or
	 * performed by a background task (which requires a {@link #flush()} to see the result).
	 * @param child child to be removed
	 */
	protected void removeNode(AbstractPreferences child) {
		try {
			writeLock();
			children.remove(child.name());
		} finally {
			writeUnlock();
		}
		firePropertyChange(CHILD_REMOVED, child, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void put(String key, String value) {
		String oldValue = null;
		try {
			writeLock();
			oldValue = values.put(key, value);
		} finally {
			writeUnlock();
		}
		firePropertyChange(key, oldValue, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String get(String key, String def) {
		try {
			readLock();
			String rc = values.get(key);
			return rc != null ? rc : def;
		} finally {
			readUnlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(String key) {
		String oldValue = null;
		try {
			writeLock();
			oldValue = values.remove(key);
		} finally {
			writeUnlock();
		}

		firePropertyChange(key, oldValue, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() throws BackingStoreException {
		try {
			writeLock();
			values.clear();
		} finally {
			writeUnlock();
		}
		firePropertyChange(VALUES_CLEARED, null, AbstractPreferences.this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putInt(String key, int value) {
		put(key, Integer.toString(value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(String key, int def) {
		return LangUtils.getInt(get(key, null), def);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putLong(String key, long value) {
		put(key, Long.toString(value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(String key, long def) {
		return LangUtils.getLong(get(key, null), def);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putBoolean(String key, boolean value) {
		put(key, Boolean.toString(value));
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
		put(key, Float.toString(value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloat(String key, float def) {
		return LangUtils.getFloat(get(key, null), def);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putDouble(String key, double value) {
		put(key, Double.toString(value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(String key, double def) {
		return LangUtils.getDouble(get(key, null), def);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putByteArray(String key, byte[] value) {
		put(key, Base64.encodeBase64String(value));
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
		Lock lock = PreferencesService.INSTANCE.getReadLock(this);
		try {
			if (lock != null) lock.lock();
			return values.keySet().toArray(new String[values.size()]);
		} finally {
			if (lock != null) lock.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] childrenNames() throws BackingStoreException {
		try {
			readLock();
			return children.keySet().toArray(new String[children.size()]);
		} finally {
			readUnlock();
		}
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
			AbstractPreferences child = null;
			try {
				readLock();
				child = children.get(names[0]);
				if (child == null) {
					// We need to release read lock before write lock can get aquired
					readUnlock();
					
					// Get write lock
					try {
						writeLock();
						// We must recheck existence of child as another thread might have interrupted
						child = children.get(names[0]);
						if (child == null) {
							child = createNode(this, names[0]);
							addNode(child);
						}
					} finally {
						writeUnlock();
					}
					
					// Read lock again for finally {} clause 
					readLock();
				}
			} finally {
				readUnlock();
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
			Lock lock = PreferencesService.INSTANCE.getReadLock(this);
			AbstractPreferences child = null;
			try {
				if (lock != null) lock.lock();
				child = children.get(names[0]);
				if (child == null) {
					return false;
				}
			} finally {
				if (lock != null) lock.unlock();
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
	protected abstract AbstractPreferencesService getPreferencesService();

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
	 * {@inheritDoc}
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		return firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
	}

	/**
	 * Fires a change event.
	 * @param event event to be fired
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(PropertyChangeEvent event) {
		if (!CommonUtils.equals(event.getOldValue(), event.getNewValue())) {
			changeSupport.firePropertyChange(event);
			return true;
		}
		return false;
	}

}
