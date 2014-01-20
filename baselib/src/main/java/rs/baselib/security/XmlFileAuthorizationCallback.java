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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * Implements authorization information retrieval from a XML file.
 * <p>
 * Configuration:
 * </p>
 * <pre>
 * &lt;AuthorizationCallback class="rs.baselib.security.XmlFileAuthorizationCallback"&gt;
 * 
 *    &lt;!-- The XML file name. --&gt;
 *    &lt;File&gt;/path/to/file.xml&lt;/File&gt;
 *    
 * &lt;/AuthorizationCallback&gt;
 * </pre>
 * <p>The XML file itself shall look like this:</p>
 * <pre>
 * &lt;?xml version="1.0" encoding="ISO-8859-1"?&gt;
 * &lt;authorization&gt;
 *        &lt;login&gt;your-login&lt;/login&gt;
 *        &lt;password&gt;your-password&lt;/password&gt;
 * &lt;/authorization&gt;
 * </pre>
 * @author Ralph Schuster
 *
 */
public class XmlFileAuthorizationCallback extends DefaultAuthorizationCallback {

	/**
	 * Default Constructor.
	 */
	public XmlFileAuthorizationCallback() {
	}

	/**
	 * Configures the callback.
	 * Configuration takes place from a XML file whose
	 * path is contained within element &lt;File&gt;. The
	 * XML file itself must contain two elements &lt;login&gt;
	 * and &lt:password&gt;.
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		String path = config.getString("File");
		configure(path);
	}

	/**
	 * Configures the callback from the XML file.
	 * @param file filename
	 * @throws ConfigurationException
	 */
	public void configure(String file) throws ConfigurationException {
		configure(new File(file));
	}
	
	/**
	 * Configures the callback from the XML file.
	 * @param file file
	 * @throws ConfigurationException
	 */
	public void configure(File file) throws ConfigurationException {
		XMLConfiguration xmlConfig = new XMLConfiguration(file);
		super.configure(xmlConfig);
	}
}
