package rs.jackson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;

/**
 * General helper utils for usage with Jackson.
 * @author ralph
 *
 */
public class JacksonUtils {

	/**
	 * Creates a {@link JavaType} reference for a list of given class (Jackson 3).
	 * @param <T> the type of the items
	 * @param type the class of the items
	 * @return the {@link JavaType} for a list of these items
	 */
	public static <T> JavaType getListType(Class<T> type) {
		return Json.JSON.getJsonMapper().getTypeFactory().constructCollectionType(ArrayList.class, type);
	}
	
	/**
	 * Creates a {@link JavaType} reference for a set of given class (Jackson 3).
	 * @param <T> the type of the items
	 * @param type the class of the items
	 * @return the {@link JavaType} for a set of these items
	 */
	public static <T> JavaType getSetType(Class<T> type) {
		return Json.JSON.getJsonMapper().getTypeFactory().constructCollectionType(HashSet.class, type);
	}
	
	/**
	 * Creates a {@link JavaType} reference for a map of given classes (Jackson 3).
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
	 * Creates a {@link JavaType} reference for a list of given class (Jackson 2).
	 * @param <T> the type of the items
	 * @param type the class of the items
	 * @return the {@link JavaType} for a list of these items
	 */
	public static <T> com.fasterxml.jackson.databind.JavaType getListType2(Class<T> type) {
		return Json2.JSON.getJsonMapper().getTypeFactory().constructCollectionType(ArrayList.class, type);
	}
	
	/**
	 * Creates a {@link JavaType} reference for a set of given class (Jackson 2).
	 * @param <T> the type of the items
	 * @param type the class of the items
	 * @return the {@link JavaType} for a set of these items
	 */
	public static <T> com.fasterxml.jackson.databind.JavaType getSetType2(Class<T> type) {
		return Json2.JSON.getJsonMapper().getTypeFactory().constructCollectionType(HashSet.class, type);
	}
	
	/**
	 * Creates a {@link JavaType} reference for a map of given classes (Jackson 2).
	 * @param <K> type of keys
	 * @param <V> type of values
	 * @param keyType class of keys
	 * @param valueType class of values
	 * @return the {@link JavaType} for a map of these items
	 */
	public static <K,V> com.fasterxml.jackson.databind.JavaType getMapType2(Class<K> keyType, Class<V> valueType) {
		return Json2.JSON.getJsonMapper().getTypeFactory().constructMapType(HashMap.class, keyType, valueType);
	}

	/**
	 * Traverse to the node given in path (Jackson 3).
	 * @param node the node to traverse from
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @return the node found
	 */
	public static Optional<JsonNode> traverse(JsonNode node, String path) {
		if (!isEmpty(path)) {
			String paths[] = path.split("\\.");
			if (paths.length > 0) {
				for (String p : paths) {
					if (!isEmpty(p)) {
						node = node.findPath(p.trim());
						if (node.isMissingNode()) return Optional.empty();
					}
				}
			}
		}
		
		return Optional.of(node);
	}

	/**
	 * Traverse to the node given in path (Jackson 2).
	 * @param node the node to traverse from
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @return the node found
	 */
	public static Optional<com.fasterxml.jackson.databind.JsonNode> traverse(com.fasterxml.jackson.databind.JsonNode node, String path) {
		if (!isEmpty(path)) {
			String paths[] = path.split("\\.");
			if (paths.length > 0) {
				for (String p : paths) {
					if (!isEmpty(p)) {
						node = node.findPath(p.trim());
						if (node.isMissingNode()) return Optional.empty();
					}
				}
			}
		}
		
		return Optional.of(node);
	}

	/**
	 * Returns true when the given string is null empty. 
	 * @param s the string to be checked
	 * @return true when string must be regarded as empty
	 */
	private static boolean isEmpty(String s) {
		if (s == null) return true;
		return s.trim().length() == 0;
	}
}
