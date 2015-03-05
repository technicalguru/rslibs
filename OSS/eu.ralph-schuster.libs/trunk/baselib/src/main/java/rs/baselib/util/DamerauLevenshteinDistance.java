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
 * Damerau Levenshstein distance from {@link http://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance}. 
 * @author ralph
 * @since 1.2.9
 *
 */
public class DamerauLevenshteinDistance implements DistanceCalculation {

	/** static instance */
	public static final DistanceCalculation INSTANCE = new DamerauLevenshteinDistance();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDistance(String s1, String s2) {
		int matrix[][] = getMatrix(s1, s2);
		return matrix[s1.length()][s2.length()];
	}

	public int[][] getMatrix(String s1, String s2) {
		int cost = -1;
		int del, sub, ins;

		int matrix[][] = new int[s1.length()+1][s2.length()+1];

		for (int i = 0; i <= s1.length(); i++) {
			matrix[i][0] = i;
		}

		for (int i = 0; i <= s2.length(); i++) {
			matrix[0][i] = i;
		}

		for (int i = 1; i <= s1.length(); i++) {
			for (int j = 1; j <= s2.length(); j++) {
				if (s1.charAt(i-1) == s2.charAt(j-1)) {
					cost = 0;
				} else {
					cost = 1;
				}

				del = matrix[i-1][j]+1;
				ins = matrix[i][j-1]+1;
				sub = matrix[i-1][j-1]+cost;

				matrix[i][j] = minimum(del,ins,sub);

				if ((i > 1) && (j > 1) && (s1.charAt(i-1) == s2.charAt(j-2)) && (s1.charAt(i-2) == s2.charAt(j-1))) {
					matrix[i][j] = minimum(matrix[i][j], matrix[i-2][j-2]+cost);
				}
			}
		}

		return matrix;
	}

	private int minimum(int d, int i, int s) {
		int m = Integer.MAX_VALUE;

		if (d < m) m = d;
		if (i < m) m = i;
		if (s < m) m = s;

		return m;
	}

	private int minimum(int d, int t) {
		int m = Integer.MAX_VALUE;

		if (d < m) m = d;
		if (t < m) m = t;

		return m;
	}

}
