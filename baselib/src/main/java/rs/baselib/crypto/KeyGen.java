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

import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import rs.baselib.crypto.cli.KeyGenCli;

/**
 * The class for generating key pairs.
 * @author ralph
 *
 */
public class KeyGen {

	public static final String RSA_KEY_ALGORITHM     = "RSA";
	public static final String ECS_KEY_ALGORITHM     = "EC";
	
	/** Default key generating algorithm */
	public static final String DEFAULT_KEY_ALGORITHM = "EC";

	private static Logger log = LoggerFactory.getLogger(KeyGen.class);
	
	/**
	 * Generates a secret key (PBE) based on the given parameters.
	 * @param iterationCount the cumber of iterations (if less than 1 then {@link EncryptionUtils#DEFAULT_ITERATIONS} will be used)
	 * @param passPhrase the passphrase (required)
	 * @param salt the slat (can be null)
	 * @return a secret key based on parameters
	 * @throws NoSuchAlgorithmException when the algorithm does not exist
	 * @throws InvalidKeySpecException when the key spec is invalid
	 */
	public static SecretKey generateSecretKey(int iterationCount, String passPhrase, byte salt[]) throws NoSuchAlgorithmException, InvalidKeySpecException {
		if (iterationCount < 1) iterationCount = EncryptionUtils.DEFAULT_ITERATIONS;
		if (salt == null) salt = EncryptionUtils.generateSalt(0);
		KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
		SecretKey key = SecretKeyFactory.getInstance(EncryptionUtils.DEFAULT_SECRET_KEY_TYPE).generateSecret(keySpec);
		return key;
	}

	/**
	 * Generates a public/private key pair.
	 * @param seed seed to be used.
	 * @return key pair
	 * @throws NoSuchProviderException when the algorithm provider does not exist
	 * @throws NoSuchAlgorithmException when the algorithm does not exist
	 */
	public static KeyPair generateKeyPairBySeed(String seed) throws NoSuchProviderException, NoSuchAlgorithmException {
		return generateKeyPairBySeed(seed, null);
	}

	/**
	 * Generates a public/private key pair.
	 * @param seed seed to be used.
	 * @param charset the charset to be used for string encoding (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @return key pair
	 * @throws NoSuchProviderException when the algorithm provider does not exist
	 * @throws NoSuchAlgorithmException when the algorithm does not exist
	 */
	public static KeyPair generateKeyPairBySeed(String seed, Charset charset) throws NoSuchProviderException, NoSuchAlgorithmException {
		if (charset == null) charset = Charset.defaultCharset();
		return generateKeyPairBySeed(seed.getBytes(charset));
	}

	/**
	 * Generates a public/private DSA key pair.
	 * @param seed seed to be used.
	 * @return key pair
	 * @throws NoSuchProviderException when the algorithm provider does not exist
	 * @throws NoSuchAlgorithmException when the algorithm does not exist
	 */
	public static KeyPair generateKeyPairBySeed(byte seed[]) throws NoSuchProviderException, NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
		SecureRandom random = EncryptionUtils.generateSecureRandom(seed);
		keyGen.initialize(1024, random);
		KeyPair pair = keyGen.generateKeyPair();
		return pair;
	}

	/**
	 * Log a key pair using the SLF4J {@link Logger}.
	 * 
	 * @param keyPair key pair to be logged
	 */
	public static void log(KeyPair keyPair) {
		log.info("Private Key: "+EncryptionUtils.encodeBase64(keyPair.getPrivate()));
		log.info("Public Key : "+EncryptionUtils.encodeBase64(keyPair.getPublic()));
	}
	
	/**
	 * Log a key pair using the {@link System#out} stream.
	 * 
	 * @param keyPair key pair to be logged
	 */
	public static void print(KeyPair keyPair) {
		System.out.println("Private Key: "+EncryptionUtils.encodeBase64(keyPair.getPrivate()));
		System.out.println("Public Key : "+EncryptionUtils.encodeBase64(keyPair.getPublic()));
	}
	
	/**
	 * Generate a key pair using default settings (EC, 256 bit).
	 * @return a new key pair
	 * @throws NoSuchAlgorithmException when the default algorithm does not exist
	 */
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		return generateKeyPair(DEFAULT_KEY_ALGORITHM);
	}

	/**
	 * Generate a key pair using default settings for the given algorithm.
	 * @param algorithm key generation algorithm
	 * @return a new key pair
	 * @see #getPreferredKeySize(String)
	 * @throws NoSuchAlgorithmException when the default algorithm does not exist
	 */
	public static KeyPair generateKeyPair(String algorithm) throws NoSuchAlgorithmException {
		return generateKeyPair(algorithm, getPreferredKeySize(algorithm));
	}
	
	/**
	 * Generate a key pair using default settings for the given algorithm.
	 * @param algorithm key generation algorithm
	 * @return a new key pair
	 * @see #getPreferredKeySize(String)
	 * @throws NoSuchAlgorithmException when the default algorithm does not exist
	 */
	public static KeyPair generateKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
		if (keySize < 10) keySize = getPreferredKeySize(algorithm);
		KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
		generator.initialize(keySize, EncryptionUtils.generateSecureRandom());
		return generateKeyPair(generator);
	}

	/**
	 * Returns preferred key size for the given algorithm.
	 * <p>The method knows about RSA, DSA and EC. All others will receive a 1024 bit key size</p>
	 * @param algorithm the algorithm
	 * @return the preferred key size
	 */
	public static int getPreferredKeySize(String algorithm) {
		switch (algorithm) {
		case "RSA" : return 4096;
		case "EC"  : return 256;
		case "DSA" : return 1024;
		}
		return 1024;
	}
	
	/**
	 * Generates a key pair using the configured generator.
	 * @param generator the key pair generator
	 * @return a new key pair
	 */
	public static KeyPair generateKeyPair(KeyPairGenerator generator) {
		return generator.generateKeyPair();
	}
	
	public static void main(String args[]) {
		try {
			try {
				KeyGenCli options = new KeyGenCli();
				CommandLine cli = new CommandLine(options);
				options.setCli(cli);
				if (cli.isUsageHelpRequested()) {
				    cli.usage(System.out);
				    return;
				} else if (cli.isVersionHelpRequested()) {
				    cli.printVersionHelp(System.out);
				    return;
				}
				int exitCode = cli.execute(args);
		        System.exit(exitCode);
			} catch (Throwable t) {
				LoggerFactory.getLogger(KeyGenCli.class).error("Cannot run CLI.", t);
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
