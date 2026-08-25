package rs.restclient.springboot;

import rs.restclient.core.api.Target;
import rs.restclient.core.api.TargetImplementation;

/**
 * The implementation of SpringBoot backend.
 * @author ralph
 *
 */
public class SpringBootImpl implements TargetImplementation {

	/** The instance for usage */
	public static final SpringBootImpl BUILDER = new SpringBootImpl();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpringBootRequestBuilder requestBuilder(Target target) {
		return new SpringBootRequestBuilder(target);
	}

}
