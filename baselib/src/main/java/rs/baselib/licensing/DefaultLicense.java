/**
 * 
 */
package rs.baselib.licensing;

import java.util.Date;

import rs.baselib.lang.LangUtils;

/**
 * Default implementation of a license.
 * @author ralph
 *
 */
public class DefaultLicense extends AbstractLicense {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	public static String PRODUCT_KEY = "product";
	public static String EXPIRATION_DATE_KEY = "expirationDate";
	public static String OWNER_KEY = "owner";
	public static String MINIMUM_VERSION_KEY = "minVersion";
	public static String MINIMUM_VERSION_INCLUDED_KEY = "minVersionIncluded";
	public static String MAXIMUM_VERSION_KEY = "maxVersion";
	public static String MAXIMUM_VERSION_INCLUDED_KEY = "maxVersionIncluded";

	/**
	 * Constructor.
	 */
	public DefaultLicense() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(ILicenseContext context) {
		initProperty(PRODUCT_KEY, context);
		initProperty(EXPIRATION_DATE_KEY, context);
		initProperty(OWNER_KEY, context);
		initProperty(MINIMUM_VERSION_KEY, context);
		initProperty(MINIMUM_VERSION_INCLUDED_KEY, context);
		initProperty(MAXIMUM_VERSION_KEY, context);
		initProperty(MAXIMUM_VERSION_KEY, context);
	}

	public String getProduct() {
		return LangUtils.getString(getProperty(PRODUCT_KEY));
	}

	public void setProduct(String s) {
		setProperty(PRODUCT_KEY, s);
	}

	public Date getExpirationDate() {
		long l = getExpiration();
		if (l == 0L) return null;
		return new Date(l);
	}

	private long getExpiration() {
		return LangUtils.getLong(getProperty(EXPIRATION_DATE_KEY), 0);
	}

	public void setExpirationDate(Date d) {
		if (d != null) setProperty(EXPIRATION_DATE_KEY, d.getTime());
		else removeProperty(EXPIRATION_DATE_KEY);
	}

	public String getOwner() {
		return LangUtils.getString(getProperty(OWNER_KEY));
	}

	public void setOwner(String s) {
		setProperty(OWNER_KEY, s);
	}

	public boolean isMinVersionIncluded() {
		return LangUtils.getBoolean(getProperty(MINIMUM_VERSION_INCLUDED_KEY));
	}

	public void setMinVersionIncluded(boolean b) {
		setProperty(MINIMUM_VERSION_INCLUDED_KEY, Boolean.valueOf(b));
	}

	public String getMinVersion() {
		return LangUtils.getString(getProperty(MINIMUM_VERSION_KEY));
	}

	public void setMinVersion(String s) {
		setProperty(MINIMUM_VERSION_KEY, s);
	}

	public boolean isMaxVersionIncluded() {
		return LangUtils.getBoolean(getProperty(MAXIMUM_VERSION_INCLUDED_KEY));
	}

	public void setMaxVersionIncluded(boolean b) {
		setProperty(MAXIMUM_VERSION_INCLUDED_KEY, Boolean.valueOf(b));
	}

	public String getMaxVersion() {
		return LangUtils.getString(getProperty(MAXIMUM_VERSION_KEY));
	}

	public void setMaxVersion(String s) {
		setProperty(MAXIMUM_VERSION_KEY, s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void verify(ILicenseContext context) {
		verifyEquals(PRODUCT_KEY, context, true);
		verifyEquals(OWNER_KEY, context, true);
		if (!context.hasKey(EXPIRATION_DATE_KEY)) {
			context.set(EXPIRATION_DATE_KEY, System.currentTimeMillis());
		}
		Date d = getExpirationDate();
		if (d != null) {
			long l = LangUtils.getLong(context.get(EXPIRATION_DATE_KEY), 0);
			if (d.getTime() < l) {
				throw new LicenseException("License expired on "+d.toString());
			}
		}
		if (hasProperty(MINIMUM_VERSION_KEY)) {
			if (isMinVersionIncluded()) {
				verifyEqualLessThan(MINIMUM_VERSION_KEY, context, false);
			} else {
				verifyLessThan(MINIMUM_VERSION_KEY, context, false);
			}
		}
		if (hasProperty(MAXIMUM_VERSION_KEY)) {
			if (isMaxVersionIncluded()) {
				verifyEqualGreaterThan(MAXIMUM_VERSION_KEY, context, false);
			} else {
				verifyGreaterThan(MAXIMUM_VERSION_KEY, context, false);
			}
		}
	}

}
