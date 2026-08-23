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

import java.time.LocalDate;
import java.time.Period;

/**
 * Builds {@link LocalDate} objects.
 * @author ralph
 *
 */
public class LocalDateBuilder extends AbstractBuilder<LocalDate> {

	/** the start time */
	private LocalDate start;
	/** the step for days and beyond */
	private Period   periodStep;
	/** the end time (only for random generation) */
	private LocalDate end;
	/** whether to create random numbers */
	private boolean random = false;
	/** The long builder for random generation */
	private LocalDateTimeBuilder localBuilder;
	
	/**
	 * Constructor.
	 */
	public LocalDateBuilder() {
		this.start        = null;
		this.end          = null;
		this.periodStep   = Period.of(0, 0, 1);
	}

	/**
	 * Start the build with a given start date.
	 * <p>If not set it will be the current date. Start is inclusive (random value can be equal)
	 * @param start - the date for the start (1st date)
	 * @return this builder for concatenation
	 */
	public LocalDateBuilder withStart(LocalDate start) {
		this.start   = start;
		return this;
	}

	/**
	 * End date when generating random {@link LocalDate} objects.
	 * <p>End is exclusive (random value cannot be equal).
	 * @param start - the first date to produce
	 * @return this builder for concatenation
	 */
	public LocalDateBuilder withEnd(LocalDate end) {
		this.end   = end;
		return this;
	}

	/**
	 * Set a given step for each build.
	 * @param period - the period to produce (null for 0)
	 * @return this builder for concatenation
	 */
	public LocalDateBuilder withStep(Period period) {
		this.periodStep   = period   != null ? period   : Period.of(0, 0, 0);
		return this;
	}

	/**
	 * Set random creation. Will respect end time set
	 * <p>Random value will be created so that start <= value < end.
	 * @return this builder for concatenation
	 */
	public LocalDateBuilder withRandom() {
		this.random   = true;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalDate _build() {
		if (localBuilder == null) localBuilder = createLocalBuilder();
		return localBuilder.build().toLocalDate();
	}

	/**
	 * Creates the correct {@link LocalDateTimeBuilder} as underlying builder.
	 * @return the builder
	 */
	protected LocalDateTimeBuilder createLocalBuilder() {
		LocalDateTimeBuilder rc = new LocalDateTimeBuilder().withStep(periodStep, null);
		rc.withStart(start != null ? start.atStartOfDay() : LocalDate.now().atStartOfDay());
		if (end   != null) rc.withEnd(end.atStartOfDay());
		if (random)        rc.withRandom();
		return rc;
	}
}
