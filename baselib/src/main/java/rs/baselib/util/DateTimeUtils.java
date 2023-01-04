package rs.baselib.util;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

/**
 * Utils for date and time.
 * @author ralph
 *
 */
public class DateTimeUtils {

	/**
	 * 
	 * @param time
	 * @param millisBefore
	 * @return
	 */
	public static boolean isBefore(ZonedDateTime time, long millisBefore) {
		return isBefore(time, ZonedDateTime.now().minus(millisBefore, ChronoUnit.MILLIS));
	}
	
	/**
	 * 
	 * @param time
	 * @param amountBefore
	 * @return
	 */
	public static boolean isBefore(ZonedDateTime time, TemporalAmount amountBefore) {
		return isBefore(time, ZonedDateTime.now().minus(amountBefore));
	}

	/**
	 * 
	 * @param time
	 * @param other
	 * @return
	 */
	public static boolean isBefore(ZonedDateTime time, ZonedDateTime other) {
		if (time != null) {
			return time.isBefore(other);
		}
		return true;
	}

	/**
	 * 
	 * @param time
	 * @param millisAfter
	 * @return
	 */
	public static boolean isAfter(ZonedDateTime time, long millisAfter) {
		return isAfter(time, ZonedDateTime.now().minus(millisAfter, ChronoUnit.MILLIS));
	}
	
	/**
	 * 
	 * @param time
	 * @param amountAfter
	 * @return
	 */
	public static boolean isAfter(ZonedDateTime time, TemporalAmount amountAfter) {
		return isAfter(time, ZonedDateTime.now().plus(amountAfter));
	}

	/**
	 * 
	 * @param time
	 * @param other
	 * @return
	 */
	public static boolean isAfter(ZonedDateTime time, ZonedDateTime other) {
		if (time != null) {
			return time.isAfter(other);
		}
		return true;
	}


	/**
	 * 
	 * @param time
	 * @return
	 */
	public static ZonedDateTime getStartOfDay(ZonedDateTime time) {
		return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	public static ZonedDateTime getEndOfDay(ZonedDateTime time) {
		return time.withHour(23).withMinute(59).withSecond(59).withNano(0);
	}
	

}
