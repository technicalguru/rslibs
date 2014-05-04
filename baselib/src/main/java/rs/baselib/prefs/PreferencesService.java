/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;

import org.apache.commons.io.Charsets;
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
	private Map<String, File> userHomes;
	private Map<String, File> systemHomes;

	/**
	 * Constructor.
	 */
	private PreferencesService() {
		userHomes = new HashMap<String, File>();
		systemHomes = new HashMap<String, File>();
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
				File parent = f.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new BackingStoreException("Cannot create parent directory: "+f.getParent());
				}
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
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));
			String s = null;
			while ((s = r.readLine()) != null) {
				if (!CommonUtils.isEmpty(s) && !s.startsWith("#")) {
					String l[] = s.split("=", 2);
					if (l.length > 1) {
						put(node, l[0].trim(), l[1].trim());
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new BackingStoreException(e);
		} catch (IOException e) {
			throw new BackingStoreException(e);
		} finally {
			try {
				if (r != null) r.close();
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
	 * {@inheritDoc}
	 */
	@Override
	public File getUserPreferencesHome(String applicationName) {
		return new File(getUserHome(), "."+applicationName);
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
	protected synchronized File getUserPreferencesFile(String applicationName) {
		File rc = userHomes.get(applicationName);
		if (rc == null) {
			rc = new File(getUserPreferencesHome(applicationName), "user.prefs");
			userHomes.put(applicationName, rc);
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getSystemPreferencesHome(String applicationName) {
		File home = getSystemHome();
		File parentDir = new File(home, applicationName);
		if (!parentDir.exists() && !home.canWrite()) {
			log.info("Cannot write to "+home.getAbsolutePath()+" - using user home directory for system preferences");
			parentDir = getUserPreferencesHome(applicationName);
		} else if (parentDir.exists() && !parentDir.canWrite()) {
			log.info("Cannot write to "+parentDir.getAbsolutePath()+" - using user home directory for system preferences");
			parentDir = getUserPreferencesHome(applicationName);
		}
		return parentDir;
	}

	/**
	 * Returns the application's preferences file of the system.
	 * @return the preferences file
	 */
	protected synchronized File getSystemPreferencesFile(String applicationName) {
		File rc = systemHomes.get(applicationName);
		if (rc == null) {
			File parentDir = getSystemPreferencesHome(applicationName);
			rc = new File(parentDir, "system.prefs");
			if (rc.exists() && !rc.canWrite()) {
				log.info("Cannot write to "+rc.getAbsolutePath()+" - using user home directory for system preferences");
				parentDir = getUserPreferencesHome(applicationName);
			}
			rc = new File(parentDir, "system.prefs");
			systemHomes.put(applicationName, rc);
		}
		return rc;
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
			}

			if (systemHome == null) {
				log.info("Cannot determine OS type from \""+CommonUtils.getOS()+"\" - using user home directory for system preferences");
				systemHome = getUserHome();
			}
		} 
		return systemHome; 
	}
}
