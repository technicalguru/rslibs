/**
 * 
 */
package rs.mail.queue;

/**
 * A helper interface that actually will send e-mails.
 * 
 * @param <T> the type of email object this sender handles.
 * 
 * @author ralph
 *
 */
public interface MailSender<T> {

	/**
	 * Sends the message object.
	 * @param message - the message object
	 * @param referenceId - a reference ID for the message
	 * @throws Exception - when sending fails
	 */
	public void sendMessage(T message, String referenceId) throws Exception;
	
}
