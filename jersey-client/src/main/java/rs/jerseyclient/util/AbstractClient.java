/**
 * 
 */
package rs.jerseyclient.util;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.CommonUtils;
import rs.jerseyclient.JerseyClientException;
import rs.jerseyclient.data.HateOasPagedList;
import rs.jerseyclient.data.HateOasPagedList.EmbeddedResultList;
import rs.jerseyclient.data.ResultList;

/**
 * The abstract implementation of all REST clients.
 * <p>The class can already handle paging (with {@code page} and {@code pageSize} query parameters)
 * as well as sorting (with {@code sort} parameter}.
 * 
 * @author ralph
 *
 */
public abstract class AbstractClient {

	private Logger log;

	private ClientHolder clients = null;
	
	/**
	 * Constructor.
	 * <p>Descendants must call {@link #setTarget(WebTarget)} to initialize class correctly.</p>
	 */
	protected AbstractClient() {
		log = LoggerFactory.getLogger(getClass());
	}

	/**
	 * Constructor.
	 * @param target - the target to request.
	 */
	protected AbstractClient(WebTarget target) {
		setTarget(target);
		log = LoggerFactory.getLogger(getClass());
	}

	/**
	 * Sets a target for this client.
	 * @param target the target
	 */
	protected void setTarget(WebTarget target) {
		if (clients == null) {
			clients = new ClientHolder(target);
		} else {
			throw new JerseyClientException("Cannot change WebTarget.");
		}
	}
	/**
	 * Returns a request builder.
	 * @return the builder
	 */
	public Builder getRequest() {
		Builder rc = getTarget().request();
		return rc;
	}

	/**
	 * Returns the target.
	 * @return the target
	 */
	protected WebTarget getTarget() {
		return clients.getTarget();
	}
	
	/**
	 * Returns the target with paging parameters applied.
	 * @param page     - page index (0-based) 
	 * @param pageSize - page size
	 * @return the target
	 */
	protected WebTarget getTarget(Integer page, Integer pageSize) {
		return applyPaging(clients.getTarget(), page, pageSize);
	}
	
	/**
	 * Returns the target with sort parameter applied.
	 * @param sort - the sort parameter
	 * @return the target
	 */
	protected WebTarget getTarget(String sort) {
		return applySort(clients.getTarget(), sort);
	}
	
	/**
	 * Returns the target with paging and sort parameters applied.
	 * @param sort     - the sort parameter
	 * @param page     - page index (0-based) 
	 * @param pageSize - page size
	 * @return the target
	 */
	protected WebTarget getTarget(String sort, Integer page, Integer pageSize) {
		return applySort(applyPaging(clients.getTarget(), page, pageSize), sort);
	}
	
	/**
	 * Applies paging parameters to the target.
	 * @param target   - the base target
	 * @param page     - page index (0-based) 
	 * @param pageSize - page size
	 * @return the new target
	 */
	protected WebTarget applyPaging(WebTarget target, Integer page, Integer pageSize) {
		if (page     != null) target = target.queryParam("page",     page);
		if (pageSize != null) target = target.queryParam("pageSize", pageSize);
		return target;
	}

	/**
	 * Applies sort parameter to the target.
	 * @param target - the base target
	 * @param sort   - the sort parameter
	 * @return the new target
	 */
	protected WebTarget applySort(WebTarget target, String sort) {
		if (sort != null) target = target.queryParam("sort", sort);
		return target;
	}
	
	/**
	 * Returns the subclient of the given type.
	 * <p>Be aware that subclient use relative REST API paths.</p>
	 * @param <T>   - Class of subclient
	 * @param clazz - class of subclient
	 * @return new or existing instance of subclient
	 */
	protected <T extends AbstractClient> T get(Class<T> clazz) {
		return clients.get(clazz);
	}
	
	/**
	 * Retrieves the results from the REST response object.
	 * @param <T> - class of result type
	 * @param pagedList - the response object
	 * @return the list of objects (or empty list)
	 */
	protected <T> ResultList<T> getResults(HateOasPagedList<T> pagedList) {
		if (pagedList != null) {
			EmbeddedResultList<T> embedded = pagedList.get_embedded();
			if (embedded != null) {
				return new ResultList<>(embedded.getResults(), pagedList.getPage());
			}
		}
		return new ResultList<>(CommonUtils.newList(), pagedList.getPage());
	}
	
	
	/**
	 * Helper method to raise exceptions in case of any other response than 2xx successful.
	 * @param response the {@link Response} object
	 */
	protected void checkResponse(Response response) {
		checkResponse(response, null);
	}
	
	/**
	 * Helper method to raise exceptions in case of any other response than 2xx successful.
	 * @param <T> - the type of successful return
	 * @param response the {@link Response} object
	 * @param successValue the value to return when response was successfull
	 * @return usually the successValue - everything else will raise a runtime exception
	 */
	protected <T> T checkResponse(Response response, T successValue) {
		switch (response.getStatusInfo().getFamily()) {
		case INFORMATIONAL:
		case SUCCESSFUL: return successValue;
		case REDIRECTION: throw new WebApplicationException(response);
		case CLIENT_ERROR: throw new ClientErrorException(response);
		case SERVER_ERROR: throw new ServerErrorException(response);
		case OTHER: throw new WebApplicationException(response);
		}
		return successValue;
	}

	/**
	 * Returns the logger for this client.
	 * @return the logger
	 */
	public Logger getLog() {
		return log;
	}
	
}
