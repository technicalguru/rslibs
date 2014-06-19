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

import java.io.Serializable;

/**
 * The interface a license has to fulfill.
 * @author ralph
 *
 */
public interface ILicense extends Serializable {

	/** A key for the product (Usage is optional). */
	public static String PRODUCT_KEY = "product";
	/** Key for expiration date (Usage is optional). */
	public static String EXPIRATION_DATE_KEY = "expirationDate";
	/** Key for owner (Usage is optional). */
	public static String OWNER_KEY = "owner";
	/** Key for minimum version (Usage is optional). */
	public static String MINIMUM_VERSION_KEY = "minVersion";
	/** Key for inclusion of minimum version (Usage is optional). */
	public static String MINIMUM_VERSION_INCLUDED_KEY = "minVersionIncluded";
	/** Key for maximum version (Usage is optional). */
	public static String MAXIMUM_VERSION_KEY = "maxVersion";
	/** Key for inclusion of maximum version (Usage is optional). */
	public static String MAXIMUM_VERSION_INCLUDED_KEY = "maxVersionIncluded";
	/** Key for version (Usage is optional). */
	public static String VERSION_KEY = "version";

	/**
	 * Initialize the license using the given context.
	 * @param context context containing parameters.
	 */
	public void init(ILicenseContext context);
	
	/**
	 * Verify the license using the given context
	 * @param context the context containing verification parameters
	 */
	public void verify(ILicenseContext context);
	
}
