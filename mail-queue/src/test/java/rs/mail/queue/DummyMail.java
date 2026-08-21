/**
 * 
 */
package rs.mail.queue;

import java.time.ZonedDateTime;

/**
 * A dummy object that represents the mail in the tests.
 * 
 * @author ralph
 *
 */
public class DummyMail {

	private String id;
	private ZonedDateTime creationTime;
	private ZonedDateTime queuedTime;
	private ZonedDateTime sentTime;
	private ZonedDateTime failedTime;
	private int errorCount;
	
	public DummyMail(String id) {
		this.id      = id;
		creationTime = ZonedDateTime.now();
		errorCount   = 0;
	}

	/**
	 * Returns the id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * Returns the queuedTime.
	 * @return the queuedTime
	 */
	public ZonedDateTime getQueuedTime() {
		return queuedTime;
	}

	/**
	 * Sets the queuedTime.
	 * @param queuedTime the queuedTime to set
	 */
	public void setQueuedTime(ZonedDateTime queuedTime) {
		this.queuedTime = queuedTime;
	}

	/**
	 * Returns the sentTime.
	 * @return the sentTime
	 */
	public ZonedDateTime getSentTime() {
		return sentTime;
	}

	/**
	 * Sets the sentTime.
	 * @param sentTime the sentTime to set
	 */
	public void setSentTime(ZonedDateTime sentTime) {
		this.sentTime = sentTime;
	}

	/**
	 * Returns the creationTime.
	 * @return the creationTime
	 */
	public ZonedDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * Returns the failedTime.
	 * @return the failedTime
	 */
	public ZonedDateTime getFailedTime() {
		return failedTime;
	}

	/**
	 * Sets the failedTime.
	 * @param failedTime the failedTime to set
	 */
	public void setFailedTime(ZonedDateTime failedTime) {
		this.failedTime = failedTime;
		this.errorCount++;
	}

	/**
	 * Returns the errorCount.
	 * @return the errorCount
	 */
	public int getErrorCount() {
		return errorCount;
	}
	
	
}
