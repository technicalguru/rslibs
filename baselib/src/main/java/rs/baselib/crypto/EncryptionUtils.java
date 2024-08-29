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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.CommonUtils;

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

	/** Default algorithm for the random seed */
	public static final String DEFAULT_RANDOM_ALGORITHM = "SHA1PRNG";
	
	/**
	 * The default number of iterations to be executed when creating the encrypting algorithm.
	 */
	public static final int DEFAULT_ITERATIONS = 19;

	/**
	 * The default number of iterations to be executed when creating the encrypting algorithm.
	 */
	public static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"&/()=?;*+'#;,:._-<>";

	/** Encoder for line limiting encoding */
	private static Base64 base64 = new Base64(80);
	
	/**
	 * Creates a key specification.
	 * @param key the key 
	 * @return the PBE param spec
	 * @throws InvalidKeySpecException when the key spec is invalid
	 * @throws NoSuchAlgorithmException when the algorithm is invalid
	 */
	public static KeySpec getKeySpec(Key key) throws InvalidKeySpecException, NoSuchAlgorithmException {
		KeyFactory fact = KeyFactory.getInstance(key.getAlgorithm());
		return fact.getKeySpec(key, KeySpec.class);
	}

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

		StringBuilder rc = new StringBuilder(length);
		while (rc.length() < length) {
			int n = random.nextInt(allowedChars.length());
			char c = allowedChars.charAt(n);
			if (!Character.isWhitespace(c)) rc.append(c);
		}

		return rc.toString();
	}
	
	/**
	 * Encodes the public key into BASE64 X.509 representation.
	 * @param key public key
	 * @return BASE64 X.509 representation of key
	 */
	public static String encodeBase64X509(PublicKey key) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key.getEncoded());
		return base64.encodeToString(x509EncodedKeySpec.getEncoded());
	}

	/**
	 * Encodes the key into BASE64 representation.
	 * @param key the key key
	 * @return BASE64 representation of key
	 */
	public static String encodeBase64(Key key) {
		byte b[] = key.getEncoded();
		return encodeBase64(b);
	}

	/**
	 * Encodes the private key into BASE64 PKCS8 representation.
	 * @param key private key
	 * @return BASE64 PKCS8 representation of key
	 */
	public static String encodeBase64Pkcs8(PrivateKey key) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key.getEncoded());
		return base64.encodeToString(pkcs8EncodedKeySpec.getEncoded());
	}

	/**
	 * Decodes a public key (DSA) from the BASE64 representation.
	 * @param s BASE64 representation
	 * @return public DSA key
	 * @throws InvalidKeySpecException - when the key spec is invalid
	 * @throws NoSuchAlgorithmException - when the algorithm is invalid
	 */
	public static PublicKey decodeBase64PublicKey(String s) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return decodeBase64PublicKey(s, "DSA");
	}

	/**
	 * Decodes a public key from the BASE64 representation.
	 * @param s BASE64 representation
	 * @param algorithm name of algorithm
	 * @return public key
	 * @throws InvalidKeySpecException - when the key spec is invalid
	 * @throws NoSuchAlgorithmException - when the algorithm is invalid
	 */
	public static PublicKey decodeBase64PublicKey(String s, String algorithm) throws InvalidKeySpecException, NoSuchAlgorithmException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeBase64(s));
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * Decodes a private DSA key from the BASE64 representation.
	 * @param s BASE64 representation
	 * @return public key
	 * @throws InvalidKeySpecException - when the key spec is invalid
	 * @throws NoSuchAlgorithmException - when the algorithm is invalid
	 */
	public static PrivateKey decodeBase64PrivateKey(String s) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return decodeBase64PrivateKey(s, "DSA");
	}

	/**
	 * Decodes a private key from the BASE64 representation.
	 * @param s BASE64 representation
	 * @param algorithm name of algorithm
	 * @return public key
	 * @throws InvalidKeySpecException - when the key spec is invalid
	 * @throws NoSuchAlgorithmException - when the algorithm is invalid
	 */
	public static PrivateKey decodeBase64PrivateKey(String s, String algorithm) throws InvalidKeySpecException, NoSuchAlgorithmException {
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodeBase64(s));
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
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
		return decodeBase64ToString(s, null);
	}

	/**
	 * Decodes a BASE64 string and generates the original
	 * string from the result.
	 * @param s BASE64 encoded string
	 * @param charset the character set to be used for constructing the string (can be <code>null</code> for default charset)
	 * @return original string
	 * @see #encodeBase64(String)
	 * @since 1.2.5
	 */
	public static String decodeBase64ToString(String s, Charset charset) {
		if (charset == null) charset = Charset.defaultCharset();
		return new String(decodeBase64(s), charset);
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
		return encodeBase64(s, null);
	}

	/**
	 * Encodes a string into its BASE64 representation.
	 * The string is actually split into its bytes and then
	 * BASE64 encoded.
	 * @param s string to encode
	 * @param charset the charset to be used for string encoding (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @return BASE64 representation
	 * @since 1.2.5
	 */
	public static String encodeBase64(String s, Charset charset) {
		if (charset == null) charset = Charset.defaultCharset();
		return encodeBase64(s.getBytes(charset));
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
	 * Generate a secure random using the default algorithm.
	 * @return the random instance
	 * @throws NoSuchAlgorithmException when the algorithm does not exist
	 */
	public static SecureRandom generateSecureRandom() throws NoSuchAlgorithmException {
		return generateSecureRandom(DEFAULT_RANDOM_ALGORITHM);
	}

	/**
	 * Generate a secure random using the default algorithm.
	 * @param seed a given seed to be used
	 * @return the random instance
	 * @throws NoSuchAlgorithmException when the algorithm does not exist
	 */
	public static SecureRandom generateSecureRandom(byte seed[]) throws NoSuchAlgorithmException {
		return generateSecureRandom(DEFAULT_RANDOM_ALGORITHM, seed);
	}

	/**
	 * Generate a secure random using the given algorithm.
	 * @param algorithm the algorithm to be used
	 * @return the random instance
	 * @throws NoSuchAlgorithmException when the algorithm does not exist
	 */
	public static SecureRandom generateSecureRandom(String algorithm) throws NoSuchAlgorithmException {
		return generateSecureRandom(algorithm, EncryptionUtils.generateSalt());
	}
	
	/**
	 * Generate a secure random using the given algorithm and seed.
	 * @param algorithm the algorithm to be used
	 * @param seed the seed to be used
	 * @return the random instance
	 * @throws NoSuchAlgorithmException when the algorithm does not exist
	 */
	public static SecureRandom generateSecureRandom(String algorithm, byte seed[]) throws NoSuchAlgorithmException {
		SecureRandom     random = SecureRandom.getInstance(algorithm);
		if (seed == null) seed  = EncryptionUtils.generateSalt();
		random.setSeed(seed);
		return random;
	}
	
	/**
	 * Get a fingerprint of the given key.
	 * @param key the key to fingerprint
	 * @return the fingerprint
	 * @throws NoSuchAlgorithmException when the hashing algorithm SHA-1 is not available
	 */
	public static String getThumbprint(Key key) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(key.getEncoded());
		return encodeBase64(md.digest()).toLowerCase();
	}
	
	/**
	 * Load the default keystore type.
	 * @param filename filename
	 * @param password password
	 * @return the key store loaded
	 * @throws IOException when the key store cannot be opened
	 */
	public static KeyStore getKeyStore(String filename, char password[]) throws IOException {
		return getKeyStore(KeyStore.getDefaultType(), new FileInputStream(filename), password);
	}

	/**
	 * Load the default keystore type.
	 * @param type type of key store
	 * @param filename filename
	 * @param password password
	 * @return the key store loaded
	 * @throws IOException when the key store cannot be opened
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
	 * @throws IOException when the key store cannot be opened
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
	 * @throws IOException when the key store cannot be opened
	 */
	public static KeyStore getKeyStore(String type, InputStream in, char password[]) throws IOException {
		try {
			KeyStore ks = KeyStore.getInstance(type);
			ks.load(in, password);
			return ks;
		} catch (KeyStoreException e) {
			throw new RuntimeException("Cannot load key store", e);
		} catch (CertificateException e) {
			throw new RuntimeException("Cannot load key store", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Cannot load key store", e);
		} finally {
			in.close();
		}
	}

	/**
	 * Transforms all bytes from the input array with a crypt alorithm
	 * and returns the transformed bytes.
	 * @param bytes bytes to crypt
	 * @param cipher crypt alorithm used
	 * @return crypted bytes
	 * @throws IOException when crypting cannot be performed due to I/O problems
	 * @throws GeneralSecurityException when crypting cannot be performed due security problems
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
	 * @throws IOException when crypting cannot be performed due to I/O problems
	 * @throws GeneralSecurityException when crypting cannot be performed due security problems
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
				out.write(cipher.doFinal(bytes, i*blockSize, blockSize));
			}
			if (lastBlockSize > 0) {
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
	
	/**
	 * Instantiates a {@link PrivateKey} from given file.
	 * @param file file where private key is stored in BASE64 PKCS8 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the private key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PrivateKey loadPrivateKey(File file, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PrivateKey(CommonUtils.loadContent(file), algorithm);
	}
	
	/**
	 * Instantiates a {@link PrivateKey} from given file.
	 * @param filename file where private key is stored in BASE64 PKCS8 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the private key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PrivateKey loadPrivateKey(String filename, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PrivateKey(CommonUtils.loadContent(filename), algorithm);
	}
	
	/**
	 * Instantiates a {@link PrivateKey} from given URL.
	 * @param url URL where private key is stored in BASE64 PKCS8 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the private key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PrivateKey loadPrivateKey(URL url, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PrivateKey(CommonUtils.loadContent(url), algorithm);
	}
	
	/**
	 * Instantiates a {@link PrivateKey} from given stream.
	 * @param in stream where private key is stored in BASE64 PKCS8 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the private key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PrivateKey loadPrivateKey(InputStream in, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PrivateKey(CommonUtils.loadContent(in), algorithm);
	}
	
	/**
	 * Instantiates a {@link PrivateKey} from given reader.
	 * @param in reader where private key is stored in BASE64 PKCS8 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the private key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PrivateKey loadPrivateKey(Reader in, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PrivateKey(CommonUtils.loadContent(in), algorithm);
	}
	
	/**
	 * Instantiates a {@link PublicKey} from given file.
	 * @param file file where private key is stored in BASE64 X.509 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the public key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PublicKey loadPublicKey(File file, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PublicKey(CommonUtils.loadContent(file), algorithm);
	}
	
	/**
	 * Instantiates a {@link PublicKey} from given file.
	 * @param filename name of file where private key is stored in BASE64 X.509 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the public key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PublicKey loadPublicKey(String filename, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PublicKey(CommonUtils.loadContent(filename), algorithm);
	}
	
	/**
	 * Instantiates a {@link PublicKey} from given URL.
	 * @param url URL where private key is stored in BASE64 X.509 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the public key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PublicKey loadPublicKey(URL url, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PublicKey(CommonUtils.loadContent(url), algorithm);
	}
	
	/**
	 * Instantiates a {@link PublicKey} from given stream.
	 * @param in stream where private key is stored in BASE64 X.509 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the public key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PublicKey loadPublicKey(InputStream in, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PublicKey(CommonUtils.loadContent(in), algorithm);
	}
	
	/**
	 * Instantiates a {@link PublicKey} from given reader.
	 * @param in reader where private key is stored in BASE64 X.509 encoding 
	 * @param algorithm algorithm that the key used
	 * @return the public key
	 * @throws NoSuchAlgorithmException - when algorithm does not exist
	 * @throws InvalidKeySpecException - when key spec cannot be created
	 * @throws IOException - when file cannot be opened
	 */
	public static PublicKey loadPublicKey(Reader in, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return decodeBase64PublicKey(CommonUtils.loadContent(in), algorithm);
	}
	
	/**
	 * Saves a {@link PrivateKey} into given file.
	 * @param file file where private key will be stored in BASE64 PKCS8 encoding 
	 * @param key key to be stored
	 * @throws IOException when key cannot be stored
	 */
	public static void save(File file, PrivateKey key) throws IOException {
		CommonUtils.writeContent(file, encodeBase64Pkcs8(key));
	}

	/**
	 * Saves a {@link PrivateKey} into given file.
	 * @param filename name of file where private key will be stored in BASE64 PKCS8 encoding 
	 * @param key key to be stored
	 * @throws IOException when key cannot be stored
	 */
	public static void save(String filename, PrivateKey key) throws IOException {
		CommonUtils.writeContent(filename, encodeBase64Pkcs8(key));
	}

	/**
	 * Saves a {@link PrivateKey} into given stream.
	 * @param out stream where private key will be stored in BASE64 PKCS8 encoding 
	 * @param key key to be stored
	 * @throws IOException when key cannot be stored
	 */
	public static void save(OutputStream out, PrivateKey key) throws IOException {
		CommonUtils.writeContent(out, encodeBase64Pkcs8(key));
	}

	/**
	 * Saves a {@link PrivateKey} into given writer.
	 * @param out writer where private key will be stored in BASE64 PKCS8 encoding 
	 * @param key key to be stored
	 * @throws IOException when key cannot be stored
	 */
	public static void save(PrintWriter out, PrivateKey key) throws IOException {
		CommonUtils.writeContent(out, encodeBase64Pkcs8(key));
	}

	/**
	 * Saves a {@link PublicKey} into given file.
	 * @param file file where private key will be stored in BASE64 X.509 encoding 
	 * @param key key to be stored
	 * @throws IOException when key cannot be stored
	 */
	public static void save(File file, PublicKey key) throws IOException {
		CommonUtils.writeContent(file, encodeBase64X509(key));
	}

	/**
	 * Saves a {@link PublicKey} into given file.
	 * @param filename name of file where private key will be stored in BASE64 X.509 encoding 
	 * @param key key to be stored
	 * @throws IOException when key cannot be stored
	 */
	public static void save(String filename, PublicKey key) throws IOException {
		CommonUtils.writeContent(filename, encodeBase64X509(key));
	}

	/**
	 * Saves a {@link PublicKey} into given stream.
	 * @param out stream where private key will be stored in BASE64 X.509 encoding 
	 * @param key key to be stored
	 * @throws IOException when key cannot be stored
	 */
	public static void save(OutputStream out, PublicKey key) throws IOException {
		CommonUtils.writeContent(out, encodeBase64X509(key));
	}

	/**
	 * Saves a {@link PublicKey} into given writer.
	 * @param out writer where private key will be stored in BASE64 X.509 encoding 
	 * @param key key to be stored
	 * @throws IOException when key cannot be stored
	 */
	public static void save(PrintWriter out, PublicKey key) throws IOException {
		CommonUtils.writeContent(out, encodeBase64X509(key));
	}

}
