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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

/**
 * Abstract implementation of a license test.
 * @author ralph
 *
 */
public abstract class AbstractLicenseTest {

	@Parameters
	public static Collection<Object[]> data() throws Exception {
		Collection<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { "product1", 0L, "aLicenseHolder" });
		data.add(new Object[] { "product1", System.currentTimeMillis()+2000L, "aLicenseHolder" });
		data.add(new Object[] { "product1", System.currentTimeMillis()+2000L, "a2ndLicenseHolder" });
		data.add(new Object[] { "product2", System.currentTimeMillis()+2000L, "a2ndLicenseHolder" });
		return data;
	}
	
	protected String product;
	protected long expiryTime;
	protected String licenseHolder;
	
	public AbstractLicenseTest(String product, long expiryTime, String licenseHolder) {
		this.product = product;
		this.expiryTime = expiryTime;
		this.licenseHolder = licenseHolder;
	}
	
	public void test(LicenseGenerator generator, LicenseManager manager) throws Exception {
		// Generate the license
		ILicenseContext createContext = new DefaultLicenseContext();
		initCreateContext(createContext);
		String s = generator.createLicenseKey(getLicenseClass(), createContext);
		
		long t = System.currentTimeMillis();
		boolean shallFail = expiryTime > 0 ? expiryTime < t : false;
		ILicenseContext verifyContext = new DefaultLicenseContext();
		initVerifyContext(verifyContext);
		verifyContext.set(ILicense.EXPIRATION_DATE_KEY, t);
		
		// Initial test
		try {
			manager.verify(s, verifyContext);
			if (shallFail) {
				fail("Verification should have failed");
			}
		} catch (LicenseException e) {
			// This should not have failed
			if (!shallFail) {
				fail("Verification failed: "+e.getMessage());
			}
		}
				
		// Perform additional tests
		try {
			int index = 0;
			while (true) {
				boolean shallSucceed = modifyVerificationContext(index, verifyContext);
				try {
					manager.verify(s, verifyContext);
					if (!shallSucceed) {
						fail("Verification should have failed for test "+index);
					}
				} catch (LicenseException e) {
					if (shallSucceed) {
						fail("Verification did not fail for test "+index);
					}
				}
				index++;
			}
		} catch (IndexOutOfBoundsException e) {
			// No more additional tests
		}		
	}

	protected Class<? extends ILicense> getLicenseClass() {
		return DefaultLicense.class;
	}
	
	protected void initCreateContext(ILicenseContext context) {
		context.set(DefaultLicense.PRODUCT_KEY, product);
		context.set(DefaultLicense.EXPIRATION_DATE_KEY, expiryTime);
		context.set(DefaultLicense.OWNER_KEY, licenseHolder);
	}
	
	protected void initVerifyContext(ILicenseContext context) {
		context.set(DefaultLicense.PRODUCT_KEY, product);
		context.set(DefaultLicense.EXPIRATION_DATE_KEY, expiryTime);
		context.set(DefaultLicense.OWNER_KEY, licenseHolder);
	}
	
	/**
	 * Modify the verification context for another test.
	 * <p>The method must also ensure that previous modifications are reverted.</p>
	 * @param index index of test
	 * @param context the context to be modified
	 * @return whether the verification shall succeed with his context
	 * @throws {@link IndexOutOfBoundsException} when there is no more test
	 */
	protected boolean modifyVerificationContext(int index, ILicenseContext context) throws IndexOutOfBoundsException {
		switch (index) {
		case 0:
			context.set(ILicense.PRODUCT_KEY, product+"x");
			return false;
		case 1:
			context.set(ILicense.PRODUCT_KEY, product);
			context.set(ILicense.OWNER_KEY, licenseHolder+"x");
			return false;
		}
		throw new IndexOutOfBoundsException("No more tests");
	}
}
