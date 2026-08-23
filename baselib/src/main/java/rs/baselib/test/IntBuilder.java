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
package rs.baselib.test;

/**
 * An Integer builder.
 * @author ralph
 *
 */
public class IntBuilder extends AbstractBuilder<Integer> {

	/** end number (for random only) */
	private int start;
	/** the current value */
	private Integer current;
	/** the step */
	private int step;
	/** end number (for random only) */
	private int end;
	/** whether to create random numbers */
	private boolean random = false;
	
	/**
	 * Constructor.
	 */
	public IntBuilder() {
		this.start = 0;
		this.step  = 1;
	}

	/**
	 * Start the build with a given int.
	 * <p>Start is inclusive (random value can be equal).
	 * @param start - the first number to produce
	 * @return this builder for concatenation
	 */
	public IntBuilder withStart(int start) {
		this.start = start;
		return this;
	}

	/**
	 * Set a given increment/decrement for each build.
	 * @param step - the increment/decrement to produce
	 * @return this builder for concatenation
	 */
	public IntBuilder withStep(int step) {
		this.step   = step;
		return this;
	}

	/**
	 * Set a given max number (for random numbers only).
	 * <p>End is exclusive (random value cannot be equal).
	 * @param end - the max number to use
	 * @return this builder for concatenation
	 */
	public IntBuilder withEnd(int end) {
		this.end   = end;
		return this;
	}

	/**
	 * Set random creation.
	 * <p>Random value will be created so that start <= value < end.
	 * @return this builder for concatenation
	 */
	public IntBuilder withRandom() {
		this.random   = true;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer _build() {
		if (!random) {
			if (current == null) current  = start;
			else                 current += step;
		} else if (start == end) {
			current = start;
		} else if (start < end) {
			current = Integer.valueOf(BuilderUtils.RNG.nextInt(start, end));
		} else {
			current = Integer.valueOf(BuilderUtils.RNG.nextInt(end, start));
		}
		return current;
	}

}
