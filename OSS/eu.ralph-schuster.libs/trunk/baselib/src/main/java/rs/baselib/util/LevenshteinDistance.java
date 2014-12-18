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
 * Implements Levensstein distance.
 * @author ralph
 * @since 1.2.9
 */
public class LevenshteinDistance implements DistanceCalculation {

	/** static instance */
	public static final DistanceCalculation INSTANCE = new LevenshteinDistance();
	
	/**
	 * Constructor.
	 */
	public LevenshteinDistance() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDistance(String s1, String s2) {
		if (s1 == null) s1 = "";
		if (s2 == null) s2 = "";
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		// i == 0
		int [] costs = new int [s2.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= s1.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= s2.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), s1.charAt(i - 1) == s2.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[s2.length()];
	}
}
