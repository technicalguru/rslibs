/**
 * 
 */
package rs.baselib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;




/**
 * Common Utils.
 * @author ralph
 *
 */
public class CommonUtils {

	/**
	 * The formatter for dates (see {@link DateFormat#SHORT}.
	 */
	public static final DateFormat DATE_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
	

	/**
	 * Formatters for numbers.
	 */
	public static final NumberFormat SIMPLE_NUMBER_FORMATTER = NumberFormat.getNumberInstance(Locale.getDefault());
	public static final NumberFormat SIMPLE_INT_FORMATTER = NumberFormat.getIntegerInstance(Locale.getDefault());
	
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
		return SIMPLE_NUMBER_FORMATTER.format(amount);
	}
	
	/**
	 * Formats the given date.
	 * @param date date to format
	 * @return the formatted string (see {@link #DATE_FORMATTER})
	 */
	public static String toString(RsDate date) {
		if ((date == null) || (date.getTimeInMillis() == 0)) return "";
		return DATE_FORMATTER.format(date.getTime());
	}
	
	/**
	 * Formats the given month (1st day of month).
	 * @param month date to format
	 * @return the formatted string (see {@link #DATE_FORMATTER})
	 */
	public static String toString(RsMonth month) {
		if ((month == null) || (month.getTimeInMillis() == 0)) return "";
		return DATE_FORMATTER.format(month.getBegin().getTime());
	}
	
	/**
	 * Generates a string presentation of the given bytes.
	 * @param b byte array
	 * @return string representation
	 */
	public static String toString(byte b[]) {
		StringBuffer buf = new StringBuffer();
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
		StringBuffer buf = new StringBuffer();
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
		String rc[] = new String[enums.length];
		for (int i=0; i<enums.length; i++) {
			rc[i] = getDisplay(enums[i], locale);
		}
		return rc;
	}
	
	/**
	 * Returns a list of options from an enumeration class.
	 * @param clazz enum class
	 * @return list of display options
	 */
	public static List<Enum<?>> getOptionList(Class<? extends Enum<?>> clazz) {
		Enum<?> arr[] = clazz.getEnumConstants();
		List<Enum<?>> rc = new ArrayList<Enum<?>>();
		for (Enum<?> s: arr) rc.add(s);
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
		for (Enum<?> e : clazz.getEnumConstants()) {
			if (display.equals(getDisplay(e, locale))) return e;
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
		String vParts[] = version.split("\\.");
		String minParts[] = null;
		String maxParts[] = null;
		if (minVersion != null) minParts = minVersion.split("\\.");
		if (maxVersion != null) maxParts = maxVersion.split("\\.");
		
		// Check minimum version
		if ((minParts != null) && (compareVersion(minParts, vParts) > 0)) return false;
		// Check maximum version
		if ((maxParts != null) && (compareVersion(maxParts, vParts) < 0)) return false;
		
		return true;
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
		String s = "";
		for (int i=0; i<parts.length; i++) s += separator + parts[i];
		if (s.length() > 0) s = s.substring(separator.length());
		return s;
	}
	
	/**
	 * Recursively debugs objects and adds this in the string buffer.
	 * @param s string buffer to enhance
	 * @param o object to debug
	 */
	public static void debugObject(StringBuffer s, Object o) {
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
			StringBuffer s = new StringBuffer();
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
        props.load(in);
        in.close();
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
        props.store(out, null);
        out.close();
    }

    /**
     * Stores a property file.
     * @param props the properties object
     * @param file the file to load
     */
    public static void storeProperties(Properties props, String file) throws IOException {
        storeProperties(props, new File(file));
    }
}
