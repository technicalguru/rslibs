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

import org.apache.commons.codec.binary.Base64;

/**
 * Class for encrypting strings.
 * @author ralph
 *
 */
public class Encrypter {

	private Cipher ecipher;
	private AlgorithmParameterSpec algorithmParameterSpec;
	
	/**
	 * Constructor from secret key.
	 * @param eCipher the cipher to be used
	 */
	public Encrypter(Cipher eCipher) {
		this.ecipher = eCipher;
	}

	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	public Encrypter(Key key, String algorithm) throws EncryptionException {
		this(key, algorithm, null);
	}

	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @param paramSpec parameters to the encrypting algorithm (will be generated if NULL)
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	public Encrypter(Key key, String algorithm, AlgorithmParameterSpec paramSpec) throws EncryptionException {
		init(key, algorithm, paramSpec);
	}

	/**
	 * Constructor from secret key.
	 * @param key the secret key to be used
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @param salt salt to be used
	 * @param iterationCount number of iterations for encryption
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	public Encrypter(Key key, String algorithm, byte salt[], int iterationCount) throws EncryptionException {
		init(key, algorithm, EncryptionUtils.generateParamSpec(salt, iterationCount));
	}

	/**
	 * Constructor from passphrase, salt and iteration spec.
	 * @param bytephrase passphrase to be used
	 * @param salt salt to be used
	 * @param iterationCount number of iterations for encryption
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 * @throws UnsupportedEncodingException when encoding cannot be found
	 */
	public Encrypter(byte bytephrase[], byte salt[], int iterationCount) throws EncryptionException, UnsupportedEncodingException {
		try {
			if (iterationCount < 1) iterationCount = EncryptionUtils.DEFAULT_ITERATIONS;
			if (salt == null) salt = EncryptionUtils.generateSalt(0);
			KeySpec keySpec = new PBEKeySpec(new String(bytephrase, "UTF8").toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance(EncryptionUtils.DEFAULT_SECRET_KEY_TYPE).generateSecret(keySpec);
			init(key, EncryptionUtils.DEFAULT_SECRET_KEY_TYPE, EncryptionUtils.generateParamSpec(salt, iterationCount));
		} catch (UnsupportedEncodingException e) {
        	throw new EncryptionException("Unsupported encoding: "+e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
        	throw new EncryptionException("Invalid key specification: "+e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
        	throw new EncryptionException("No such algorithm: "+e.getMessage(), e);
		}
	}
	
	/**
	 * Constructor from passphrase.
	 * @param passphrase passphrase to be used
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	public Encrypter(char passphrase[]) throws EncryptionException {
		this(new String(passphrase));
	}
	
	/**
	 * Constructor from passphrase.
	 * @param passphrase passphrase to be used
	 * @param salt salt to be used
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	public Encrypter(char passphrase[], byte salt[]) throws EncryptionException {
		this(new String(passphrase), salt);
	}
	
	/**
	 * Constructor from passphrase.
	 * @param passphrase passphrase to be used
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	public Encrypter(String passphrase) throws EncryptionException {
		this(passphrase, null, 0);
	}
	
	/**
	 * Constructor from passphrase.
	 * @param passphrase passphrase to be used
	 * @param salt salt to be used
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	public Encrypter(String passphrase, byte salt[]) throws EncryptionException {
		this(passphrase, salt, 0);
	}
	
	/**
	 * Constructor from passphrase, salt and iteration spec.
	 * @param passPhrase passphrase to be used
	 * @param salt salt to be used
	 * @param iterationCount number of iterations for encryption
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	public Encrypter(String passPhrase, byte salt[], int iterationCount) throws EncryptionException {
		try {
			if (iterationCount < 1) iterationCount = EncryptionUtils.DEFAULT_ITERATIONS;
			if (salt == null) salt = EncryptionUtils.generateSalt(0);
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance(EncryptionUtils.DEFAULT_SECRET_KEY_TYPE).generateSecret(keySpec);
			init(key, null, EncryptionUtils.generateParamSpec(salt, iterationCount));
		} catch (InvalidKeySpecException e) {
        	throw new EncryptionException("Invalid key specification: "+e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
        	throw new EncryptionException("No such algorithm: "+e.getMessage(), e);
		}
	}
	
	/**
	 * Initializes this encrypter.
	 * @param key secret key
	 * @param algorithm algorithm (key's algorithm will be used if NULL)
	 * @param paramSpec parameters to the encrypting algorithm (will be generated if NULL)
	 * @throws EncryptionException when encrypting algorithm cannot be generated
	 */
	private void init(Key key, String algorithm, AlgorithmParameterSpec paramSpec) throws EncryptionException {
		try {
			algorithmParameterSpec = paramSpec;
			if (algorithm == null) algorithm = key.getAlgorithm();
			//if (paramSpec == null) paramSpec = EncryptionUtils.generateParamSpec();
			ecipher = Cipher.getInstance(algorithm);
			if (paramSpec != null) {
				ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			} else {
				ecipher.init(Cipher.ENCRYPT_MODE, key);
			}
		} catch (NoSuchPaddingException e) {
			throw new EncryptionException("No such padding: "+e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
        	throw new EncryptionException("No such algorithm: "+e.getMessage(), e);
		} catch (InvalidAlgorithmParameterException e) {
        	throw new EncryptionException("Invalid algorithm: "+e.getMessage(), e);
		} catch (InvalidKeyException e) {
        	throw new EncryptionException("Invalid key: "+e.getMessage(), e);
		} catch (Throwable t) {
        	throw new EncryptionException("Cannot initialize Encrypter: "+t.getMessage(), t);
		}
	}
	
	/**
	 * Returns the algorithm.
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return ecipher.getAlgorithm();
	}

	/**
	 * Returns the algorithmParameterSpec.
	 * @return the algorithmParameterSpec
	 */
	public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		return algorithmParameterSpec;
	}

    /**
     * Takes a single String as an argument and returns an encrypted version
     * of that string.
     * @param str string to be encrypted
     * @return <code>String</code> encrypted version of the provided String
     * @throws EncryptionException when encryption fails
     */
    public String encrypt(String str) throws EncryptionException {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return Base64.encodeBase64String(enc).trim();

        } catch (Throwable t) {
        	throw new EncryptionException("Cannot encrypt string: "+t.getMessage(), t);
        }
    }

    /**
     * Encrypt a byte array
     * @param bytes bytes to be encrypted
     * @return <code>byte</code> encrypted version of the provided array
     * @throws EncryptionException when encryption fails
     */
    public byte[] encrypt(byte bytes[]) throws EncryptionException {
        try {
            // Encrypt
            return ecipher.doFinal(bytes);
        } catch (Throwable t) {
        	throw new EncryptionException("Cannot encrypt: "+t.getMessage(), t);
        }
    }

}
