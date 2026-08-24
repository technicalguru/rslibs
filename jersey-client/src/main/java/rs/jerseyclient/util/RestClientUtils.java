package rs.jerseyclient.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utils required.
 * 
 * @author ralph
 *
 */
public class RestClientUtils {

	/**
	 * Returns true when the given string is null empty. 
	 * @param s the string to be checked
	 * @return true when string must be regarded as empty
	 */
	public static boolean isEmpty(String s) {
		if (s == null) return true;
		return s.trim().length() == 0;
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
	
}
