package rs.restclient.core.util;

import java.io.IOException;

import rs.restclient.core.api.TargetImplementation;
import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * @author ralph
 *
 */
public class EndofChainRequestExecution implements RestRequestExecution {

	private TargetImplementation implementation;
	
	public EndofChainRequestExecution(TargetImplementation implementation) {
		this.implementation = implementation;
	}
	
	@Override
	public RestResponse execute(RestRequest request) throws IOException {
		//RestRequest delegate = this.requestFactory.createRequest(request.getURI(), request.getMethod());
		
		//request.getHeaders().forEach((key, value) -> delegate.getHeaders().addAll(key, value));
		//request.getAttributes().forEach((key, value) -> delegate.getAttributes().put(key, value));
		
		return executeWithRequestAndBody(request);
	}

	final RestResponse executeWithRequestAndBody(RestRequest request) throws IOException {

//		if (bufferedOutput.length > 0) {
//			long contentLength = request.getHeaders().getContentLength();
//			if (contentLength > -1 && contentLength != bufferedOutput.length) {
//				request.getHeaders().setContentLength(bufferedOutput.length);
//			}
//			if (request instanceof StreamingHttpOutputMessage streamingOutputMessage) {
//				streamingOutputMessage.setBody(bufferedOutput);
//			}
//			else {
//				StreamUtils.copy(bufferedOutput, request.getBody());
//			}
//		}

		return implementation.execute(request);
	}

}
