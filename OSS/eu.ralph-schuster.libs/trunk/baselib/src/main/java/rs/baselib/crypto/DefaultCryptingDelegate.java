/**
 * 
 */
package rs.baselib.crypto;

import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements basic crypting decrypting.
 * @author ralph
 *
 */
public class DefaultCryptingDelegate implements ICryptingDelegate {

	private static Logger log = LoggerFactory.getLogger(DefaultCryptingDelegate.class);

	private Cipher eCipher;
	private Cipher dCipher;
	private boolean blockBased;
	
	/**
	 * Constructor.
	 */
	public DefaultCryptingDelegate() {
	}

	/**
	 * Initializes this instance.
	 * @param factory the factory providing initialization parameters
	 */
	public void init(ICryptingDelegateFactory factory) {
		try {
			KeyPair keyPair = factory.getKeyPair();
			String algorithm = factory.getAlgorithm();
			if (keyPair != null) {
				// Use key pair algorithm
				if (algorithm == null) algorithm = keyPair.getPrivate().getAlgorithm();
				log.debug("Key uses algorithm: "+algorithm);
				AlgorithmParameterSpec spec = factory.getParamSpec();
				algorithm = EncryptionUtils.DEFAULT_SECRET_KEY_TYPE;
				spec = EncryptionUtils.generateParamSpec();
				dCipher = Cipher.getInstance(algorithm);
				eCipher = Cipher.getInstance(algorithm);
				SecureRandom random = new SecureRandom();
				if (spec == null) {
					dCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate(), random);
					eCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic(), random);
				} else {
					spec.getClass().getName();
					dCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate(), spec, random);
					eCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic(), spec, random);
				}
				blockBased = true;
			} else {
				// Use passphrase algorithm
				char passphrase[] = factory.getPassphrase();
				int iterationCount = EncryptionUtils.DEFAULT_ITERATIONS;
				byte salt[] = EncryptionUtils.generateSalt(0);
				KeySpec keySpec = new PBEKeySpec(passphrase, salt, iterationCount);
				SecretKey key = SecretKeyFactory.getInstance(EncryptionUtils.DEFAULT_SECRET_KEY_TYPE).generateSecret(keySpec);
				AlgorithmParameterSpec paramSpec = EncryptionUtils.generateParamSpec(salt, iterationCount);
				algorithm = key.getAlgorithm();
				if (paramSpec == null) paramSpec = EncryptionUtils.generateParamSpec();
				eCipher = Cipher.getInstance(algorithm);
				eCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
				dCipher = Cipher.getInstance(algorithm);
				dCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
				blockBased = false;
			}
			log.trace("Cipher block length (encrypt) = "+eCipher.getBlockSize());
			log.trace("Cipher block length (decrypt) = "+dCipher.getBlockSize());
		} catch (Exception e) {
			throw new RuntimeException("Canot create ciphers", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] encrypt(byte[] bytes) throws Exception {
		if (isBlockBased()) {
			int blockSize = eCipher.getBlockSize();
			if (blockSize == 0) blockSize = 245;
			return EncryptionUtils.crypt(bytes, eCipher, blockSize);
		}
		return EncryptionUtils.crypt(bytes, eCipher);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decrypt(byte[] bytes) throws Exception {
		if (isBlockBased()) {
			int blockSize = dCipher.getBlockSize();
			if (blockSize == 0) blockSize = 256;
			return EncryptionUtils.crypt(bytes, dCipher, blockSize);
		}
		return EncryptionUtils.crypt(bytes, dCipher);
	}

	/**
	 * Returns true when the crypting algorithm is block based.
	 * @return true or false
	 */
	public boolean isBlockBased() {
		return blockBased;
	}
}
