package rs.jackson;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import rs.baselib.util.CommonUtils;

/**
 * General helper utils for usage with Jackson.
 * @author ralph
 *
 */
public class JacksonUtils {

	/**
	 * Creates a {@link TypeReference} reference for a list of given class.
	 * @param <T> the type of the items
	 * @param type the class of the items
	 * @return the {@link TypeReference} for a list of these items
	 */
	public static <T> TypeReference<ArrayList<T>> getListTypeRef(Class<T> type) {
		return new TypeReference<ArrayList<T>>() {};
	}
	
	/**
	 * Creates a {@link TypeReference} reference for a set of given class.
	 * @param <T> the type of the items
	 * @param type the class of the items
	 * @return the {@link TypeReference} for a set of these items
	 */
	public static <T> TypeReference<Set<T>> getSetTypeRef(Class<T> type) {
		return new TypeReference<Set<T>>() {};
	}
	
	/**
	 * Creates a {@link TypeReference} reference for a map of given key and value classes.
	 * @param <K> type of keys
	 * @param <V> type of values
	 * @param keyClass class of keys
	 * @param valueClass class of values
	 * @return the {@link TypeReference} for a map of these types
	 */
	public static <K,V> TypeReference<Map<K,V>> getMapTypeRef(Class<K> keyClass, Class<V> valueClass) {
		return new TypeReference<Map<K,V>>() {};
	}

	/**
	 * Traverse to the node given in path.
	 * @param node the node to traverse from
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @return the node found
	 */
	public static Optional<JsonNode> traverse(JsonNode node, String path) {
		if (!CommonUtils.isEmpty(path)) {
			String paths[] = path.split("\\.");
			if (paths.length > 0) {
				for (String p : paths) {
					if (!CommonUtils.isEmpty(p)) {
						node = node.findPath(p.trim());
						if (node.isMissingNode()) return Optional.empty();
					}
				}
			}
		}
		
		return Optional.of(node);
	}

}
