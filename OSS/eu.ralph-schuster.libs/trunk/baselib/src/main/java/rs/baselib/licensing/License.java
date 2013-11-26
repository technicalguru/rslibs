/**
 * 
 */
package rs.baselib.licensing;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import rs.baselib.io.ConverterUtils;
import rs.baselib.util.RsDate;

/**
 * A decoded license.
 * @author ralph
 *
 */
public class License {

	/** The ID of the product */
	private int productId;
	/** The validity of the license (end date) */
	private long expiryTime;
	/** The license holder */
	private String licenseHolder;
	
	/**
	 * Constructor.
	 */
	public License() {
	}

	/**
	 * Constructor.
	 */
	public License(int productId, long expiryTime, String licenseHolder) {
		setProductId(productId);
		setExpiryTime(expiryTime);
		setLicenseHolder(licenseHolder);
	}

	/**
	 * Returns the {@link #productId}.
	 * @return the productId
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * Sets the {@link #productId}.
	 * @param productId the productId to set
	 */
	public void setProductId(int productId) {
		if ((productId < 0) || (productId > 65535)) throw new LicenseException("Invalid product ID: "+productId);
		this.productId = productId;
	}

	/**
	 * Returns the {@link #expiryTime}.
	 * @return the expiryTime
	 */
	public long getExpiryTime() {
		return expiryTime;
	}

	/**
	 * Sets the {@link #expiryTime}.
	 * @param expiryTime the expiryTime to set
	 */
	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}

	/**
	 * Returns the {@link #licenseHolder}.
	 * @return the licenseHolder
	 */
	public String getLicenseHolder() {
		return licenseHolder;
	}

	/**
	 * Sets the {@link #licenseHolder}.
	 * @param licenseHolder the licenseHolder to set
	 */
	public void setLicenseHolder(String licenseHolder) {
		this.licenseHolder = licenseHolder;
	}

	/**
	 * Returns whether this license has expired or not (using the current system time).
	 */
	public boolean isExpired() {
		long t = getExpiryTime();
		if (t > 0) return System.currentTimeMillis() > t;
		return false;
	}
	
	/**
	 * Returns the expiry time as a date object.
	 * @return the expiry time object or <code>null</code> if not set
	 */
	public Date getExpiryDate() {
		long t = getExpiryTime();
		if (t > 0) return new Date(t);
		return null;
	}
	
	/**
	 * Returns the expiry time as a RS date object.
	 * @return the expiry time object or <code>null</code> if not set
	 */
	public RsDate getExpiryRsDate() {
		long t = getExpiryTime();
		if (t > 0) return new RsDate(t);
		return null;
	}
	
	/**
	 * Serializes this license into bytes.
	 * @return the bytes representing this license.
	 */
	public static byte[] serialize(License license) throws UnsupportedEncodingException {
		byte p[] = ConverterUtils.toBytes(license.getProductId());
		byte d[] = ConverterUtils.toBytes((int)(license.getExpiryTime()/1000));
		String lh = license.getLicenseHolder();
		if (lh == null) lh = "";
		byte s[] = ConverterUtils.toBytes(lh);
		
		byte rc[] = new byte[s.length+6];
		System.arraycopy(p, 2, rc, 0, 2);
		System.arraycopy(d, 0, rc, 2, 4);
		if (s.length > 0) System.arraycopy(s, 0, rc, 6, s.length);
		return rc;
	}
	
	/**
	 * Returns the license from the given bytes. 
	 * @param bytes the bytes representing a license
	 * @return the license
	 * @throws UnsupportedEncodingException
	 */
	public static License unserialize(byte bytes[]) throws UnsupportedEncodingException {
		if (bytes == null) throw new LicenseException("Cannot unserialize license from 0 bytes");
		if (bytes.length < 6) throw new LicenseException("Cannot unserialize license from "+bytes.length+" bytes");
		License rc = new License();
		byte b[] = new byte[4];
		System.arraycopy(bytes, 0, b, 2, 2);
		rc.setProductId(ConverterUtils.toInt(b));
		System.arraycopy(bytes, 2, b, 0, 4);
		rc.setExpiryTime(ConverterUtils.toInt(b)*1000L);
		if (bytes.length > 6) {
			b = Arrays.copyOfRange(bytes, 6, bytes.length);
			rc.setLicenseHolder(ConverterUtils.toString(b).trim());
		}
		return rc;
	}
}
