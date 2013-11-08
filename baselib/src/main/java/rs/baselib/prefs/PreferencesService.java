/**
 * 
 */
package rs.baselib.prefs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.prefs.BackingStoreException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.CommonUtils;


/**
 * The default implementation of {@link IPreferencesService}.
 * System preferences are stored in a common location depending on the OS. User
 * preferences are stored in a hidden directory of the user's home directory.
 * @author ralph
 *
 */
public class PreferencesService extends AbstractPreferencesService {

	private static final Logger log = LoggerFactory.getLogger(PreferencesService.class);
	
	/** The default global preference service */
	public static final IPreferencesService INSTANCE = new PreferencesService();

	private File userHome = null;
	private File systemHome = null;

	/**
	 * Constructor.
	 */
	private PreferencesService() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IPreferences createRootNode() {
		return new Preferences(null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadUserPreferences(IPreferences node, String applicationName) throws BackingStoreException {
		File f = getUserPreferencesFile(applicationName);
		if (log.isDebugEnabled()) log.debug("Loading user preferences: "+f.getAbsolutePath());
		load(node, getInputStream(f));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadSystemPreferences(IPreferences node, String applicationName) throws BackingStoreException {
		File f = getSystemPreferencesFile(applicationName);
		if (log.isDebugEnabled()) log.debug("Loading system preferences: "+f.getAbsolutePath());
		load(node, getInputStream(f));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flushUserPreferences(IPreferences node, String applicationName) throws BackingStoreException {
		save(node, getOutputStream(getUserPreferencesFile(applicationName)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flushSystemPreferences(IPreferences node, String applicationName) throws BackingStoreException {
		save(node, getOutputStream(getSystemPreferencesFile(applicationName)));
	}

	/**
	 * Returns an input stream that can be safely read.
	 * @param f file
	 * @return input stream (will be empty is file does not exists or cannot be read)
	 * @throws BackingStoreException when the file cannot be opened
	 */
	protected InputStream getInputStream(File f) throws BackingStoreException {
		try {
			if (f.exists() && f.canRead()) {
				return new FileInputStream(f);
			}
			return new ByteArrayInputStream(new byte[0]);
		} catch (IOException e) {
			throw new BackingStoreException(e);
		}
	}

	/**
	 * Returns an output stream that can be written to.
	 * @param f file
	 * @return output stream (intermediate directories will be created if required)
	 * @throws BackingStoreException when the file cannot be opened
	 */
	protected OutputStream getOutputStream(File f) throws BackingStoreException {
		try {
			if (!f.exists()) {
				f.getParentFile().mkdirs();
			}
			return new FileOutputStream(f);
		} catch (IOException e) {
			throw new BackingStoreException(e);
		}		
	}

	/**
	 * Loads the information from the stream into the node
	 * @param node node to be filled
	 * @param in input stream
	 * @throws BackingStoreException when the stream contains errors
	 */
	protected void load(IPreferences node, InputStream in) throws BackingStoreException {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		try {
			String s = null;
			while ((s = r.readLine()) != null) {
				String l[] = s.split("=", 2);
				put(node, l[0].trim(), l[1].trim());
			}
		} catch (IOException e) {
			throw new BackingStoreException(e);
		} finally {
			try {
				r.close();
			} catch (IOException e) { } // Ignore 
		}
	}

	/**
	 * Stores the key at the appropriate sub-node.
	 * @param node node
	 * @param key path of key (including node)
	 * @param value value of key
	 */
	protected void put(IPreferences node, String key, String value) {
		if (key.length() == 0) return;
		
		// split off any node name
		String l[] = key.split("\\/");
		if (l.length > 1) {
			node = node.node(CommonUtils.join("/", l, 0, l.length-2));
		}
		node.put(l[l.length-1], value);
	}
	
	/**
	 * Saves the node information into the stream
	 * @param node node to be saved
	 * @param out output stream
	 * @throws BackingStoreException when the stream produces errors
	 */
	protected void save(IPreferences node, OutputStream out) throws BackingStoreException {
		PrintWriter w = new PrintWriter(out);
		save(null, node, w);
		w.close();
	}

	/**
	 * Recursively stores the given node into the writer.
	 * @param prefix prefix to be used for keys (can be null)
	 * @param node node to be written
	 * @param out print writer
	 * @throws BackingStoreException when a node cannot be written
	 */
	protected void save(String prefix, IPreferences node, PrintWriter out) throws BackingStoreException {
		try {
			for (String k : node.keys()) {
				if (prefix != null) out.print(prefix+"/");
				out.println(k+"="+node.get(k, ""));
			}
			for (String n : node.childrenNames()) {
				String nodePrefix = prefix != null ? prefix+"/"+n : n;
				save (nodePrefix, node.node(n), out);
			}
		} catch (BackingStoreException e) {
			throw e;
		} catch (Exception e) {
			throw new BackingStoreException(e);
		} 
	}

	/**
	 * Returns the home directory of the user.
	 * @return the user home dir
	 */
	protected File getUserHome() {
		if (userHome == null) {
			userHome = new File(System.getProperty("user.home"));
		}
		return userHome;
	}

	/**
	 * Returns the application's preferences file of the user.
	 * @return the preferences file
	 */
	protected File getUserPreferencesFile(String applicationName) {
		return new File(new File(getUserHome(), "."+applicationName), "user.prefs");
	}

	/**
	 * Returns the application's preferences file of the system.
	 * @return the preferences file
	 */
	protected File getSystemPreferencesFile(String applicationName) {
		return new File(new File(getSystemHome(), applicationName), "system.prefs");
	}

	/**
	 * Returns the system home dir to store system prefs at.
	 * @return the system home dir (usually /var or C:\Users\AllUsers)
	 */
	protected File getSystemHome() {
		if (systemHome == null) {
			if (CommonUtils.isWindows()) {
				systemHome = new File("C:/Documents and Settings/All Users");
			} else if (CommonUtils.isMac()) {
				systemHome = new File("/var");
			} else if (CommonUtils.isUnix()) {
				systemHome = new File("/var");
			} else {
				systemHome = getUserHome();
			}
		} 
		return systemHome; 
	}
}
