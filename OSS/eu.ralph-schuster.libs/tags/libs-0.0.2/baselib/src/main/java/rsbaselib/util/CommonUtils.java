/**
 * 
 */
package rsbaselib.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;




/**
 * Common Utils.
 * @author ralph
 *
 */
public class CommonUtils {

	/**
	 * The formatter for dates.
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
	 * Formats the given date
	 * @param date
	 * @return
	 */
	public static String toString(RsDate date) {
		if ((date == null) || (date.getTimeInMillis() == 0)) return "";
		return DATE_FORMATTER.format(date.getTime());
	}
	
	/**
	 * Formats the given date
	 * @param date
	 * @return
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
	 * Returns a list of options for display in given locale.
	 * @param clazz enum class
	 * @param locale locale
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

}
