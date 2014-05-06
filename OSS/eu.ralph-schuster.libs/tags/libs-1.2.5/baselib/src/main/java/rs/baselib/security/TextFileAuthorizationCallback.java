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
package rs.baselib.security;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;

import rs.baselib.configuration.IConfigurable;

/**
 * Implements authorization information retrieval from a plain text file.
 * <p>
 * Configuration:
 * </p>
 * <pre>
 * &lt;AuthorizationCallback class="rs.baselib.security.TextFileAuthorizationCallback"&gt;
 * 
 *    &lt;!-- The text file name. --&gt;
 *    &lt;File&gt;/path/to/file.txt&lt;/File&gt;
 *    
 * &lt;/AuthorizationCallback&gt;
 * </pre>
 * <p>The text file itself shall look like this:</p>
 * <pre>
 * your-login:your-password
 * </pre>
 * @author Ralph Schuster
 *
 */
public class TextFileAuthorizationCallback extends AbstractAuthorizationCallback implements IConfigurable {

	/**
	 * Default Constructor.
	 */
	public TextFileAuthorizationCallback() {
	}

	/**
	 * Configures the callback.
	 * Configuration takes place from a plain text filewhose
	 * path is contained within element &lt;File&gt;. 
	 * The text file itself must contain login and password 
	 * as its only content. Syntax is &lt;login&gt;:&lt;password&gt;
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		String path = config.getString("File");
		configure(path);
	}

	/**
	 * Configures the callback from the text file.
	 * @param file filename
	 * @throws ConfigurationException
	 */
	public void configure(String file) throws ConfigurationException {
		configure(new File(file));
	}
	
	/**
	 * Configures the callback from the text file.
	 * @param file file
	 * @throws ConfigurationException
	 */
	public void configure(File file) throws ConfigurationException {
		try {
			String s = FileUtils.readFileToString(file);
			int pos = s.indexOf(':');
			if (pos > 0) {
				setName(s.substring(0, pos));
				setPassword(s.substring(pos+1));
			} else {
				throw new ConfigurationException("Invalid text file format: "+file.getAbsolutePath());
			}
		} catch (IOException e) {
			throw new ConfigurationException("Cannot read file", e);
		}
	}
}
