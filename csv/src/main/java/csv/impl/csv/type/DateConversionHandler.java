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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import csv.mapper.TypeConverter;

/**
 * A conversion handler for dates. The default implementation
 * can recognize these date strings: dd/MM/yyyy, dd.MM.yyyy, 
 * dd/MM/yy, dd.MM.yy, yyyy/MM/dd, yyyy.MM.dd. Please not that
 * for a specific date string the first suitable format will be used.
 * @author ralph
 *
 */
public class DateConversionHandler implements TypeConverter {

	public static final DateConversionHandler INSTANCE = new DateConversionHandler();
	
	private static final String DEFAULT_DATE_FORMATS[] = new String[] {
		"dd/MM/yyyy",
		"dd.MM.yyyy",
		"dd/MM/yy",
		"dd.MM.yy",
		"yyyy/MM/dd",
		"yyyy.MM.dd"
	};
	
	private String parsingFormats[] = null;
	private DateFormat parsingFormatters[] = null;
	private String printFormat = null;
	private DateFormat printFormatter = null;
	private TimeZone timezone = null;
	
	/**
	 * Constructor.
	 */
	public DateConversionHandler() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Date.class };
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
		DateFormat formats[] = getParsingFormatters(s);
		// Get all parsing formats and return the first possible
		for (DateFormat parser : formats) {
			try {
				Date rc = parser.parse(s);
				
				// Return the date if successful
				return rc;
			} catch (ParseException e) {
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
		if (o instanceof Date) {
			return getPrintFormatter().format((Date)o);
		}
		
		// Return from toString method
		return o.toString();
	}

	/**
	 * Returns the date formatters created from our date formatters.
	 * @return array of formatters to be used (never null!)
	 */
	public DateFormat[] getParsingFormatters() {
		if (parsingFormatters == null) {
			String formats[] = getParsingFormats();
			parsingFormatters = new DateFormat[formats.length];
			for (int i=0; i<formats.length; i++) {
				parsingFormatters[i] = new SimpleDateFormat(formats[i]);
				parsingFormatters[i].setTimeZone(getTimezone());
			}
		}
		return parsingFormatters;
	}
	
	/**
	 * Returns date formatters that fit the given string.
	 * This pre-selection is required due to some unexpected
	 * results when it comes to parsing (e.g. 2 digits are excepted
	 * by parsers when 4 were required).
	 * @param s the string to analyze
	 * @return array of formatters to be used (never null!)
	 */
	protected DateFormat[] getParsingFormatters(String s) {
		DateFormat parsers[] = getParsingFormatters();
		List<DateFormat> rc = new ArrayList<DateFormat>();
		String formats[] = getParsingFormats();
		for (int i=0; i<formats.length; i++) {
			String format = formats[i];
			if (matches(format, s)) rc.add(parsers[i]);
		}
		return rc.toArray(new DateFormat[rc.size()]);
	}
	
	protected boolean matches(String format, String s) {
		if (format.length() != s.length()) return false;
		for (int i=0; i<format.length(); i++) {
			char c1 = format.charAt(i);
			char c2 = s.charAt(i);
			if (!Character.isDigit(c2) && !Character.isLetter(c2)) {
				// Extra character must match
				if (c1 != c2) return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the formats that will be used.
	 * This method returns default formats when no formats were set
	 * @return the format strings used for parsing dates (never null!).
	 */
	public String[] getParsingFormats() {
		if (parsingFormats == null) parsingFormats = DEFAULT_DATE_FORMATS;
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
		if (printFormat == null) printFormat = getParsingFormats()[0];
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
	public DateFormat getPrintFormatter() {
		if (printFormatter == null) {
			printFormatter = new SimpleDateFormat(getPrintFormat());
			printFormatter.setTimeZone(getTimezone());
		}
		return printFormatter;
	}
	
	/**
	 * Sets the timezone for formatting and parsing.
	 * @param timezone timezone to be used.
	 */
	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
		if (printFormatter != null) printFormatter.setTimeZone(timezone);
		if (parsingFormatters != null) {
			for (DateFormat format : parsingFormatters) format.setTimeZone(timezone);
		}
	}
	
	/**
	 * Returns the timezone this handler uses.
	 * @return timezone
	 */
	public TimeZone getTimezone() {
		if (timezone == null) {
			if (printFormatter != null) return printFormatter.getTimeZone();
			return TimeZone.getDefault();
		}
		return timezone;
	}
}
