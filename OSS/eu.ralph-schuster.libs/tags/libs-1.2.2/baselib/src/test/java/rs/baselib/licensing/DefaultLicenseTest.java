/**
 * 
 */
package rs.baselib.licensing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link DefaultLicense} class.
 * @author ralph
 *
 */
public class DefaultLicenseTest {

	private static Date expirationDate;
	
	@BeforeClass
	public static void setupClass() {
		expirationDate = new Date(System.currentTimeMillis()+3*DateUtils.MILLIS_PER_DAY);
	}
	
	protected DefaultLicenseContext createInitContext(String product, String owner, Date expirationDate, String minVersion, boolean minIncluded, String maxVersion, boolean maxIncluded) {
		DefaultLicenseContext context = new DefaultLicenseContext();
		if (product != null) {
			context.set(ILicense.PRODUCT_KEY, product);
		}
		if (owner != null) {
			context.set(ILicense.OWNER_KEY, owner);
		}
		if (expirationDate != null) {
			context.set(ILicense.EXPIRATION_DATE_KEY, expirationDate);
		}
		if (minVersion != null) {
			context.set(ILicense.MINIMUM_VERSION_KEY, minVersion);
			context.set(ILicense.MINIMUM_VERSION_INCLUDED_KEY, minIncluded);
		}
		if (maxVersion != null) {
			context.set(ILicense.MAXIMUM_VERSION_KEY, maxVersion);
			context.set(ILicense.MAXIMUM_VERSION_INCLUDED_KEY, maxIncluded);		
		}
		return context;
	}
	
	protected DefaultLicense createLicense(String product, String owner, Date expirationDate, String minVersion, boolean minIncluded, String maxVersion, boolean maxIncluded) {
		DefaultLicense license = new DefaultLicense();
		DefaultLicenseContext context = createInitContext(product, owner, expirationDate, minVersion, minIncluded, maxVersion, maxIncluded);
		license.init(context);
		return license;
	}

	protected DefaultLicenseContext createVerifyContext(String product, String owner, Date expirationDate, String version) {
		DefaultLicenseContext context = new DefaultLicenseContext();
		if (product != null) {
			context.set(ILicense.PRODUCT_KEY, product);
		}
		if (owner != null) {
			context.set(ILicense.OWNER_KEY, owner);
		}
		if (expirationDate != null) {
			context.set(ILicense.EXPIRATION_DATE_KEY, expirationDate);
		}
		if (version != null) {
			context.set(ILicense.VERSION_KEY, version);
		}
		return context;
	}
	
	/**
	 * Test method for {@link DefaultLicense#init(ILicenseContext)}.
	 */
	@Test
	public void testInit() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, "4.0.0", true);
		assertEquals("Product not initialized", "product", license.getProduct());
		assertEquals("Owner not initialized", "owner", license.getOwner());
		assertEquals("Expiration Date not initialized", expirationDate, license.getExpirationDate());
		assertEquals("Min Version not initialized", "2.0.0", license.getMinVersion());
		assertEquals("Max Version not initialized", "4.0.0", license.getMaxVersion());
		assertEquals("Min Included Flag not initialized", true, license.isMinVersionIncluded());
		assertEquals("Max Included Flag not initialized", true, license.isMaxVersionIncluded());
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (included minVersion).
	 */
	@Test
	public void testVerify1() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "2.0.0");
		assertValid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (included maxVersion).
	 */
	@Test
	public void testVerify2() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "4.0.0");
		assertValid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (version inbetween).
	 */
	@Test
	public void testVerify3() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "3.0.0");
		assertValid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (before minVersion).
	 */
	@Test
	public void testVerify4() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "1.9.9");
		assertInvalid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (after maxVersion).
	 */
	@Test
	public void testVerify5() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "4.0.1");
		assertInvalid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (excluded minVersion).
	 */
	@Test
	public void testVerify6() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", false, "4.0.0", false);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "2.0.0");
		assertInvalid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (excluded maxVersion).
	 */
	@Test
	public void testVerify7() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", false, "4.0.0", false);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "4.0.0");
		assertInvalid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (no minVersion, ok).
	 */
	@Test
	public void testVerify8() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, null, false, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "3.0.0");
		assertValid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (no maxVersion, ok).
	 */
	@Test
	public void testVerify9() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, null, false);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "3.0.0");
		assertValid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (no minVersion, not ok).
	 */
	@Test
	public void testVerify10() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, null, false, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "4.0.1");
		assertInvalid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (no maxVersion, not ok).
	 */
	@Test
	public void testVerify11() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, null, false);
		DefaultLicenseContext context = createVerifyContext("product", "owner", expirationDate, "1.9.9");
		assertInvalid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (expiration).
	 */
	@Test
	public void testVerify12() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", createDate(5), "3.0.0");
		assertInvalid(license, context);
	}

	/**
	 * Test method for {@link DefaultLicense#verify(ILicenseContext)} (expiration).
	 */
	@Test
	public void testVerify13() {
		DefaultLicense license = createLicense("product", "owner", expirationDate, "2.0.0", true, "4.0.0", true);
		DefaultLicenseContext context = createVerifyContext("product", "owner", createDate(-2), "3.0.0");
		assertValid(license, context);
	}

	protected void assertValid(ILicense license, ILicenseContext context) {
		license.verify(context);
	}
	
	protected void assertInvalid(ILicense license, ILicenseContext context) {
		try {
			license.verify(context);
			fail("License validation was incorrect");
		} catch (LicenseException e) {
			// OK
		}
	}
	
	protected Date createDate(int dayDifference) {
		return new Date(expirationDate.getTime()+dayDifference*DateUtils.MILLIS_PER_DAY);
	}
}
