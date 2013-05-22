/**
 * 
 */
package rsbaselib.crypto;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic function for helping in encryption.
 * @author ralph
 *
 */
public class EncryptionUtils {

	private static Logger log = LoggerFactory.getLogger(EncryptionUtils.class);

	/**
	 * The default secret key type to be generated from a password.
	 */
	public static final String DEFAULT_SECRET_KEY_TYPE = "PBEWithMD5AndDES";

	/**
	 * The default number of iterations to be executed when creating the encrypting algorithm.
	 */
	public static final int DEFAULT_ITERATIONS = 19;

	/**
	 * The default number of iterations to be executed when creating the encrypting algorithm.
	 */
	public static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"&/()=?;*+'#;,:._-<>";

	/**
	 * Creates a PBE Parameter specification randomly.
	 * @see #generateParamSpec(byte[], int)
	 * @return the PBE param spec
	 */
	public static PBEParameterSpec generateParamSpec() {
		return generateParamSpec(null, 0);
	}

	/**
	 * Creates a PBE Parameter Spec for encryption algorithm.
	 * @param salt salt to be used (if NULL, a random one will be generated)
	 * @param iterationCount number of iterations for encryption (if 0 then default number will be used)
	 * @return the generated PBE random spec
	 * @see #DEFAULT_ITERATIONS
	 * @see #generateSalt(long)
	 */
	public static PBEParameterSpec generateParamSpec(byte salt[], int iterationCount) {
		if (salt == null) salt = generateSalt(System.currentTimeMillis());
		if (iterationCount < 1) iterationCount = DEFAULT_ITERATIONS;
		return new PBEParameterSpec(salt, iterationCount);
	}

	/**
	 * Creates a random salt array.
	 * @return the random salt
	 */
	public static byte[] generateSalt() {
		return generateSalt(0);
	}

	/**
	 * Creates a random salt array.
	 * @param randomInit initializer
	 * @return the random salt
	 */
	public static byte[] generateSalt(long randomInit) {
		return generateRandomBytes(randomInit, 8);
	}

	/**
	 * Generate a random array of bytes
	 * @param randomInit initializer
	 * @param size size of returned array
	 * @return random byte array
	 */
	public static byte[] generateRandomBytes(long randomInit, int size) {
		if (randomInit == 0) randomInit = System.currentTimeMillis();
		Random random = new Random(randomInit);
		byte rc[] = new byte[size];
		random.nextBytes(rc);
		return rc;
	}

	/**
	 * Creates a random password.
	 * @return the random password
	 */
	public static String generatePassword() {
		return generatePassword(null, 0, 0);
	}

	/**
	 * Creates a random password.
	 * @param length length of password
	 * @return the random password
	 */
	public static String generatePassword(int length) {
		return generatePassword(null, 0, length);
	}

	/**
	 * Creates a random password.
	 * @param allowedChars all characters allowed
	 * @return the random password
	 */
	public static String generatePassword(String allowedChars) {
		return generatePassword(allowedChars, 0, 0);
	}

	/**
	 * Creates a random password.
	 * @param allowedChars allowedCharacters
	 * @param randomInit initializer
	 * @param length length of password
	 * @return the random password
	 */
	public static String generatePassword(String allowedChars, long randomInit, int length) {
		if ((allowedChars == null) || (allowedChars.trim().length() == 0)) allowedChars = PASSWORD_CHARS;
		if (randomInit == 0) randomInit = System.currentTimeMillis();
		if (length < 1) length = DEFAULT_ITERATIONS;

		Random random = new Random(randomInit);

		String rc = "";
		while (rc.length() < length) {
			int n = random.nextInt(allowedChars.length());
			char c = allowedChars.charAt(n);
			if (!Character.isWhitespace(c)) rc += c;
		}

		return rc;
	}

	public static SecretKey getSecretKey(int iterationCount, String passPhrase, byte salt[]) throws NoSuchAlgorithmException, InvalidKeySpecException {
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
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKey(String seed) throws NoSuchProviderException, NoSuchAlgorithmException {
		return generateKey(seed.getBytes());
	}

	/**
	 * Generates a public/private key pair.
	 * @param seed seed to be used.
	 * @return key pair
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKey(byte seed[]) throws NoSuchProviderException, NoSuchAlgorithmException {
		if (seed == null) seed = generateSalt();
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(seed);
		keyGen.initialize(1024, random);
		KeyPair pair = keyGen.generateKeyPair();
		return pair;
	}

	/**
	 * Encodes the public key into BASE64 representation.
	 * @param key public key
	 * @return BASE64 representation of key
	 */
	public static String encodeBase64(PublicKey key) {
		byte b[] = key.getEncoded();
		return encodeBase64(b);
	}

	/**
	 * Encodes the private key into BASE64 representation.
	 * @param key private key
	 * @return BASE64 representation of key
	 */
	public static String encodeBase64(PrivateKey key) {
		byte b[] = key.getEncoded();
		return encodeBase64(b);
	}

	/**
	 * Decodes a public key from the BASE64 representation.
	 * @param s BASE64 representation
	 * @return public key
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static PublicKey decodeBase64PublicKey(String s) throws InvalidKeySpecException, NoSuchAlgorithmException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeBase64(s));
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * Decodes a public key from the BASE64 representation.
	 * @param s BASE64 representation
	 * @return public key
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static PrivateKey decodeBase64PrivateKey(String s) throws InvalidKeySpecException, NoSuchAlgorithmException {
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodeBase64(s));
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		return keyFactory.generatePrivate(privateKeySpec);
	}

	/**
	 * Decodes a BASE64 string and generates the original
	 * string from the result.
	 * @param s BASE64 encoded string
	 * @return original string
	 * @see #encodeBase64(String)
	 */
	public static String decodeBase64ToString(String s) {
		return new String(decodeBase64(s));
	}

	/**
	 * Decodes a BASE64 string into bytes.
	 * @param s BASE64 encoded string
	 * @return bytes that were encoded.
	 */
	public static byte[] decodeBase64(String s) {
		return Base64.decodeBase64(s);
	}

	/**
	 * Encodes a string into its BASE64 representation.
	 * The string is actually split into its bytes and then
	 * BASE64 encoded.
	 * @param s string to encode
	 * @return BASE64 representation
	 */
	public static String encodeBase64(String s) {
		return encodeBase64(s.getBytes());
	}

	/**
	 * Encodes a byte array into its BASE64 representation.
	 * @param b bytes to encode
	 * @return BASE64 representation
	 */
	public static String encodeBase64(byte b[]) {
		return Base64.encodeBase64String(b).trim();
	}

	/**
	 * Load the default keystore type.
	 * @param filename filename
	 * @param password password
	 * @return the key store loaded
	 */
	public static KeyStore getKeyStore(String filename, char password[]) throws IOException {
		return getKeyStore(KeyStore.getDefaultType(), new FileInputStream(filename), password);
	}

	/**
	 * Load the default keystore type.
	 * @param filename filename
	 * @param password password
	 * @return the key store loaded
	 */
	public static KeyStore getKeyStore(String type, String filename, char password[]) throws IOException {
		log.debug("Reading "+type+" keystore: "+filename);
		return getKeyStore(type, new FileInputStream(filename), password);
	}

	/**
	 * Load the default keystore type.
	 * @param in input stream
	 * @param password password
	 * @return the key store loaded
	 */
	public static KeyStore getKeyStore(InputStream in, char password[]) throws IOException {
		return getKeyStore(KeyStore.getDefaultType(), in, password);
	}

	/**
	 * Load the given keystore.
	 * @param type of keystore
	 * @param in input stream
	 * @param password password
	 * @return the key store loaded
	 */
	public static KeyStore getKeyStore(String type, InputStream in, char password[]) throws IOException {
		try {
			KeyStore ks = KeyStore.getInstance(type);

			ks.load(in, password);
			in.close();
			return ks;
		} catch (KeyStoreException e) {
			throw new RuntimeException("Cannot load key store", e);
		} catch (CertificateException e) {
			throw new RuntimeException("Cannot load key store", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Cannot load key store", e);
		}
	}

	/**
	 * Transforms all bytes from the input array with a crypt alorithm
	 * and returns the transformed bytes.
	 * @param bytes bytes to crypt
	 * @param cipher crypt alorithm used
	 * @return crypted bytes
	 */
	public static byte[] crypt(byte bytes[], Cipher cipher) throws IOException, GeneralSecurityException {
		return crypt(bytes, cipher, 0);
	}

	/**
	 * Transforms all bytes from the input array with a crypt alorithm
	 * and returns the transformed bytes.
	 * @param bytes bytes to crypt
	 * @param cipher crypt alorithm used
	 * @param blockSize block size to be applied (or 0 if none)
	 * @return crypted bytes
	 */
	public static byte[] crypt(byte bytes[], Cipher cipher, int blockSize) throws IOException, GeneralSecurityException {
		if ((blockSize == 0) || (bytes.length <= blockSize)) {
			return cipher.doFinal(bytes);
		} else {
			// Compute the length of the return buffer
			int firstOutputLength = cipher.getOutputSize(blockSize);
			int lastBlockSize = bytes.length % blockSize;
			int lastOutputLength = cipher.getOutputSize(lastBlockSize);
			int firstBlockCount = bytes.length / blockSize;
			int outputLen = firstOutputLength * firstBlockCount + lastOutputLength;

			ByteArrayOutputStream out = new ByteArrayOutputStream(outputLen);
			for (int i=0; i<firstBlockCount; i++) {
				log.debug("crypt.doFinal("+(i*blockSize)+", "+blockSize+")");
				out.write(cipher.doFinal(bytes, i*blockSize, blockSize));
			}
			if (lastBlockSize > 0) {
				log.debug("crypt.doFinal("+(firstBlockCount*blockSize*blockSize)+", "+lastBlockSize+")");
				out.write(cipher.doFinal(bytes, firstBlockCount*blockSize, lastBlockSize));
			}
			byte rc[] = out.toByteArray();
			out.close();
			return rc;
		}
	}

	/**
	 * Creates an MD5 hash from the string.
	 * @param s string to create the hash from
	 * @return the MD5 hash
	 */
	public static byte[] createMD5(String s) {
		byte b[] = null;
		try {
			b = s.getBytes("UTF-8");
		} catch (Throwable t) {
			throw new RuntimeException("Cannot get UTF8 bytes from \""+s+"\".", t);
		}
		return createMD5(b);
	}

	/**
	 * Creates an MD5 hash from the byte array.
	 * @param b bytes to create the hash from
	 * @return the MD5 hash
	 */
	public static byte[] createMD5(byte b[]) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(b);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot hash string", t);
		}
	}
}
