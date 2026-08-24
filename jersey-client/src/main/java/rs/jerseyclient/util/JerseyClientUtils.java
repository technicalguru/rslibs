package rs.jerseyclient.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utils for ease of use.
 * @author ralph
 *
 */
public class JerseyClientUtils {

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

}
