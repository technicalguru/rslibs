package rs.restclient.example.reqres;

import rs.restclient.core.api.RestClientConfiguration;
import rs.restclient.core.api.Target;
import rs.restclient.core.util.UserAgentInterceptor;
import rs.restclient.springboot.SpringBootImpl;

/**
 * The bootstrap demonstrated
 * 
 * @author ralph
 *
 */
public class ReqResClientTest {

	private static ReqResClient client;
	
	//@BeforeAll
	public static void beforeAll() {
		RestClientConfiguration configuration = new RestClientConfiguration();
		configuration.setUri("https://reqres.in/api");
		configuration.setVerbose(true);
		Target.Builder targetBuilder = Target.builder()
				.with(SpringBootImpl.BUILDER)
				.with(configuration)
				.register(new UserAgentInterceptor("ReqResClient/0.1beta"));
		client = new ReqResClient(targetBuilder);
	}
	
	//@Test
	public void testGet() {
		System.out.println(client.users().get(2));
	}
	
	
	//@Test
	public void testCreate() {
		System.out.println(client.users().create(new User(0, "someone.else@reqres.in", "Jane", "Doe", null)));
	}
	
	
}
