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
package rs.baselib.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.CommonUtils;

/**
 * Utility to find a file on disk.
 * @see #find(Class, String)
 * @author RalphSchuster
 *
 */
public class FileFinder {

	private static Logger log = LoggerFactory.getLogger(FileFinder.class);
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @return URL to the file
	 * @see #find(Class, String)
	 */
	public static URL find(String name) {
		return find(null, name);
	}
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * <p>The file will be searched based on the following procedure:</p>
	 * <ul>
	 * <li>Try to find the file in current working dir (unless absolute path is given).</li>
	 * <li>Try to fine the file in package of the class given as argument using the default class loader</li>
	 * <li>Try to find the file in parent packages of the class given as argument using the default class loader</li>
	 * <li>Try to fine the file in package of the class given as argument using the context class loader</li>
	 * <li>Try to find the file in parent packages of the class given as argument using the context class loader</li>
	 * <li>Repeat the procedure by trying to find the file with a prepended slash.</li>
	 * </ul>
	 * 
	 * @param name - name of file, can be fully qualified
	 * @param clazz class to get the class loader from
	 * @return URL to the file
	 */
	public static URL find(Class<?> clazz, String name) {
		URL rc = null;
		if (clazz == null) clazz = FileFinder.class;
		
		// try to find as simple file in file system
		try {
			File f = new File(name);
			if (f.exists() && f.isFile() && f.canRead()) {
				rc = f.toURI().toURL();
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) 
				log.debug("No such local file: "+name, e);
		}
		
		// Create dirs
		String dirs[] = null;
		if (!name.startsWith("/")) {
			dirs = clazz.getPackage().getName().split("\\.");
		}

		// get it from class' class loader
		if ((rc == null) && (clazz != null)) {
				ClassLoader loader = clazz.getClassLoader();
				rc = find(loader, dirs, name);
		}
		
		// Not yet found? Use the threads class loader
		if (rc == null) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			if (loader != null) rc = find(loader, dirs, name);
		}
		
		if (log.isDebugEnabled()) {
			log.debug(name+" located at "+rc);
		}
		if (rc == null) {
			// Try with prepended slash if possible
			if (!name.startsWith("/") && !name.startsWith(".")) {
				rc = find(clazz, "/"+name);
			}
		}
		return rc;
	}
	
	/**
	 * Find the resource using the given class loader.
	 * @param classLoader the loader to be used
	 * @param dirs the directory parts (e.g. from package name)
	 * @param name the name of the resource
	 * @return the URL of the resource if found
	 */
	private static URL find(ClassLoader classLoader, String dirs[], String name) {
		URL rc = null;
		if (dirs != null) {
			try {
				if (!name.startsWith("/")) {
					for (int i=dirs.length; i>0; i--) {
						String pkgDir = StringUtils.join(dirs, '/', 0, i);
						rc = classLoader.getResource(pkgDir+"/"+name);
						if (rc != null) break;
					}
				}
			} catch (Exception e) {
				if (log.isDebugEnabled()) 
					log.debug("No such classpath file: "+name, e);
			}
		}
		if (rc == null) {
			rc = classLoader.getResource(name);
		}
		return rc;
	}
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @return stream to the file
	 * @throws IOException when file cannot be opened
	 */
	public static InputStream open(String name) throws IOException {
		return open(null, name);
	}
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @param clazz class to get the class loader from
	 * @return stream to the file
	 * @see #find(Class, String)
	 * @throws IOException when file cannot be opened
	 */
	public static InputStream open(Class<?> clazz, String name) throws IOException {
		// try to find as simple file in file system
		URL url = find(clazz, name);
		if (url != null) return url.openStream();
		
		return null;
	}
	
	/**
	 * Finds and loads a file.
	 * @param name - name of file
	 * @return contents of file
	 * @see #find(Class, String)
	 * @throws IOException when file cannot be opened
	 */
	public static String load(String name) throws IOException {
		return load(null, name);
	}
		
	/**
	 * Finds and loads a file.
	 * @param name - name of file
	 * @param clazz class to get the class loader from
	 * @see #find(Class, String)
	 * @return contents of file
	 * @throws IOException when file cannot be opened
	 */
	public static String load(Class<?> clazz, String name) throws IOException {
		InputStream in = open(clazz, name);
		return CommonUtils.loadContent(in);
	}
		
}
