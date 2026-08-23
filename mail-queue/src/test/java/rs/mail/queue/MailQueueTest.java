/**
 * 
 */
package rs.mail.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.github.cowwoc.tokenbucket.Bucket;

import rs.mail.queue.MailQueue.MessageEntry;

/**
 * Tests the {@link MailQueue}.
 * 
 * @author ralph
 *
 */
public class MailQueueTest {

	// Test message was added to normal queue
	@Test
	public void testQueue_withNormal() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		assertEquals(0, queue.size(false));
		boolean success = queue.queue(new DummyMail("Message1"), "Message1");
		assertTrue(success);
		assertEquals(1, queue.size(false));
	}
	
	// Test message was added to prio queue
	@Test
	public void testQueue_withPriority() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		assertEquals(0, queue.size(true));
		boolean success = queue.queue(new DummyMail("Message2"), "Message2", true);
		assertTrue(success);
		assertEquals(1, queue.size(true));		
	}

	// Test correct creation of normal message entry
	@Test
	public void testQueue_withNormal_thenCorrectMessageEntry() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message3"), "Message3", false, 2, 0);
		assertTrue(success);
		MessageEntry<DummyMail> entry = queue.getNext(false);
		assertNotNull(entry);
		assertEquals("Message3", entry.referenceId);
		assertEquals("Message3", entry.message.getId());
		assertFalse(entry.isPriority);
		assertEquals(0, entry.notBeforeTimeInMillis);
		assertEquals(2, entry.failedAttempts);
	}
	
	// Test correct creation of normal message entry
	@Test
	public void testQueue_withPriority_thenCorrectMessageEntry() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message4"), "Message4", true, 1, 0);
		assertTrue(success);
		MessageEntry<DummyMail> entry = queue.getNext(true);
		assertNotNull(entry);
		assertEquals("Message4", entry.referenceId);
		assertEquals("Message4", entry.message.getId());
		assertTrue(entry.isPriority);
		assertEquals(0, entry.notBeforeTimeInMillis);
		assertEquals(1, entry.failedAttempts);
	}
		
	// Test getNext() with normal messages
	@Test
	public void testGetNextBoolean_withNormal() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message5"), "Message5");
		assertTrue(success);
		MessageEntry<DummyMail> entry = queue.getNext(false);
		assertNotNull(entry);
		assertEquals("Message5", entry.referenceId);
	}
	
	// Test getNext() with normal messages
	@Test
	public void testGetNextBoolean_withPriority() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message6"), "Message6", true);
		assertTrue(success);
		MessageEntry<DummyMail> entry = queue.getNext(true);
		assertNotNull(entry);
		assertEquals("Message6", entry.referenceId);
	}
	
	// Test getNext() with normal and prio messages
	@Test
	public void testGetNext_withNormal() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message7"), "Message7");
		assertTrue(success);
		MessageEntry<DummyMail> entry = queue.getNext();
		assertNotNull(entry);
		assertEquals("Message7", entry.referenceId);		
	}
	
	// Test getNext() with prio messages
	@Test
	public void testGetNext_withPriority() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message8"), "Message8", true);
		assertTrue(success);
		MessageEntry<DummyMail> entry = queue.getNext();
		assertNotNull(entry);
		assertEquals("Message8", entry.referenceId);		
	}
	
	// Test getNext() with normal and prio messages
	@Test
	public void testGetNext_withNormalAndPriority() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message9"), "Message9");
		assertTrue(success);
		success = queue.queue(new DummyMail("Message10"), "Message10", true);
		assertTrue(success);
		MessageEntry<DummyMail> entry = queue.getNext();
		assertNotNull(entry);
		assertEquals("Message10", entry.referenceId);		
	}
	
	// Test non-blocking with full queue (normal message)
	@Test
	public void testQueue_withNormal_thenNonBlocking() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		for (int i=0; i<10; i++) {
			boolean success = queue.queue(new DummyMail("Message"+i), "Message"+i);
			assertTrue(success);
		}
		boolean success = queue.queue(new DummyMail("Message11"), "Message11");
		assertFalse(success);
	}
	
	// Test non-blocking with full queue (prio message)
	@Test
	public void testQueue_withPriority_thenNonBlocking() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		for (int i=0; i<5; i++) {
			boolean success = queue.queue(new DummyMail("Message"+i), "Message"+i, true);
			assertTrue(success);
		}
		boolean success = queue.queue(new DummyMail("Message12"), "Message12", true);
		assertFalse(success);
	}
	
	// Test blocking with full queue (normal message)
	@Test
	public void testQueue_withNormal_thenBlocking() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		for (int i=0; i<10; i++) {
			boolean success = queue.queue(new DummyMail("Message"+i), "Message"+i);
			assertTrue(success);
		}
		long start = System.currentTimeMillis();
		boolean success = queue.queue(new DummyMail("Message13"), "Message13", 2L);
		long diff  = System.currentTimeMillis() - start;
		assertFalse(success);
		assertTrue((diff > 1000) && (diff < 3000));
	}
	
	// Test blocking with full queue (prio message)
	@Test
	public void testQueue_withPriority_thenBlocking() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		for (int i=0; i<5; i++) {
			boolean success = queue.queue(new DummyMail("Message"+i), "Message"+i, true);
			assertTrue(success);
		}
		long start = System.currentTimeMillis();
		boolean success = queue.queue(new DummyMail("Message14"), "Message14", true, 2L);
		long diff  = System.currentTimeMillis() - start;
		assertFalse(success);
		assertTrue((diff > 1000) && (diff < 3000));
		
	}
	
	// Test size()
	@Test
	public void testSize() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		assertEquals(0, queue.size());
		boolean success = queue.queue(new DummyMail("Message15"), "Message15");
		assertTrue(success);
		success = queue.queue(new DummyMail("Message16"), "Message16", true);
		assertTrue(success);
		assertEquals(2, queue.size());
	}
	
	// Test remainingCapacity() with normal messages
	@Test
	public void testRemainingCapacity_withNormal() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		assertEquals(10, queue.remainingCapacity(false));
		assertEquals(5, queue.remainingCapacity(true));
		boolean success = queue.queue(new DummyMail("Message17"), "Message17");
		assertTrue(success);
		assertEquals(9, queue.remainingCapacity(false));
		assertEquals(5, queue.remainingCapacity(true));
	}
	
	// Test remainingCapacity() with priority messages
	@Test
	public void testRemainingCapacity_withPriority() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		assertEquals(10, queue.remainingCapacity(false));
		assertEquals(5, queue.remainingCapacity(true));
		boolean success = queue.queue(new DummyMail("Message18"), "Message18", true);
		assertTrue(success);
		assertEquals(10, queue.remainingCapacity(false));
		assertEquals(4, queue.remainingCapacity(true));
	}
	
	// Test capacity()
	@Test
	public void testCapacity_withNormal() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		assertEquals(10, queue.capacity(false));
		boolean success = queue.queue(new DummyMail("Message19"), "Message19");
		assertTrue(success);
		assertEquals(10, queue.capacity(false));
	}
	
	// Test capacity()
	@Test
	public void testCapacity_withPriority() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		assertEquals(5, queue.capacity(true));
		boolean success = queue.queue(new DummyMail("Message20"), "Message20", true);
		assertTrue(success);
		assertEquals(5, queue.capacity(true));
	}
	
	// Test retry of failed normal message
	@Test
	public void testSendFailed_withNormal_thenRetry() throws Exception {
		MailQueue<DummyMail> queue = createQueue(10, 5, true, null);
		DummyMail mail = new DummyMail("Message21");
		boolean success = queue.queue(mail, "Message21");
		assertTrue(success);
		assertEquals(1, queue.size(false));
		MessageEntry<DummyMail> entry = queue.getNext(false);
		queue.run();
		assertEquals(1, queue.size(false));
		assertNotNull(mail.getFailedTime());
		assertEquals(1, mail.getErrorCount());
		assertTrue(entry.notBeforeTimeInMillis > System.currentTimeMillis() + MailQueue.DEFAULT_RETRY_PERIOD - 1000L);
	}
	
	// Test maxRetry of failed normal message
	@Test
	public void testSendFailed_withNormal_withMaxRetries() throws Exception {
		MailQueue<DummyMail> queue = createQueue(10, 5, true, null);
		DummyMail mail = new DummyMail("Message22");
		boolean success = queue.queue(mail, "Message22", false, 3, 0);
		assertTrue(success);
		assertEquals(1, queue.size(false));
		queue.run();
		assertEquals(0, queue.size(false));
		assertNotNull(mail.getFailedTime());
	}
	
	// Test retry of failed priority message
	@Test
	public void testSendFailed_withPriority_thenRetry() throws Exception {
		MailQueue<DummyMail> queue = createQueue(10, 5, true, null);
		DummyMail mail = new DummyMail("Message23");
		boolean success = queue.queue(mail, "Message23", true);
		assertTrue(success);
		assertEquals(1, queue.size(true));
		MessageEntry<DummyMail> entry = queue.getNext(true);
		queue.run();
		assertEquals(1, queue.size(true));
		assertNotNull(mail.getFailedTime());
		assertEquals(1, mail.getErrorCount());
		assertTrue(entry.notBeforeTimeInMillis > System.currentTimeMillis() + MailQueue.DEFAULT_RETRY_PERIOD - 1000L);
	}
	
	// Test maxRetry of failed priority message
	@Test
	public void testSendFailed_withPriority_withMaxRetries() throws Exception {
		MailQueue<DummyMail> queue = createQueue(10, 5, true, null);
		DummyMail mail = new DummyMail("Message24");
		boolean success = queue.queue(mail, "Message24", true, 3, 0);
		assertTrue(success);
		assertEquals(1, queue.size(true));
		queue.run();
		assertEquals(0, queue.size(true));
		assertNotNull(mail.getFailedTime());
	}
	
	// Test remove() with normal
	@Test
	public void testRemove_withNormal() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message25"), "Message25");
		assertTrue(success);
		assertEquals(1, queue.size(false));
		MessageEntry<DummyMail> entry = queue.getNext(false);
		queue.remove(entry);
		assertEquals(0, queue.size(false));
	}
	
	// Test remove() with priority
	@Test
	public void testRemove_withPriority() {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		boolean success = queue.queue(new DummyMail("Message25"), "Message25", true);
		assertTrue(success);
		assertEquals(1, queue.size(true));
		MessageEntry<DummyMail> entry = queue.getNext(true);
		queue.remove(entry);
		assertEquals(0, queue.size(true));
	}
	
	// Test listener notification with success
	@Test
	public void testSend_withSuccess_thenListenerNotified() throws Exception {
		MailQueue<DummyMail> queue = createQueue(10, 5, false, null);
		DummyMailQueueListener listener = new DummyMailQueueListener();
		queue.addListener(listener);
		boolean success = queue.queue(new DummyMail("Message26"), "Message26", true);
		assertTrue(success);
		queue.run();
		assertNotNull(listener.queuedReferenceId);
		assertEquals("Message26", listener.queuedReferenceId);
		assertNotNull(listener.sendingReferenceId);
		assertEquals("Message26", listener.sendingReferenceId);
		assertNotNull(listener.sentReferenceId);
		assertEquals("Message26", listener.sentReferenceId);
	}
	
	// Test listener notification with failure
	@Test
	public void testSend_withFailed_thenListenerNotified() throws Exception {
		MailQueue<DummyMail> queue = createQueue(10, 5, true, null);
		DummyMailQueueListener listener = new DummyMailQueueListener();
		queue.addListener(listener);
		boolean success = queue.queue(new DummyMail("Message27"), "Message27", true);
		assertTrue(success);
		queue.run();
		assertNotNull(listener.queuedReferenceId);
		assertEquals("Message27", listener.queuedReferenceId);
		assertNotNull(listener.sendingReferenceId);
		assertEquals("Message27", listener.sendingReferenceId);
		assertNotNull(listener.failedReferenceId);
		assertEquals("Message27", listener.failedReferenceId);
	}
	
	/**
	 * Creates the queue for a test.
	 * @param maxSize - size of queue
	 * @param maxPrioritySize - size of prio queue
	 * @param bucket - token bucket to be used
	 * @return the queue
	 */
	protected MailQueue<DummyMail> createQueue(int maxSize, int maxPrioritySize, boolean failSending, Bucket bucket) {
		MailQueue<DummyMail> rc = new MailQueue<>(new DummyMailSender(failSending), maxSize, maxPrioritySize);
		if (bucket != null) rc.setTokenBucket(bucket);
		return rc;
	}
	
	/**
	 * Creates a token bucket.
	 * @param count number of email to be sent
	 * @param minutes number of minutes for the mails
	 * @return the token bucket
	 */
	protected Bucket createBucket(int count, int minutes) {
		return Bucket.builder().addLimit(
				limit -> limit
					.tokensPerPeriod(count)
					.period(Duration.ofMinutes(minutes))
					.build()
				)
				.build();
	}
}
