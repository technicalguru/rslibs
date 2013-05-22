/**
 * 
 */
package rsbaselib.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A {@link Date} replacement object to allow better date computations.
 * @author ralph
 *
 */
public class RsDate extends GregorianCalendar {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -4325303811108690168L;

	/**
	 * Constructor.
	 */
	public RsDate() {
	}

	/**
	 * Constructor.
	 */
	public RsDate(Date date) {
		if (date != null) setTimeInMillis(date.getTime());
		else setTimeInMillis(0);
	}

	/**
	 * Constructor.
	 */
	public RsDate(long timeInMillis) {
		setTimeInMillis(timeInMillis);
	}

	/**
	 * Constructor.
	 */
	public RsDate(Calendar calendar) {
		setTimeInMillis(calendar.getTimeInMillis());
	}

	/**
	 * Returns the year object for this date.
	 * @return year
	 */
	public RsYear getYear() {
		return new RsYear(getTimeInMillis());
	}
	
	/**
	 * Returns the month object for this date.
	 * @return month
	 */
	public RsMonth getMonth() {
		return new RsMonth(getTimeInMillis());
	}
	
	/**
	 * Returns the day object for this date.
	 * @return day
	 */
	public RsDay getDay() {
		return new RsDay(getTimeInMillis());
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
}
