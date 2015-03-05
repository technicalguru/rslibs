package rs.data.util;

/*
 * This file is part of RS Library (Data Base Library).
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

import static org.junit.Assert.assertEquals;

import javax.transaction.Status;

import org.junit.Test;

import rs.data.util.TxStatus;

/**
 * Test the {@link TxStatus} implementation.
 * @author ralph
 *
 */
public class TxStatusTest {

	@Test
	public void testActive() {
		assertEquals("Not the correct status", TxStatus.ACTIVE, TxStatus.getStatus(Status.STATUS_ACTIVE));
	}

	@Test
	public void testCommitted() {
		assertEquals("Not the correct status", TxStatus.COMMITTED, TxStatus.getStatus(Status.STATUS_COMMITTED));
	}

	@Test
	public void testCommitting() {
		assertEquals("Not the correct status", TxStatus.COMMITTING, TxStatus.getStatus(Status.STATUS_COMMITTING));
	}

	@Test
	public void testMarkedRollback() {
		assertEquals("Not the correct status", TxStatus.MARKED_ROLLBACK, TxStatus.getStatus(Status.STATUS_MARKED_ROLLBACK));
	}

	@Test
	public void testNoTransaction() {
		assertEquals("Not the correct status", TxStatus.NO_TRANSACTION, TxStatus.getStatus(Status.STATUS_NO_TRANSACTION));
	}

	@Test
	public void testPrepared() {
		assertEquals("Not the correct status", TxStatus.PREPARED, TxStatus.getStatus(Status.STATUS_PREPARED));
	}

	@Test
	public void testPreparing() {
		assertEquals("Not the correct status", TxStatus.PREPARING, TxStatus.getStatus(Status.STATUS_PREPARING));
	}

	@Test
	public void testRolledBack() {
		assertEquals("Not the correct status", TxStatus.ROLLEDBACK, TxStatus.getStatus(Status.STATUS_ROLLEDBACK));
	}

	@Test
	public void testRollingBack() {
		assertEquals("Not the correct status", TxStatus.ROLLING_BACK, TxStatus.getStatus(Status.STATUS_ROLLING_BACK));
	}

	@Test
	public void testUnknown() {
		assertEquals("Not the correct status", TxStatus.UNKNOWN, TxStatus.getStatus(Status.STATUS_UNKNOWN));
	}

}
