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
package rs.baselib.lang;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

/**
 * List resources available from the classpath.
 * 
 * @author ralph
 */
public class ResourceList{

	private static String classPath = System.getProperty("java.class.path", ".");
	private static String classPathElements[] = classPath.split(File.pathSeparator);
	/**
	 * Returns all manifest files found.
	 * @return the manifests in the order they are found
	 */
	public static Collection<Manifest> getManifests() {
		List<Manifest> rc = new ArrayList<Manifest>();
		for (String element : classPathElements) {
			rc.addAll(getManifests(element));
		}
		return rc;
	}

	private static Collection<Manifest> getManifests(String fileName) {
		List<Manifest> rc = new ArrayList<Manifest>();
		try {
			File file = new File(fileName);
			if (!file.isDirectory()) {
				rc.add(getManifest(file));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rc;
	}

	private static Manifest getManifest(File file) throws IOException {
		@SuppressWarnings("resource")
		JarFile jarFile = new JarFile(file);
		Manifest rc = jarFile.getManifest();
		jarFile.close();
		return rc;
	}

	/**
	 * Returns all JAR files found.
	 * @return the JARs in the order they are found
	 */
	public static Collection<JarDescriptor> getJars() {
		List<JarDescriptor> rc = new ArrayList<JarDescriptor>();
		for (String element : classPathElements) {
			rc.addAll(getJars(element));
		}
		return rc;
	}

	private static Collection<JarDescriptor> getJars(String fileName) {
		List<JarDescriptor> rc = new ArrayList<JarDescriptor>();
		try {
			File file = new File(fileName);
			if (!file.isDirectory()) {
				rc.add(new JarDescriptor(file));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rc;
	}

	/**
	 * Returns all directories found.
	 * @return the directories in the order they are found
	 */
	public static Collection<File> getDirectories() {
		List<File> rc = new ArrayList<File>();
		for (String element : classPathElements) {
			rc.addAll(getDirectories(element));
		}
		return rc;
	}

	private static Collection<File> getDirectories(String fileName) {
		List<File> rc = new ArrayList<File>();

		File file = new File(fileName);
		if (file.isDirectory()) {
			rc.add(file);
		}

		return rc;
	}

	/**
	 * for all elements of java.class.path get a Collection of resources Pattern
	 * pattern = Pattern.compile(".*"); gets all resources
	 * 
	 * @param pattern
	 *            the pattern to match
	 * @return the resources in the order they are found
	 */
	public static Collection<URL> getResources(Pattern pattern) {
		List<URL> rc = new ArrayList<URL>();
		for (String element : classPathElements) {
			rc.addAll(getResources(element, pattern));
		}
		return rc;
	}

	private static Collection<URL> getResources(String element, Pattern pattern) {
		List<URL> rc = new ArrayList<URL>();
		try {
			File file = new File(element);
			if (file.isDirectory()) {
				rc.addAll(getResourcesFromDirectory(file, pattern));
			} else {
				rc.addAll(getResourcesFromJarFile(file, pattern));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rc;
	}

	private static Collection<URL> getResourcesFromJarFile(File file, Pattern pattern) throws IOException {
		List<URL> rc = new ArrayList<URL>();
		JarFile jarFile = new JarFile(file);

		try {
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String fileName = entry.getName();
				boolean accept = pattern.matcher(fileName).matches();
				if (accept) {
					rc.add(new URL("jar:file:"+file.getCanonicalPath()+"!/"+fileName));
				}
			}
		} finally {
			jarFile.close();
		}
		return rc;
	}

	private static Collection<URL> getResourcesFromDirectory(File directory, Pattern pattern) {
		List<URL> retval = new ArrayList<URL>();
		File[] fileList = directory.listFiles();

		for (File file : fileList) {
			if (file.isDirectory()) {
				retval.addAll(getResourcesFromDirectory(file, pattern));
			} else {
				try {
					String fileName = file.getCanonicalPath();
					boolean accept = pattern.matcher(fileName).matches();
					if (accept) {
						retval.add(new File(fileName).toURI().toURL());
					}
				} catch (IOException e) {
					throw new Error(e);
				}
			}
		}
		return retval;
	}

}  