/**
 * 
 */
package rsbaselib.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;




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
		if (e instanceof LocaleDisplayProvider) {
			return ((LocaleDisplayProvider)e).getDisplay(locale);
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

}
