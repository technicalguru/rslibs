package rs.mail.templates.impl;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import rs.mail.templates.BuilderResult;
import rs.mail.templates.ContentType;
import rs.mail.templates.util.MailUtils;

/**
 * Creates a {@link javax.mail.Message} from the {@link BuilderResult}
 * 
 * @author ralph
 *
 */
public class JavaxMailMessageCreator implements MessageCreator<Message> {

	private Session session;
	
	/**
	 * Constructor.
	 * @param mailProperties - the mail properties
	 */
	public JavaxMailMessageCreator(Properties mailProperties) {
		this(mailProperties, null);
	}
	
	/**
	 * Constructor.
	 * @param mailProperties - the mail properties
	 * @param authenticator -  Authenticator object. Used only if a new Session object is created. Otherwise, it must match the Authenticator used to create the Session.
	 */
	public JavaxMailMessageCreator(Properties mailProperties, Authenticator authenticator) {
		this(Session.getInstance(mailProperties, authenticator));
	}
	
	/**
	 * Constructor.
	 * @param session - the mail session
	 */
	public JavaxMailMessageCreator(Session session) {
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
	public Message create(BuilderResult result) throws Exception {
		Message rc = new MimeMessage(getSession());
		if (result.getSubject() != null) rc.setSubject(result.getSubject());

		Multipart mp = MailUtils.addMultipart(rc);
		if (result.getContent(ContentType.HTML) != null) MailUtils.addMimePart(mp, result.getContent(ContentType.HTML), "text/html");
		if (result.getContent(ContentType.TEXT) != null) MailUtils.addMimePart(mp, result.getContent(ContentType.TEXT), "text/plain");
		
		return rc;
	}

	
}
