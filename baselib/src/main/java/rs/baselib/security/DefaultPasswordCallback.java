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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.ArrayUtils;

import rs.baselib.configuration.IConfigurable;

/**
 * Returns the password given in configuration.
 * @author ralph
 *
 */
public class DefaultPasswordCallback implements IPasswordCallback, IConfigurable {

	private char[] password;
	
	/**
	 * Constructor.
	 */
	public DefaultPasswordCallback() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {
		if (cfg != null) {
			String s = cfg.getString("password(0)");
			if (s != null) password = s.toCharArray();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public char[] getPassword() {
		return password;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getSalt() {
		try {
			byte b[] = new String(getPassword()).getBytes("UTF-8");
			while (b.length < 8) b = ArrayUtils.addAll(b, b);
			if (b.length > 8) b = ArrayUtils.subarray(b, 0, 8);
			return b;
		} catch (Exception e) {
			throw new RuntimeException("Cannot create salt from passphrase", e);
		}
	}

	
}
