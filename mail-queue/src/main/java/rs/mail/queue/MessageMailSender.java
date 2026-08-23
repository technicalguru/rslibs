package rs.mail.queue;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;

/**
 * Sends messages for {@link javax.mail.Message} objects.
 *    
 * @author ralph
 *
 */
public class MessageMailSender implements MailSender<Message> {

	private Session session;
	
	/**
	 * Constructor from system properties.
	 */
	public MessageMailSender() {
		this(System.getProperties(), null);
	}
	
	/**
	 * Constructor.
	 * @param mailProperties - the mail properties
	 */
	public MessageMailSender(Properties mailProperties) {
		this(mailProperties, null);
	}
	
	/**
	 * Constructor.
	 * @param mailProperties - the mail properties
	 * @param authenticator -  Authenticator object. Used only if a new Session object is created. 
	 *                         Otherwise, it must match the Authenticator used to create the Session.
	 */
	public MessageMailSender(Properties mailProperties, Authenticator authenticator) {
		this(Session.getInstance(mailProperties, authenticator));
	}

	/**
	 * Constructor.
	 * @param session - the mail session
	 */
	public MessageMailSender(Session session) {
		this.session = session;
	}
	
	/**
	 * Returns the mail session.
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(Message message, String referenceId) throws Exception {
		Transport.send(message);
	}				  
}
