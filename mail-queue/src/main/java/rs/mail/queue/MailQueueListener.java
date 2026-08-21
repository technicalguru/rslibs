package rs.mail.queue;

/**
 * A listener that gets informed about events in a mail queue.
 * 
 * @author ralph
 *
 */
public interface MailQueueListener {

	/**
	 * Informs that a message was added to the queue.
	 * @param referenceId - the reference ID of the message
	 */
	public void onQueued(String referenceId);
	
	/**
	 * Informs that the message is now being sent.
	 * @param referenceId - the reference ID of the message
	 */
	public void onSending(String referenceId);
	
	/**
	 * Informs that a message was sent successfully.
	 * @param referenceId - the reference ID of the message
	 */
	public void onSent(String referenceId);
	
	/**
	 * Informs that a message could not be sent.
	 * @param referenceId - the reference ID of the message
	 * @param reason - reason string why it failed
	 * @param failedCount - the number of failed attempts
	 */
	public void onFailed(String referenceId, int failedCount, String reason);
	
}
