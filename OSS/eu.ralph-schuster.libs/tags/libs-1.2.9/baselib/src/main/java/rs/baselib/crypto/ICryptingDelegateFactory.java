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
	
	/**
	 * Provide a salt for encryption usage.
	 * @return salt
	 */
	public byte[] getSalt();
}
