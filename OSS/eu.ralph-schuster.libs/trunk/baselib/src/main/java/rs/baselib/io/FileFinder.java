/**
 * 
 */
package rs.baselib.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to find a file on disk.
 * @author RalphSchuster
 *
 */
public class FileFinder {

	private static Logger log = LoggerFactory.getLogger(FileFinder.class);
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @return URL to the file
	 */
	public static URL find(String name) {
		return find(null, name);
	}
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
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
		
		// get it from classpath
		if (rc == null) {
			try {
				ClassLoader loader = clazz.getClassLoader();
				rc = loader.getResource(name);
			} catch (Exception e) {
				if (log.isDebugEnabled()) 
					log.debug("No such classpath file: "+name, e);
			}
		}
		
		if ((rc == null) && (clazz != null)) {
			try {
				ClassLoader loader = clazz.getClassLoader();
				rc = loader.getResource(name);
			} catch (Exception e) {
				if (log.isDebugEnabled()) 
					log.debug("No such classpath file: "+name, e);
			}
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
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @return stream to the file
	 */
	public static InputStream open(String name) throws IOException {
		return open(null, name);
	}
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @param clazz class to get the class loader from
	 * @return stream to the file
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
	 */
	public static String load(String name) throws IOException {
		return load(null, name);
	}
		
	/**
	 * Finds and loads a file.
	 * @param name - name of file
	 * @param clazz class to get the class loader from
	 * @return contents of file
	 */
	public static String load(Class<?> clazz, String name) throws IOException {
		InputStream in = open(clazz, name);
		return load(in);
	}
		
	/**
	 * Loads a file.
	 * @param in - file to load
	 * @return contents of file
	 */
	public static String load(File in) throws IOException {
		return load(new FileInputStream(in));
	}
	
	/**
	 * Loads a file.
	 * @param in - input stream
	 * @return contents of stream
	 */
	public static String load(InputStream in) throws IOException {
		String rc = "";
		if (in != null) {
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			while (r.ready()) {
				rc += r.readLine()+"\n";
			}
			r.close();
		}
		return rc;
	}
}
