/*
 * This file is part of CSV package.
 *
 *  CSV is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.impl.csv.type;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import csv.mapper.TypeConverter;

/**
 * A conversion handler for dates. The default implementation
 * can recognize these date strings: dd/MM/yyyy, dd.MM.yyyy, 
 * dd/MM/yy, dd.MM.yy, yyyy/MM/dd, yyyy.MM.dd. Please not that
 * for a specific date string the first suitable format will be used.
 * @author ralph
 *
 */
public class ZonedDateTimeConversionHandler implements TypeConverter {

	public static final ZonedDateTimeConversionHandler INSTANCE = new ZonedDateTimeConversionHandler();
	
	private static final String DEFAULT_PRINT_FORMAT   = "yyyy-MM-dd HH:mm:ss";
	
	private static final String DEFAULT_DATE_FORMATS[] = new String[] {
			"dd/MM/yyyy",
			"dd.MM.yyyy",
			"dd/MM/yy",
			"dd.MM.yy",
			"yyyy/MM/dd",
			"yyyy.MM.dd",
			"yyyy-MM-dd",
		};
	
	private static final String DEFAULT_TIME_FORMATS[] = new String[] {
			"HH:mm",
			"HH:mm:ss",
			"HH:mm:ss.S",
			"HH:mm Z",
			"HH:mm:ss Z",
			"HH:mm:ss.S Z",
		};
	private static DateTimeFormatter STANDARD_FORMATTERS[] = {
			DateTimeFormatter.BASIC_ISO_DATE,
			DateTimeFormatter.ISO_DATE,
			DateTimeFormatter.ISO_DATE_TIME,
			DateTimeFormatter.ISO_INSTANT,
			DateTimeFormatter.ISO_LOCAL_TIME,
			DateTimeFormatter.ISO_LOCAL_DATE_TIME,
			DateTimeFormatter.ISO_OFFSET_DATE,
			DateTimeFormatter.ISO_OFFSET_DATE_TIME,
			DateTimeFormatter.ISO_OFFSET_TIME,
			DateTimeFormatter.ISO_ORDINAL_DATE,
			DateTimeFormatter.ISO_TIME,
			DateTimeFormatter.ISO_WEEK_DATE,
			DateTimeFormatter.ISO_ZONED_DATE_TIME,
			DateTimeFormatter.RFC_1123_DATE_TIME,
	};
	
	private static String DEFAULT_FORMATS[];
	
	static {
		List<String> l = new ArrayList<>();
		for (String d : DEFAULT_DATE_FORMATS) {
			l.add(d);
			for (String t : DEFAULT_TIME_FORMATS) {
				l.add(t);
				l.add(d+" "+t);
			}
		}
		DEFAULT_FORMATS = l.toArray(new String[l.size()]);
	}
	
	private String parsingFormats[] = null;
	private DateTimeFormatter parsingFormatters[] = null;
	private String printFormat = null;
	private DateTimeFormatter printFormatter = null;
	private ZoneId zoneId = null;
	
	/**
	 * Constructor.
	 */
	public ZonedDateTimeConversionHandler() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { ZonedDateTime.class };
	}

	/**
	 * Converts string to date.
	 * This method tries to parse the given string by checking each
	 * possible date format. If no format applies then the original
	 * string will be returned
	 * @param s string to be parsed
	 * @return date 
	 * @see csv.mapper.TypeConverter#fromStream(java.lang.String)
	 */
	@Override
	public Object fromStream(Object o) {
		if (o == null) return null;
		String s = o.toString().trim();
		
		// We need to select the right format
		DateTimeFormatter formats[] = getParsingFormatters();
		// Get all parsing formats and return the first possible
		for (DateTimeFormatter parser : formats) {
			try {
				TemporalAccessor rc = parser.parse(s);
				
				// Return the date if successful
				return ZonedDateTime.from(rc);
			} catch (DateTimeParseException e) {
				// Ignore, just try next
			}
		}
		
		// Return the original value
		return s;
	}

	/**
	 * Converts the date to its string representation.
	 * @param o date to be converted
	 * @return string representation of date
	 * @see csv.mapper.TypeConverter#toStream(java.lang.Object)
	 */
	@Override
	public Object toStream(Object o) {
		if (o == null) return null;
		
		// Assuming this is a date
		if (o instanceof TemporalAccessor) {
			return getPrintFormatter().format((TemporalAccessor)o);
		}
		
		// Return from toString method
		return o.toString();
	}

	/**
	 * Returns the date formatters created from our date formatters.
	 * @return array of formatters to be used (never null!)
	 */
	public DateTimeFormatter[] getParsingFormatters() {
		if (parsingFormatters == null) {
			String formats[] = getParsingFormats();
			parsingFormatters = new DateTimeFormatter[formats.length+STANDARD_FORMATTERS.length];
			for (int i=0; i<formats.length; i++) {
				parsingFormatters[i] = DateTimeFormatter.ofPattern(formats[i]).withZone(getZoneId());
			}
			for (int i=0; i<STANDARD_FORMATTERS.length; i++) {
				parsingFormatters[i+formats.length] = STANDARD_FORMATTERS[i];
			}
		}
		return parsingFormatters;
	}
	
	/**
	 * Returns the formats that will be used.
	 * This method returns default formats when no formats were set
	 * @return the format strings used for parsing dates (never null!).
	 */
	public String[] getParsingFormats() {
		if (parsingFormats == null) parsingFormats = DEFAULT_FORMATS;
		return parsingFormats;
	}

	/**
	 * Sets the parsing date formats to be used.
	 * @param parsingFormats the parsingFormats to set
	 */
	public void setParsingFormats(String[] parsingFormats) {
		this.parsingFormats = parsingFormats;
		parsingFormatters = null;
		printFormatter = null;
	}

	/**
	 * Returns the printing format.
	 * This method will return the first parsing format if no format was set.
	 * @return the printFormat
	 */
	public String getPrintFormat() {
		if (printFormat == null) printFormat = DEFAULT_PRINT_FORMAT;
		return printFormat;
	}

	/**
	 * Sets the format used for printing.
	 * @param printFormat the printFormat to set
	 */
	public void setPrintFormat(String printFormat) {
		this.printFormat = printFormat;
		printFormatter = null;
	}

	/**
	 * Returns the print formatter created from the print format.
	 * @return print formatter
	 */
	public DateTimeFormatter getPrintFormatter() {
		if (printFormatter == null) {
			printFormatter = DateTimeFormatter.ofPattern(getPrintFormat()).withZone(getZoneId());
		}
		return printFormatter;
	}
	
	/**
	 * Sets the timezone for formatting and parsing.
	 * @param zoneId timezone to be used.
	 */
	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
		if (printFormatter != null) printFormatter = printFormatter.withZone(zoneId);
		if (parsingFormatters != null) {
			for (int i=0; i<parsingFormatters.length; i++) {
				parsingFormatters[i] = parsingFormatters[i].withZone(zoneId);
			}
		}
	}
	
	/**
	 * Returns the timezone this handler uses.
	 * @return timezone
	 */
	public ZoneId getZoneId() {
		if (zoneId == null) {
			if (printFormatter != null) return printFormatter.getZone();
			return ZoneId.systemDefault();
		}
		return zoneId;
	}
}
