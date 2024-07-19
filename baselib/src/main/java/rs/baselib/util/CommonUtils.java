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

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * Common Utils.
 * @author ralph
 *
 */
public class CommonUtils {

	private static Pattern ENV_VAR_PATTERN     = Pattern.compile("\\$ENV\\{([^\\}]+)\\}");
	private static Pattern RUNTIME_VAR_PATTERN = Pattern.compile("\\$RUNTIME\\{([^\\}]+)\\}");

	/** Default timeout for connecting to URLs (10sec) */
	public static int DEFAULT_CONNECT_TIMEOUT = 10000;
	/** Default timeout for reading from URLs (20sec) */
	public static int DEFAULT_READ_TIMEOUT    = 20000;
	/** alpha chars (uper and lower case letters) */
	public static final String ALPHA_CHARS             = "ABCDEFGHIJKLMNOPQRSTUVWXYZabzdefghijklmnopqrstuvwxyz";
	/** numeric chars (0-9) */
	public static final String NUM_CHARS               = "0123456789";
	/** special chars ({@code !\"&/()=?;*+'#;,:._-<>}) */
	public static final String SPECIAL_CHARS           = "!\\\"&/()=?;*+'#;,:._-<>";
	/** alpha and numeric chars */
	public static final String ALPHA_NUM_CHARS         = ALPHA_CHARS+NUM_CHARS;
	/** alpha and numeric and special chars */
	public static final String ALPHA_NUM_SPECIAL_CHARS = ALPHA_CHARS+NUM_CHARS+SPECIAL_CHARS;
	
	private static int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
	private static int readTimeout    = DEFAULT_READ_TIMEOUT;

	/**
	 * The formatter for dates (see {@link DateFormat#SHORT}).
	 * @return the formatter
	 */
	public static DateFormat DATE_FORMATTER() {
		return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
	}

	/**
	 * The formatter for dates incl. times (see {@link DateFormat#SHORT}).
	 * @return the formatter
	 */
	public static DateFormat DATE_TIME_FORMATTER() {
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
	}


	/**
	 * Formatter for real numbers.
	 * @return the formatter
	 */
	public static NumberFormat SIMPLE_NUMBER_FORMATTER() {
		return NumberFormat.getNumberInstance(Locale.getDefault());
	}

	/** 
	 * Formatter for integers. 
	 * @return the formatter
	 */
	public static NumberFormat SIMPLE_INT_FORMATTER() {
		return NumberFormat.getIntegerInstance(Locale.getDefault());
	}

	/** The OS name */
	private static String OS = System.getProperty("os.name").toLowerCase();

	/**
	 * Formats the string for display.
	 * @param o the category
	 * @return string display
	 */
	public static String toString(String o) {
		if (o == null) return "";
		return o;
	}

	/**
	 * Formats the money value.
	 * @param amount amount to be formatted
	 * @return formatted string
	 */
	public static String toString(float amount) {
		return SIMPLE_NUMBER_FORMATTER().format(amount);
	}

	/**
	 * Generates a string presentation of the given bytes.
	 * @param b byte array
	 * @return string representation (Hex)
	 */
	public static String toString(byte b[]) {
		if (b == null) return "null";
		StringBuilder buf = new StringBuilder();
		if (b != null) {
			for (int i=0; i<b.length; i++) {
				buf.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
			}
		}
		return buf.toString();
	}

	/**
	 * Generates a string presentation of the given objects.
	 * @param o object array
	 * @return string representation
	 */
	public static String toString(Object o[]) {
		if (o == null) return "null";
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		for (Object object : o) {
			if (buf.length() > 1) buf.append(",");
			if (object == null) buf.append("null");
			else buf.append(object.toString());
		}
		buf.append("]");
		return buf.toString();
	}

	/**
	 * Checks for equality null-safe.
	 * @param o1 object 1
	 * @param o2 object 2
	 * @return true when both values are equal
	 */
	public static boolean equals(Object o1, Object o2) {
		if ((o1 == null) && (o2 == null)) return true;
		if ((o1 != null) && (o2 != null)) {
			if ((o1 instanceof BigDecimal) && (o2 instanceof BigDecimal)) {
				return ((BigDecimal)o1).compareTo((BigDecimal)o2) == 0;
			}
			return o1.equals(o2);
		}
		return false;
	}

	/**
	 * Checks strings for equality null-safe and case ignore.
	 * <p>Strings will not be trimmed before test.</p>
	 * @param o1 string 1
	 * @param o2 string 2
	 * @return true when both values are equal
	 */
	public static boolean equalsIgnoreCase(String o1, String o2) {
		return equalsIgnoreCase(o1, o2, false);
	}
	
	/**
	 * Checks strings for equality null-safe and case ignore.
	 * @param o1 string 1
	 * @param o2 string 2
	 * @param trim whether to trim strings before comparison
	 * @return true when both values are equal
	 */
	public static boolean equalsIgnoreCase(String o1, String o2, boolean trim) {
		if ((o1 == null) && (o2 == null)) return true;
		if ((o1 != null) && (o2 != null)) {
			if (trim) {
				o1 = trim(o1);
				o2 = trim(o2);
			}
			return o1.equalsIgnoreCase(o2);
		}
		return false;
	}

	/**
	 * Returns true when the given string is null or - when trimmed - empty. 
	 * @param s the string to be checked
	 * @return true when string must be regarded as empty
	 */
	public static boolean isEmpty(String s) {
		return isEmpty(s, true);
	}

	/**
	 * Returns true when the given string is null empty. 
	 * @param s the string to be checked
	 * @param trim whether whitespaces shall be trimmed first
	 * @return true when string must be regarded as empty
	 */
	public static boolean isEmpty(String s, boolean trim) {
		if (s == null) return true;
		if (trim) s = trim(s);
		return s.length() == 0;
	}

	/**
	 * Trims the string by removing whitespaces and newlines from begin and end of the string.
	 * @param s - the string to trim
	 * @return the trimmed string ({@code null} if input was {@code null})
	 * @since 3.1.0
	 */
	public static String trim(String s) {
		if (s == null) return null;
		StringBuilder b = new StringBuilder(s);
		
		// Delete from beginning:
		while (b.length() > 0 && !isAllowedTrimmingChar(b.charAt(0))) {
			b.deleteCharAt(0);
		}

		// Delete from end
		while (b.length() > 0 && !isAllowedTrimmingChar(b.charAt(b.length()-1))) {
			b.deleteCharAt(b.length()-1);
		}
		return b.toString();
	}
	
	/**
	 * Returns true when the given character shall be kept when trimming a string.
	 * @param c - character to check
	 * @return true when character is allowed
	 * @since 3.1.0
	 */
	public static boolean isAllowedTrimmingChar(char c) {
		return !Character.isWhitespace(c) && !Character.isSpaceChar(c);
	}

	/**
	 * Returns true if version is in range of minVersion and maxVersion.
	 * Note that 1.2.0 is greater than 1.2
	 * @param minVersion - minimum required version (can be null)
	 * @param maxVersion - maximum required version (can be null)
	 * @param version - version to check
	 * @return true when version is within range
	 */
	public static boolean isCompatibleVersion(String minVersion, String maxVersion, String version) {
		if (version == null) return true;

		// Check minimum version
		if ((minVersion != null) && (compareVersion(minVersion, version) > 0)) return false;
		// Check maximum version
		if ((maxVersion != null) && (compareVersion(maxVersion, version) < 0)) return false;

		return true;
	}

	/**
	 * Compares software versions.
	 * @param v1 - version 1 
	 * @param v2 - version 2 
	 * @return -1 if v1 &lt; v2, 1 if v1 &gt; v2, 0 if v1 = v2
	 */
	public static int compareVersion(String v1, String v2) {
		String v1Parts[] = ArrayUtils.EMPTY_STRING_ARRAY;
		String v2Parts[] = ArrayUtils.EMPTY_STRING_ARRAY;
		if (v1 != null) v1Parts = v1.split("\\.");
		if (v2 != null) v2Parts = v2.split("\\.");
		return compareVersion(v1Parts, v2Parts);
	}

	/**
	 * Compares versions.
	 * @param v1 - version 1 divided into separate parts
	 * @param v2 - version 2 divided into separate parts
	 * @return -1 if v1 &lt; v2, 1 if v1 &gt; v2, 0 if v1 = v2
	 */
	public static int compareVersion(String v1[], String v2[]) {
		int maxCount = Math.max(v1.length, v2.length);
		for (int i=0; i<maxCount; i++) {
			try {
				long l1 = 0;
				if (i < v1.length) l1 = Long.parseLong(v1[i]);
				long l2 = 0;
				if (i <v2.length) l2 = Long.parseLong(v2[i]);
				if (l1 < l2) return -1;
				if (l1 > l2) return 1;
			} catch (NumberFormatException e) {
				int rc = v1[i].compareTo(v2[i]);
				if (rc != 0) return rc;
			}
		}

		// Usually equal here, but check remaining minor versions
		if (v1.length > v2.length) return 1;
		if (v1.length < v2.length) return -1;

		// Equal now
		return 0;
	}

	/**
	 * Makes a join of a collection.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the collection to join
	 * @return the joined string
	 */
	public static String join(String separator, Collection<?> parts) {
		return join(separator, parts, 0, parts.size()+1);
	}

	/**
	 * Makes a join of a string array.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the parts to join
	 * @return the joined string
	 */
	public static String join(String separator, String parts[]) {
		return join(separator, parts, 0, parts.length+1);
	}

	/**
	 * Makes a join of an object array.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the parts to join
	 * @return the joined string
	 * @since 1.2.9
	 */
	public static String join(String separator, Object parts[]) {
		return join(separator, parts, 0, parts.length+1);
	}

	/**
	 * Makes a join of a string array.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the parts to join
	 * @param startIndex - starting index (negative values not allowed)
	 * @return the joined string
	 */
	public static String join(String separator, String parts[], int startIndex) {
		return join(separator, parts, startIndex, parts.length+1);
	}

	/**
	 * Makes a join of a collection.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the collection to join
	 * @param startIndex - starting index (negative values not allowed)
	 * @return the joined string
	 * @since 1.2.9
	 */
	public static String join(String separator, Collection<?> parts, int startIndex) {
		return join(separator, parts, startIndex, parts.size()+1);
	}

	/**
	 * Makes a join of an object array.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the parts to join
	 * @param startIndex - starting index (negative values not allowed)
	 * @return the joined string
	 * @since 1.2.9
	 */
	public static String join(String separator, Object parts[], int startIndex) {
		return join(separator, parts, startIndex, parts.length+1);
	}

	/**
	 * Makes a join of a string array.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the parts to join
	 * @param startIndex - starting index (negative values not allowed)
	 * @param endIndex - endIndex (bigger values than number or array elements have no effect)
	 * @return the joined string
	 */
	public static String join(String separator, String parts[], int startIndex, int endIndex) {
		StringBuilder s = new StringBuilder();
		for (int i=startIndex; i<parts.length && i<=endIndex; i++) {
			if (i != startIndex) s.append(separator);
			s.append(parts[i]);
		}
		return s.toString();
	}

	/**
	 * Makes a join of an object array.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the parts to join
	 * @param startIndex - starting index (negative values not allowed)
	 * @param endIndex - endIndex (bigger values than number or array elements have no effect)
	 * @return the joined string
	 * @since 1.2.9
	 */
	public static String join(String separator, Object parts[], int startIndex, int endIndex) {
		StringBuilder s = new StringBuilder();
		for (int i=startIndex; i<parts.length && i<=endIndex; i++) {
			if (i != startIndex) s.append(separator);
			s.append(parts[i] != null ? parts[i].toString() : "");
		}
		return s.toString();
	}

	/**
	 * Makes a join of a collection.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the collection to join
	 * @param startIndex - starting index (negative values not allowed)
	 * @param endIndex - endIndex (bigger values than number or collection elements have no effect)
	 * @return the joined string
	 * @since 1.2.9
	 */
	public static String join(String separator, Collection<?> parts, int startIndex, int endIndex) {
		return join(separator, parts.toArray(new Object[parts.size()]), startIndex, endIndex);
	}

	/**
	 * Splits the string using comma as separation char.
	 * @param s - the string to split
	 * @return collection of separated entries
	 * @since 3.1.0
	 */
	public static Collection<String> split(String s) {
		return split(s, ",", (List<String>)null);
	}
	
	/**
	 * 
	 * Splits the string using comma as separation char.
	 * @param s - the string to split
	 * @param defaultCollection - the default collection when input is empty.
	 * @return collection of separated entries or the default collection
	 * @since 3.1.0
	 */
	public static Collection<String> split(String s, Collection<String> defaultCollection) {
		return split(s, ",", defaultCollection);
	}
	
	/**
	 * Splits the string using comma as separation char.
	 * @param s - the string to split
	 * @param delim - the delimiter character
	 * @param defaultCollection - the default collection when input is empty.
	 * @return collection of separated entries or the default collection
	 * @since 3.1.0
	 */
	public static Collection<String> split(String s, String delim, Collection<String> defaultCollection) {
		if (isEmpty(s)) return defaultCollection;
		return newList(s.split(delim));
	}

	/**
	 * Splits the string and returns a collection of Enum values from specified class.
	 * <p>Useful to convert a string into a collection of enum values.</p>
	 * @param <T> - the Enum type
	 * @param s - the string to split
	 * @param enumClass - the Enum class
	 * @return the list of Enum values
	 * @since 3.1.0
	 */
	public static <T extends Enum<T>> Collection<T> split(String s, Class<T> enumClass) {
		return split(s, ",", enumClass);
	}
	
	/**
	 * Splits the string (by commas) and returns a collection of Enum values from specified class.
	 * <p>Useful to convert a string into a collection of enum values.</p>
	 * @param <T> - the Enum type
	 * @param s - the string to split
	 * @param delim - the delimiter to split upon
	 * @param enumClass - the Enum class
	 * @return the list of Enum values
	 * @since 3.1.0
	 */
	public static <T extends Enum<T>> Collection<T> split(String s, String delim, Class<T> enumClass) {
		if (isEmpty(s)) return newList(enumClass.getEnumConstants());
		return Arrays.stream(s.split(delim)).map(t -> Enum.valueOf(enumClass, t)).collect(Collectors.toList());
	}
	
	/**
	 * Creates a list from elements.
	 * @param <T> - the element type
	 * @param elems - the elements
	 * @return new {@link List} of elements
	 * @since 3.1.0
	 */
	@SafeVarargs
	public static <T> List<T> newList(T ...elems) {
		List<T> rc = new ArrayList<>();
	    Collections.addAll(rc, elems);
		return rc;
	}
	
	/**
	 * Creates a set from elements.
	 * @param <T> - the element type
	 * @param elems - the elements
	 * @return new {@link Set} of elements
	 * @since 3.1.0
	 */
	@SafeVarargs
	public static <T> Set<T> newSet(T ...elems) {
		Set<T> rc = new HashSet<>();
	    Collections.addAll(rc, elems);
		return rc;
	}
	
	/**
	 * Recursively debugs objects.
	 * @param o object to debug
	 * @return the debug string
	 * @since 1.2.3
	 */
	public static String debugObject(Object o) {
		StringBuilder b = new StringBuilder();
		debugObject(b, o);
		return b.toString();
	}

	/**
	 * Recursively debugs objects and adds this in the string buffer.
	 * @param s string buffer to enhance
	 * @param o object to debug
	 */
	public static void debugObject(StringBuffer s, Object o) {
		StringBuilder b = new StringBuilder();
		debugObject(b, o);
		s.append(b);
	}

	/**
	 * Recursively debugs objects and adds this in the string builder.
	 * @param s string builder to enhance
	 * @param o object to debug
	 * @since 1.2.3
	 */
	public static void debugObject(StringBuilder s, Object o) {
		if (o == null) {
			s.append("NULL");
		} else if (o instanceof Collection<?>) {
			s.append(o.getClass().getSimpleName());
			s.append('[');
			boolean isFirst = true;
			for (Object o2 : (Collection<?>)o) {
				if (!isFirst) s.append(',');
				isFirst = false;
				debugObject(s, o2);
			}
			s.append(']');
		} else if (o instanceof Map<?, ?>) {
			s.append(o.getClass().getSimpleName());
			s.append('[');
			boolean isFirst = true;
			for (Object key : ((Map<?,?>)o).keySet()) {
				if (!isFirst) s.append(',');
				isFirst = false;
				s.append(key);
				s.append('=');
				debugObject(s, ((Map<?,?>)o).get(key));
			}
			s.append(']');
		} else if (o.getClass().isArray()) {
			s.append('[');
			boolean isFirst = true;
			for (Object o2 : (Object[])o) {
				if (!isFirst) s.append(',');
				isFirst = false;
				debugObject(s, o2);
			}
			s.append(']');
		} else {
			s.append(o.getClass().getName());
			s.append('{');
			s.append(o.toString());
			s.append('}');
		}
	}

	/**
	 * Returns the current time as UNIX timestamp.
	 * @return time in seconds since January 1st, 1970, 00:00:00 UTC.
	 */
	public static long getUnixTimestamp() {
		return getUnixTimestamp(System.currentTimeMillis());
	}

	/**
	 * Returns the given date as UNIX timestamp.
	 * @param date date object.
	 * @return time in seconds since January 1st, 1970, 00:00:00 UTC.
	 */
	public static long getUnixTimestamp(Date date) {
		return getUnixTimestamp(date.getTime());
	}

	/**
	 * Returns the given Java time as UNIX timestamp.
	 * @param time Java timestamp
	 * @return time in seconds since January 1st, 1970, 00:00:00 UTC.
	 */
	public static long getUnixTimestamp(long time) {
		return time / 1000;
	}

	/**
	 * Returns an iterable for the given iterator.
	 * @param iterator the iterator to be wrapped
	 * @param <T> type of objects in the iterator
	 * @return the iterable.
	 */
	public static <T> Iterable<T> iterable(Iterator<T> iterator) {
		return new IterableImpl<T>(iterator);
	}

	/**
	 * Returns the current stacktrace.
	 * @param ignoreLines the number of lines to be ignored at the top of the trace
	 * @return the stacktrace
	 */
	public static List<String> getStackTrace(int ignoreLines) {
		List<String> rc = new ArrayList<String>();
		ignoreLines++;
		Exception e = new RuntimeException();
		StackTraceElement lines[] = e.getStackTrace();
		for (StackTraceElement line : lines) {
			if (ignoreLines > 0) {
				ignoreLines--;
				continue;
			}
			StringBuilder s = new StringBuilder();
			s.append("      at ");
			s.append(line.getClassName());
			s.append(".");
			s.append(line.getMethodName());
			s.append("(");
			if (line.getFileName() != null) {
				s.append(line.getFileName());
				s.append(":");
				s.append(line.getLineNumber());
			} else {
				s.append("unknown source");
			}
			s.append(")");
			rc.add(s.toString());
		}
		return rc;
	}

	/**
	 * Dumps the stacktrace so stdout.
	 */
	public static void stdoutStackTrace() {
		printStackTrace(System.out, 1);
	}

	/**
	 * Dumps the stacktrace so stderr.
	 */
	public static void stderrStackTrace() {
		printStackTrace(System.err, 1);
	}

	/**
	 * Dumps the stacktrace into the print stream.
	 * @param out the stream to be used
	 * @param ignoreLines the number of lines to be ignored at top of trace
	 */
	public static void printStackTrace(PrintStream out, int ignoreLines) {
		for (String s : getStackTrace(ignoreLines+1)) {
			out.println(s);
		}
	}

	/**
	 * Dumps the stacktrace in ERROR mode.
	 * @param log the logger to be used
	 */
	public static void errorStackTrace(Logger log) {
		for (String s : getStackTrace(2)) {
			log.debug(s);
		}
	}

	/**
	 * Dumps the stacktrace in INFO mode.
	 * @param log the logger to be used
	 */
	public static void infoStackTrace(Logger log) {
		for (String s : getStackTrace(2)) {
			log.info(s);
		}
	}

	/**
	 * Dumps the stacktrace in DEBUG mode.
	 * @param log the logger to be used
	 */
	public static void debugStackTrace(Logger log) {
		for (String s : getStackTrace(2)) {
			log.debug(s);
		}
	}

	/**
	 * Dumps the stacktrace in TRACE mode.
	 * @param log the logger to be used
	 */
	public static void traceStackTrace(Logger log) {
		for (String s : getStackTrace(2)) {
			log.trace(s);
		}
	}

	/**
	 * Loads a property file.
	 * <p>The URI resource is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param uri the URI to load from
	 * @return the properties
	 * @throws IOException when file cannot be loaded
	 */
	public static Properties loadProperties(URI uri) throws IOException {
		Properties props = new Properties();
		loadProperties(props, uri);
		return props;
	}

	/**
	 * Loads a property file.
	 * <p>The URL resource is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param url the URL to load from
	 * @return the properties
	 * @throws IOException when file cannot be loaded
	 */
	public static Properties loadProperties(URL url) throws IOException {
		Properties props = new Properties();
		loadProperties(props, url);
		return props;
	}

	/**
	 * Loads a property file.
	 * <p>The File resource is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param file the file to load
	 * @return the properties
	 * @throws IOException when file cannot be loaded
	 */
	public static Properties loadProperties(File file) throws IOException {
		Properties props = new Properties();
		loadProperties(props, file);
		return props;
	}

	/**
	 * Loads a property file.
	 * <p>The file resource is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param file the file to load
	 * @return the properties
	 * @throws IOException when file cannot be loaded
	 */
	public static Properties loadProperties(String file) throws IOException {
		return loadProperties(new File(file));
	}

	/**
	 * Loads a property file.
	 * <p>The File resource is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param props the properties object
	 * @param file the file to load
	 * @throws IOException when file cannot be loaded
	 */
	public static void loadProperties(Properties props, File file) throws IOException {
		loadProperties(props, new FileInputStream(file));
	}

	/**
	 * Loads a property file.
	 * <p>The URI resource is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param props the properties object
	 * @param uri the uri to load
	 * @throws IOException when file cannot be loaded
	 */
	public static void loadProperties(Properties props, URI uri) throws IOException {
		loadProperties(props, uri.toURL());
	}

	/**
	 * Loads a property file.
	 * <p>The URL resource is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param props the properties object
	 * @param url the url to load
	 * @throws IOException when file cannot be loaded
	 */
	public static void loadProperties(Properties props, URL url) throws IOException {
		loadProperties(props, url.openStream());
	}

	/**
	 * Loads a property file.
	 * <p>The input stream is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param props the properties object
	 * @param inputStream the stream to load from
	 * @throws IOException when file cannot be loaded
	 */
	public static void loadProperties(Properties props, InputStream inputStream) throws IOException {
		try {
			props.load(inputStream);
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Loads a property file.
	 * <p>The file resource is in a simple line-oriented format as specified in {@link Properties#load(Reader)} and is assumed to use
	 * the ISO 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and 
	 * certain special characters, are represented in keys and elements using Unicode escapes as defined in
	 * section 3.3 of The Java Language Specification.</p> 
	 * @param props the properties object
	 * @param file the file to load
	 * @throws IOException when file cannot be loaded
	 */
	public static void loadProperties(Properties props, String file) throws IOException {
		loadProperties(props, new File(file));
	}

	/**
	 * Stores a property file.
	 * @param props the properties object
	 * @param file the file to load
	 * @throws IOException when file cannot be stored
	 */
	public static void storeProperties(Properties props, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			props.store(out, null);
		} finally {
			out.close();
		}
	}

	/**
	 * Stores a property file.
	 * @param props the properties object
	 * @param file the file to load
	 * @throws IOException when file cannot be stored
	 */
	public static void storeProperties(Properties props, String file) throws IOException {
		storeProperties(props, new File(file));
	}

	/**
	 * Returns the URL connect timeout.
	 * @return the connectTimeout
	 */
	public static int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * Sets the URL connect timeout.
	 * @param connectTimeout the connectTimeout to set
	 */
	public static void setConnectTimeout(int connectTimeout) {
		CommonUtils.connectTimeout = connectTimeout;
	}

	/**
	 * Returns the URL read timeout.
	 * @return the readTimeout
	 */
	public static int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * Sets the URL read timeout.
	 * @param readTimeout the readTimeout to set
	 */
	public static void setReadTimeout(int readTimeout) {
		CommonUtils.readTimeout = readTimeout;
	}

	/**
	 * Loads the content of the URI as a string.
	 * @param uri URI to be loaded
	 * @return the content of the URI
	 * @throws IOException when content of URI cannot be loaded
	 */
	public static String loadContent(URI uri) throws IOException {
		return loadContent(uri, null);
	}

	/**
	 * Loads the content of the URI as a string.
	 * @param uri URI to be loaded
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @return the content of the URI
	 * @throws IOException when content of URI cannot be loaded
	 */
	public static String loadContent(URI uri, Charset charset) throws IOException {
		return loadContent(uri.toURL(), charset);
	}

	/**
	 * Loads the content of the URL as a string.
	 * @param url URL to be loaded
	 * @return the content of the URL
	 * @throws IOException when content of URL cannot be loaded
	 */
	public static String loadContent(URL url) throws IOException {
		return loadContent(url, null);
	}

	/**
	 * Loads the content of the URL as a string.
	 * @param url URL to be loaded
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @return the content of the URL
	 * @throws IOException when content of URL cannot be loaded
	 */
	public static String loadContent(URL url, Charset charset) throws IOException {
		return loadContent(url.openConnection(), charset);
	}

	/**
	 * Loads the content of the URL as a string.
	 * @param con URL connection to be used
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @return the content of the URL
	 * @throws IOException when content of URL cannot be loaded
	 */
	public static String loadContent(URLConnection con, Charset charset) throws IOException {
		con.setConnectTimeout(getConnectTimeout());
		con.setReadTimeout(getReadTimeout());
		return loadContent(con.getInputStream(), charset);
	}

	/**
	 * Loads the content of a file as a string.
	 * @param name name of file to be loaded
	 * @return the content of the file
	 * @throws IOException when content of file cannot be loaded
	 */
	public static String loadContent(String name) throws IOException {
		return loadContent(name, null);
	}

	/**
	 * Loads the content of a file as a string.
	 * @param name name of file to be loaded
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @return the content of the file
	 * @throws IOException when content of file cannot be loaded
	 */
	public static String loadContent(String name, Charset charset) throws IOException {
		return loadContent(new File(name), charset);
	}

	/**
	 * Loads the content of a file as a string.
	 * @param f file to be loaded
	 * @return the content of the file
	 * @throws IOException when content of file cannot be loaded
	 */
	public static String loadContent(File f) throws IOException {
		return loadContent(f, null);    	
	}

	/**
	 * Loads the content of a file as a string.
	 * @param f file to be loaded
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @return the content of the file
	 * @throws IOException when content of file cannot be loaded
	 */
	public static String loadContent(File f, Charset charset) throws IOException {
		return loadContent(new FileInputStream(f), charset);    	
	}

	/**
	 * Loads the content of a stream as a string.
	 * @param in stream to be loaded
	 * @return the content of the stream
	 * @throws IOException when content of stream cannot be loaded
	 */
	public static String loadContent(InputStream in) throws IOException {
		return loadContent(in, null);
	}

	/**
	 * Loads the content of a stream as a string.
	 * @param in stream to be loaded
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @return the content of the stream
	 * @throws IOException when content of stream cannot be loaded
	 */
	public static String loadContent(InputStream in, Charset charset) throws IOException {
		if (charset == null) charset = Charset.defaultCharset();
		try {
			return IOUtils.toString(in, charset);
		} finally {
			in.close();
		}
	}

	/**
	 * Loads the content of a reader as a string.
	 * @param reader reader to be loaded
	 * @return the content of the reader
	 * @throws IOException when content of reader cannot be loaded
	 */
	public static String loadContent(Reader reader) throws IOException {
		try {
			return IOUtils.toString(reader);
		} finally {
			reader.close();
		}
	}

	/**
	 * Writes the string to a file.
	 * @param name name of file to be written to
	 * @param content the content to be written
	 * @throws IOException when content cannot be written
	 */
	public static void writeContent(String name, String content) throws IOException {
		writeContent(name, content, null);
	}

	/**
	 * Writes the string to a file.
	 * @param name name of file to be written to
	 * @param content the content to be written
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @throws IOException when content cannot be written
	 */
	public static void writeContent(String name, String content, Charset charset) throws IOException {
		writeContent(new File(name), content, charset);
	}

	/**
	 * Writes the string to a file.
	 * @param f file to be written to
	 * @param content the content to be written
	 * @throws IOException when content cannot be written
	 */
	public static void writeContent(File f, String content) throws IOException {
		writeContent(f, content, null);    	
	}

	/**
	 * Writes the string to a file.
	 * @param f file to be written to
	 * @param content the content to be written
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @throws IOException when content cannot be written
	 */
	public static void writeContent(File f, String content, Charset charset) throws IOException {
		writeContent(new FileOutputStream(f), content, charset);    	
	}

	/**
	 * Writes the string to a stream.
	 * @param out stream to be written to
	 * @param content the content to be written
	 * @throws IOException when content cannot be written
	 */
	public static void writeContent(OutputStream out, String content) throws IOException {
		writeContent(out, content, null);
	}

	/**
	 * Writes the string to a stream.
	 * @param out stream to be written to
	 * @param content the content to be written
	 * @param charset the charset of the content (<code>null</code> for {@link Charset#defaultCharset() default charset})
	 * @throws IOException when content cannot be written
	 */
	public static void writeContent(OutputStream out, String content, Charset charset) throws IOException {
		if (charset == null) charset = Charset.defaultCharset();
		writeContent(new OutputStreamWriter(out, charset), content);
	}

	/**
	 * Writes the string to a writer.
	 * @param writer writer to be written to
	 * @param content the content to be written
	 * @throws IOException when content cannot be written
	 */
	public static void writeContent(Writer writer, String content) throws IOException {
		try {
			writer.write(content);
		} finally {
			if (writer != null) writer.close();
		}
	}

	/**
	 * Tells whether runtime is a Windows system. 
	 * @return <code>true</code> when OS is a Windows system
	 */
	public static boolean isWindows() {
		return OS.indexOf("win") >= 0;
	}

	/**
	 * Tells whether runtime is a Mac system. 
	 * @return <code>true</code> when OS is a Mac system
	 */
	public static boolean isMac() {
		return OS.indexOf("mac") >= 0;
	}

	/**
	 * Tells whether runtime is a Unix system. 
	 * @return <code>true</code> when OS is a Unix system
	 */
	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0) || (OS.indexOf("nux") >= 0) || (OS.indexOf("aix") > 0) || (OS.indexOf("sunos") >= 0);
	}

	/**
	 * Tells whether runtime is a Windows system. 
	 * @return <code>true</code> when OS is a Windows system
	 */
	public static boolean isSolaris() {
		return OS.indexOf("sunos") >= 0;
	}

	/**
	 * Checks validity of an email address.
	 * @param s the string to be checked
	 * @return <code>true</code> when string is a valid email address
	 * @since 1.2.9
	 */
	public static boolean isEmail(String s) {
		return !isEmpty(s) && EmailValidator.getInstance().isValid(s.trim());
	}

	/**
	 * Returns the OS name.
	 * @return the OS name as returned by <code>System.getProperty("os.name")</code>
	 */
	public static String getOS() {
		return OS;
	}

	/**
	 * Set the anchor id for at the given URL.
	 * @param url url to be modified
	 * @param anchor new anchor to be set
	 * @return the modified URL but other components are kept
	 */
	public static URL setAnchor(URL url, String anchor) {
		try {
			String paths[] = url.getPath().split("#", 2);
			if (isEmpty(anchor)) {
				return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), paths[0], url.getQuery(), null).toURL();
			} else {
				return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), paths[0], url.getQuery(), anchor).toURL();
			}
		} catch (MalformedURLException e) {
			LoggerFactory.getLogger(CommonUtils.class).error("Cannot append anchor", e);
		} catch (URISyntaxException e) {
			LoggerFactory.getLogger(CommonUtils.class).error("Cannot append anchor", e);
		}
		return url;
	}

	/**
	 * Replaces environment variables. 
	 * <p>
	 * The variables must be formed as <code>$ENV{&lt;name&gt;}</code>, e.g. <code>$ENV{PATH}</code>.
	 * </p>
	 * @param s string to be analyzed (can be null or empty)
	 * @return string with variables replaced
	 */
	public static String replaceEnvVariables(String s) {
		if (isEmpty(s)) return s;
		int replaces = 0;
		do {
			replaces = 0;
			StringBuilder replaced = new StringBuilder();
			Matcher m = ENV_VAR_PATTERN.matcher(s);
			int lastEnd = 0;
			while (m.find()) {
				String name = m.group(1);
				int start = m.start();
				if (start > lastEnd) replaced.append(s.substring(lastEnd, start));
				replaced.append(System.getenv(name));
				lastEnd = m.end();
				replaces++;
			}
			if (lastEnd < s.length()) {
				replaced.append(s.substring(lastEnd, s.length()));
			}
			s = replaced.toString();

		} while (replaces > 0);

		return s;
	}			

	/**
	 * Replaces environment variables. 
	 * <p>
	 * The variables must be formed as <code>$SYSTEM{&lt;name&gt;}</code>, e.g. <code>$SYSTEM{user.home}</code>.
	 * </p>
	 * @param s string to be analyzed (can be null or empty)
	 * @return string with variables replaced
	 */
	public static String replaceRuntimeVariables(String s) {
		if (isEmpty(s)) return s;
		int replaces = 0;
		do {
			replaces = 0;
			StringBuilder replaced = new StringBuilder();
			Matcher m = RUNTIME_VAR_PATTERN.matcher(s);
			int lastEnd = 0;
			while (m.find()) {
				String name = m.group(1);
				int start = m.start();
				if (start > lastEnd) replaced.append(s.substring(lastEnd, start));
				replaced.append(System.getProperty(name));
				lastEnd = m.end();
				replaces++;
			}
			if (lastEnd < s.length()) {
				replaced.append(s.substring(lastEnd, s.length()));
			}
			s = replaced.toString();

		} while (replaces > 0);

		return s;
	}			

	/**
	 * Replaces environment and system variables. 
	 * <p>
	 * The variables must be formed as:
	 * <ul>
	 * <li><code>$ENV{&lt;name&gt;}</code>, e.g. <code>$ENV{PATH}</code></li>
	 * <li><code>$SYSTEM{&lt;name&gt;}</code>, e.g. <code>$SYSTEM{user.home}</code></li>
	 * </ul>
	 * @param s string to be analyzed (can be null or empty)
	 * @return string with variables replaced
	 */
	public static String replaceVariables(String s) {
		if (isEmpty(s)) return s;
		String oldS = s;
		do {
			oldS = s;
			s = replaceEnvVariables(s);
			s = replaceRuntimeVariables(s);
		} while (!s.equals(oldS));

		return s;
	}

	/**
	 * Sets markers in a template.
	 * <p>The markers must be like &#123;@prefix:attribute-name&#125;. The attribute value
	 * is the value of the getter method of the value object.</p>
	 * @param template the template
	 * @param prefix the marker prefix
	 * @param valueObject the object that contains values
	 * @return the template with markers replaced
	 */
	public static String setMarkers(String template, String prefix, Object valueObject) {
		if (valueObject == null) return template;
		if (template == null) return null;

		Map<?,?> oValues = null;
		if (valueObject instanceof Map) {
			oValues = (Map<?,?>)valueObject;
		} else {
			Map<Object,Object> map = new HashMap<Object,Object>();
			for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(valueObject)) {
				try {
					Method m = desc.getReadMethod();
					if (m != null) {
						Object value = m.invoke(valueObject);
						map.put(desc.getName(), value);
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					map.put(desc.getName(), null);
				}
			}
			oValues = map;
		}
		for (Map.Entry<?, ?> entry : oValues.entrySet()) {
			Object value = entry.getValue();
			String key   = entry.getKey().toString();
			String marker = "\\{@"+prefix+":"+key+"\\}";
			if (value != null) {
				template = template.replaceAll(marker, value.toString());
			} else  {
				template = template.replaceAll(marker, "");
			}
		}

		return template;
	}
	
	/**
	 * Generates a random string of length 10 with {@link #ALPHA_NUM_SPECIAL_CHARS}.
	 * @return the generated random string
	 * @since 4.0.2
	 */
	public static String generateRandomString() {
		return generateRandomString(ALPHA_NUM_SPECIAL_CHARS, 10);
	}
	
	/**
	 * Generates a random string with {@link #ALPHA_NUM_SPECIAL_CHARS}.
	 * @param length       - length of string (using 10 if less than 1)
	 * @return the generated random string
	 * @since 4.0.2
	 */
	public static String generateRandomString(int length) {
		return generateRandomString(ALPHA_NUM_SPECIAL_CHARS, length);
	}
	
	/**
	 * Generates a random string of length 10.
	 * @param allowedChars - allowed chars (using {@link #ALPHA_NUM_SPECIAL_CHARS} when {@code null})
	 * @return the generated random string
	 * @since 4.0.2
	 */
	public static String generateRandomString(String allowedChars) {
		return generateRandomString(allowedChars, 10);
	}

	/**
	 * Generates a random string.
	 * @param allowedChars - allowed chars (using {@link #ALPHA_NUM_SPECIAL_CHARS} when {@code null})
	 * @param length       - length of string (using 10 if less than 1)
	 * @return the generated random string
	 * @since 4.0.2
	 */
	public static String generateRandomString(String allowedChars, int length) {
		if ((allowedChars == null) || (allowedChars.trim().length() == 0)) allowedChars = ALPHA_NUM_SPECIAL_CHARS;
		long randomInit = System.currentTimeMillis();
		if (length < 1) length = 10;

		Random random = new Random(randomInit);

		StringBuilder rc = new StringBuilder(length);
		while (rc.length() < length) {
			int n = random.nextInt(allowedChars.length());
			char c = allowedChars.charAt(n);
			if (!Character.isWhitespace(c)) rc.append(c);
		}

		return rc.toString();
	}

	/** 
	 * Create hex string (uppercase) from byte array.
	 * @param bytes bytes to convert
	 * @return hexadecimal formatting of the bytes
	 */
	public static String hex(byte[] bytes) {
		return hex(bytes, true);
	}
	
	/** 
	 * Create hex string from byte array.
	 * @param bytes bytes to convert
	 * @param upperCase when return value shall be uppercase.
	 * @return hexadecimal formatting of the bytes
	 */
	public static String hex(byte[] bytes, boolean upperCase) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return upperCase ? result.toString().toUpperCase() : result.toString();
    }

}
