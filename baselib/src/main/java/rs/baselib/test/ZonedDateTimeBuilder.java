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
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Builds {@link ZonedDateTime} objects.
 * @author ralph
 *
 */
public class ZonedDateTimeBuilder implements Builder<ZonedDateTime> {

	/** the current time */
	private ZonedDateTime current;
	/** The timezone to be used */
	private ZoneId zoneId = null;
	/** the start time */
	private ZonedDateTime start;
	/** the step for days and beyond */
	private Period   periodStep;
	/** the step for time */
	private Duration durationStep;
	/** the end time (only for random generation) */
	private ZonedDateTime end;
	/** whether to create random numbers */
	private boolean random = false;
	/** The long builder for random generation */
	private LongBuilder msBuilder;
	private ZonedDateTime last;
	
	/**
	 * Constructor.
	 */
	public ZonedDateTimeBuilder() {
		this.start        = null;
		this.current      = null;
		this.end          = null;
		this.periodStep   = Period.of(0, 0, 0);
		this.durationStep = Duration.ofSeconds(1);
	}

	/**
	 * Sets the zone ID to be produced.
	 * <p>If not set it will be the zone ID of the start time or respectively the current default.
	 * @param zoneId - the zone ID to produce
	 * @return this builder for concatenation
	 */
	public ZonedDateTimeBuilder withZoneId(ZoneId zoneId) {
		this.zoneId   = zoneId;
		return this;
	}

	/**
	 * Start the build with a given start date and time.
	 * <p>If not set it will be the current date time.
	 * @param start - the date time for the start (1st date)
	 * @return this builder for concatenation
	 */
	public ZonedDateTimeBuilder withStart(ZonedDateTime start) {
		this.start   = start;
		this.current = null;
		return this;
	}

	/**
	 * End date and time when generating random {@link ZonedDateTime} objects.
	 * @param start - the first number to produce
	 * @return this builder for concatenation
	 */
	public ZonedDateTimeBuilder withEnd(ZonedDateTime end) {
		this.end   = end;
		return this;
	}

	/**
	 * Set a given step for each build.
	 * @param ms - the duration in ms
	 * @return this builder for concatenation
	 */
	public ZonedDateTimeBuilder withStep(long ms) {
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
	public ZonedDateTimeBuilder withStep(Period period, Duration duration) {
		this.periodStep   = period   != null ? period   : Period.of(0, 0, 0);
		this.durationStep = duration != null ? duration : Duration.ZERO;
		return this;
	}

	/**
	 * Set random creation. Will respect end time set
	 * @return this builder for concatenation
	 */
	public ZonedDateTimeBuilder withRandom() {
		this.random   = true;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ZonedDateTime build() {
		if (random) {
			if (msBuilder == null) msBuilder = createRandomBuilder();
			current = ZonedDateTime.ofInstant(Instant.ofEpochMilli(msBuilder.build()), zoneId);
		} else if (current == null) {
			current = createCurrent();
		} else {
			current = current.plus(periodStep).plus(durationStep);
		}
		last = current;
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ZonedDateTime last() {
		return last;
	}

	/**
	 * Creates the current value based on start.
	 * @return the current value to be used
	 */
	protected ZonedDateTime createCurrent() {
		if (zoneId == null) zoneId = getZoneId();
		if (start == null) return getStart();
		return start.withZoneSameInstant(zoneId);
	}
	
	/**
	 * Get the zone id to be used (assuming it was not defined)
	 * @return zone id from start or default
	 */
	protected ZoneId getZoneId() {
		if (start == null) return ZoneId.systemDefault();
		return start.getZone();
	}
	
	/**
	 * Get the start using the correct zone id
	 * @return the start
	 */
	protected ZonedDateTime getStart() {
		if (zoneId == null) zoneId = getZoneId();
		if (start  == null) return ZonedDateTime.now(zoneId);
		return start.withZoneSameInstant(zoneId);
	}
	
	/**
	 * Creates the random builder.
	 * @return the random builder to time in ms
	 */
	protected LongBuilder createRandomBuilder() {
		if (start == null) throw new RuntimeException("Minimum ZonedDateTime is not set");
		if (end   == null) throw new RuntimeException("Maximum ZonedDateTime is not set");
		long start = this.start.toInstant().toEpochMilli();
		long end   = this.end.toInstant().toEpochMilli();
		if (zoneId == null) zoneId = getZoneId();
		return new LongBuilder().withStart(start).withEnd(end).withRandom();
	}
}
