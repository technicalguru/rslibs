/**
 * 
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
	 * @throws java.lang.Exception
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
