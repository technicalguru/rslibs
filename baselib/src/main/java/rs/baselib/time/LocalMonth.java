/**
 * 
 */
package rs.baselib.time;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Replacement for RsMonth.
 * 
 * @author ralph
 *
 */
public class LocalMonth implements Serializable, Comparable<LocalMonth> {

	private static final long serialVersionUID = 1L;
	
	private static final String KEY_FORMAT = "%04d%02d";
	private static final int MONTHS_0000_TO_1970 = 23640;
	
	private int year;
	private int month;
	
	/**
	 * Private constructor to create an instance.
	 * @param year year
	 * @param month month from January (1) to December (12)
	 */
	private LocalMonth(int year, int month) {
		this.year  = year;
		this.month = month;
	}
	
    /**
     * Obtains an instance of {@code LocalMonth} from a text string such as {@code 2007-12-03}.
     * <p>
     * The string must represent a valid date and is parsed using
     * {@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE}.
     *
     * @param text  the text to parse such as "2007-12-03", not null
     * @return the parsed local month, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalMonth parse(CharSequence text) {
        return parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Obtains an instance of {@code LocalMonth} from a text string using a specific formatter.
     * <p>
     * The text is parsed using the formatter, returning a date.
     *
     * @param text  the text to parse, not null
     * @param formatter  the formatter to use, not null
     * @return the parsed local month, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalMonth parse(CharSequence text, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return ofLocalDate(formatter.parse(text, LocalDate::from));
    }

    /**
     * Obtains an instance of {@code LocalMonth} from a temporal object.
     * <p>
     * This obtains a local month based on the specified temporal.
     * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
     * which this factory converts to an instance of {@code LocalMonth}.
     * <p>
     * The conversion uses the {@link TemporalQueries#localDate()} query, which relies
     * on extracting the {@link ChronoField#EPOCH_DAY EPOCH_DAY} field.
     * <p>
     *
     * @param temporal  the temporal object to convert, not null
     * @return the local month, not null
     * @throws DateTimeException if unable to convert to a {@code LocalMonth}
     */
    public static LocalMonth from(TemporalAccessor temporal) {
        Objects.requireNonNull(temporal, "temporal");
        LocalDate date = temporal.query(TemporalQueries.localDate());
        if (date == null) {
            throw new DateTimeException("Unable to obtain LocalDate from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName());
        }
        return ofLocalDate(date);
    }
    
    /**
     * Obtains the current month from the system clock in the default time-zone.
     * <p>
     * This will query the {@link Clock#systemDefaultZone() system clock} in the default
     * time-zone to obtain the current date.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @return the current month using the system clock and default time-zone, not null
     */
	public static LocalMonth now() {
		return now(Clock.systemDefaultZone());
	}
	
    /**
     * Obtains the current month from the specified clock.
     * <p>
     * This will query the specified clock to obtain the current date - today.
     * Using this method allows the use of an alternate clock for testing.
     * The alternate clock may be introduced using {@link Clock dependency injection}.
     *
     * @param clock  the clock to use, not null
     * @return the current month, not null
     */
	public static LocalMonth now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        final Instant now = clock.instant();  // called once
        return ofInstant(now, clock.getZone());
	}
	
    /**
     * Obtains the current month from the system clock in the specified time-zone.
     * <p>
     * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date.
     * Specifying the time-zone avoids dependence on the default time-zone.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @param zoneId  the zone ID to use, not null
     * @return the current month using the system clock, not null
     */
	public static LocalMonth now(ZoneId zoneId) {
		return now(Clock.system(zoneId));
	}
	
    /**
     * Obtains an instance of {@code LocalMonth} from a year and month.
     * <p>
     * This returns a {@code LocalMonth} with the specified year and month.
      *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
     * @return the local month, not null
     */
	public static LocalMonth of(int year, int month) {
		return new LocalMonth(year, month);
	}
	
    /**
     * Obtains an instance of {@code LocalMonth} from a year and month.
     * <p>
     * This returns a {@code LocalMonth} with the specified year and month.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month  the month-of-year to represent, not null
     * @return the local month, not null
     */
	public static LocalMonth of(int year, Month month) {
		return new LocalMonth(year, month.getValue());
	}
	
    /**
     * Obtains an instance of {@code LocalMOnth} from an {@code Instant} and zone ID.
     * <p>
     * This creates a local month based on the specified instant.
     * First, the offset from UTC/Greenwich is obtained using the zone ID and instant,
     * which is simple as there is only one valid offset for each instant.
     * Then, the instant and offset are used to calculate the local date.
     *
     * @param instant  the instant to create the date from, not null
     * @param zone  the time-zone, which may be an offset, not null
     * @return the local month, not null
     * @throws DateTimeException if the result exceeds the supported range
     * @since 9
     */
	public static LocalMonth ofInstant(Instant instant, ZoneId zoneId) {
		return ofLocalDate(LocalDate.ofInstant(instant, zoneId));
	}
	
    /**
     * Obtains an instance of {@code LocalMonth} from the epoch day count.
     * <p>
     * This returns a {@code LocalMonth} based on the specified epoch-day.
     * The {@link ChronoField#EPOCH_DAY EPOCH_DAY} is a simple incrementing count
     * of days where day 0 is 1970-01-01. Negative numbers represent earlier days.
     *
     * @param epochDay  the Epoch Day to convert, based on the epoch 1970-01-01
     * @return the local month, not null
     * @throws DateTimeException if the epoch day exceeds the supported date range
     */
	public static LocalMonth ofEpochDay(int epochDay) {
		return ofLocalDate(LocalDate.ofEpochDay(epochDay));
	}
	
    /**
     * Obtains an instance of {@code LocalMonth} from the epoch month count.
     * <p>
     * This returns a {@code LocalMonth} based on the specified epoch-month.
     * The emoch-month is a simple incrementing count of months where month 0 is 
     * January 1970. Negative numbers represent earlier months.
     *
     * @param epochMonth  the Epoch Month to convert, based on the epoch 1970-01
     * @return the local month, not null
     */
	public static LocalMonth ofEpochMonth(int epochMonth) {
		// Month 0 is 1970-01
		int zeroMonths = epochMonth + MONTHS_0000_TO_1970;
		int year      = zeroMonths/12;
		int month      = zeroMonths - (12*year) + 1;
		return new LocalMonth(year, month);
	}
	
    /**
     * Obtains an instance of {@code LocalMonth} from the local date.
     * <p>
     * This returns a {@code LocalMonth} based on the specified local date.
     *
     * @param localDate  the local date object
     * @return the local month, not null
     */
	public static LocalMonth ofLocalDate(LocalDate localDate) {
		return new LocalMonth(localDate.getYear(), localDate.getMonthValue());
	}
	
    /**
     * Obtains an instance of {@code LocalMonth} from a year and day-of-year.
     * <p>
     * This returns a {@code LocalMonth} with the specified year and day-of-year.
     * The day-of-year must be valid for the year, otherwise an exception will be thrown.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @param dayOfYear  the day-of-year to represent, from 1 to 366
     * @return the local month, not null
     * @throws DateTimeException if the value of any field is out of range,
     *  or if the day-of-year is invalid for the year
     */
    public static LocalMonth ofYearDay(int year, int dayOfYear) {
		return ofLocalDate(LocalDate.ofYearDay(year, dayOfYear));
	}
    
    /**
     * Obtains an instance of {@code LocalMonth} from its key string.
     * <p>
     * This returns a {@code LocalMonth} with the specified year and month in key.
     *
     * @param key the key of the month, e.g. "202209".
     * @return the local month, not null
     * @throws NumberFormatException - if the string does not contain parsable integers.
     */
	public static LocalMonth ofKey(String key) {
		return new LocalMonth(Integer.parseInt(key.substring(0, 4)), Integer.parseInt(key.substring(4, 6)));
	}
	
    /**
     * Gets the year field.
     * <p>
     * This method returns the primitive {@code int} value for the year.
     * <p>
     * The year returned by this method is proleptic as per {@code get(YEAR)}.
     * To obtain the year-of-era, use {@code get(YEAR_OF_ERA)}.
     *
     * @return the year, from MIN_YEAR to MAX_YEAR
     */
    public int getYear() {
    	return year;
    }
    
    /**
     * Gets the month-of-year field from 1 to 12.
     * <p>
     * This method returns the month as an {@code int} from 1 to 12.
     * Application code is frequently clearer if the enum {@link Month}
     * is used by calling {@link #getMonth()}.
     *
     * @return the month-of-year, from 1 to 12
     * @see #getMonth()
     */
    public int getMonthValue() {
    	return month;
    }
    
    /**
     * Gets the month-of-year field using the {@code Month} enum.
     * <p>
     * This method returns the enum {@link Month} for the month.
     * This avoids confusion as to what {@code int} values mean.
     * If you need access to the primitive {@code int} value then the enum
     * provides the {@link Month#getValue() int value}.
     *
     * @return the month-of-year, not null
     * @see #getMonthValue()
     */
    public Month getMonth() {
    	return Month.of(month);
    }
    
    /**
     * Checks if the year is a leap year, according to the ISO proleptic
     * calendar system rules.
     * <p>
     * This method applies the current rules for leap years across the whole time-line.
     * In general, a year is a leap year if it is divisible by four without
     * remainder. However, years divisible by 100, are not leap years, with
     * the exception of years divisible by 400 which are.
     * <p>
     * For example, 1904 is a leap year it is divisible by 4.
     * 1900 was not a leap year as it is divisible by 100, however 2000 was a
     * leap year as it is divisible by 400.
     * <p>
     * The calculation is proleptic - applying the same rules into the far future and far past.
     * This is historically inaccurate, but is correct for the ISO-8601 standard.
     *
     * @return true if the year is leap, false otherwise
     */
    public boolean isLeapYear() {
        return IsoChronology.INSTANCE.isLeapYear(year);
    }

    /**
     * Returns the length of the month.
     * <p>
     * This returns the length of the month in days.
     * For example, a date in January would return 31.
     *
     * @return the length of the month in days
     */
    public int lengthOfMonth() {
    	return getFirstDay().lengthOfMonth();
    }
    
    /**
     * Returns the length of the year represented by this month.
     * <p>
     * This returns the length of the year in days, either 365 or 366.
     *
     * @return 366 if the year is leap, 365 otherwise
     */
    public int lengthOfYear() {
        return (isLeapYear() ? 366 : 365);
    }

    /**
     * Returns the identifiable key for this month in format "YYYYMM".
     * @return the key of this month
     */
    public String getKey() {
    	return String.format(KEY_FORMAT, year, month);
    }

    /**
     * Returns the first day of this month as a {@link LocalDate}.
     * @return the first day in this month
     */
    public LocalDate getFirstDay() {
    	return LocalDate.of(year, month, 1);
    }
    
    /**
     * Returns the last day of this month as a {@link LocalDate}.
     * @return the last day in this month
     */
    public LocalDate getLastDay() {
    	return LocalDate.of(year, month, lengthOfMonth());
    }
    
    /**
     * Returns the start of this month as a {@link LocalDateTime}.
     * @return the start of this month
     */
	public LocalDateTime atStartOfMonth() {
		return LocalDateTime.of(getFirstDay(), LocalTime.of(0, 0, 0, 0));
	}

    /**
     * Returns the start of this month as a {@link ZonedDateTime} using a specific time zone.
     * @param zone the time zone for the return value
     * @return the start of this month
     */
	public ZonedDateTime atStartOfMonth(ZoneId zone) {
		if (zone == null) zone = ZoneId.of("UTC");
		return ZonedDateTime.of(atStartOfMonth(), zone);
	}

    /**
     * Returns the end of this month as a {@link LocalDateTime}.
     * @return the end of this month
     */
	public LocalDateTime atEndOfMonth() {
		return LocalDateTime.of(getLastDay(), LocalTime.of(23, 59, 59, 999999999));
	}

    /**
     * Returns the end of this month as a {@link ZonedDateTime} using a specific time zone.
     * @param zone the time zone for the return value
     * @return the end of this month
     */
	public ZonedDateTime atEndOfMonth(ZoneId zone) {
		if (zone == null) zone = ZoneId.of("UTC");
		return ZonedDateTime.of(atEndOfMonth(), zone);
	}

    /**
     * Obtains epoch month count.
     * <p>
     * This returns epoch-month represented by this LocalMonth.
     * The emoch-month is a simple incrementing count of months where month 0 is 
     * January 1970. Negative numbers represent earlier months.
     *
     * @return the Epoch Month, based on the epoch 1970-01
     */
	public int toEpochMonth() {
        int y = year;
        int m = month;
        int total = 12 * y + m -1;
        return total - MONTHS_0000_TO_1970;
	}
	
    /**
     * Returns a sequential ordered stream of months. The returned stream starts from this month
     * (inclusive) and goes to {@code endExclusive} (exclusive) by an incremental step of 1 month.
     *
     * @param endExclusive  the end month, exclusive, not null
     * @return a sequential {@code Stream} for the range of {@code LocalMonth} values
     * @throws IllegalArgumentException if end month is before this month
     */
    public Stream<LocalMonth> monthsUntil(LocalMonth endExclusive) {
        int end = endExclusive.toEpochMonth();
        int start = toEpochMonth();
        if (end < start) {
            throw new IllegalArgumentException(endExclusive + " < " + this);
        }
        return IntStream.range(start, end).mapToObj(LocalMonth::ofEpochMonth);
    }

    /**
     * Returns a copy of this {@code LocalMonth} with the specified number of months added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToAdd  the months to add, may be negative
     * @return a {@code LocalMonth} based on this date with the months added, not null
     */
    public LocalMonth plusMonths(int monthsToAdd) {
        if (monthsToAdd == 0) {
            return this;
        }
        return ofEpochMonth(toEpochMonth()+monthsToAdd);
    }

    /**
     * Returns a copy of this {@code LocalMonth} with the specified number of years added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToAdd  the years to add, may be negative
     * @return a {@code LocalMonth} based on this date with the years added, not null
     */
    public LocalMonth plusYear(int yearsToAdd) {
        if (yearsToAdd == 0) {
            return this;
        }
        return ofEpochMonth(toEpochMonth()+yearsToAdd*12);
    }

    /**
     * Returns a copy of this {@code LocalMonth} with the specified number of months subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToSubtract  the months to subtract, may be negative
     * @return a {@code LocalMonth} based on this date with the months subtracted, not null
     */
    public LocalMonth minusMonths(int monthsToSubtract) {
        if (monthsToSubtract == 0) {
            return this;
        }
        return ofEpochMonth(toEpochMonth()-monthsToSubtract);
    }

    /**
     * Returns a copy of this {@code LocalMonth} with the specified number of years subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToSubtract  the years to subtract, may be negative
     * @return a {@code LocalMonth} based on this date with the years subtracted, not null
     */
    public LocalMonth minusYear(int yearsToSubtract) {
        if (yearsToSubtract == 0) {
            return this;
        }
        return ofEpochMonth(toEpochMonth()-yearsToSubtract*12);
    }

    /**
     * Returns the subsequent (following) month.
     * @return the next month
     */
    public LocalMonth nextMonth() {
    	return plusMonths(1);
    }
    
    /**
     * Returns the previous month
     * @return the month before.
     */
    public LocalMonth previousMonth() {
    	return minusMonths(1);
    }
    
    /**
     * Returns a copy of this {@code LocalMonth} with the month-of-year altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param month  the month-of-year to set in the result, from 1 (January) to 12 (December)
     * @return a {@code LocalMOnth} based on this date with the requested month, not null
     * @throws DateTimeException if the month-of-year value is invalid
     */
    public LocalMonth withMonth(int month) {
        if (this.month == month) {
            return this;
        }
        MONTH_OF_YEAR.checkValidValue(month);
        return of(year, month);
    }

    /**
     * Returns a copy of this {@code LocalMonth} with the year altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param year  the year to set in the result, from MIN_YEAR to MAX_YEAR
     * @return a {@code LocalMonth} based on this date with the requested year, not null
     * @throws DateTimeException if the year value is invalid
     */
    public LocalMonth withYear(int year) {
        if (this.year == year) {
            return this;
        }
        YEAR.checkValidValue(year);
        return of(year, month);
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
	public int compareTo(LocalMonth otherMonth) {
        int cmp = (year - otherMonth.year);
        if (cmp == 0) {
            cmp = (month - otherMonth.month);
        }
        return cmp;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public int hashCode() {
		return Objects.hash(month, year);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LocalMonth)) {
			return false;
		}
		LocalMonth other = (LocalMonth) obj;
		return month == other.month && year == other.year;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public String toString() {
		return "LocalMonth ["+getKey()+"]";
	}
    
    void writeExternal(DataOutput out) throws IOException {
        out.writeInt(year);
        out.writeByte(month);
    }

    static LocalMonth readExternal(DataInput in) throws IOException {
        int year = in.readInt();
        int month = in.readByte();
        return LocalMonth.of(year, month);
    }
    
}
