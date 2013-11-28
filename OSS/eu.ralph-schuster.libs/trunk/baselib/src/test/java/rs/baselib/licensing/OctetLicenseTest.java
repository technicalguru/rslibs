/**
 * 
 */
package rs.baselib.licensing;

import java.security.KeyPair;

import org.apache.commons.codec.binary.Hex;
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
public class OctetLicenseTest extends AbstractLicenseTest {

	private static KeyPair keyPair;
	private static LicenseGenerator generator;
	private static LicenseManager manager;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		keyPair = EncryptionUtils.generateKey("DSA", 512);
		generator = new LicenseGenerator(keyPair.getPrivate(), LicensingScheme.OCTETS);
		manager = new LicenseManager(keyPair.getPublic(), LicensingScheme.OCTETS);
		
		System.out.println("private="+Hex.encodeHexString(keyPair.getPrivate().getEncoded()));
		System.out.println("public ="+Hex.encodeHexString(keyPair.getPublic().getEncoded()));
	}

	public OctetLicenseTest(int productId, long expiryTime, String licenseHolder) {
		super(productId, expiryTime, licenseHolder);
	}
	
	@Test
	public void test() throws Exception {
		test(generator, manager);
	}
	
}
