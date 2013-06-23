/**
 * 
 */
package rs.data.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Converts names to URLs.
 * @author ralph
 *
 */
public interface URLTransformer {

	/**
	 * Converts the given string to a URL.
	 * @param url url
	 * @return URL
	 */
	public URL toURL(String url) throws MalformedURLException;
	
}
