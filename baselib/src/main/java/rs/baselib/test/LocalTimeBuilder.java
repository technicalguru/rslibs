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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Builds {@link LocalTime} objects.
 * @author ralph
 *
 */
public class LocalTimeBuilder extends AbstractBuilder<LocalTime> {

	/** the start time */
	private LocalTime start;
	/** the step for incrementing */
	private Duration step;
	/** the end time (only for random generation) */
	private LocalTime end;
	/** whether to create random numbers */
	private boolean random = false;
	/** The long builder for random generation */
	private LocalDateTimeBuilder localBuilder;
	
	/**
	 * Constructor.
	 */
	public LocalTimeBuilder() {
		this.start        = null;
		this.end          = null;
		this.step         = Duration.ofSeconds(1);
	}

	/**
	 * Start the build with a given start time.
	 * <p>If not set it will be the current time. Start is inclusive (random value can be equal).
	 * @param start - the time for the start (1st time)
	 * @return this builder for concatenation
	 */
	public LocalTimeBuilder withStart(LocalTime start) {
		this.start   = start;
		return this;
	}

	/**
	 * End time when generating random {@link LocalTime} objects.
	 * <p>End is exclusive (random value cannot be equal).
	 * @param start - the first time to produce
	 * @return this builder for concatenation
	 */
	public LocalTimeBuilder withEnd(LocalTime end) {
		this.end   = end;
		return this;
	}

	/**
	 * Set a given step for each build.
	 * @param period - the period to produce (null for 0)
	 * @return this builder for concatenation
	 */
	public LocalTimeBuilder withStep(Duration duration) {
		this.step   = duration != null ? duration : Duration.ZERO;
		return this;
	}

	/**
	 * Set random creation. Will respect end time set
	 * <p>Random value will be created so that start <= value < end.
	 * @return this builder for concatenation
	 */
	public LocalTimeBuilder withRandom() {
		this.random   = true;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalTime _build() {
		if (localBuilder == null) localBuilder = createLocalBuilder();
		return localBuilder.build().toLocalTime();
	}

	/**
	 * Creates the correct {@link LocalDateTimeBuilder} as underlying builder.
	 * @return the builder
	 */
	protected LocalDateTimeBuilder createLocalBuilder() {
		LocalDateTimeBuilder rc = new LocalDateTimeBuilder().withStep(null, step);
		rc.withStart(LocalDateTime.of(LocalDate.now(), start != null ? start : LocalTime.now()));
		if (end   != null) rc.withEnd(LocalDateTime.of(LocalDate.now(), end));
		if (random)        rc.withRandom();
		return rc;
	}
}
