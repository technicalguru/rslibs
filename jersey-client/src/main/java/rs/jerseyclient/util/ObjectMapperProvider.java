package rs.jerseyclient.util;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides a custom {@link ObjectMapper}.
 * 
 * @author ralph
 *
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private static ObjectMapper mapper;

	/**
	 * Sets the {@link ObjectMapper} object.
	 * 
	 * @param mapper the mapper to provide
	 */
	public static void setMapper(ObjectMapper mapper) {
		ObjectMapperProvider.mapper = mapper;
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}
