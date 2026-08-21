/**
 * 
 */
package rs.mail.queue;

/**
 * @author ralph
 *
 */
public class DummyMailQueueListener implements MailQueueListener {

	public String        queuedReferenceId;
	public String        sendingReferenceId;
	public String        sentReferenceId;
	public String        failedReferenceId;
	
	@Override
	public void onQueued(String referenceId) {
		queuedReferenceId = referenceId;
	}

	@Override
	public void onSending(String referenceId) {
		sendingReferenceId = referenceId;
	}

	@Override
	public void onSent(String referenceId) {
		sentReferenceId = referenceId;
	}

	@Override
	public void onFailed(String referenceId, int failedCount, String reason) {
		failedReferenceId = referenceId;
	}
	
}
