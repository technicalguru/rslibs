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
package rs.baselib.crypto;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class for generating key pairs.
 * @author ralph
 *
 */
public class KeyGen {

	private static Logger log = LoggerFactory.getLogger(KeyGen.class);
	
	/**
	 * Main method for encryption on command line.
	 * @param seed the seed for the random process
	 */
	public static KeyPair generateKeyPair(String seed) {
		try {
			if (seed == null) seed = EncryptionUtils.generatePassword(8);
			
			KeyPair keyPair = EncryptionUtils.generateKey(seed.getBytes(StandardCharsets.UTF_8));
			String privateKey = EncryptionUtils.encodeBase64(keyPair.getPrivate());
			String publicKey  = EncryptionUtils.encodeBase64(keyPair.getPublic());
			log.info("Private Key: "+privateKey);
			log.info("Public Key : "+publicKey);
			return keyPair;
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage(), t);
		}
	}

}
