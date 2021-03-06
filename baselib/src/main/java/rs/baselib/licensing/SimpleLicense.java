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

import java.io.UnsupportedEncodingException;
import java.util.Date;

import rs.baselib.io.ConverterUtils;
import rs.baselib.lang.LangUtils;

/**
 * A limited, simple implementation of a license.
 * The license knows the product as an integer only and does
 * not care for any version.
 * @author ralph
 *
 */
public class SimpleLicense extends AbstractLicense {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public SimpleLicense() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(ILicenseContext context) {
		initProperty(PRODUCT_KEY, context);
		initProperty(EXPIRATION_DATE_KEY, context);
		initProperty(OWNER_KEY, context);
	}

	/**
	 * Returns the product ID.
	 * @return the product ID
	 */
	public int getProduct() {
		return LangUtils.getInt(getProperty(PRODUCT_KEY), 0);
	}

	/**
	 * Sets the product ID.
	 * @param i the product ID
	 */
	public void setProduct(int i) {
		setProperty(PRODUCT_KEY, i);
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
	public long getExpiration() {
		return LangUtils.getLong(getProperty(EXPIRATION_DATE_KEY), 0);
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
	}

	/**
	 * Serializes this license into bytes.
	 * @param license the license to be serialized
	 * @return the bytes representing this license.
	 * @throws UnsupportedEncodingException when the license cannot be encoded
	 */
	public static byte[] serialize(SimpleLicense license) throws UnsupportedEncodingException {
		byte p[] = ConverterUtils.toBytes(license.getProduct());
		byte d[] = ConverterUtils.toBytes((int)(license.getExpiration()/1000));
		String lh = license.getOwner();
		if (lh == null) lh = "";
		byte s[] = ConverterUtils.toBytes(lh);
		
		byte rc[] = new byte[s.length+6];
		System.arraycopy(p, 2, rc, 0, 2);
		System.arraycopy(d, 0, rc, 2, 4);
		if (s.length > 0) System.arraycopy(s, 0, rc, 6, s.length);
		return rc;
	}

}
