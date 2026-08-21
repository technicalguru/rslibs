/**
 * 
 */
package rs.mail.templates.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

/**
 * Provide easy methods to use messages.
 * 
 * @author ralph
 *
 */
public class MailUtils {

	/**
	 * Adds a {@link Multipart} to the message.
	 * @param message the message object
	 * @return the {@link Multipart} object
	 * @throws MessagingException when the {@link Multipart} cannot be created
	 */
	public static Multipart addMultipart(Message message)  throws MessagingException {
		Multipart rc = new MimeMultipart();
		message.setContent(rc);
		return rc;
	}
	
	/**
	 * Adds MIME part to the {@link Multipart} of the message.
	 * @param multipart multipart object
	 * @param content content of part
	 * @param contentType type of content
	 * @return the {@link MimeBodyPart} that was added
	 * @throws MessagingException when adding the part failed
	 */
	public static MimeBodyPart addMimePart(Multipart multipart, String content, String contentType) throws MessagingException {
		MimeBodyPart mimePart = new MimeBodyPart();
		mimePart.setContent(content, contentType);
		multipart.addBodyPart(mimePart);
		return mimePart;
	}
	
	// TODO Attach files and images
	// https://cloud.google.com/appengine/docs/standard/java/mail/mail-with-headers-attachments?hl=de#mail_with_attachments
}
