/**
 * 
 */
package rs.jackson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * YAML utils for mapping back and forth
 *
 * @author ralph
 *
 */
public class YamlUtils {

	private static YAMLFactory  yamlFactory;
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
			yamlMapper = new ObjectMapper(getYamlFactory());
			yamlMapper.findAndRegisterModules();
			yamlMapper.registerModule(new JavaTimeModule());
			yamlMapper.setSerializationInclusion(Include.NON_NULL);
		}
		return yamlMapper;
	}

	public static YAMLFactory getYamlFactory() {
		if (yamlFactory == null) {
			yamlFactory = new YAMLFactory();
		}
		return yamlFactory;
	}
	
	/**
	 * Returns a parser for the given file.
	 * @param file - the file to be parsed
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 */
	public static YAMLParser getParser(File file) throws IOException {
		return getYamlFactory().createParser(file);
	}

	/**
	 * Returns a parser for the given string.
	 * @param content - the content
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.lang.String)
	 */
	public static YAMLParser getParser(String content) throws IOException {
		return getYamlFactory().createParser(content);
	}

	/**
	 * Returns a parser for the given URL resource.
	 * @param url - the URL
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.net.URL)
	 */
	public static YAMLParser getParser(URL url) throws IOException {
		return getYamlFactory().createParser(url);
	}

	/**
	 * Returns a parser for the given input stream.
	 * @param in - the input stream
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.io.InputStream)
	 */
	public static YAMLParser getParser(InputStream in) throws IOException {
		return getYamlFactory().createParser(in);
	}

	/**
	 * Returns a parser for the given reader.
	 * @param r - the reader
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.io.Reader)
	 */
	public static YAMLParser getParser(Reader r) throws IOException {
		return getYamlFactory().createParser(r);
	}

	/**
	 * Returns a parser for the given bytes.
	 * @param data - the data in bytes
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(byte[])
	 */
	public static YAMLParser getParser(byte[] data) throws IOException {
		return getYamlFactory().createParser(data);
	}

	/**
	 * Parses multiple documents in a YAML file.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param file - the YAML file to read from
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(File file, Class<T> clazz) throws IOException {
		return parseMultiple(getParser(file), clazz);
		
	}

	/**
	 * Parses multiple documents from a reader.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param reader - the reader to read from
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(Reader reader, Class<T> clazz) throws IOException {
		return parseMultiple(getParser(reader), clazz);
		
	}

	/**
	 * Parses multiple documents from an input stream.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param inputStream - the inputStream to read from
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(InputStream inputStream, Class<T> clazz) throws IOException {
		return parseMultiple(getParser(inputStream), clazz);
		
	}

	/**
	 * Parses multiple documents froma URL.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param url - the URL to read from
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(URL url, Class<T> clazz) throws IOException {
		return parseMultiple(getParser(url), clazz);
	}

	/**
	 * Reads multiple documents from a parser.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param parser - the YAML parser initialized with content
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(YAMLParser parser, Class<T> clazz) throws IOException {
		ObjectMapper mapper = getYamlMapper();
		return mapper.readValues(parser, new TypeReference<T>() {}).readAll();
	}
}
