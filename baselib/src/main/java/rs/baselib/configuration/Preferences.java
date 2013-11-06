/**
 * 
 */
package rs.baselib.configuration;

import java.util.concurrent.locks.Lock;

/**
 * Default implementation of preferences.
 * @author ralph
 *
 */
public class Preferences extends AbstractPreferences {

	/**
	 * Constructor.
	 */
	public Preferences(AbstractPreferences parent, String name) {
		super(parent, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractPreferences createNode(AbstractPreferences parent, String name) {
		return new Preferences(parent, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IPreferencesService getPreferencesService() {
		return PreferencesService.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Lock createReadLock() {
		return PreferencesService.INSTANCE.getReadLock(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Lock createWriteLock() {
		return PreferencesService.INSTANCE.getWriteLock(this);
	}

	
}
