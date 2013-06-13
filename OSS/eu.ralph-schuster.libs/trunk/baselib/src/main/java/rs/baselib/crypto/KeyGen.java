/**
 * 
 */
package rs.baselib.crypto;

import java.security.KeyPair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class for signing and verifying signatures.
 * @author ralph
 *
 */
public class KeyGen {

	private static Logger log = LoggerFactory.getLogger(KeyGen.class);
	
	/**
	 * Main program for encryption on command line.
	 * @param seed the seed for the random process
	 */
	public static KeyPair generateKeyPair(String seed) {
		try {
			if (seed == null) seed = EncryptionUtils.generatePassword(8);
			
			KeyPair keyPair = EncryptionUtils.generateKey(seed.getBytes());
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
