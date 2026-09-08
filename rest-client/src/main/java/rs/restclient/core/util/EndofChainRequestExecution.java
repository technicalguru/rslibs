package rs.restclient.core.util;

import java.io.IOException;

import rs.restclient.core.api.TargetImplementation;
import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * The last execution object in an interceptor chain.
 * <p>It mainly calls the configured implementation to execute the request.
 * @author ralph
 *
 */
public class EndofChainRequestExecution implements RestRequestExecution {

	private TargetImplementation implementation;
	
	/**
	 * The constructor.
	 * @param implementation HTTP execution implementation
	 */
	public EndofChainRequestExecution(TargetImplementation implementation) {
		this.implementation = implementation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestResponse execute(RestRequest request) throws IOException {
		return executeWithRequestAndBody(request);
	}

	/**
	 * Execute the request through the implementation object.
	 * @param request request to be executed (it is final here)
	 * @return the response as returned by the implementation
	 * @throws IOException when the request was not successful
	 */
	final RestResponse executeWithRequestAndBody(RestRequest request) throws IOException {
		return implementation.execute(request);
	}

}
