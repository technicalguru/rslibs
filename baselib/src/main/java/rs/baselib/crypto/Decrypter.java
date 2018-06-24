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

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
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
	 * @param dCipher the cipher to be used
	 */
	public Decrypter(Cipher dCipher) {
		this.dcipher = dCipher;
	}

	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(Key key, String algorithm) throws DecryptionException {
		this(key, algorithm, null);
	}

	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @param paramSpec parameters to the decrypting algorithm (will be generated if NULL)
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(Key key, String algorithm, AlgorithmParameterSpec paramSpec) throws DecryptionException {
		init(key, algorithm, paramSpec);
	}

	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @param salt salt to be used
	 * @param iterationCount number of iterations for decryption
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(Key key, String algorithm, byte salt[], int iterationCount) throws DecryptionException {
		init(key, algorithm, EncryptionUtils.generateParamSpec(salt, iterationCount));
	}

	/**
	 * Constructor from bytephrase, salt and iteration spec.
	 * @param bytephrase bytephrase to be used
	 * @param salt salt to be used
	 * @param iterationCount number of iterations for decryption
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(byte bytephrase[], byte salt[], int iterationCount) throws DecryptionException {
		
		try {
			if (iterationCount < 1) iterationCount = EncryptionUtils.DEFAULT_ITERATIONS;
			if (salt == null) salt = EncryptionUtils.generateSalt(0);
			KeySpec keySpec = new PBEKeySpec(new String(bytephrase, "UTF8").toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance(EncryptionUtils.DEFAULT_SECRET_KEY_TYPE).generateSecret(keySpec);
			init(key, EncryptionUtils.DEFAULT_SECRET_KEY_TYPE, EncryptionUtils.generateParamSpec(salt, iterationCount));
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
	 * @param salt salt to be used
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
	 * @param salt salt to be used
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
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	public Decrypter(String passPhrase, byte salt[], int iterationCount) throws DecryptionException {
		try {
			if (iterationCount < 1) iterationCount = EncryptionUtils.DEFAULT_ITERATIONS;
			if (salt == null) salt = EncryptionUtils.generateSalt(0);
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance(EncryptionUtils.DEFAULT_SECRET_KEY_TYPE).generateSecret(keySpec);
			init(key, EncryptionUtils.DEFAULT_SECRET_KEY_TYPE, EncryptionUtils.generateParamSpec(salt, iterationCount));
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
	 * @throws DecryptionException when decrypting algorithm cannot be generated
	 */
	private void init(Key key, String algorithm, AlgorithmParameterSpec paramSpec) throws DecryptionException {
		try {
			algorithmParameterSpec = paramSpec;
			if (algorithm == null) algorithm = key.getAlgorithm();
			dcipher = Cipher.getInstance(algorithm);
			if (paramSpec != null) {
				dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			} else {
				dcipher.init(Cipher.DECRYPT_MODE, key);
			}
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
	 * @throws DecryptionException when decrypting fails
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
	 * @throws DecryptionException when decrypting fails
     */
    public byte[] decrypt(byte bytes[]) throws DecryptionException {
        try {
            // Encrypt
            return dcipher.doFinal(bytes);
        } catch (Throwable t) {
        	throw new DecryptionException("Cannot decrypt: "+t.getMessage(), t);
        }
    }
}
