package rs.jackson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;

import rs.baselib.util.CommonUtils;

/**
 * General helper utils for usage with Jackson.
 * @author ralph
 *
 */
public class JacksonUtils {

	/**
	 * Creates a {@link JavaType} reference for a list of given class.
	 * @param <T> the type of the items
	 * @param type the class of the items
	 * @return the {@link JavaType} for a list of these items
	 */
	public static <T> JavaType getListType(Class<T> type) {
		return Json.JSON.getJsonMapper().getTypeFactory().constructCollectionType(ArrayList.class, type);
	}
	
	/**
	 * Creates a {@link JavaType} reference for a set of given class.
	 * @param <T> the type of the items
	 * @param type the class of the items
	 * @return the {@link JavaType} for a set of these items
	 */
	public static <T> JavaType getSetType(Class<T> type) {
		return Json.JSON.getJsonMapper().getTypeFactory().constructCollectionType(HashSet.class, type);
	}
	
	/**
	 * Creates a {@link JavaType} reference for a map of given classes.
	 * @param <K> type of keys
	 * @param <V> type of values
	 * @param keyType class of keys
	 * @param valueType class of values
	 * @return the {@link JavaType} for a map of these items
	 */
	public static <K,V> JavaType getMapType(Class<K> keyType, Class<V> valueType) {
		return Json.JSON.getJsonMapper().getTypeFactory().constructMapType(HashMap.class, keyType, valueType);
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
