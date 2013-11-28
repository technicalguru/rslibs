/**
 * 
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
		String s = generator.createLicenseKey(DefaultLicense.class, createContext);
		
		long t = System.currentTimeMillis();
		boolean shallFail = expiryTime > 0 ? expiryTime < t : false;
		ILicenseContext verifyContext = new DefaultLicenseContext();
		initVerifyContext(verifyContext);
		verifyContext.set(DefaultLicense.EXPIRATION_DATE_KEY, t);
		
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
				
		// Test other productId
		try {
			verifyContext.set(DefaultLicense.PRODUCT_KEY, product+"x");
			manager.verify(s, verifyContext);
			// we need to fail
			fail("Verification succeeded with other product ID");
		} catch (LicenseException e) {
			// Success
		}		
		verifyContext.set(DefaultLicense.PRODUCT_KEY, product);
		
		// Test other license holder
		try {
			verifyContext.set(DefaultLicense.OWNER_KEY, licenseHolder+"x");
			manager.verify(s, verifyContext);
			// we need to fail
			fail("Verification succeeded with other licenseHolder");
		} catch (LicenseException e) {
			// Success
		}		
		verifyContext.set(DefaultLicense.PRODUCT_KEY, licenseHolder);
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
}
