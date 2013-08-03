/**
 * 
 */
package rs.baselib.crypto;

import java.security.KeyPair;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Interface for factories creating crypting delegates.
 * @author ralph
 *
 */
public interface ICryptingDelegateFactory {

	/**
	 * Returns a crypting delegate.
	 * @return a delegate
	 */
	public ICryptingDelegate getCryptingDelegate();
	
	/**
	 * Returns the key.
	 * @return the key
	 */
	public KeyPair getKeyPair();

	/**
	 * Returns the algorithm.
	 * @return the algorithm
	 */
	public String getAlgorithm();
	
	/**
	 * Returns the paramSpec.
	 * @return the paramSpec
	 */
	public AlgorithmParameterSpec getParamSpec();
	
	/**
	 * Alternatively provide a passphrase for encryption usage.
	 * @return passphrase
	 */
	public char[] getPassphrase();
}