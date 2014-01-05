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
import java.util.Calendar;
import java.util.Date;

/**
 * A day.
 * @author Ralph
 *
 */
public class RsDay extends RsDate {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6154891935288280677L;

	private static final SimpleDateFormat KEY_FORMATTER = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * Constructor.
	 */
	public RsDay() {
		ensureBegin();
	}

	/**
	 * Constructor.
	 */
	public RsDay(Date date) {
		super(date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 */
	public RsDay(long timeInMillis) {
		super(timeInMillis);
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
	 */
	public RsDay(int day, int month, int year) {
		super(0);
		set(DAY_OF_MONTH, day);
		set(MONTH, month);
		set(YEAR, year);
		ensureBegin();
	}

	/**
	 * Adjusts the time stamp to the start of the day.
	 */
	protected void ensureBegin() {
		set(HOUR_OF_DAY, 0);
		set(MINUTE, 0);
		set(SECOND, 0);
		set(MILLISECOND, 0);
	}
	
	/**
	 * Returns the key of this month.
	 * @return the key of this day.
	 */
	public String getKey() {
		return KEY_FORMATTER.format(getTime());
	}
	
	/**
	 * Returns the begin of this month.
	 * @return begin of month
	 */
	public RsDate getBegin() {
		RsDate rc = new RsDate(getTimeInMillis());
		rc.set(HOUR_OF_DAY, 0);
		rc.set(MINUTE, 0);
		rc.set(SECOND, 0);
		rc.set(MILLISECOND, 0);
		return rc;
	}
	
	/**
	 * Returns the end of this month.
	 * @return begin of month
	 */
	public RsDate getEnd() {
		RsDate rc = new RsDate(getTimeInMillis());
		rc.set(HOUR_OF_DAY, rc.getActualMaximum(HOUR_OF_DAY));
		rc.set(MINUTE, rc.getActualMaximum(MINUTE));
		rc.set(SECOND, rc.getActualMaximum(SECOND));
		rc.set(MILLISECOND, rc.getActualMaximum(MILLISECOND));
		return rc;
	}
	
	/**
	 * Returns the next month.
	 * @return next month
	 */
	public RsDay getNext() {
		RsDay rc = new RsDay(getTimeInMillis());
		rc.add(DAY_OF_MONTH, 1);
		return rc;
	}
	
	/**
	 * Returns the previous month.
	 * @return previous month
	 */
	public RsDay getPrevious() {
		RsDay rc = new RsDay(getTimeInMillis());
		rc.add(DAY_OF_MONTH, -1);
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
			return new RsDay(KEY_FORMATTER.parse(key));
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse day: "+key, e);
		}
	}
}
