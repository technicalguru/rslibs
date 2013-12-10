/**
 * 
 */
package rs.baselib.licensing;

import java.util.Date;

import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;
import rs.baselib.util.RsDate;

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
		initProperty(MAXIMUM_VERSION_INCLUDED_KEY, context);
	}

	/**
	 * Returns the product name.
	 * @return the product name
	 */
	public String getProduct() {
		return LangUtils.getString(getProperty(PRODUCT_KEY));
	}

	/**
	 * Sets the product name.
	 * @param s the product name
	 */
	public void setProduct(String s) {
		setProperty(PRODUCT_KEY, s);
	}

	/**
	 * Returns the expiration date.
	 * @return the date or <code>null</code> if not set
	 */
	public Date getExpirationDate() {
		long l = getExpiration();
		if (l == 0L) return null;
		return new Date(l);
	}

	/**
	 * Returns the expiration date.
	 * @return the date or <code>0</code> if not set
	 */
	private long getExpiration() {
		return getDate(getProperty(EXPIRATION_DATE_KEY));
	}

	/**
	 * Sets the new expiration date
	 * @param d the new date or <code>null</code> if no expiration
	 */
	public void setExpirationDate(Date d) {
		if (d != null) setProperty(EXPIRATION_DATE_KEY, d.getTime());
		else removeProperty(EXPIRATION_DATE_KEY);
	}

	/**
	 * Returns the license owner.
	 * @return the license owner
	 */
	public String getOwner() {
		return LangUtils.getString(getProperty(OWNER_KEY));
	}

	/**
	 * Sets the license owner.
	 * @param s the license owner
	 */
	public void setOwner(String s) {
		setProperty(OWNER_KEY, s);
	}

	/**
	 * Returns whether verification includes the minimum version.
	 * @return <code>true</code> when minimum version is included
	 */
	public boolean isMinVersionIncluded() {
		return LangUtils.getBoolean(getProperty(MINIMUM_VERSION_INCLUDED_KEY));
	}

	/**
	 * Sets whether verification includes the minimum version.
	 * @param b <code>true</code> when minimum version is included
	 */
	public void setMinVersionIncluded(boolean b) {
		setProperty(MINIMUM_VERSION_INCLUDED_KEY, Boolean.valueOf(b));
	}

	/**
	 * Returns the minimum version this license is valid for.
	 * @return the minimum version or <code>null</code> if no such version
	 */
	public String getMinVersion() {
		return LangUtils.getString(getProperty(MINIMUM_VERSION_KEY));
	}

	/**
	 * Sets the minimum version this license is valid for.
	 * @param s the minimum version or <code>null</code> if no such version
	 */
	public void setMinVersion(String s) {
		setProperty(MINIMUM_VERSION_KEY, s);
	}

	/**
	 * Returns whether verification includes the maximum version.
	 * @return <code>true</code> when maximum version is included
	 */
	public boolean isMaxVersionIncluded() {
		return LangUtils.getBoolean(getProperty(MAXIMUM_VERSION_INCLUDED_KEY));
	}

	/**
	 * Sets whether verification includes the maximum version.
	 * @param b <code>true</code> when maximum version is included
	 */
	public void setMaxVersionIncluded(boolean b) {
		setProperty(MAXIMUM_VERSION_INCLUDED_KEY, Boolean.valueOf(b));
	}

	/**
	 * Returns the maximum version this license is valid for.
	 * @return the maximum version or <code>null</code> if no such version
	 */
	public String getMaxVersion() {
		return LangUtils.getString(getProperty(MAXIMUM_VERSION_KEY));
	}

	/**
	 * Sets the maximum version this license is valid for.
	 * @param s the maximum version or <code>null</code> if no such version
	 */
	public void setMaxVersion(String s) {
		setProperty(MAXIMUM_VERSION_KEY, s);
	}

	/**
	 * Returns the time in millis for the given object.
	 * @param o an object (a date or long value)
	 * @return the time in milliseconds
	 */
	protected long getDate(Object o) {
		if (o instanceof Date) {
			return ((Date)o).getTime();
		} else if (o instanceof RsDate) {
			return ((RsDate)o).getTimeInMillis();
		}
		return LangUtils.getLong(o, 0);
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
			long l = getDate(context.get(EXPIRATION_DATE_KEY));
			if (d.getTime() < l) {
				throw new LicenseException("License expired on "+d.toString());
			}
		}
		String version = (String)context.get(VERSION_KEY);
		if (version != null) {
			if (hasProperty(MINIMUM_VERSION_KEY)) {
				int cmp = CommonUtils.compareVersion(getMinVersion(), version);
				if (isMinVersionIncluded() && (cmp > 0)) {
					throw new LicenseException("License is not valid for this version ("+version+"). "+toString());
				} else if (!isMinVersionIncluded() && (cmp >= 0)) {
					throw new LicenseException("License is not valid for this version ("+version+"). "+toString());
				}
			}
			if (hasProperty(MAXIMUM_VERSION_KEY)) {
				int cmp = CommonUtils.compareVersion(getMaxVersion(), version);
				if (isMaxVersionIncluded() && (cmp < 0)) {
					throw new LicenseException("License is not valid for this version ("+version+"). "+toString());
				} else if (!isMaxVersionIncluded() && (cmp <= 0)) {
					throw new LicenseException("License is not valid for this version ("+version+"). "+toString());
				}
			}
		}
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Valid for \""+getProduct()+"\"");
		String minVersion = getMinVersion();
		String maxVersion = getMaxVersion();
		if ((minVersion != null) || (maxVersion != null)) {
			if (minVersion != null) {
				if (isMinVersionIncluded()) buf.append('[');
				else buf.append('(');
				buf.append(minVersion);
			} else {
				buf.append('(');
			}
			buf.append(',');
			if (maxVersion != null) {
				buf.append(maxVersion);
				if (isMaxVersionIncluded()) buf.append(']');
				else buf.append(')');
			} else {
				buf.append(')');
			}
		}

		Date d = getExpirationDate();
		if (d != null) {
			buf.append(" until ");
			buf.append(CommonUtils.DATE_TIME_FORMATTER.format(d));
		} else {
			buf.append(" (unlimited)");
		}
		buf.append(". Licensed to \""+getOwner()+"\".");
		return buf.toString();
	}
}
