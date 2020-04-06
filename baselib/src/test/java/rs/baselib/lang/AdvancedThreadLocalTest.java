/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.lang;

import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import rs.baselib.util.AdvancedThreadLocal;

/**
 * Tests {@link AdvancedThreadLocal}.
 * @author ralph
 *
 */
public class AdvancedThreadLocalTest {

	private static Logger log = LoggerFactory.getLogger(AdvancedThreadLocalTest.class);
	
	@Test
	public void testThreads() throws Exception {
		AdvancedThreadLocal<String> local = new AdvancedThreadLocal<String>();
		
		// Create threads
		int MAX_THREADS = 10;
		LocalThread threads[] = new LocalThread[MAX_THREADS];
		// Create threads and start them
		for (int i=0; i<MAX_THREADS; i++) {
			threads[i] = new LocalThread(local, i, i % 2 == 0);
			threads[i].start();
		}
		
		// Wait for thread to finish
		for (int i=0; i<MAX_THREADS; i++) {
			threads[i].join();
		}
		
		// Test number of threads still in map
		assertEquals("Local values were not removed", MAX_THREADS/2, local.getEntries().size());
		
		// Verify
		local.verifyThreads();
		
		// Assert that no thread is in map anymore
		assertEquals("Obsolete values were not removed", 0, local.getEntries().size());
		
	}
	
	protected static class LocalThread extends Thread {
		
		private AdvancedThreadLocal<String> map;
		private int id;
		private boolean removeValueAtEnd;
		
		public LocalThread(AdvancedThreadLocal<String> map, int id, boolean removeValueAtEnd) {
			this.map = map;
			this.id = id;
			this.removeValueAtEnd = removeValueAtEnd;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			try {
				// Set value
				map.set(toString());
				
				// Loop for ten seconds sleeping different amount of time inbetween
				long endTime = System.currentTimeMillis()+10000L;
				Random random = new Random();
				while (System.currentTimeMillis() < endTime) {
					long sleepTime = random.nextLong();
					try {
						sleep(Math.abs(sleepTime) % 200);
					} catch (InterruptedException e) {}
					// Check valid value
					assertEquals("Thread "+id+": value is not correct", toString(), map.get());
				}
				
				if (removeValueAtEnd) {
					map.remove();
				}
			} catch (Exception e) {
				log.error("Thread "+id+": test fails", e);
				fail("Thread "+id+": test fails");
			}
		}
		
		
	}
}
