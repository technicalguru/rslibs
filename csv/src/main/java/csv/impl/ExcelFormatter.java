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

import org.apache.poi.ss.usermodel.Cell;

/**
 * Interface for formatting Excel sheets
 * @author RalphSchuster
 *
 */
public interface ExcelFormatter {

	/**
	 * Sets the cell style.
	 * @param writer writer that requires the information
	 * @param cell cell to be formatted
	 * @param value value in cell
	 */
	public void setStyle(ExcelWriter writer, Cell cell, Object value);
	
	/**
	 * Finalizes the workbook.
	 * This method is called immediately before the {@link ExcelWriter} writes the
	 * complete workbook to the underlying output stream.
	 * @param writer the calling writer
	 * @param rowCount the number of rows in the selected sheet
	 * @param columnCount the number of columns modified in the selected sheet
	 * @deprecated Use the new method {@link #finalize(ExcelWriter)}.
	 */
	@Deprecated
	public void finalize(ExcelWriter writer, int rowCount, int columnCount);
	
	/**
	 * Finalizes the workbook.
	 * This method is called immediately before the {@link ExcelWriter} writes the
	 * complete workbook to the underlying output stream.
	 * @param writer the calling writer
	 * @since 4.1
	 */
	public void finalize(ExcelWriter writer);

}
