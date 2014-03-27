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

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base32;

import rs.baselib.crypto.DataSigner;
import rs.baselib.crypto.DecryptionException;
import rs.baselib.crypto.SigningException;
import rs.baselib.io.ConverterUtils;
import rs.baselib.licensing.DefaultLicenseContext;
import rs.baselib.licensing.ILicense;
import rs.baselib.licensing.ILicenseContext;
import rs.baselib.licensing.ILicenseVerifier;
import rs.baselib.licensing.LicenseException;
import rs.baselib.licensing.SimpleLicense;

/**
 * Verifies an Octet License.
 * @author ralph
 *
 */
public class OctetLicenseVerifier implements ILicenseVerifier {

	private Base32 decoder;
	
	/**
	 * Constructor.
	 */
	public OctetLicenseVerifier() {
		decoder = new Base32(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILicense verify(String licenseKey, ILicenseContext context) {
		try {
			byte bytes[] = decoder.decode(ungroup(licenseKey));
			
			// XOR the first 4 byte with the second four bytes to remove variance for unlimited
			for (int i=0; i<4; i++) {
				bytes[i] = (byte) (bytes[i] ^ bytes[4+i]);
			}

			// expiration
			byte b[] = new byte[4];
			System.arraycopy(bytes, 0, b, 0, 4);
			long expirationTime = ConverterUtils.toInt(b)*1000L;
			
			// signature
			byte signature[] = new byte[bytes.length-4];
			System.arraycopy(bytes, 4, signature, 0, signature.length);

			// Create the template license
			ILicenseContext createContext = new DefaultLicenseContext();
			createContext.set(ILicense.PRODUCT_KEY, context.get(ILicense.PRODUCT_KEY));
			createContext.set(ILicense.OWNER_KEY, context.get(ILicense.OWNER_KEY));
			createContext.set(ILicense.EXPIRATION_DATE_KEY, expirationTime);
			SimpleLicense license = new SimpleLicense();
			license.init(createContext);
			
			// Verify signature (includes all fields already)
			if (!verify(signature, license, context)) {
				throw new LicenseException("Invalid license: "+licenseKey);
			}
			
			// License verifies the expiration date
			license.verify(context);
			return license;
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Invalid license: "+licenseKey, t);
		}
	}

	/**
	 * Verifies the signature
	 * @param signature signature to be verified
	 * @param license the license to be checked against
	 * @param context the context
	 * @return <code>true</code> when signature is ok
	 * @throws DecryptionException
	 * @throws SigningException
	 * @throws UnsupportedEncodingException
	 */
	protected boolean verify(byte signature[], SimpleLicense license, ILicenseContext context) throws DecryptionException, SigningException, UnsupportedEncodingException {
		DataSigner signer = context.get(DataSigner.class);
		if (signer == null) {
			signer = new DataSigner(context.get(PublicKey.class));
			context.set(DataSigner.class, signer);
		}
		return signer.verify(signature, SimpleLicense.serialize(license));
	}
	
	/**
	 * Returns the un-grouped version of the key.
	 * @param s the key
	 * @return the un-grouped version (dashes removed)
	 */
	protected static String ungroup(String s) {
		return s.replaceAll("\\-", "");
	}

	
}
