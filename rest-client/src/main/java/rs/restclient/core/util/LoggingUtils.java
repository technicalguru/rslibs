package rs.restclient.core.util;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;

/**
 * Used for logging purposes.
 * @author ralph
 *
 */
public class LoggingUtils {

	/**
	 * Header names that contain sensitive data.
	 */
    public static List<String> SENSITIVE_HEADERS = RestClientUtils.newList("authorization", "proxy-authorization", "api-key", 
    		"x-api-key", "x-auth-token", "x-session-token");
    
    /**
     * Whether the header contains sensitive data.
     * @param header name of header
     * @return true when header value is sensitive.
     */
    public static boolean isSensitiveHeader(String header) {
    	return SENSITIVE_HEADERS.contains(header.toLowerCase());
    }
    
    /**
     * Masks a sensitive header value
     * @param value the value to be masked
     * @return the masked value (usually first 3 characters are readable to allow debugging)
     */
    public static String maskSensitiveHeaderValue(String value) {
    	if (value.toLowerCase().startsWith("bearer ")) value = "Bearer "+value.substring(7, 10)+"******";
    	if (value.toLowerCase().startsWith("basic ")) value = "Basic "+value.substring(6, 9)+"******";
    	if (value.toLowerCase().startsWith("digest ")) value = "Digest "+value.substring(7, 10)+"******";
    	if (value.toLowerCase().startsWith("negotiate ")) value = "Negotiate "+value.substring(9, 12)+"******";
    	if (value.toLowerCase().startsWith("aws4-hmac-sha256 ")) value = "AWS4-HMAC-SHA256 "+value.substring(17, 20)+"******";
    	return value.substring(0, 3)+"******";
    }

    /**
     * Logs the headers and masks sensitive headers.
     * @param log the logging object
     * @param headers the headers
     */
    public static void logHeaders(Logger log, MultiValuedMap<String, ? extends Object> headers) {
    	for (String header : headers.keySet()) {
			if (!":status".equals(header)) {
	    		for (Object value : headers.get(header)) {
	    			if (value != null) {
		    			if (LoggingUtils.isSensitiveHeader(header)) value = LoggingUtils.maskSensitiveHeaderValue(value.toString());
		    			log.info("   {}: {}", header, value.toString());
	    			}
	    		}
			}
    	}
    }
    

}
