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
package rs.data.util;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

/**
 * A transaction status as enumeration.
 * @author ralph
 *
 */
public enum TxStatus {

	/**
	 * A transaction is associated with the target object and it is in the active state.
	 */
	ACTIVE(Status.STATUS_ACTIVE),
	/**
	 * A transaction is associated with the target object and it has been committed.
	 */
	COMMITTED(Status.STATUS_COMMITTED),
	/**
	 * A transaction is associated with the target object and it is in the process of committing.
	 */
	COMMITTING(Status.STATUS_COMMITTING),
	/**
	 * A transaction is associated with the target object and it has been marked for rollback, perhaps as a result of a setRollbackOnly operation.
	 */
	MARKED_ROLLBACK(Status.STATUS_MARKED_ROLLBACK),
	/**
	 * No transaction is currently associated with the target object.
	 */
	NO_TRANSACTION(Status.STATUS_NO_TRANSACTION),
	/**
	 * A transaction is associated with the target object and it has been prepared.
	 */
	PREPARED(Status.STATUS_PREPARED),
	/**
	 * A transaction is associated with the target object and it is in the process of preparing.
	 */
	PREPARING(Status.STATUS_PREPARING),
	/**
	 * A transaction is associated with the target object and the outcome has been determined to be rollback.
	 */
	ROLLEDBACK(Status.STATUS_ROLLEDBACK),
	/**
	 * A transaction is associated with the target object and it is in the process of rolling back.
	 */
	ROLLING_BACK(Status.STATUS_ROLLING_BACK),
	/**
	 * A transaction is associated with the target object but its current status cannot be determined.
	 */
	UNKNOWN(Status.STATUS_UNKNOWN);
	
	private int id;
	
	private TxStatus(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the status of the given transaction.
	 * @param tx the transaction
	 * @return the status of the transaction as enum object
	 */
	public static TxStatus getStatus(Transaction tx) throws SystemException {
		if (tx != null) {
			return getStatus(tx.getStatus());
		}
		return NO_TRANSACTION;
	}
	
	/**
	 * Returns the status of the given transaction.
	 * @param id id of status
	 * @return the status as enum object
	 */
	public static TxStatus getStatus(int id) {
		for (TxStatus status : values()) {
			if (status.getId() == id) return status;
		}
		return null;
	}
	
}
