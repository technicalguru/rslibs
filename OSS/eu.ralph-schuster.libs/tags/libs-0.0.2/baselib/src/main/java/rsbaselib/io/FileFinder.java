/**
 * 
 */
package rsbaselib.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility to find a file on disk.
 * @author RalphSchuster
 *
 */
public class FileFinder {

	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @return stream to the file
	 */
	public static URL find(String name) throws MalformedURLException {
		URL rc = null;
		
		// try to find as simple file in file system
		File f = new File(name);
		if (f.exists() && f.isFile() && f.canRead()) {
			rc = f.toURI().toURL();
		}

		// get it from classpath
		if (rc == null) {
				ClassLoader loader = FileFinder.class.getClassLoader();
				rc = loader.getResource(name);
		}
		return rc;
	}
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @return stream to the file
	 */
	public static InputStream open(String name) throws IOException {
		// try to find as simple file in file system
		URL url = find(name);
		if (url != null) return url.openStream();
		
		return null;
	}
	
	/**
	 * Finds and loads a file.
	 * @param name - name of file
	 * @return contents of file
	 */
	public static String load(String name) throws IOException {
		InputStream in = open(name);
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
