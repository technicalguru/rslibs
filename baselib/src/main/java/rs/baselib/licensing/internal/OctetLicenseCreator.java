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

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import org.apache.commons.codec.binary.Base32;

import rs.baselib.crypto.DataSigner;
import rs.baselib.crypto.DecryptionException;
import rs.baselib.crypto.SigningException;
import rs.baselib.io.ConverterUtils;
import rs.baselib.licensing.ILicense;
import rs.baselib.licensing.ILicenseContext;
import rs.baselib.licensing.ILicenseCreator;
import rs.baselib.licensing.LicenseException;
import rs.baselib.licensing.SimpleLicense;

/**
 * Creates an Octet License.
 * @author ralph
 *
 */
public class OctetLicenseCreator implements ILicenseCreator {

	private Base32 encoder;

	/**
	 * Constructor.
	 */
	public OctetLicenseCreator() {
		encoder = new Base32(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createLicenseKey(ILicenseContext context, ILicense license) {
		try {
			SimpleLicense lic = (SimpleLicense)license;
			byte licenseBytes[] = SimpleLicense.serialize(lic);
			byte signature[] = sign(context, licenseBytes);
			byte bytes[] = combine((int)(lic.getExpiration()/1000), signature);
			return group(encoder.encodeToString(bytes));
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

	/**
	 * Signs the given byte array.
	 * @param context - the licensing context
	 * @param bytes - the bytes to be signed
	 * @return the signature
	 * @throws SigningException - when signing fails
	 * @throws NoSuchAlgorithmException - when the signing algorithm is invalid
	 * @throws DecryptionException - when other signing error occur
	 */
	protected byte[] sign(ILicenseContext context, byte bytes[]) throws SigningException, NoSuchAlgorithmException, DecryptionException {
		DataSigner signer = context.get(DataSigner.class);
		if (signer == null) {
			signer = new DataSigner(context.get(PrivateKey.class));
			context.set(DataSigner.class, signer);
		}
		return signer.getByteSignature(bytes);
	}

	/**
	 * Builds the combined license key (raw bytes).
	 * @param prefix the integer prefix basically containing the expiry time in seconds since epoch time
	 * @param signature teh signature
	 * @return the combined byte array
	 */
	protected byte[] combine(int prefix, byte signature[]) {
		int len = signature.length+4;
		byte rc[] = new byte[len];
		System.arraycopy(ConverterUtils.toBytes(prefix), 0, rc, 0, 4);
		System.arraycopy(signature, 0, rc, 4, signature.length);
		// XOR the first 4 byte with the second four bytes to allow more variance for unlimited
		for (int i=0; i<4; i++) {
			rc[i] = (byte) (rc[i] ^ rc[4+i]);
		}
		return rc;
	}

	/**
	 * Groups a string into 8-character blocks.
	 * @param s the string to be grouped
	 * @return the grouped string
	 */
	public static String group(String s) {
		StringBuilder b = new StringBuilder(s.length()+10);
		int count = 0;
		for (char c : s.toCharArray()) {
			if (count >= 8) {
				b.append('-');
				count = 0;
			}
			b.append(c);
			count++;
		}
		// Remove trailing =
		while (b.charAt(b.length()-1) == '=') b.deleteCharAt(b.length()-1);
		return b.toString();
	}


}
