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

import rs.baselib.licensing.internal.OctetLicenseCreator;
import rs.baselib.licensing.internal.OctetLicenseVerifier;
import rs.baselib.licensing.internal.RsaLicenseCreator;
import rs.baselib.licensing.internal.RsaLicenseVerifier;

/**
 * Describes the alternative ways to produce a license key.
 * @author ralph
 *
 */
public enum LicensingScheme implements ILicensingScheme {

	/** A minimum scheme where the key consists of octets of characters, e.g. 12345678-ABCDEF23-78AB45ZG.
	 * <p>This scheme requires DSA keys.</p>
	 */
	OCTET_LICENSE(OctetLicenseCreator.class, OctetLicenseVerifier.class),

	/** A full blown scheme where the license consists of an encrypted message containing all data.
	 *  This scheme requires RSA keys.
	 */
	RSA_LICENSE(RsaLicenseCreator.class, RsaLicenseVerifier.class);
	
	private Class<? extends ILicenseCreator> creatorClass;
	private Class<? extends ILicenseVerifier> verifierClass;
	private ILicenseCreator creator;
	private ILicenseVerifier verifier;
	
	/**
	 * Constructor.
	 * @param creatorClass the creator class
	 * @param verifierClass the verifier class
	 */
	private LicensingScheme(Class<? extends ILicenseCreator> creatorClass, Class<? extends ILicenseVerifier> verifierClass) {
		this.creatorClass = creatorClass;
		this.verifierClass = verifierClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILicenseCreator getLicenseCreator() {
		if (creator == null) {
			creator = create(creatorClass);
		}
		return creator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILicenseVerifier getLicenseVerifier() {
		if (verifier == null) {
			verifier = create(verifierClass);
		}
		return verifier;
	}
	
	/**
	 * Creates instance of the given class.
	 * @param clazz the class to be instantiated
	 * @return the instance
	 * @throws LicenseException when instantiation fails
	 */
	protected <T> T create(Class<T> clazz) {
		try {
			return clazz.getConstructor().newInstance();
		} catch (Throwable t) {
			throw new LicenseException("Cannot create instance of "+clazz.getName(), t);
		}
	}
}
