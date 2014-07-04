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

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import rs.baselib.crypto.EncryptionUtils;

/**
 * Tests the licensing mechanism.
 * @author ralph
 *
 */
@RunWith(Parameterized.class)
public class SimpleLicenseTest extends AbstractLicenseTest {

	private static KeyPair keyPair;
	private static LicenseGenerator generator;
	private static LicenseManager manager;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		keyPair = EncryptionUtils.generateKey("DSA", 512);
		generator = new LicenseGenerator(LicensingScheme.OCTET_LICENSE);
		manager = new LicenseManager(LicensingScheme.OCTET_LICENSE);		
	}

	@Parameters
	public static Collection<Object[]> data() throws Exception {
		Collection<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { 1, 0L, "aLicenseHolder" });
		data.add(new Object[] { 1, System.currentTimeMillis()+DateUtils.MILLIS_PER_HOUR, "aLicenseHolder" });
		data.add(new Object[] { 1, System.currentTimeMillis()+DateUtils.MILLIS_PER_HOUR, "a2ndLicenseHolder" });
		data.add(new Object[] { 2, System.currentTimeMillis()+DateUtils.MILLIS_PER_HOUR, "a2ndLicenseHolder" });
		return data;
	}
	
	protected int productId;
	
	public SimpleLicenseTest(int productId, long expiryTime, String licenseHolder) {
		super(""+productId, expiryTime, licenseHolder);
		this.productId = productId;
	}
	
	@Test
	public void test() throws Exception {
		test(generator, manager);
	}

	protected void initCreateContext(ILicenseContext context) {
		super.initCreateContext(context);
		context.set(DefaultLicense.PRODUCT_KEY, productId);
		context.set(PrivateKey.class, keyPair.getPrivate());
	}
	
	protected void initVerifyContext(ILicenseContext context) {
		super.initVerifyContext(context);
		context.set(DefaultLicense.PRODUCT_KEY, productId);
		context.set(PublicKey.class, keyPair.getPublic());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<? extends ILicense> getLicenseClass() {
		return SimpleLicense.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean modifyVerificationContext(int index, ILicenseContext context) throws IndexOutOfBoundsException {
		switch (index) {
		case 0:
			context.set(ILicense.PRODUCT_KEY, productId+1);
			return false;
		case 1:
			context.set(ILicense.PRODUCT_KEY, productId);
			context.set(ILicense.OWNER_KEY, licenseHolder+"x");
			return false;
		}
		throw new IndexOutOfBoundsException("No more tests");
	}

	
}
