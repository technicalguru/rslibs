package rs.restclient.example.reqres;

import org.junit.jupiter.api.Test;

import rs.restclient.core.api.RestClientConfiguration;
import rs.restclient.core.api.Target;
import rs.restclient.springboot.SpringBootImpl;

/**
 * The bootstrap demonstrated
 * 
 * @author ralph
 *
 */
public class ReqResClientTest {

	@Test
	public void test() {
		RestClientConfiguration configuration = new RestClientConfiguration();
		configuration.setUri("https://reqres.in/api");
		configuration.setVerbose(false);
		Target.Builder targetBuilder = Target.builder()
				.with(SpringBootImpl.BUILDER)
				.with(configuration);
		ReqResClient client = new ReqResClient(targetBuilder);
		System.out.println(client.users().get(2));
	}
}
