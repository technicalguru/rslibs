package rs.mail.queue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.cowwoc.tokenbucket.Bucket;
import com.github.cowwoc.tokenbucket.ConsumptionResult;

/**
 * Abstract implementation of the queue that leaves the underlying 
 * mailing implemebtation open.
 * 
 * @param <T> the email message object
 * 
 * @author ralph
 *
 */
public class MailQueue<T> {

	/** Default size of the mail queue for non-priority emails */
	public static final int DEFAULT_MAX_SIZE          = 1000;
	/** Default size of the mail queue for priority emails */
	public static final int DEFAULT_MAX_PRIORITY_SIZE = 50;
	/** Default number of retries before giving up when sending fails */
	public static final int DEFAULT_MAX_RETRIES = 3;
	/** Default period of time after a failed message will be tried again */
	public static final long DEFAULT_RETRY_PERIOD = 60000;
	/** The default period of time a queuing operation shall wait before giving up. */
	public static final long DEFAULT_QUEUING_TIMEOUT_SECONDS = 10;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private MailSender<T> mailSender;
	private LinkedBlockingDeque<MessageEntry<T>> queue;
	private LinkedBlockingDeque<MessageEntry<T>> priorityQueue;
	private int maxSize;
	private int maxPrioritySize;
	private Bucket tokenBucket;
	private int maxRetries;
	private long retryPeriod;
	private Set<MailQueueListener> listeners;
	
	/**
	 * Constructor.
	 * <p>The queue will use the standard max capacity./p>
	 * @param mailSender the handler that actually sends the mail
	 * @see #DEFAULT_MAX_SIZE
	 * @see #DEFAULT_MAX_PRIORITY_SIZE
	 */
	public MailQueue(MailSender<T> mailSender) {
		this(mailSender, DEFAULT_MAX_SIZE, DEFAULT_MAX_PRIORITY_SIZE);
	}
	
	/**
	 * Constructor.
	 * <p>The queue will use the standard max capacity for priority messages./p>
	 * @param mailSender the handler that actually sends the mail
	 * @param maxSize - the maximum size of the queue for non-priority messages
	 * @see #DEFAULT_MAX_PRIORITY_SIZE
	 */
	public MailQueue(MailSender<T> mailSender, int maxSize) {
		this(mailSender, maxSize, DEFAULT_MAX_PRIORITY_SIZE);
	}
	
	/**
	 * Constructor.
	 * @param mailSender the handler that actually sends the mail
	 * @param maxSize - the maximum size of the queue for non-priority messages
	 * @param maxPrioritySize - the maximum size of the queue for priority messages
	 */
	public MailQueue(MailSender<T> mailSender, int maxSize, int maxPrioritySize) {
		this.mailSender      = mailSender;
		this.queue           = new LinkedBlockingDeque<>(maxSize);
		this.priorityQueue   = new LinkedBlockingDeque<>(maxPrioritySize);
		this.listeners       = new HashSet<>();
		this.maxRetries      = DEFAULT_MAX_RETRIES;
		this.retryPeriod     = DEFAULT_RETRY_PERIOD;
		this.maxSize         = maxSize;
		this.maxPrioritySize = maxPrioritySize;
	}
	
	/**
	 * Returns the token bucket.
	 * <p>A token bucket control how many emails may be sent in a certain time window.
	 *    There will be no restrictions when the bucket is not set.</p>
	 * @return the token bucket
	 */
	public Bucket getTokenBucket() {
		return tokenBucket;
	}

	/**
	 * Sets the token bucket.
	 * <p>A token bucket control how many emails may be sent in a certain time window.
	 *    There will be no restrictions when the bucket is not set.</p>
	 * @param tokenBucket the token bucket to set
	 */
	public void setTokenBucket(Bucket tokenBucket) {
		this.tokenBucket = tokenBucket;
	}

	/**
	 * Returns the number of retries before giving up when sending fails.
	 * @return the number of retries before giving up when sending fails
	 */
	public int getMaxRetries() {
		return maxRetries;
	}

	/**
	 * Sets the number of retries before giving up when sending fails.
	 * @param maxRetries the number of retries before giving up when sending fails
	 */
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	/**
	 * Returns the period in milliseconds that has to pass before a failed message will be tried again.
	 * @return the retry period in millisecond (Default: 60000)
	 */
	public long getRetryPeriod() {
		return retryPeriod;
	}

	/**
	 * Sets the period in milliseconds that has to pass before a failed message will be tried again.
	 * @param retryPeriod the retry period in milliseconds (Default: 60000)
	 */
	public void setRetryPeriod(long retryPeriod) {
		this.retryPeriod = retryPeriod;
	}

	/**
	 * Add the message to the queue for sending with normal priority.
	 * <p>The message will be tried to be queued immediately without any blocking.</p>
	 * @param message - message to be sent
	 * @param referenceId - reference id for the client
	 * @return {@code true} when the message was queued, {@code false} when it cannot be queued at this time
	 */
	public boolean queue(T message, String referenceId) {
		return queue(message, referenceId, false, 0, 0);
	}
	
	/**
	 * Add the message to the queue for sending with normal priority. 
	 * @param message - message to be sent
	 * @param referenceId - reference id for the client
	 * @param timeoutInSeconds the maximum waiting time to queue before giving up
	 * @return {@code true} when the message was queued, {@code false} when it cannot be queued at this time
	 */
	public boolean queue(T message, String referenceId, long timeoutInSeconds) {
		return queue(message, referenceId, false, 0, timeoutInSeconds);
	}
	
	/**
	 * Add the message to the queue for sending. 
	 * <p>The message will be tried to be queued immediately without any blocking.</p>
	 * @param message - message to be sent
	 * @param referenceId - reference id for the client
	 * @param isPriority - when the mail shall be sent with priority
	 * @return {@code true} when the message was queued, {@code false} when it cannot be queued at this time
	 */
	public boolean queue(T message, String referenceId, boolean isPriority) {
		return queue(message, referenceId, isPriority, 0, 0);
	}

	/**
	 * Add the message to the queue for sending. 
	 * @param message - message to be sent
	 * @param referenceId - reference id for the client
	 * @param isPriority - when the mail shall be sent with priority
	 * @param timeoutInSeconds the maximum waiting time to queue before giving up
	 * @return {@code true} when the message was queued, {@code false} when it cannot be queued at this time
	 */
	public boolean queue(T message, String referenceId, boolean isPriority, long timeoutInSeconds) {
		return queue(message, referenceId, isPriority, 0, timeoutInSeconds);
	}

	/**
	 * Add the message to the queue for sending.
	 * <p>This method is intended to be used when you need you re-populate the queue after
	 *    a system restart. You can pass the additional errorCount parameter to re-establish
	 *    the previous queue status.</p>
	 * <p>The message will be tried to be queued immediately without any blocking.</p>
	 * @param message - message to be sent
	 * @param referenceId - reference id for the client
	 * @param isPriority - when the mail shall be sent with priority
	 * @param previousErrorCount - error count from previous attempts
	 * @return {@code true} when the message was queued, {@code false} when it cannot be queued at this time
	 */
	public boolean queue(T message, String referenceId, boolean isPriority, int previousErrorCount) {
		return queue(message, referenceId, isPriority, previousErrorCount, 0);
	}
	
	/**
	 * Add the message to the queue for sending.
	 * <p>This method is intended to be used when you need you re-populate the queue after
	 *    a system restart. You can pass the additional errorCount parameter to re-establish
	 *    the previous queue status.</p>
	 * @param message - message to be sent
	 * @param referenceId - reference id for the client
	 * @param isPriority - when the mail shall be sent with priority
	 * @param previousErrorCount - error count from previous attempts
	 * @param timeoutInSeconds the maximum waiting time to queue before giving up
	 * @return {@code true} when the message was queued, {@code false} when it cannot be queued at this time
	 */
	public boolean queue(T message, String referenceId, boolean isPriority, int previousErrorCount, long timeoutInSeconds) {
		MessageEntry<T> entry = new MessageEntry<T>(referenceId, message, isPriority);
		entry.failedAttempts = previousErrorCount;
		return queue(entry, isPriority ? priorityQueue : queue, timeoutInSeconds);
	}
	
	/**
	 * Internal queuing implementation - queues the entry in th egiven queue using the given maximum waiting time.
	 * @param message the message to be queued
	 * @param queue the queue to be used
	 * @param timeoutInSeconds the maximum waiting time to queue before giving up
	 * @return {@code true} when the message was queued, {@code false} when it cannot be queued at this time
	 */
	protected boolean queue(MessageEntry<T> message, LinkedBlockingDeque<MessageEntry<T>> queue, long timeoutInSeconds) {
		try {
			boolean rc = false;
			if (timeoutInSeconds > 0) {
				rc = queue.offer(message, timeoutInSeconds, TimeUnit.SECONDS);
			} else {
				rc = queue.offer(message);
			}
			if (rc) mailQueued(message);
			return rc;
		} catch (InterruptedException e) {
			log.error("Queuing interrupted on "+message.referenceId, e);
		}
		return false;
	}
	
	/**
	 * Returns the current size of the queue.
	 * @return the size of the queue
	 */
	public int size() {
		return size(true)+size(false);
	}
	
	/**
	 * Returns the current size of the queue for normal or prioritized messages only.
	 * @param isPriority whether the priority or normal number of messages shall be returned
	 * @return the size of the queue
	 */
	public int size(boolean isPriority) {
		return isPriority ? priorityQueue.size() : queue.size();
	}

	/**
	 * Returns the total capacity of the normal or prioritized queue.
	 * @param isPriority whether the capacity for priority or normal messages shall be returned
	 * @return the total capacity
	 */
	public int capacity(boolean isPriority) {
		return isPriority ? maxPrioritySize : maxSize;
	}
		
	/**
	 * Returns the remaining capacity of the normal or prioritized queue.
	 * @param isPriority whether the capacity for priority or normal messages shall be returned
	 * @return the remaining capacity
	 */
	public int remainingCapacity(boolean isPriority) {
		return isPriority ? priorityQueue.remainingCapacity() : queue.remainingCapacity();
	}
	
	/**
	 * Adds a listener to this queue.
	 * @param listener the listener to add
	 */
	public void addListener(MailQueueListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener from this queue.
	 * @param listener the listener to remove
	 */
	public void removeListener(MailQueueListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Informs listeners that a message was queued.
	 * @param entry the message entry
	 */
	protected void mailQueued(MessageEntry<T> entry) {
		for (MailQueueListener listener : listeners) {
			listener.onQueued(entry.referenceId);
		}
	}
	
	/**
	 * Informs listeners that a message is being sent.
	 * @param entry the message entry
	 */
	protected void mailSending(MessageEntry<T> entry) {
		for (MailQueueListener listener : listeners) {
			listener.onSending(entry.referenceId);
		}
	}
	
	/**
	 * Informs listeners that a message was sent successfully.
	 * @param entry the message entry
	 */
	protected void mailSent(MessageEntry<T> entry) {
		for (MailQueueListener listener : listeners) {
			listener.onSent(entry.referenceId);
		}
	}
	
	/**
	 * Informs listeners that a message failed.
	 * @param entry the message entry
	 * @param reason the reson why sending failed
	 */
	protected void mailFailed(MessageEntry<T> entry, String reason) {
		for (MailQueueListener listener : listeners) {
			listener.onFailed(entry.referenceId, entry.failedAttempts, reason);
		}
	}
	
	/**
	 * Process the queue once.
	 * <p>The method must be called periodically. The method stops when</p>
	 * <ul>
	 * <li>The queue is empty and no more messages to be sent, or</li>
	 * <li>The rate limit has been reached and we need to wait before sending</li>
	 * </ul>
	 * @throws Exception when the processing caused a severe faiulure
	 */
	public void run() throws Exception {
		if (log.isDebugEnabled()) log.debug("I have "+size()+" messages queued");
		MessageEntry<T> candidate = getNext();
		while (candidate != null) {
			if (getBucketToken()) {
				mailSending(candidate);
				try {
					mailSender.sendMessage(candidate.message, candidate.referenceId); 
					mailSent(candidate);
					remove(candidate);
				} catch (Throwable t) {
					candidate.failedAttempts++;
					mailFailed(candidate, t.getMessage());
					log.error("Cannot send message", t);
					if (candidate.failedAttempts > getMaxRetries()) {
						remove(candidate);
					} else {
						candidate.notBeforeTimeInMillis = System.currentTimeMillis()+retryPeriod;
					}
				}
				// Try next
				candidate = getNext();
			} else {
				candidate = null;
			}
		} 
	}
	
	/**
	 * Picks the next message for sending if available.
	 * <p>The method does not yet remove the message from the queue.</p>
	 * @return a message cleared to be sent
	 */
	protected MessageEntry<T> getNext() {
		MessageEntry<T> rc = getNext(true);
		if (rc == null) rc = getNext(false);
		return rc;
	}
	
	/**
	 * Picks the next message from the given queue.
	 * <p>The method does not yet remove the message from the queue.</p>
	 * @param isPriority the queue to check (priority or normal)
	 * @return a message cleared to be sent
	 */
	protected MessageEntry<T> getNext(boolean isPriority) {
		long now = System.currentTimeMillis();
		BlockingQueue<MessageEntry<T>> queue = isPriority ? this.priorityQueue : this.queue;
		Object list[] = queue.toArray();
		for (Object o : list) {
			@SuppressWarnings("unchecked")
			MessageEntry<T> entry = (MessageEntry<T>)o;
			if (entry.notBeforeTimeInMillis < now) return entry;
		}
		return null;
	}
	
	/**
	 * Removes the message from the queue
	 * @param entry the message to be removed
	 */
	protected void remove(MessageEntry<T> entry) {
		if (entry.isPriority) priorityQueue.remove(entry);
		else queue.remove(entry);
	}
	
	/**
	 * Tries to consume a token from a bucket.
	 * <p>The method will ask the token bucket if available and consume one
	 *    token if possible. If no token is available, the method will 
	 *    not block and return {@code false}.</p>
	 * <p>Always returns {@code true} when no token bucket was set.</p>
	 * @return {@code true} when a token was consumed, {@code false} otherwise.
	 */
	protected boolean getBucketToken() {
		if (tokenBucket != null) {
			ConsumptionResult result = tokenBucket.tryConsume();
			return result.getTokensConsumed() > 0;
		}
		return true;
	}
	
	/**
	 * Helper class to store messages in the queue.
	 * 
	 * @author ralph
	 *
	 */
	protected static class MessageEntry<X> {
		/** Reference ID of the message */
		protected String referenceId;
		/** The message itself */
		protected X message;
		/** A timestamp to hold the message */
		protected long notBeforeTimeInMillis;
		/** Number of failed attempts */
		protected int failedAttempts;
		/** Whether it is priority */
		protected boolean isPriority;
		/**
		 * Constructor.
		 * @param referenceId reference ID of th emessage
		 * @param message the message
		 * @param isPriority whether it is prioritized
		 */
		public MessageEntry(String referenceId, X message, boolean isPriority) {
			this.referenceId           = referenceId;
			this.message               = message;
			this.isPriority            = isPriority;
			this.notBeforeTimeInMillis = 0;
			this.failedAttempts        = 0;
		}
	}

}
