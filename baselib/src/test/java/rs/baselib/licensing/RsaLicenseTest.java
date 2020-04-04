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

import java.security.Key;
import java.security.KeyPair;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import rs.baselib.crypto.EncryptionUtils;

/**
 * Tests the licensing mechanism.
 * @author ralph
 *
 */
@RunWith(Parameterized.class)
public class RsaLicenseTest extends AbstractLicenseTest {

	private static KeyPair keyPair;
	private static LicenseGenerator generator;
	private static LicenseManager manager;
	
	/**
	 * @throws java.lang.Exception - when the RSA method cannot be setup
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		keyPair = EncryptionUtils.generateKey("RSA", 2048);
		generator = new LicenseGenerator(LicensingScheme.RSA_LICENSE);
		manager = new LicenseManager(LicensingScheme.RSA_LICENSE);		
	}

	public RsaLicenseTest(String product, long expiryTime, String licenseHolder) {
		super(product, expiryTime, licenseHolder);
	}
	
	@Test
	public void test() throws Exception {
		test(generator, manager);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initCreateContext(ILicenseContext context) {
		super.initCreateContext(context);
		context.set(Key.class, keyPair.getPrivate());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initVerifyContext(ILicenseContext context) {
		super.initVerifyContext(context);
		context.set(Key.class, keyPair.getPublic());
	}
	
	
}
