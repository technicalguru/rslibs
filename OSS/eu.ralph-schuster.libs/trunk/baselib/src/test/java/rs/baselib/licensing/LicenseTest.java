/**
 * 
 */
package rs.baselib.licensing;

import java.security.KeyPair;

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
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		keyPair = EncryptionUtils.generateKey("DSA", 512);
	}

	@Test
	public void test() throws Exception {
		LicenseGenerator generator = new LicenseGenerator(keyPair.getPrivate());
		String s = generator.createLicenseKey(1, 0L, "aLicenseHolder");
		System.out.println("Testing license: "+s);
		LicenseManager manager = new LicenseManager(keyPair.getPublic());
		manager.verify(s, 1, "aLicenseHolder");
	}

}
