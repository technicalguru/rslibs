/**
 * 
 */
package rs.mail.queue;

import java.time.ZonedDateTime;

/**
 * A dummy implementation for {@link MailSender} in tests.
 * 
 * @author ralph
 *
 */
public class DummyMailSender implements MailSender<DummyMail>{

	private boolean failSending;
	
	public DummyMailSender(boolean failSending) {
		this.failSending = failSending;
	}
	@Override
	public void sendMessage(DummyMail message, String referenceId) throws Exception {
		if (failSending) {
			message.setFailedTime(ZonedDateTime.now());
			throw new Exception("Sending Message Failure Test - ignore in tests!");
		} else {
			message.setSentTime(ZonedDateTime.now());
		}
	}

	
}
