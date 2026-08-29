package rs.restclient.reqres;

import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.restclient.core.api.RestClient;
import rs.restclient.core.api.RestClientConfiguration;
import rs.restclient.core.api.Target;
import rs.restclient.core.api.auth.AuthorizationInterceptor.AuthorizationType;
import rs.restclient.core.api.auth.FixedHeaderAuthorizationStrategy;
import rs.restclient.core.util.UserAgentInterceptor;
import rs.restclient.springboot.SpringBootImpl;

/**
 * The bootstrap demonstrated
 * 
 * @author ralph
 *
 */
public class SpringBootTest {

	private static Logger log = LoggerFactory.getLogger(SpringBootTest.class);
	
	private static ReqResClient client;
	private static String       apiKey;
	
	@BeforeAll
	public static void beforeAll() {
		ClassLoader classLoader = SpringBootTest.class.getClassLoader();
		if (classLoader != null) try {
			URL url = classLoader.getResource("reqres-key.txt");
			apiKey = IOUtils.toString(url, Charset.defaultCharset()).trim();
			log.info("API key available.");
		} catch (Exception e) {
			log.info("API key not available. No tests will be executed.");
		} else {
			log.info("ClassLoader not found. No tests will be executed.");
		}
		
		if (apiKey != null) {
			RestClientConfiguration configuration = RestClientConfiguration.builder()
				.with("https://reqres.in/api")
				.verbose(true)
				.build();
			Target.Builder targetBuilder = Target.builder()
					.with(SpringBootImpl.SPRING_BOOT)
					.with(configuration)
					.register(new UserAgentInterceptor("ReqResClient/0.1beta"));
			client = RestClient.builder(ReqResClient.class)
					.with(targetBuilder)
					.withAuthorization(r -> new FixedHeaderAuthorizationStrategy(r, AuthorizationType.X_API_KEY, apiKey))
					.build();
		}
	}
	
	@Test
	public void testGet() {
		if (client != null) {
			log.info(client.users().get(2).toString());
		}
	}
	
	@Test
	public void testCreate() {
		if (client != null) {
			log.info(client.users().create(new User(0, "someone.else@reqres.in", "Jane", "Doe", null)).toString());
		}
	}
	
	
}
