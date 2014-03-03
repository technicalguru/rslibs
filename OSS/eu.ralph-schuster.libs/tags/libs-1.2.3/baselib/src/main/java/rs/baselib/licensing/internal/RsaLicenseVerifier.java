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
package rs.baselib.licensing.internal;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.io.ConverterUtils;
import rs.baselib.licensing.ILicense;
import rs.baselib.licensing.ILicenseContext;
import rs.baselib.licensing.ILicenseVerifier;
import rs.baselib.licensing.LicenseException;

/**
 * Verifies a full license.
 * @author ralph
 *
 */
public class RsaLicenseVerifier implements ILicenseVerifier {

	private Base64 decoder;
	
	/**
	 * Constructor.
	 */
	public RsaLicenseVerifier() {
		decoder = new Base64(64);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILicense verify(String licenseKey, ILicenseContext context) {
		try {
			byte bytes[] = decrypt(context, decoder.decode(licenseKey));
			ILicense license = (ILicense)ConverterUtils.toObject(bytes);
			
			license.verify(context);
			
			return license;
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Invalid license: "+licenseKey, t);
		}
	}

	/**
	 * Decrypts the bytes.
	 * @param bytes bytes to be decrypted
	 * @return the plain message
	 * @throws DecryptionException
	 */
	protected byte[] decrypt(ILicenseContext context, byte bytes[]) throws GeneralSecurityException, IOException {
		Cipher cipher = context.get(Cipher.class);
		if (cipher == null) {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, context.get(Key.class));
			context.set(Cipher.class, cipher);
		}
		int blockSize = cipher.getBlockSize();
		if (blockSize == 0) blockSize = 256;
		return EncryptionUtils.crypt(bytes, cipher, blockSize);
	}
	
}
