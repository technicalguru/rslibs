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


/**
 * The Key Manager, responsible to verify licenses.
 * @author ralph
 *
 */
public class LicenseManager {

	private ILicenseVerifier licenseVerifier;

	/**
	 * Constructor.
	 */
	public LicenseManager() {
		this((ILicensingScheme)null);
	}

	/**
	 * Constructor.
	 * @param scheme - the licensing scheme
	 */
	public LicenseManager(ILicensingScheme scheme) {
		this(scheme != null ? scheme.getLicenseVerifier() : LicensingScheme.RSA_LICENSE.getLicenseVerifier());
	}

	/**
	 * Constructor.
	 * @param licenseVerifier - the licensing verifier
	 */
	public LicenseManager(ILicenseVerifier licenseVerifier) {
		this.licenseVerifier = licenseVerifier;
	}

	/**
	 * Returns the {@link #licenseVerifier}.
	 * @return the licenseVerifier
	 */
	public ILicenseVerifier getLicenseVerifier() {
		return licenseVerifier;
	}

	/**
	 * Verifies the given license string.
	 * @param licenseKey the license string
	 * @param context the context containing verification parameters
	 * @return the license
	 */
	public ILicense verify(String licenseKey, ILicenseContext context) {
		try {
			return getLicenseVerifier().verify(licenseKey, context);
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

}
