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
package rs.baselib.licensing;

import rs.baselib.crypto.DecryptionException;

/**
 * The generator for license.
 * @author ralph
 *
 */
public class LicenseGenerator {

	/** The license creator */
	private ILicenseCreator licenseCreator;

	/**
	 * Constructor.
	 */
	public LicenseGenerator() throws DecryptionException {
		this((ILicensingScheme)null);
	}

	/**
	 * Constructor.
	 */
	public LicenseGenerator(ILicensingScheme scheme) throws DecryptionException {
		this(scheme != null ? scheme.getLicenseCreator() : LicensingScheme.RSA_LICENSE.getLicenseCreator());
	}

	/**
	 * Constructor.
	 */
	public LicenseGenerator(ILicenseCreator licenseCreator) throws DecryptionException {
		this.licenseCreator = licenseCreator;
	}

	/**
	 * Returns the {@link #licenseCreator}.
	 * @return the licenseCreator
	 */
	public ILicenseCreator getLicenseCreator() {
		return licenseCreator;
	}

	/**
	 * Generates a key using the given context.
	 * @param context containing all properties for the license
	 * @return the license key generated
	 */
	public String createLicenseKey(ILicenseContext context) {
		return createLicenseKey(DefaultLicense.class, context);
	}
	
	/**
	 * Generates a license key using the given license class and context.
	 * @param licenseClass the class to be used for generating the license
	 * @param context containing all properties for the license
	 * @return the license key generated
	 */
	public String createLicenseKey(Class<? extends ILicense> licenseClass, ILicenseContext context) {
		try {
			ILicense license = licenseClass.newInstance();
			license.init(context);
			return createLicenseKey(context, license);
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license", t);
		}
	}

	/**
	 * Generates a key from the given license.
	 * @param context the licnesing context
	 * @param license the license to be created
	 * @return the license key
	 */
	public String createLicenseKey(ILicenseContext context, ILicense license) {
		try {
			return getLicenseCreator().createLicenseKey(context, license);
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

}
