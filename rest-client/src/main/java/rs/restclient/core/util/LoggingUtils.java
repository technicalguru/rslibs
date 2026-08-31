package rs.restclient.core.util;

import java.util.List;
import java.util.Optional;
import java.util.logging.LogRecord;

import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.CommonUtils;

/**
 * Used for logging purposes.
 * @author ralph
 *
 */
public class LoggingUtils {

	public static Logger LOG = LoggerFactory.getLogger(LoggingUtils.class);

	/**
	 * Header names that contain sensitive data.
	 */
    public static List<String> SENSITIVE_HEADERS = CommonUtils.newList("authorization", "proxy-authorization", "api-key", 
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

    public static Optional<Integer> findSensitiveHeaderKey(String s) {
    	s = s.toLowerCase();
    	for (String header : SENSITIVE_HEADERS) {
    		int pos = s.indexOf(header+":");
    		if (pos > 0) {
    			return Optional.of(pos+header.length()+1);
    		}
    	}
    	return Optional.empty();
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
		    			if (isSensitiveHeader(header)) value = maskSensitiveHeaderValue(value.toString());
		    			log.info("   {}: {}", header, value.toString());
	    			}
	    		}
			}
    	}
    }
    
    public static java.util.logging.Logger JUL = new JulLogger();
    
    public static class JulLogger extends java.util.logging.Logger {

    	private Logger log;
    	
		/**
		 * Constructor.
		 */
		public JulLogger() {
			this(LOG);
		}
		
		/**
		 * Constructor with specific slf4j logger.
		 * @param log
		 */
		public JulLogger(Logger log) {
			super("rs.restclient.core.util.JulLogger", null);
			this.log= log;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void log(LogRecord record) {
			String messages[] = record.getMessage().split("\\n");
			for (String s : messages) {
				Optional<Integer> position = findSensitiveHeaderKey(s);
				if (position.isPresent()) {
					int p = position.get();
					String start = s.substring(0, p);
					String value = maskSensitiveHeaderValue(s.substring(p+1).trim());
	    			log.info("{}: {}", start, value.toString());
				} else {
					log.info(s);
				}
			}
			if (messages.length > 1) log.info("");
		}
    	
    	
    }
}
