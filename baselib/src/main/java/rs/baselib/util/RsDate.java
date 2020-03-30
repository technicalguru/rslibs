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

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A {@link Date} replacement object to allow better date computations.
 * @author ralph
 *
 */
public class RsDate extends GregorianCalendar {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public RsDate() {
		this(TimeZone.getDefault());
	}

	/**
	 * Constructor.
	 * @param timezone - the timezone for this date
	 * @since 1.2.8
	 */
	public RsDate(TimeZone timezone) {
		super(timezone != null ? timezone : TimeZone.getDefault());
	}

	/**
	 * Constructor.
	 * @param date - init with this date
	 */
	public RsDate(Date date) {
		this(TimeZone.getDefault(), date);
	}

	/**
	 * Constructor.
	 * @param timezone - the timezone for this date
	 * @param date - init with this date
	 */
	public RsDate(TimeZone timezone, Date date) {
		setTimeZone(timezone != null ? timezone : TimeZone.getDefault());
		if (date != null) setTimeInMillis(date.getTime());
		else setTimeInMillis(0);
	}

	/**
	 * Constructor.
	 * @param timeInMillis - init with this timestamp
	 */
	public RsDate(long timeInMillis) {
		this(TimeZone.getDefault(), timeInMillis);
	}
	
	/**
	 * Constructor.
	 * @param timeInMillis - init with this timestamp
	 * @param timezone - the timezone for this date
	 */
	public RsDate(TimeZone timezone, long timeInMillis) {
		super(timezone != null ? timezone : TimeZone.getDefault());
		setTimeInMillis(timeInMillis);
	}

	/**
	 * Constructor.
	 * @param calendar - init with calendar time
	 */
	public RsDate(Calendar calendar) {
		setTimeInMillis(calendar.getTimeInMillis());
		setTimeZone(calendar.getTimeZone());
		get(DAY_OF_MONTH);
	}

	/**
	 * Constructor with default timezone.
	 * <p>time will be 00:00:00.000.</p>
	 * @param year year value
	 * @param month month value (0-11)
	 * @param dayOfMonth day of month value (1-31)
	 * @since 1.2.7
	 */
	public RsDate(int year, int month, int dayOfMonth) {
		this(TimeZone.getDefault(), year, month, dayOfMonth, 0, 0, 0, 0);
	}

	/**
	 * Constructor.
	 * <p>time will be 00:00:00.000.</p>
	 * @param timezone the timezone to be used
	 * @param year year value
	 * @param month month value (0-11)
	 * @param dayOfMonth day of month value (1-31)
	 * @since 1.2.8
	 */
	public RsDate(TimeZone timezone, int year, int month, int dayOfMonth) {
		this(timezone, year, month, dayOfMonth, 0, 0, 0, 0);
	}

	/**
	 * Constructor with default timezone.
	 * <p>{@link Calendar#SECOND}s and {@link Calendar#MILLISECOND}s will be 0.</p>
	 * @param year year value
	 * @param month month value (0-11)
	 * @param dayOfMonth day of month value (1-31)
	 * @param hourOfDay hoir of day (0-23)
	 * @param minute minute value (0-59)
	 * @since 1.2.7
	 */
	public RsDate(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
		this(TimeZone.getDefault(), year, month, dayOfMonth, hourOfDay, minute, 0, 0);
	}

	/**
	 * Constructor.
	 * <p>{@link Calendar#SECOND}s and {@link Calendar#MILLISECOND}s will be 0.</p>
	 * @param timezone the timezone to be used
	 * @param year year value
	 * @param month month value (0-11)
	 * @param dayOfMonth day of month value (1-31)
	 * @param hourOfDay hoir of day (0-23)
	 * @param minute minute value (0-59)
	 * @since 1.2.8
	 */
	public RsDate(TimeZone timezone, int year, int month, int dayOfMonth, int hourOfDay, int minute) {
		this(timezone, year, month, dayOfMonth, hourOfDay, minute, 0, 0);
	}

	/**
	 * Constructor with default timezone.
	 * <p>{@link Calendar#MILLISECOND}s will be 0.</p>
	 * @param year year value
	 * @param month month value (0-11)
	 * @param dayOfMonth day of month value (1-31)
	 * @param hourOfDay hoir of day (0-23)
	 * @param minute minute value (0-59)
	 * @param seconds seconds value(0-59)
	 * @since 1.2.7
	 */
	public RsDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int seconds) {
		this(TimeZone.getDefault(), year, month, dayOfMonth, hourOfDay, minute, seconds, 0);
	}
	
	/**
	 * Constructor.
	 * <p>{@link Calendar#MILLISECOND}s will be 0.</p>
	 * @param timezone the timezone to be used
	 * @param year year value
	 * @param month month value (0-11)
	 * @param dayOfMonth day of month value (1-31)
	 * @param hourOfDay hoir of day (0-23)
	 * @param minute minute value (0-59)
	 * @param seconds seconds value(0-59)
	 * @since 1.2.8
	 */
	public RsDate(TimeZone timezone, int year, int month, int dayOfMonth, int hourOfDay, int minute, int seconds) {
		this(timezone, year, month, dayOfMonth, hourOfDay, minute, seconds, 0);
	}
	
	/**
	 * Constructor with default timezone.
	 * @param year year value
	 * @param month month value (0-11)
	 * @param dayOfMonth day of month value (1-31)
	 * @param hourOfDay hoir of day (0-23)
	 * @param minute minute value (0-59)
	 * @param seconds seconds value(0-59)
	 * @param milliseconds milliseconds value (0-999)
	 * @since 1.2.7
	 */
	public RsDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int seconds, int milliseconds) {
		this(TimeZone.getDefault(), year, month, dayOfMonth, hourOfDay, minute, seconds, milliseconds);
	}
	
	/**
	 * Constructor.
	 * @param timezone the timezone to be used
	 * @param year year value
	 * @param month month value (0-11)
	 * @param dayOfMonth day of month value (1-31)
	 * @param hourOfDay hoir of day (0-23)
	 * @param minute minute value (0-59)
	 * @param seconds seconds value(0-59)
	 * @param milliseconds milliseconds value (0-999)
	 * @since 1.2.8
	 */
	public RsDate(TimeZone timezone, int year, int month, int dayOfMonth, int hourOfDay, int minute, int seconds, int milliseconds) {
		setTimeZone(timezone != null ? timezone : TimeZone.getDefault());
		set(Calendar.YEAR,         year);
		set(Calendar.MONTH,        month);
		set(Calendar.DAY_OF_MONTH, dayOfMonth);
		set(Calendar.HOUR_OF_DAY,  hourOfDay);
		set(Calendar.MINUTE,       minute);
		set(Calendar.SECOND,       seconds);
		set(Calendar.MILLISECOND,  milliseconds);
	}
	
	/**
	 * Returns the year object for this date.
	 * @return year
	 */
	public RsYear getYear() {
		return new RsYear(getTimeZone(), getTimeInMillis());
	}
	
	/**
	 * Returns the month object for this date.
	 * @return month
	 */
	public RsMonth getMonth() {
		return new RsMonth(getTimeZone(), getTimeInMillis());
	}
	
	/**
	 * Returns the day object for this date.
	 * @return day
	 */
	public RsDay getDay() {
		return new RsDay(getTimeZone(), getTimeInMillis());
	}
	
	/**
	 * Create a date of given timestamp or null.
	 * @param date date to be represented
	 * @return the RsDate object
	 */
	public static RsDate get(Date date) {
		if (date == null) return null;
		return get(date.getTime());
	}

	/**
	 * Create a date of given timestamp or null.
	 * @param timestamp time to be represented
	 * @return the RsDate object
	 */
	public static RsDate get(Timestamp timestamp) {
		if (timestamp == null) return null;
		return get(timestamp.getTime());
	}

	/**
	 * Create a date of given timestamp or null.
	 * @param timestamp timestamp
	 * @return RsDate object
	 */
	public static RsDate get(long timestamp) {
		if (timestamp == 0) return null;
		return new RsDate(timestamp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "RsDate ["+toZonedDateTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)+"]";
	}
	
	
}
