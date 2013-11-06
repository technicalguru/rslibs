/**
 * 
 */
package rs.baselib.configuration;

import java.util.prefs.BackingStoreException;


/**
 * The default implementation of {@link IPreferencesService}.
 * System preferences are stored in a common location depending on the OS. User
 * preferences are stored in a hidden directory of the user's home directory.
 * @author ralph
 *
 */
public class PreferencesService extends AbstractPreferencesService {

	/** The default global preference service */
	public static final IPreferencesService INSTANCE = new PreferencesService();
	
	/**
	 * Constructor.
	 */
	private PreferencesService() {
		super(new Preferences(null, null));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadUserPreferences(IPreferences node, String applicationName) throws BackingStoreException {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadSystemPreferences(IPreferences node, String applicationName) throws BackingStoreException {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flushUserPreferences(IPreferences node, String applicationName) throws BackingStoreException {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flushSystemPreferences(IPreferences node, String applicationName) throws BackingStoreException {
		// TODO Auto-generated method stub
	}


}
