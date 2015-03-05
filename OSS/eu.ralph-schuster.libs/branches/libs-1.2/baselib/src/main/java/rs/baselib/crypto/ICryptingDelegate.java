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

/**
 * Delegate for encrypting and decrypting.
 * @author ralph
 *
 */
public interface ICryptingDelegate {

	/**
	 * Initialize the delegate.
	 * @param factory factory for the delegate.
	 */
	public void init(ICryptingDelegateFactory factory);
	
	/**
	 * Encrypts the given bytes.
	 * @param bytes bytes to be encrypted
	 * @return encrypted bytes
	 * @throws Exception when an error occurs
	 */
	public byte[] encrypt(byte[] bytes) throws Exception;
	
	/**
	 * Decrypts the given bytes.
	 * @param bytes bytes to be decrypted
	 * @return decrypted bytes
	 * @throws Exception when an error occurs
	 */
	public byte[] decrypt(byte[] bytes) throws Exception;
}
