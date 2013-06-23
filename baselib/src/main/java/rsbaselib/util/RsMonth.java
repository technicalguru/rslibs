/**
 * 
 */
package rsbaselib.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An indicator for adapting into Eclipse.
 * @author Ralph
 *
 */
public class RsMonth extends RsDate {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6154891935288280677L;

	private static final SimpleDateFormat KEY_FORMATTER = new SimpleDateFormat("yyyyMM");
	
	/**
	 * Constructor.
	 */
	public RsMonth() {
		ensureBegin();
	}

	/**
	 * Constructor.
	 */
	public RsMonth(Date date) {
		super(date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 */
	public RsMonth(long timeInMillis) {
		super(timeInMillis);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param calendar
	 */
	public RsMonth(Calendar calendar) {
		super(calendar);
		ensureBegin();
	}

	/**
	 * Constructor.
	 */
	public RsMonth(int month, int year) {
		super(0);
		set(MONTH, month);
		set(YEAR, year);
		ensureBegin();
	}

	/**
	 * Adjusts the time stamp to the start of the month.
	 */
	protected void ensureBegin() {
		set(DAY_OF_MONTH, 1);
		set(HOUR_OF_DAY, 0);
		set(MINUTE, 0);
		set(SECOND, 0);
		set(MILLISECOND, 0);
	}
	
	/**
	 * Returns the key of this month.
	 * @return the key of this month
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
		rc.set(DAY_OF_MONTH, 1);
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
		rc.set(DAY_OF_MONTH, rc.getActualMaximum(DAY_OF_MONTH));
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
	public RsMonth getNext() {
		RsMonth rc = new RsMonth(getTimeInMillis());
		rc.add(MONTH, 1);
		return rc;
	}
	
	/**
	 * Returns the previous month.
	 * @return previous month
	 */
	public RsMonth getPrevious() {
		RsMonth rc = new RsMonth(getTimeInMillis());
		rc.add(MONTH, -1);
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof RsMonth)) return false;
		
		RsMonth m = (RsMonth)o;
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
	 * Returns a month object for the given key.
	 * @param key key of month
	 * @return month object
	 */
	public static RsMonth getMonth(String key) {
		try {
			return new RsMonth(KEY_FORMATTER.parse(key));
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse month: "+key, e);
		}
	}
}
