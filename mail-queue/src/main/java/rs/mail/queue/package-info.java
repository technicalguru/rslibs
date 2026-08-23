/**
 * Implements the mailing queue.
 * 
 * <h2>Getting Started</h2>
 *
 * <p>The projects provides two sending implementations:</p> 
 * <ul>
 * <li>traditional <a href="https://javaee.github.io/javamail/">JavaMail</a> framework ({@code javax.mail.Mail})</li>
 * <li>flexible <a href="https://www.simplejavamail.org/">Simple Java Mail</a> framework</li>
 * </ul>
 * <p>You can setup your mail queue quite easily:</p>
 * 
 * <pre>
 * // The traditional JavaMail way
 * MailQueue&lt;Mail&gt; queue1 = new MailQueue&lt;&gt;(new MessageMailSender());
 * 
 * // The SimpleJavaMail way
 * MailQueue&lt;Email&gt; queue2 = new MailQueue&lt;&gt;(new SimpleJavaMailSender());
 * 
 * </pre>
 * 
 * <p>Now you can start to queue mail objects. You pass the message and the reference ID
 * which will being used to notify you about status changes of this message.</p>
 * 
 * <pre>
 * boolean success = queue.queue(message, referenceId);
 * </pre>
 * 
 * <p>The method returns whether the message was queued successfully.</p>
 * 
 * <h2>Queue Capacity and Size</h2>
 * 
 * <p>As the queue has a certain capacity, the {@code queue()} method will return whether
 * it was able to queue the given message.</p>
 * 
 * <p>You can add some waiting time (in seconds) that the method shall wait before giving up:</p>
 * 
 * <pre>
 * boolean success = queue.queue(message, referenceId, 10);
 * </pre>
 * 
 * <p>The queue can give you a status of its current size (not capacity!):</p>
 * 
 * <pre>
 * int allMessages      = queue.size();
 * int normalMessages   = queue.size(false);
 * int priorityMessages = queue.size(true);
 * </pre>
 * 
 * <p>Please notice that you can control the capacity only when creating the queue. Once set
 * it cannot be modified anymore.</p>
 * 
 * <h2>Throttling</h2>
 * 
 * <p>The sending process can be throttled by using a token bucket. The {@code MailQueue} can be
 * configured with an implementation of {@code Bucket} from the <a href="https://github.com/cowwoc/token-bucket">Token Bucket</a>
 * project:</p>
 * 
 * <pre>
 * Bucket bucket = ...
 * queue.setTokenBucket(bucket);
 * </pre>
 * 
 * <p>Refer to the <a href="https://github.com/cowwoc/token-bucket">Token Bucket</a> documentation for details about
 * how to create such a bucket.</p>
 * 
 * <h2>Sending the Messages</h2>
 * 
 * <p>The {@code MailQueue} implementation makes no assumption about how you want to organize the
 * sending process. The {@code run()} method will process the queue until it is empty or
 * no token is available from the token bucket and return. It is up to the caller to re-call
 * the method again to start processing the queue again.</p>
 * 
 * <p>That said it is made clear that {@code run()} will never block when there is nothing to do at
 * the given moment. It will simply return.</p>
 * 
 * <p>You can start a separate thread in your application and run the method periodically.</p>
 * 
 * <h2>Failed Message Sending</h2>
 * 
 * <p>Messages that cannot be sent due to some failure will be queued for a limited number of retries
 * before giving up. You can fine-tune this process:</p>
 * 
 * <pre>
 * queue.setMaxRetries(5);
 * queue.setRetryPeriod(3600000);
 * </pre>
 * 
 * <p>Default behaviour is to retry every 60 seconds and 3 times.</p>
 * 
 * <h2>Listening to the MailQueue</h2>
 * 
 * <p>You can listen to any status change of messages by registering a {@code MailQueueListener}:</p>
 * 
 * <pre>
 * queue.addListener(new MailQueueAdapter() {
 * 	public void onSent(String referenceId) {
 * 		// Do something
 * 	}
 * );
 * </pre>
 * 
 * <p>The reference ID is the ID of the message that was affected.</p>
 * 
 * <p>{@code MailQueue} knows 4 states of a message:</p>
 * <ul>
 * <li>QUEUED - the message was accepted to the queue</li>
 * <li>SENDING - the message was picked for sending and is currently being sent</li>
 * <li>SENT - the message has been sent successfully</li>
 * <li>FAILED - The message could not be sent.</li>
 * </ul>
 * 
 * <h2>Using your own Mailing Framework</h2>
 * 
 * <p>If you want to use your own mailing framework, you can write an implementation of the {@code MailSender}
 * interface and pass an instance of it to the {@code MailQueue} constructor.</P>
 * 
 */
package rs.mail.queue;

