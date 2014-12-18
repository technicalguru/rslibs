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
 * Implementation of Hamming Distance from {@link http://en.wikipedia.org/wiki/Hamming_distance}
 * @author ralph
 * @since 1.2.9
 */
public class HammingDistance implements DistanceCalculation {

	/** static instance */
	public static final DistanceCalculation INSTANCE = new HammingDistance();

	/**
	 * {@inheritDoc}
	 */
	public int getDistance(String s1, String s2) {
		if (s1.length() != s2.length()) {
			return -1;
		}

		int counter = 0;

		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i)) counter++;
		}

		return counter;
	}

	///
	// Hamming distance works best with binary comparisons, this function takes a string arrary of binary
	// values and returns the minimum distance value
	///
	public int minDistance(String[] numbers) {
		int minDistance = Integer.MAX_VALUE;

		if (checkConstraints(numbers)) {
			for (int i = 1; i < numbers.length; i++) {
				int counter = 0;
				for (int j = 1; j <= numbers[i].length(); j++) {
					if (numbers[i-1].charAt(j-1) != numbers[i].charAt(j-1)) {
						counter++;
					}
				}

				if (counter == 0) return counter;
				if (counter < minDistance) minDistance = counter;
			}
		} else {
			return -1;
		}

		return minDistance;
	}

	private Boolean checkConstraints(String[] numbers) {
		if (numbers.length > 1 && numbers.length <=50) {
			int prevLength = -1;
			for (int i = 0; i < numbers.length; i++) {
				if ((numbers[i].length() > 0) && (numbers[i].length() <= 50)) {
					if (prevLength == -1) {
						prevLength = numbers[i].length();
					} else {
						if (prevLength != numbers[i].length()) {
							return false;
						}
					}

					for (int j = 0; j < numbers[i].length(); j++) {
						if ((numbers[i].charAt(j) != '0') && (numbers[i].charAt(j) != '1')) {
							return false;
						}
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}

		return true;
	}

}
