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
package csv;

/**
 * Will be called when a commen is discovered in an input stream
 * @author RalphSchuster
 * @see TableReader#registerCommentCallBack(CommentCallback)
 */
public interface CommentCallback {

	/**
	 * Informs about a comment appeared in underlying stream.
	 * @param reader the reader that notifies
	 * @param comment comment
	 * @param row line number
	 * @param cell cell number in row (might be -1 if not applicable)
	 */
	public void comment(TableReader reader, String comment, int row, int cell);
	
}
