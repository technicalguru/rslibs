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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.bean.NamedObject;




/**
 * Common Utils.
 * @author ralph
 *
 */
public class CommonUtils {

	private static Pattern ENV_VAR_PATTERN     = Pattern.compile("\\$ENV\\{([^\\}]+)\\}");
	private static Pattern RUNTIME_VAR_PATTERN = Pattern.compile("\\$RUNTIME\\{([^\\}]+)\\}");
	
	/**
	 * The formatter for dates (see {@link DateFormat#SHORT}).
	 */
	public static DateFormat DATE_FORMATTER() {
		return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
	}

	/**
	 * The formatter for dates incl. times (see {@link DateFormat#SHORT}).
	 */
	public static DateFormat DATE_TIME_FORMATTER() {
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
	}


	/**
	 * Formatter for real numbers.
	 */
	public static NumberFormat SIMPLE_NUMBER_FORMATTER() {
		return NumberFormat.getNumberInstance(Locale.getDefault());
	}

	/** Formatter for integers. */
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
	 * Formats the given date.
	 * @param date date to format
	 * @return the formatted string (see {@link #DATE_TIME_FORMATTER})
	 */
	public static String toString(RsDate date) {
		if ((date == null) || (date.getTimeInMillis() == 0)) return "";
		return DATE_TIME_FORMATTER().format(date.getTime());
	}

	/**
	 * Formats the given day.
	 * @param day day to format
	 * @return the formatted string (see {@link #DATE_FORMATTER})
	 */
	public static String toString(RsDay day) {
		if ((day == null) || (day.getTimeInMillis() == -TimeZone.getDefault().getOffset(day.getTimeInMillis()))) return "";
		return DATE_FORMATTER().format(day.getTime());
	}

	/**
	 * Formats the given year.
	 * @param year year to format
	 * @return the formatted string
	 */
	public static String toString(RsYear year) {
		if ((year == null) || (year.get(Calendar.YEAR) == 1)) return "";
		return year.getKey();
	}

	/**
	 * Formats the given month (1st day of month).
	 * @param month date to format
	 * @return the formatted string (see {@link #DATE_FORMATTER})
	 */
	public static String toString(RsMonth month) {
		if ((month == null) || (month.getTimeInMillis() == 0)) return "";
		return DATE_FORMATTER().format(month.getBegin().getTime());
	}

	/**
	 * Generates a string presentation of the given bytes.
	 * @param b byte array
	 * @return string representation (Hex)
	 */
	public static String toString(byte b[]) {
		StringBuilder buf = new StringBuilder();
		for (int i=0; i<b.length; i++) {
			buf.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
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
	 * Checks for equality null-safe
	 * @param o1 object 1
	 * @param o2 object 2
	 * @return true when both values are equal
	 */
	public static boolean equals(Object o1, Object o2) {
		if ((o1 == null) && (o2 == null)) return true;
		if ((o1 != null) && (o2 != null)) return o1.equals(o2);
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
		if (trim) s = s.trim();
		return s.length() == 0;
	}

	/**
	 * Returns a list of options for display in default locale.
	 * @param clazz enum class
	 * @return list of display options
	 */
	public static String[] getOptions(Class<? extends Enum<?>> clazz) {
		return getOptions(clazz, Locale.getDefault());
	}

	/**
	 * Returns a list of options for display in given locale.
	 * @param clazz enum class
	 * @param locale locale
	 * @return list of display options
	 */
	public static String[] getOptions(Class<? extends Enum<?>> clazz, Locale locale) {
		Enum<?> enums[] = clazz.getEnumConstants();
		if (enums != null) {
			String rc[] = new String[enums.length];
			for (int i=0; i<enums.length; i++) {
				rc[i] = getDisplay(enums[i], locale);
			}
			return rc;
		}
		return new String[0];
	}

	/**
	 * Returns a list of options from an enumeration class.
	 * @param clazz enum class
	 * @return list of display options
	 */
	public static List<Enum<?>> getOptionList(Class<? extends Enum<?>> clazz) {
		Enum<?> arr[] = clazz.getEnumConstants();
		List<Enum<?>> rc = new ArrayList<Enum<?>>();
		if (arr != null) for (Enum<?> s: arr) rc.add(s);
		return rc;
	}

	/**
	 * Returns the display string for the default locale.
	 * @param e enum value
	 * @return display
	 */
	public static String getDisplay(Enum<?> e) {
		return getDisplay(e, Locale.getDefault());
	}

	/**
	 * Returns the display string for the given locale.
	 * @param e enum value
	 * @param locale locale
	 * @return display
	 */
	public static String getDisplay(Enum<?> e, Locale locale) {
		if (e instanceof ILocaleDisplayProvider) {
			return ((ILocaleDisplayProvider)e).getDisplay(locale);
		}
		return e.name();
	}

	/**
	 * Returns the enum constant for given display in default locale.
	 * @param clazz enum class
	 * @param display display of enum
	 * @return enum constant
	 */
	public static Enum<?> getEnum(Class<? extends Enum<?>> clazz, String display) {
		return getEnum(clazz, display, Locale.getDefault());
	}

	/**
	 * Returns the enum constant for given display in given locale.
	 * @param clazz enum class
	 * @param display display of enum
	 * @param locale locale
	 * @return enum constant
	 */
	public static Enum<?> getEnum(Class<? extends Enum<?>> clazz, String display, Locale locale) {
		Enum<?> arr[] = clazz.getEnumConstants();
		if (arr != null) {
			for (Enum<?> e : arr) {
				if (display.equals(getDisplay(e, locale))) return e;
			}
		}
		return null;
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
	 * @return -1 if v1 < v2, 1 if v1 > v2, 0 if v1 = v2
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
	 * @return -1 if v1 < v2, 1 if v1 > v2, 0 if v1 = v2
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
	 * Returns the given date as UNIX timestamp.
	 * @param date date object.
	 * @return time in seconds since January 1st, 1970, 00:00:00 UTC.
	 */
	public static long getUnixTimestamp(RsDate date) {
		return getUnixTimestamp(date.getTimeInMillis());
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
	 * @param file the file to load
	 * @return the properties
	 */
	public static Properties loadProperties(File file) throws IOException {
		Properties props = new Properties();
		loadProperties(props, file);
		return props;
	}

	/**
	 * Loads a property file.
	 * @param file the file to load
	 * @return the properties
	 */
	public static Properties loadProperties(String file) throws IOException {
		return loadProperties(new File(file));
	}

	/**
	 * Loads a property file.
	 * @param props the properties object
	 * @param file the file to load
	 */
	public static void loadProperties(Properties props, File file) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			props.load(in);
		} finally {
			in.close();
		}
	}

	/**
	 * Loads a property file.
	 * @param props the properties object
	 * @param file the file to load
	 */
	public static void loadProperties(Properties props, String file) throws IOException {
		loadProperties(props, new File(file));
	}

	/**
	 * Stores a property file.
	 * @param props the properties object
	 * @param file the file to load
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
	 */
	public static void storeProperties(Properties props, String file) throws IOException {
		storeProperties(props, new File(file));
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
		return loadContent(url.openStream(), charset);
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
		return loadContent(new InputStreamReader(in, charset));
	}

	/**
	 * Loads the content of a reader as a string.
	 * @param reader reader to be loaded
	 * @return the content of the reader
	 * @throws IOException when content of reader cannot be loaded
	 */
	public static String loadContent(Reader reader) throws IOException {
		BufferedReader r = null;
		try {
			StringBuilder rc = new StringBuilder(1000);
			r = new BufferedReader(reader);
			String line = null;
			while ((line = r.readLine()) != null) {
				rc.append(line);
				rc.append('\n');
			}
			return rc.toString();
		} finally {
			if (r != null) r.close();
			else reader.close();
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

	public static String getDisplay(Object o) {
		return getDisplay(o, Locale.getDefault());
	}

	/**
	 * Returns the display string of an object.
	 * The method detects {@link IDisplayable}, {@link IDisplayProvider} and {@link NamedObject}.
	 * @param o
	 * @param locale Locale to be used for {@link IDisplayable}
	 * @return a displayable string
	 */
	public static String getDisplay(Object o, Locale locale) {
		if (o == null) return "";
		String rc = o.toString();

		if (o instanceof IDisplayable) rc = ((IDisplayable)o).toString(locale);
		else if (o instanceof IDisplayProvider) rc = ((IDisplayProvider)o).getDisplay();
		else if (o instanceof NamedObject) rc = ((NamedObject)o).getName();
		return rc;
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
				return new URL(url.getProtocol(), url.getHost(), url.getPort(), paths[0]);
			} else {
				return new URL(url.getProtocol(), url.getHost(), url.getPort(), paths[0]+"#"+anchor);
			}
		} catch (MalformedURLException e) {
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
	 * @param values the object that contains values
	 * @return the template with markers replaced
	 */
	public static String setMarkers(String template, String prefix, Object valueObject) {
		try {
			Map<String,Object> oValues = PropertyUtils.describe(valueObject);
			for (Map.Entry<String, Object> entry : oValues.entrySet()) {
				Object value = entry.getValue();
				String key   = entry.getKey();
				String marker = "\\{@"+prefix+":"+key+"\\}";
				if (value != null) {
					template = template.replaceAll(marker, value.toString());
				} else  {
					template = template.replaceAll(marker, "");
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
		}
		return template;
	}
}
