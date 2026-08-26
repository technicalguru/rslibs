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

    public static List<String> SENSITIVE_HEADERS = RestClientUtils.newList("authorization", "proxy-authorization", "api-key", 
    		"x-api-key", "x-auth-token", "x-session-token");
    
    public static boolean isSensitiveHeader(String header) {
    	return SENSITIVE_HEADERS.contains(header.toLowerCase());
    }
    
    public static String maskSensitiveHeaderValue(String value) {
    	if (value.toLowerCase().startsWith("bearer ")) value = "Bearer ***masked***";
    	if (value.toLowerCase().startsWith("basic ")) value = "Basic ***masked***";
    	if (value.toLowerCase().startsWith("digest ")) value = "Digest ***masked***";
    	if (value.toLowerCase().startsWith("negotiate ")) value = "Negotiate ***masked***";
    	if (value.toLowerCase().startsWith("aws4-hmac-sha256 ")) value = "AWS4-HMAC-SHA256 ***masked***";
    	return "***masked***";
    }

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
