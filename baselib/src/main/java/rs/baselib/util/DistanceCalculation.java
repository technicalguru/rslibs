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
package rs.baselib.util;

/**
 * Can calculate the distance between two strings.
 * @author ralph
 * @since 1.2.9
 *
 */
public interface DistanceCalculation {

	/**
	 * Return the distance according to the algorithm used.
	 * <p>Important! Both strings must not be null.</p>
	 * @param s1 first string
	 * @param s2 second string
	 * @return the distance between two strings
	 */
	public int getDistance(String s1, String s2);
}
