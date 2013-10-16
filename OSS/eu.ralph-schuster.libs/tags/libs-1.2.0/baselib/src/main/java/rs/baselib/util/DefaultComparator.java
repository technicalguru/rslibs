/**
 * 
 */
package rs.baselib.util;

import java.util.Comparator;
import java.util.Locale;

/**
 * Default comparator for objects.
 * The comparator compares NULL-safe and evaluates interfaces that help
 * when comparing, such as {@link Comparable}, {@link IDisplayProvider}
 * and {@link IDisplayable}. If none of these interfaces are implemented
 * then the {@link Object#toString()} values will be ignore with
 * {@link String#compareToIgnoreCase(String)}.
 * @author ralph
 *
 */
public class DefaultComparator implements Comparator<Object> {

	/** A default final instance for fast usage */
	public static final Comparator<Object> INSTANCE = new DefaultComparator();
	
	/**
	 * Constructor.
	 */
	public DefaultComparator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int compare(Object o1, Object o2) {
		if ((o1 == null) && (o2 == null)) return 0;
		if ((o1 == null) && (o2 != null)) return 1;
		if ((o1 != null) && (o2 == null)) return -1;

		if (o1 instanceof Comparable<?>) return ((Comparable)o1).compareTo(o2);
		if (o2 instanceof Comparable<?>) return -((Comparable)o2).compareTo(o1);
		
		String s1 = o1.toString();
		String s2 = o2.toString();
		
		if (o1 instanceof IDisplayable) s1 = ((IDisplayable)o1).toString(Locale.getDefault());
		if (o2 instanceof IDisplayable) s2 = ((IDisplayable)o2).toString(Locale.getDefault());
		
		if (o1 instanceof IDisplayProvider) s1 = ((IDisplayProvider)o1).getDisplay();
		if (o2 instanceof IDisplayProvider) s2 = ((IDisplayProvider)o2).getDisplay();
		return s1.compareToIgnoreCase(s2);
	}

	
}
