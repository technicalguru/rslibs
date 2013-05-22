/**
 * 
 */
package rsbaselib.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The class for decrypting strings.
 * @author ralph
 *
 */
public class Decrypter {

    private Cipher dcipher;
	private AlgorithmParameterSpec algorithmParameterSpec;
	
	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @throws NoSuchPaddingException when decrypting algorithm cannot be generated
	 * @throws NoSuchAlgorithmException when decrypting algorithm cannot be generated
	 * @throws InvalidKeyException when decrypting algorithm cannot be generated
	 * @throws InvalidAlgorithmParameterException when decrypting algorithm cannot be generated
	 */
	public Decrypter(SecretKey key, String algorithm) throws DecryptionException {
		this(key, algorithm, null);
	}

	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @param paramSpec parameters to the decrypting algorithm (will be generated if NULL)
	 * @throws NoSuchPaddingException when decrypting algorithm cannot be generated
	 * @throws NoSuchAlgorithmException when decrypting algorithm cannot be generated
	 * @throws InvalidKeyException when decrypting algorithm cannot be generated
	 * @throws InvalidAlgorithmParameterException when decrypting algorithm cannot be generated
	 */
	public Decrypter(SecretKey key, String algorithm, AlgorithmParameterSpec paramSpec) throws DecryptionException {
		init(key, algorithm, paramSpec);
	}

	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @param salt salt to be used
	 * @param iterationCount number of iterations for decryption
	 * @throws NoSuchPaddingException when decrypting algorithm cannot be generated
	 * @throws NoSuchAlgorithmException when decrypting algorithm cannot be generated
	 * @throws InvalidKeyException when decrypting algorithm cannot be generated
	 * @throws InvalidAlgorithmParameterException when decrypting algorithm cannot be generated
	 */
	public Decrypter(SecretKey key, String algorithm, byte salt[], int iterationCount) throws DecryptionException {
        AlgorithmParameterSpec paramSpec = EncryptionUtils.generateParamSpec(salt, iterationCount);
		init(key, algorithm, paramSpec);
	}

	/**
	 * Constructor from bytephrase, salt and iteration spec.
	 * @param bytephrase bytephrase to be used
	 * @param salt salt to be used
	 * @param iterationCount number of iterations for decryption
	 * @throws InvalidKeySpecException when key cannot be generated
	 * @throws NoSuchPaddingException when decrypting algorithm cannot be generated
	 * @throws NoSuchAlgorithmException when decrypting algorithm cannot be generated
	 * @throws InvalidKeyException when decrypting algorithm cannot be generated
	 * @throws InvalidAlgorithmParameterException when decrypting algorithm cannot be generated
	 */
	public Decrypter(byte bytephrase[], byte salt[], int iterationCount) throws DecryptionException {
		
		try {
			if (iterationCount < 1) iterationCount = EncryptionUtils.DEFAULT_ITERATIONS;
			if (salt == null) salt = EncryptionUtils.generateSalt(0);
			KeySpec keySpec = new PBEKeySpec(new String(bytephrase, "UTF8").toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance(EncryptionUtils.DEFAULT_SECRET_KEY_TYPE).generateSecret(keySpec);
			AlgorithmParameterSpec paramSpec = EncryptionUtils.generateParamSpec(salt, iterationCount);
			init(key, EncryptionUtils.DEFAULT_SECRET_KEY_TYPE, paramSpec);
		} catch (UnsupportedEncodingException e) {
        	throw new DecryptionException("Unsupported encoding: "+e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
        	throw new DecryptionException("Invalid key specification: "+e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
        	throw new DecryptionException("No such algorithm: "+e.getMessage(), e);
		}
	}
	
	/**
	 * Constructor from passphrase.
	 * @param passPhrase passphrase to be used
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(char passPhrase[]) throws DecryptionException {
		this(new String(passPhrase));
	}
	
	/**
	 * Constructor from passphrase.
	 * @param passPhrase passphrase to be used
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(char passPhrase[], byte salt[]) throws DecryptionException {
		this(new String(passPhrase), salt);
	}
	
	/**
	 * Constructor from passphrase.
	 * @param passPhrase passphrase to be used
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(String passPhrase) throws DecryptionException {
		this(passPhrase, null, 0);
	}
	
	/**
	 * Constructor from passphrase.
	 * @param passPhrase passphrase to be used
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(String passPhrase, byte salt[]) throws DecryptionException {
		this(passPhrase, salt, 0);
	}
	
	/**
	 * Constructor from passphrase, salt and iteration spec.
	 * @param passPhrase passphrase to be used
	 * @param salt salt to be used
	 * @param iterationCount number of iterations for decryption
	 * @throws InvalidKeySpecException when key cannot be generated
	 * @throws NoSuchPaddingException when decrypting algorithm cannot be generated
	 * @throws NoSuchAlgorithmException when decrypting algorithm cannot be generated
	 * @throws InvalidKeyException when decrypting algorithm cannot be generated
	 * @throws InvalidAlgorithmParameterException when decrypting algorithm cannot be generated
	 */
	public Decrypter(String passPhrase, byte salt[], int iterationCount) throws DecryptionException {
		try {
			if (iterationCount < 1) iterationCount = EncryptionUtils.DEFAULT_ITERATIONS;
			if (salt == null) salt = EncryptionUtils.generateSalt(0);
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance(EncryptionUtils.DEFAULT_SECRET_KEY_TYPE).generateSecret(keySpec);
			AlgorithmParameterSpec paramSpec = EncryptionUtils.generateParamSpec(salt, iterationCount);
			init(key, EncryptionUtils.DEFAULT_SECRET_KEY_TYPE, paramSpec);
		} catch (InvalidKeySpecException e) {
        	throw new DecryptionException("Invalid key specification: "+e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
        	throw new DecryptionException("No such algorithm: "+e.getMessage(), e);
		}
	}
	
	/**
	 * Initializes this decrypter.
	 * @param key secret key
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @param paramSpec parameters to the decrypting algorithm (will be generated if NULL)
	 * @throws NoSuchPaddingException when decrypting algorithm cannot be generated
	 * @throws NoSuchAlgorithmException when decrypting algorithm cannot be generated
	 * @throws InvalidKeyException when decrypting algorithm cannot be generated
	 * @throws InvalidAlgorithmParameterException when decrypting algorithm cannot be generated
	 */
	private void init(SecretKey key, String algorithm, AlgorithmParameterSpec paramSpec) throws DecryptionException {
		try {
			if (algorithm == null) algorithm = key.getAlgorithm();
			if (paramSpec == null) paramSpec = EncryptionUtils.generateParamSpec();
			dcipher = Cipher.getInstance(algorithm);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (NoSuchPaddingException e) {
			throw new DecryptionException("No such padding: "+e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
        	throw new DecryptionException("No such algorithm: "+e.getMessage(), e);
		} catch (InvalidAlgorithmParameterException e) {
        	throw new DecryptionException("Invalid algorithm: "+e.getMessage(), e);
		} catch (InvalidKeyException e) {
        	throw new DecryptionException("Invalid key: "+e.getMessage(), e);
		} catch (Throwable t) {
        	throw new DecryptionException("Cannot initialize decrypter: "+t.getMessage(), t);
		}
	}
	
	/**
	 * Returns the algorithm.
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return dcipher.getAlgorithm();
	}

	/**
	 * Returns the algorithmParameterSpec.
	 * @return the algorithmParameterSpec
	 */
	public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		return algorithmParameterSpec;
	}

    /**
     * Takes a single string as an argument and returns an decrypted version
     * of that string.
     * @param str string to be decrypted
     * @return <code>string</code> decrypted version of the provided String
     */
    public String decrypt(String str) throws DecryptionException {
        try {
            // Decode base64 to get bytes
            byte[] dec = EncryptionUtils.decodeBase64(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (Throwable t) {
        	throw new DecryptionException("Cannot decrypt string: "+t.getMessage(), t);
        }
    }

    /**
     * Decrypt a byte array
     * @param bytes bytes to be decrypted
     * @return <code>byte</code> decrypted version of the provided array
     */
    public byte[] decrypt(byte bytes[]) throws EncryptionException {
        try {
            // Encrypt
            return dcipher.doFinal(bytes);
        } catch (Throwable t) {
        	throw new EncryptionException("Cannot decrypt: "+t.getMessage(), t);
        }
    }
}
