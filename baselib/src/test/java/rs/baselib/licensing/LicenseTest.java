/**
 * 
 */
package rs.baselib.licensing;

import static org.junit.Assert.fail;

import java.security.KeyPair;

import org.apache.commons.codec.binary.Hex;
import org.junit.BeforeClass;
import org.junit.Test;

import rs.baselib.crypto.EncryptionUtils;

/**
 * Tests the licensing mechanism.
 * @author ralph
 *
 */
public class LicenseTest {

	private static KeyPair keyPair;
	private static LicenseGenerator generator;
	private static LicenseManager manager;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		keyPair = EncryptionUtils.generateKey("DSA", 512);
		generator = new LicenseGenerator(keyPair.getPrivate());
		manager = new LicenseManager(keyPair.getPublic());
		
		System.out.println("private="+Hex.encodeHexString(keyPair.getPrivate().getEncoded()));
		System.out.println("public ="+Hex.encodeHexString(keyPair.getPublic().getEncoded()));
	}

	@Test
	public void test() throws Exception {
		testLicense(1, 0L, "aLicenseHolder");
		testLicense(1, System.currentTimeMillis()+2000L, "aLicenseHolder");
		testLicense(1, System.currentTimeMillis()+2000L, "a2ndLicenseHolder");
		testLicense(2, System.currentTimeMillis()+2000L, "a2ndLicenseHolder");
	}

	/** Test positive and negative values */
	protected void testLicense(int productId, long expiryTime, String licenseHolder) {
		// Generate the license
		String s = generator.createLicenseKey(productId, expiryTime, licenseHolder);
		System.out.println("Testing license: "+s);
		
		// Test lifetime
		try {
			manager.verify(s, productId, licenseHolder);
		} catch (LicenseException e) {
			// This should not have failed
			fail("Verification failed with timestamp");
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
