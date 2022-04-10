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

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A day.
 * @author Ralph
 *
 */
public class RsDay extends RsDate {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	private static SimpleDateFormat KEY_PARSER() {
		return new SimpleDateFormat("yyyyMMdd");
	}
	
	/**
	 * Constructor.
	 */
	public RsDay() {
		this(TimeZone.getDefault());
	}

	/**
	 * Constructor.
	 * @param timezone - init with this timezone
	 * @since 1.2.8
	 */
	public RsDay(TimeZone timezone) {
		super(timezone);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param date - init with this date
	 */
	public RsDay(Date date) {
		super(date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timezone - init with this timezone
	 * @param date - init with this date
	 */
	public RsDay(TimeZone timezone, Date date) {
		super(timezone, date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timeInMillis - init with this timestamp
	 */
	public RsDay(long timeInMillis) {
		super(timeInMillis);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timezone - init with this timezone
	 * @param timeInMillis - init with this timestamp
	 */
	public RsDay(TimeZone timezone, long timeInMillis) {
		super(timezone, timeInMillis);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param calendar calendar to be based on
	 */
	public RsDay(Calendar calendar) {
		super(calendar);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param day day of month value (1-31)
	 * @param month month value (0-11)
	 * @param year year value
	 * @since 1.2.7
	 */
	public RsDay(int day, int month, int year) {
		this(TimeZone.getDefault(), day, month, year);
	}

	/**
	 * Constructor.
	 * @param timezone - init with this timezone
	 * @param day day of month value (1-31)
	 * @param month month value (0-11)
	 * @param year year value
	 * @since 1.2.8
	 */
	public RsDay(TimeZone timezone, int day, int month, int year) {
		super(timezone, 0);
		set(DAY_OF_MONTH, day);
		set(MONTH, month);
		set(YEAR, year);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timestamp the zoned date time object from JavaTime API
	 * @since 3.0.0
	 */
	public RsDay(ZonedDateTime timestamp) {
		this(TimeZone.getTimeZone(timestamp.getZone()), timestamp.getDayOfMonth(), timestamp.getMonthValue()-1, timestamp.getYear());
	}
	
	/**
	 * Adjusts the time stamp to the start of the day.
	 */
	protected void ensureBegin() {
		set(HOUR_OF_DAY, 0);
		set(MINUTE, 0);
		set(SECOND, 0);
		set(MILLISECOND, 0);
		getKey();
	}
	
	/**
	 * Returns the key of this day.
	 * @return the key of this day.
	 */
	public String getKey() {
		StringBuffer rc = new StringBuffer();
		rc.append(get(YEAR));
		int m = get(MONTH);
		if (m < 9) rc.append('0');
		rc.append(m+1);
		int d = get(DAY_OF_MONTH);
		if (d < 10) rc.append('0');
		rc.append(d);
		return rc.toString();
	}
	
	/**
	 * Returns the begin of this day.
	 * @return begin of day
	 */
	public RsDate getBegin() {
		RsDate rc = new RsDate(getTimeZone(), getTimeInMillis());
		rc.set(HOUR_OF_DAY, 0);
		rc.set(MINUTE, 0);
		rc.set(SECOND, 0);
		rc.set(MILLISECOND, 0);
		return rc;
	}
	
	/**
	 * Returns the end of this day.
	 * @return begin of day
	 */
	public RsDate getEnd() {
		RsDate rc = new RsDate(getTimeZone(), getTimeInMillis());
		rc.set(HOUR_OF_DAY, rc.getActualMaximum(HOUR_OF_DAY));
		rc.set(MINUTE, rc.getActualMaximum(MINUTE));
		rc.set(SECOND, rc.getActualMaximum(SECOND));
		rc.set(MILLISECOND, rc.getActualMaximum(MILLISECOND));
		return rc;
	}
	
	/**
	 * Returns the next day.
	 * @param count number of days to add
	 * @return next day
	 */
	public RsDay getNext() {
		return roll(1);
	}
	
	/**
	 * Returns the n-th day after this day.
	 * @param count number of days to add
	 * @return n-th day after
	 * @since 3.0.0
	 */
	public RsDay getNext(int count) {
		return roll(count);
	}
	
	/**
	 * Returns the previous day.
	 * @return previous day
	 */
	public RsDay getPrevious() {
		return roll(-1);
	}
	
	/**
	 * Returns the n-th day before this day.
	 * @param count number of days to go back
	 * @return n-th day before this day
	 * @since 3.0.0
	 */
	public RsDay getPrevious(int count) {
		return roll(-count);
	}
	
	/**
	 * Returns a day by rolling back or forth this day
	 * @param days days to roll (can be negative)
	 * @return the n-th day before or after this day
	 * @since 3.0.0
	 */
	public RsDay roll(int days) {
		RsDay rc = new RsDay(getTimeZone(), getTimeInMillis());
		rc.add(DAY_OF_MONTH, days);
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof RsDay)) return false;
		
		RsDay m = (RsDay)o;
		return getKey().equals(m.getKey());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getKey();
	}

	/**
	 * Returns a day object for the given key.
	 * @param key key of day
	 * @return day object
	 */
	public static RsDay getDay(String key) {
		try {
			return new RsDay(KEY_PARSER().parse(key));
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse day: "+key, e);
		}
	}
	
	/**
	 * Returns a day object for the given timestamp.
	 * @param timestamp any timestamp of JavaTime API
	 * @return day object
	 * @since 3.0.0
	 */
	public static RsDay from(ZonedDateTime timestamp) {
		return new RsDay(timestamp);
	}
	
}
