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
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class for generating key pairs.
 * @author ralph
 *
 */
public class KeyGen {

	public static final String RSA_KEY_ALGORITHM     = "RSA";
	public static final String ECS_KEY_ALGORITHM     = "EC";
	public static final String DEFAULT_KEY_ALGORITHM = "EC";

	public static final String DEFAULT_RANDOM_ALGORITHM = "SHA1PRNG";

	private static Logger log = LoggerFactory.getLogger(KeyGen.class);
	
	/**
	 * Main method for encryption on command line.
	 * @param seed the seed for the random process
	 * @return the key pair
	 */
	public static KeyPair generateFromSeed(String seed) {
		try {
			if (seed == null) seed = EncryptionUtils.generatePassword(8);
			
			KeyPair keyPair = EncryptionUtils.generateKey(seed.getBytes(StandardCharsets.UTF_8));
			log(keyPair);
			return keyPair;
		} catch (Throwable t) {
			throw new RuntimeException(t.getMessage(), t);
		}
	}

	public static void log(KeyPair keyPair) {
		String privateKey = EncryptionUtils.encodeBase64(keyPair.getPrivate());
		String publicKey  = EncryptionUtils.encodeBase64(keyPair.getPublic());
		log.info("Private Key: "+privateKey);
		log.info("Public Key : "+publicKey);
	}
	
	public static void print(KeyPair keyPair) {
		String privateKey = EncryptionUtils.encodeBase64(keyPair.getPrivate());
		String publicKey  = EncryptionUtils.encodeBase64(keyPair.getPublic());
		System.out.println("Private Key: "+privateKey);
		System.out.println("Public Key : "+publicKey);
	}
	
	public static KeyPair generateKeyPair() throws InvalidKeySpecException, NoSuchAlgorithmException {
		return generateKeyPair(DEFAULT_KEY_ALGORITHM);
	}

	public static KeyPair generateKeyPair(String algorithm) throws InvalidKeySpecException, NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
		int keySize = getPreferredKeySize(algorithm);
		generator.initialize(keySize, generateSecureRandom());
		return generateKeyPair(generator);
	}

	public static int getPreferredKeySize(String algorithm) {
		switch (algorithm) {
		case "RSA" : return 4096;
		case "EC"  : return 256;
		case "DSA" : return 1024;
		}
		return 1024;
	}
	
	public static KeyPair generateKeyPair(KeyPairGenerator generator) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return generator.generateKeyPair();
	}
	
	public static SecureRandom generateSecureRandom() throws NoSuchAlgorithmException {
		return generateSecureRandom(DEFAULT_RANDOM_ALGORITHM);
	}

	public static SecureRandom generateSecureRandom(String algorithm) throws NoSuchAlgorithmException {
		return generateSecureRandom(algorithm, EncryptionUtils.generateSalt());
	}
	
	public static SecureRandom generateSecureRandom(String algorithm, byte seed[]) throws NoSuchAlgorithmException {
		SecureRandom     random    = SecureRandom.getInstance(algorithm);
		random.setSeed(seed);
		return random;
	}
	
	public static void main(String args[]) {
		try {
			String types[] = new String[] { "KeyFactory" };
			//String types[] = new String[] { "KeyGenerator", "KeyFactory", "SecretKeyFactory" };
			for (String type : types) {
				System.out.println("Available "+type+"s:");
				System.out.println();
				for (Provider provider : Security.getProviders()) {
					for (Service service : provider.getServices()) {
						if (type.equals(service.getType())) System.out.println("   - "+service.getAlgorithm());
					}
				}
				System.out.println();
			}
			System.out.println("Generating key pair with: "+DEFAULT_KEY_ALGORITHM+" ("+getPreferredKeySize(DEFAULT_KEY_ALGORITHM)+" bits)");
			System.out.println();
			KeyPair keyPair = KeyGen.generateKeyPair();
			print(keyPair);
		} catch (Throwable t) {
			log.error("Error", t);
		}
	}
}
