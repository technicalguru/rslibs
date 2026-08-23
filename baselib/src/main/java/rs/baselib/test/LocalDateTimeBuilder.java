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
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Builds {@link LocalDateTime} objects.
 * @author ralph
 *
 */
public class LocalDateTimeBuilder extends AbstractBuilder<LocalDateTime> {

	private static ZoneId DEFAULT = ZoneId.systemDefault();;
	
	/** the start time */
	private LocalDateTime start;
	/** the step for days and beyond */
	private Period   periodStep;
	/** the step for time */
	private Duration durationStep;
	/** the end time (only for random generation) */
	private LocalDateTime end;
	/** whether to create random numbers */
	private boolean random = false;
	/** The long builder for random generation */
	private ZonedDateTimeBuilder zonedBuilder;
	
	/**
	 * Constructor.
	 */
	public LocalDateTimeBuilder() {
		this.start        = null;
		this.end          = null;
		this.periodStep   = Period.of(0, 0, 1);
		this.durationStep = Duration.ZERO;
	}

	/**
	 * Start the build with a given start date and time.
	 * <p>If not set it will be the current date time. Start is inclusive (random value can be equal).
	 * @param start - the date time for the start (1st date)
	 * @return this builder for concatenation
	 */
	public LocalDateTimeBuilder withStart(LocalDateTime start) {
		this.start   = start;
		return this;
	}

	/**
	 * End date and time when generating random {@link LocalDateTime} objects.
	 * <p>End is exclusive (random value cannot be equal).
	 * @param start - the first date/time to produce
	 * @return this builder for concatenation
	 */
	public LocalDateTimeBuilder withEnd(LocalDateTime end) {
		this.end   = end;
		return this;
	}

	/**
	 * Set a given step for each build.
	 * @param ms - the duration in ms
	 * @return this builder for concatenation
	 */
	public LocalDateTimeBuilder withStep(long ms) {
		long msDay = ms % DateUtils.MILLIS_PER_DAY;
		ms = ms - msDay;
		return withStep(Period.ofDays((int)(ms / DateUtils.MILLIS_PER_DAY)), Duration.ofMillis(msDay));
	}
	
	/**
	 * Set a given step for each build.
	 * @param period - the period to produce (null for 0)
	 * @param duration - the duration to produce (null for 0)
	 * @return this builder for concatenation
	 */
	public LocalDateTimeBuilder withStep(Period period, Duration duration) {
		this.periodStep   = period   != null ? period   : Period.of(0, 0, 0);
		this.durationStep = duration != null ? duration : Duration.ZERO;
		return this;
	}

	/**
	 * Set random creation. Will respect end time set
	 * <p>Random value will be created so that start <= value < end.
	 * @return this builder for concatenation
	 */
	public LocalDateTimeBuilder withRandom() {
		this.random   = true;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalDateTime _build() {
		if (zonedBuilder == null) zonedBuilder = createZonedBuilder();
		return zonedBuilder.build().toLocalDateTime();
	}

	/**
	 * Creates the correct {@link ZonedDateTimeBuilder} as underlying builder.
	 * @return the builder
	 */
	protected ZonedDateTimeBuilder createZonedBuilder() {
		ZonedDateTimeBuilder rc = new ZonedDateTimeBuilder().withZoneId(DEFAULT).withStep(periodStep, durationStep);
		if (start != null) rc.withStart(start.atZone(DEFAULT));
		if (end   != null) rc.withEnd(end.atZone(DEFAULT));
		if (random)        rc.withRandom();
		return rc;
	}
}
