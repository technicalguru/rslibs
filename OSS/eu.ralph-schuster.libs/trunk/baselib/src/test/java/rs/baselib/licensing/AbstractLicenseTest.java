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
		data.add(new Object[] { 1, 0L, "aLicenseHolder" });
		data.add(new Object[] { 1, System.currentTimeMillis()+2000L, "aLicenseHolder" });
		data.add(new Object[] { 1, System.currentTimeMillis()+2000L, "a2ndLicenseHolder" });
		data.add(new Object[] { 2, System.currentTimeMillis()+2000L, "a2ndLicenseHolder" });
		return data;
	}
	
	protected int productId;
	protected long expiryTime;
	protected String licenseHolder;
	
	public AbstractLicenseTest(int productId, long expiryTime, String licenseHolder) {
		this.productId = productId;
		this.expiryTime = expiryTime;
		this.licenseHolder = licenseHolder;
	}
	
	public void test(LicenseGenerator generator, LicenseManager manager) throws Exception {
		// Generate the license
		String s = generator.createLicenseKey(productId, expiryTime, licenseHolder);
		System.out.println("Testing license: "+s);
		boolean timeShallFail = expiryTime > 0 ? expiryTime < System.currentTimeMillis() : false;
		
		try {
			manager.verify(s, productId, licenseHolder);
			if (timeShallFail) {
				fail("Verification should have failed with timestamp");
			}
		} catch (LicenseException e) {
			// This should not have failed
			if (!timeShallFail) {
				fail("Verification failed with timestamp");
			}
		}
				
		// Test other productId
		try {
			manager.verify(s, productId+1, licenseHolder);
			// we need to fail
			fail("Verification succeeded with other product ID");
		} catch (LicenseException e) {
			// Success
		}		
		
		// Test other license holder
		try {
			manager.verify(s, productId, licenseHolder+"x");
			// we need to fail
			fail("Verification succeeded with other licenseHolder");
		} catch (LicenseException e) {
			// Success
		}		
	}

}
