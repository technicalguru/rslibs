/**
 * 
 */
package rs.restclient.core.api.response;

import rs.jackson.Json;
import rs.restclient.core.api.RestClientConfiguration;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;

/**
 * A wrapper for the response.
 */
public class RestResponse {

	protected RestClientConfiguration configuration;
	private   JsonMapper              jsonMapper;
	private   Json                    json;
	
	protected RestResponse(RestClientConfiguration configuration) {
		this(configuration, null);
	}
		
	/**
	 * Constructs response using the configuration and optional jsonMapper.
	 * @param configuration configuration
	 * @param jsonMapper json mapper object (can be null)
	 */
	protected RestResponse(RestClientConfiguration configuration, JsonMapper jsonMapper) {
		this.configuration = configuration;
		this.jsonMapper    = jsonMapper;
		this.json          = null;
	}

	public String getBody() {
		return null;
	}
	
	public <T> T as(Class<T> responseType) {
		return getJson().fromJson(getBody(), responseType);
	}
	public <T> T as(JavaType responseType) {
		return getJson().fromJson(getBody(), responseType);
	}
	public <T> T as(TypeReference<T> responseType) {
		return getJson().fromJson(getBody(), responseType);
	}

//    public <T> T convertBody(RestResponse response, Class<T> responseType) {
//		try (InputStream body = response.getBody()) {
//			return getJson().fromJson(body, responseType);
//		} catch (IOException e) {
//			throw new HttpConnectionException("Cannot read response body", e);
//		}
//	}
//
//    public <T> T convertBody(RestResponse response, JavaType responseType) {
//		try (InputStream body = response.getBody()) {
//			return getJson().fromJson(body, responseType);
//		} catch (IOException e) {
//			throw new HttpConnectionException("Cannot read response body", e);
//		}
//	}
//
//    public <T> T convertBody(ClientHttpResponse response, TypeReference<T> responseType) {
//		try (InputStream body = response.getBody()) {
//			return getJson().fromJson(body, responseType);
//		} catch (IOException e) {
//			throw new HttpConnectionException("Cannot read response body", e);
//		}
//	}
//
	/**
	 * Returns teh Json object for mapping ease of use.
	 * @return the Json object
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
	 * Returns the jsonMapper.
	 * @return the jsonMapper
	 */
	public JsonMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = configuration.getMapper();
		}
		return jsonMapper;
	}

	
}
