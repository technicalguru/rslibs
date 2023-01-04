/**
 * 
 */
package rs.jackson;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * YAML utils for mapping back and forth
 *
 * @author ralph
 *
 */
public class YamlUtils {

	private static ObjectMapper yamlMapper;
	
	/**
	 * Convert any object to its Json representation.
	 * @param o - the object to convert
	 * @return the YAML string
	 */
	public static String toYaml(Object o) {
		try {
			return getYamlMapper().writeValueAsString(o);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert to YAML", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param json YAML string
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromYaml(String json, Class<T> clazz) {
		try {
			return getYamlMapper().readValue(json, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+json+"\"", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param json YAML string
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromYaml(String json, JavaType type) {
		try {
			return getYamlMapper().readValue(json, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+json+"\"", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param json YAML string
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromYaml(String json, TypeReference<T> type) {
		try {
			return getYamlMapper().readValue(json, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+json+"\"", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromYaml(File file, Class<T> clazz) {
		try {
			return getYamlMapper().readValue(file, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromYaml(File file, JavaType type) {
		try {
			return getYamlMapper().readValue(file, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromYaml(File file, TypeReference<T> type) {
		try {
			return getYamlMapper().readValue(file, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromYaml(InputStream stream, Class<T> clazz) {
		try {
			return getYamlMapper().readValue(stream, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromYaml(InputStream stream, JavaType type) {
		try {
			return getYamlMapper().readValue(stream, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromYaml(InputStream stream, TypeReference<T> type) {
		try {
			return getYamlMapper().readValue(stream, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromYaml(Reader reader, Class<T> clazz) {
		try {
			return getYamlMapper().readValue(reader, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromYaml(Reader reader, JavaType type) {
		try {
			return getYamlMapper().readValue(reader, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromYaml(Reader reader, TypeReference<T> type) {
		try {
			return getYamlMapper().readValue(reader, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}
	
	/**
	 * Returns a configured JsonMapper object.
	 * @return the JsonMapper
	 */
	public static ObjectMapper getYamlMapper() {
		if (yamlMapper == null) {
			yamlMapper = new ObjectMapper(new YAMLFactory());
			yamlMapper.findAndRegisterModules();
			yamlMapper.registerModule(new JavaTimeModule());
			yamlMapper.setSerializationInclusion(Include.NON_NULL);
		}
		return yamlMapper;
	}

}
