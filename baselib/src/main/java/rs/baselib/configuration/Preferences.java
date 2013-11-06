/**
 * 
 */
package rs.baselib.configuration;

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

}
