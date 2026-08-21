package rs.mail.queue;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

/**
 * Sends messages for SimpleJavaMail {@link Email} objects.
 *    
 * @author ralph
 *
 */
public class SimpleJavaMailSender implements MailSender<Email> {

	private Mailer mailer;
	
	/**
	 * Constructor.
	 */
	public SimpleJavaMailSender() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * @param mailer - the mailer object from SimpleJavaMail
	 */
	public SimpleJavaMailSender(Mailer mailer) {
		this.mailer        = mailer;
	}
	
	/**
	 * Sets the mailer.
	 * @param mailer the mailer to set
	 */
	public void setMailer(Mailer mailer) {
		this.mailer = mailer;
	}


	/**
	 * Returns the mailer.
	 * @return the mailer
	 */
	public Mailer getMailer() {
		return mailer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(Email message, String referenceId) throws Exception {
		Mailer mailer = getMailer();
		if (mailer != null) mailer.sendMail(message);
	}

	
}
