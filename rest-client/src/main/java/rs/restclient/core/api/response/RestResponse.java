/**
 * 
 */
package rs.restclient.core.api.response;

import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;

import rs.jackson.Json;
import rs.jackson.Json2;
import rs.restclient.core.api.Target;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;

/**
 * A wrapper for the response.
 */
public abstract class RestResponse {

	private Target                         target;
	private JsonMapper                     jsonMapper;
	private com.fasterxml.jackson.databind.json.JsonMapper jsonMapper2;
	private Json                           json;
	private Json2                          json2;
	private MultiValuedMap<String, String> headers;
	private Integer                        statusCode;
	private String                         statusMessage;
	private Optional<String>               body;
	
	/**
	 * Constructs response using the target and its configuration.
	 * @param configuration configuration
	 */
	protected RestResponse(Target target) {
		this(target, null, null);
	}
		
	/**
	 * Constructs response using the target and its configuration.
	 * @param jsonMapper json mapper object (can be null for configured mapper)
	 * @param configuration configuration
	 * @deprecated This is a Jackson2 method. Do use only when you have not alternate way of using Jackson 3.
	 */
	@Deprecated
	protected RestResponse(Target target, JsonMapper jsonMapper) {
		this(target, jsonMapper, null);
	}
		
	/**
	 * Constructs response using the target and its configuration.
	 * @param jsonMapper json mapper object for Jacksonn 2 (can be null for configured mapper)
	 * @param configuration configuration
	 * @deprecated This is a Jackson2 method. Do use only when you have not alternate way of using Jackson 3.
	 */
	@Deprecated
	protected RestResponse(Target target, com.fasterxml.jackson.databind.json.JsonMapper jsonMapper) {
		this(target, null, jsonMapper);
	}
		
	/**
	 * Constructs response using the target and an optional jsonMapper.
	 * @param configuration configuration
	 * @param jsonMapper json mapper object (can be null for configured mapper)
	 * @param jsonMapper2 json mapper object for Jackson 2 (can be null for configured mapper)
	 * @deprecated This is a Jackson2 method. Do use only when you have not alternate way of using Jackson 3.
	 */
	@Deprecated
	protected RestResponse(Target target, JsonMapper jsonMapper, com.fasterxml.jackson.databind.json.JsonMapper jsonMapper2) {
		this.target      = target;
		this.jsonMapper  = jsonMapper;
		this.jsonMapper2 = jsonMapper2;
		this.json        = null;
	}

	/**
	 * Returns the headers.
	 * @return the headers
	 */
	public final MultiValuedMap<String, String> getHeaders() {
		if (headers == null) {
			headers = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(retrieveHeaders());
		}
		return headers;
	}

	/**
	 * Returns the statusCode.
	 * @return the statusCode
	 */
	public final int getStatusCode() {
		if (statusCode == null) {
			statusCode = retrieveStatusCode();
		}
		return statusCode;
	}

	/**
	 * Returns the statusMessage.
	 * @return the statusMessage
	 */
	public final String getStatusMessage() {
		if (statusMessage == null) {
			statusMessage = retrieveStatusMessage();
		}
		return statusMessage;
	}

	/**
	 * Returns the body.
	 * @return the body or null if no body is available.
	 */
	public final String getBody() {
		if (body == null) {
			body = retrieveBody();
		};
		return body.orElse(null);
	}
	
	/**
	 * Retrieves the body.
	 * @return returns an empty {@link Optional} when no body is available.
	 */
	protected abstract Optional<String> retrieveBody();
	
	/**
	 * Must return the headers as retrieved in the response.
	 * @return the response headers
	 */
	protected abstract MultiValuedMap<String, String> retrieveHeaders();
	
	/**
	 * Must retrieve the status code of the response.
	 * @return the status code
	 */
	protected abstract int retrieveStatusCode();
	
	/**
	 * Must retrieve the status message of the response.
	 * @return the status message
	 */
	protected abstract String retrieveStatusMessage();
	
	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(Class<T> responseType) {
		return getJson().fromJson(getBody(), responseType);
	}
	
	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(JavaType responseType) {
		return getJson().fromJson(getBody(), responseType);
	}
	
	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(TypeReference<T> responseType) {
		return getJson().fromJson(getBody(), responseType);
	}

	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(com.fasterxml.jackson.databind.JavaType responseType) {
		return getJson2().fromJson(getBody(), responseType);
	}
	
	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(com.fasterxml.jackson.core.type.TypeReference<T> responseType) {
		return getJson2().fromJson(getBody(), responseType);
	}

	/**
	 * Returns the Json object (Jackson 3) for mapping ease of use.
	 * @return the Json object (Jackson 3)
	 */
	public Json getJson() {
		if (json == null) {
			Json.Builder builder = Json.builder();
			JsonMapper   mapper  = getJsonMapper();
			if (mapper != null) builder = builder.with(mapper);
			json = builder.build();
		}
		return json;
	}

	/**
	 * Returns the Json object (Jackson 2) for mapping ease of use.
	 * @return the Json object (Jackson 2)
	 */
	public Json2 getJson2() {
		if (json2 == null) {
			Json2.Builder builder = Json2.builder();
			com.fasterxml.jackson.databind.json.JsonMapper   mapper2  = getJsonMapper2();
			if (mapper2 != null) builder = builder.with(mapper2);
			json2 = builder.build();
		}
		return json2;
	}

	/**
	 * Returns the jsonMapper.
	 * @return the jsonMapper
	 */
	public JsonMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = getTarget().getConfiguration().getMapper();
		}
		return jsonMapper;
	}

	/**
	 * Returns the jsonMapper.
	 * @return the jsonMapper
	 */
	public com.fasterxml.jackson.databind.json.JsonMapper getJsonMapper2() {
		if (jsonMapper2 == null) {
			jsonMapper2 = getTarget().getConfiguration().getMapper2();
		}
		return jsonMapper2;
	}

	/**
	 * Returns the target.
	 * @return the target
	 */
	public Target getTarget() {
		return target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "RestResponse [target=" + target + ", statusCode=" + getStatusCode() + ", statusMessage=" + getStatusMessage() +
				", headers=" + getHeaders() + ", body=" + getBody() + "]";
	}

	
}
