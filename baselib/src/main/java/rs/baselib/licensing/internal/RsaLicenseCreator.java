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
import rs.baselib.licensing.ILicenseCreator;
import rs.baselib.licensing.LicenseException;

/**
 * Creates a full license.
 * @author ralph
 *
 */
public class RsaLicenseCreator implements ILicenseCreator {

	private Base64 encoder;

	/**
	 * Constructor.
	 */
	public RsaLicenseCreator() {
		encoder = new Base64(64);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createLicenseKey(ILicenseContext context, ILicense license) {
		try {
			byte licenseBytes[] = ConverterUtils.toBytes(license);
			byte bytes[] = encrypt(context, licenseBytes);
			return encoder.encodeToString(bytes);
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

	/**
	 * Encrypts the given bytes.
	 * @param context the licensing context
	 * @param bytes the plain bytes
	 * @return the bytes encrypted
	 * @throws IOException when encryption fails
	 * @throws GeneralSecurityException when cipher is invalid
	 */
	protected byte[] encrypt(ILicenseContext context, byte bytes[]) throws GeneralSecurityException, IOException {
		Cipher cipher = context.get(Cipher.class);
		if (cipher== null) {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, context.get(Key.class));
			context.set(Cipher.class, cipher);
		}
		int blockSize = cipher.getBlockSize();
		if (blockSize == 0) blockSize = 245;
		return EncryptionUtils.crypt(bytes, cipher, blockSize);
	}

}
