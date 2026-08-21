/*
 * This file is part of CSV package.
 *
 *  CSV is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.impl;

import org.apache.poi.ss.usermodel.Row;

/**
 * Notifies about Excel based events.
 * @author RalphSchuster
 *
 */
public interface ExcelListener {

	/**
	 * Notifies about rows created.
	 * Beware that only new Excel rows will be notified. Excel usually
	 * contains blank rows in a sheet that will not be notified by an
	 * Excel writer.
	 * @param writer ExcelWriter that notifies
	 * @param row rows index that was created
	 * @see ExcelWriter#getOrCreateRow(int)
	 */
	public void rowCreated(ExcelWriter writer, Row row);
	
}
