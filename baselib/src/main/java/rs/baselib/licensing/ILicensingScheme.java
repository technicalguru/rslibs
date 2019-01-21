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

/**
 * Describes a licensing scheme.
 * @author ralph
 *
 */
public interface ILicensingScheme {

	/**
	 * Returns an instance of the responsible license creator implementation.
	 * @return the license creator
	 */
	public ILicenseCreator getLicenseCreator();
	
	/**
	 * Returns an instance of the responsible license verifier implementation.
	 * @return the license verifier
	 */
	public ILicenseVerifier getLicenseVerifier();
	
}
