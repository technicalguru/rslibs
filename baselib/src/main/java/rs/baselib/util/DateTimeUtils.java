package rs.baselib.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
	 * Returns whether a given time is earlier than the given duration.
	 * @param time the time to compare
	 * @param millisBefore ms before now
	 * @return {@code true} when time is before that time span
	 */
	public static boolean isBefore(ZonedDateTime time, long millisBefore) {
		return isBefore(time, ZonedDateTime.now().minus(millisBefore, ChronoUnit.MILLIS));
	}
	
	/**
	 * Returns whether a given time is earlier than the given duration.
	 * @param time the time to compare
	 * @param amountBefore the duration before now
	 * @return {@code true} when time is before that time span
	 */
	public static boolean isBefore(ZonedDateTime time, TemporalAmount amountBefore) {
		return isBefore(time, ZonedDateTime.now().minus(amountBefore));
	}

	/**
	 * Returns whether a given time is earlier than the given other time.
	 * @param time the time to compare
	 * @param other the other time
	 * @return {@code true} when time is before that other time
	 */
	public static boolean isBefore(ZonedDateTime time, ZonedDateTime other) {
		if (time != null) {
			return time.isBefore(other);
		}
		return true;
	}

	/**
	 * Returns whether a given time is later than the given duration.
	 * @param time the time to compare
	 * @param millisAfter the duration after now
	 * @return {@code true} when time is after that duration from now
	 */
	public static boolean isAfter(ZonedDateTime time, long millisAfter) {
		return isAfter(time, ZonedDateTime.now().minus(millisAfter, ChronoUnit.MILLIS));
	}
	
	/**
	 * Returns whether a given time is earlier than the given duration.
	 * @param time the time to compare
	 * @param amountAfter the duration after now
	 * @return {@code true} when time is after that duration from now
	 */
	public static boolean isAfter(ZonedDateTime time, TemporalAmount amountAfter) {
		return isAfter(time, ZonedDateTime.now().plus(amountAfter));
	}

	/**
	 * Returns whether a given time is earlier than the other time.
	 * @param time the time to compare
	 * @param other the other time
	 * @return {@code true} when time is after that other time
	 */
	public static boolean isAfter(ZonedDateTime time, ZonedDateTime other) {
		if (time != null) {
			return time.isAfter(other);
		}
		return true;
	}


	/**
	 * Returns the timestamp at the start of the day (00:00:00.000).
	 * @param time time of a day
	 * @return beginning of that day
	 */
	public static ZonedDateTime getStartOfDay(ZonedDateTime time) {
		return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
	
	/**
	 * Returns the timestamp at the start of the day (23:59:59.999).
	 * @param time time of a day
	 * @return end of that day
	 */
	public static ZonedDateTime getEndOfDay(ZonedDateTime time) {
		return time.withHour(23).withMinute(59).withSecond(59).withNano(0);
	}
	
	/**
	 * Tries to parse the string in order to retrieve a date/time with default ZoneId and default time 00:00:00.
	 * @param s - the string to be parsed
	 * @return the {@link ZonedDateTime} object or {@code null} if not successful.
	 */
	public static  ZonedDateTime parseZonedDateTime(String s) {
		return parseZonedDateTime(s, ZoneId.systemDefault());
	}
	
	/**
	 * Tries to parse the string in order to retrieve a date/time default time 00:00:00.
	 * @param s - the string to be parsed
	 * @param defaultZone - default zone ID to be used
	 * @return the {@link ZonedDateTime} object or {@code null} if not successful.
	 */
	public static  ZonedDateTime parseZonedDateTime(String s, ZoneId defaultZone) {
		return parseZonedDateTime(s, LocalTime.of(0, 0, 0), defaultZone);
	}
	
	/**
	 * Tries to parse the string in order to retrieve a date/time with default ZoneId and default time 23:59:59.999999999.
	 * @param s - the string to be parsed
	 * @return the {@link ZonedDateTime} object or {@code null} if not successful.
	 */
	public static  ZonedDateTime parseZonedDateTimeWithEndOfDay(String s) {
		return parseZonedDateTimeWithEndOfDay(s, ZoneId.systemDefault());
	}
	
	/**
	 * Tries to parse the string in order to retrieve a date/time with default time 23:59:59.999999999.
	 * @param s - the string to be parsed
	 * @param defaultZone - default zone ID to be used
	 * @return the {@link ZonedDateTime} object or {@code null} if not successful.
	 */
	public static  ZonedDateTime parseZonedDateTimeWithEndOfDay(String s, ZoneId defaultZone) {
		return parseZonedDateTime(s, LocalTime.of(23, 59, 59, 999999999), defaultZone);
	}
	
	/**
	 * Tries to parse the string in order to retrieve a date/time with default ZoneId and given default time.
	 * @param s - the string to be parsed
	 * @param defaultTime - default time to be used
	 * @return the {@link ZonedDateTime} object or {@code null} if not successful.
	 */
	public static  ZonedDateTime parseZonedDateTime(String s, LocalTime defaultTime) {
		return parseZonedDateTime(s, defaultTime, ZoneId.systemDefault());
	}
	
	/**
	 * Tries to parse the string in order to retrieve a date/time with default ZoneId and given default time and zoneId.
	 * @param s - the string to be parsed
	 * @param defaultTime - default time to be used
	 * @param defaultZone - default zone ID to be used
	 * @return the {@link ZonedDateTime} object or {@code null} if not successful.
	 */
	public static  ZonedDateTime parseZonedDateTime(String s, LocalTime defaultTime, ZoneId defaultZone) {
		if (CommonUtils.isEmpty(s)) return null;
		s = CommonUtils.trim(s);
		// try a few things
		try { return ZonedDateTime.parse(s); } catch (Throwable t) {}
		try { return ZonedDateTime.of(LocalDateTime.parse(s), defaultZone ); } catch (Throwable t) {}
		try { return ZonedDateTime.of(LocalDate.parse(s), defaultTime, defaultZone ); } catch (Throwable t) {}
		return null; 
	}


}
