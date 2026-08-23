package rs.mail.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the {@link MailQueueListener} interface.
 * <p>This implementation only writes the information into the log (debug, info and error).
 * 
 * @author ralph
 *
 */
public class MailQueueAdapter implements MailQueueListener {

	private Logger log;
	
	/**
	 * Constructor.
	 */
	public MailQueueAdapter() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param log - external logger to be used
	 */
	public MailQueueAdapter(Logger log) {
		this.log = log != null ? log : LoggerFactory.getLogger(getClass());
	}

	/**
	 * Returns the log.
	 * @return the log
	 */
	public Logger getLog() {
		return log;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onQueued(String referenceId) {
		if (log.isDebugEnabled()) log.debug("Message queued: "+referenceId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSending(String referenceId) {
		if (log.isDebugEnabled()) log.debug("Message sending: "+referenceId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSent(String referenceId) {
		if (log.isInfoEnabled()) log.info("Message sent: "+referenceId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onFailed(String referenceId, int failedAttempts, String reason) {
		log.error("Message cannot be sent ("+failedAttempts+"): "+referenceId);
		log.error("Reason: "+reason);
	}

	
}
