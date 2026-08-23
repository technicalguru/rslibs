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
package csv.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import csv.CommentCallback;
import csv.TableReader;
import csv.TableWriter;

/**
 * Various methods for working with TableReader and TableWriter.
 * This class provides useful method for easily copying table-like data 
 * from either JDBC results, JTable or table readers into table writers.
 * @author RalphSchuster
 *
 */
public class CSVUtils {

	/** The default charset to be used in the JVM for table streams. */
	private static Charset defaultCharset = Charset.defaultCharset();
	
	/**
	 * Returns the {@link #defaultCharset}.
	 * @return the defaultCharset
	 */
	public static Charset getDefaultCharset() {
		return defaultCharset;
	}

	/**
	 * Sets the {@link #defaultCharset}.
	 * @param defaultCharset the defaultCharset to set
	 */
	public static void setDefaultCharset(Charset defaultCharset) {
		CSVUtils.defaultCharset = defaultCharset;
	}

	/**
	 * Sets the {@link #defaultCharset}.
	 * @param defaultCharset the defaultCharset to set
	 */
	public static void setDefaultCharset(String defaultCharset) {
		setDefaultCharset(Charset.forName(defaultCharset));
	}

	/**
	 * Copies the header of the JDBC result set into the table writer.
	 * @param resultSet result set to copy
	 * @param writer CSV writer to write to 
	 * @throws Exception if an exception occurs
	 */
	public static void copyHeader(ResultSet resultSet, TableWriter writer) throws Exception {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int colLength = metaData.getColumnCount();
		String row[] = new String[colLength];
		for (int i=0; i<colLength; i++) {
			row[i] = metaData.getColumnLabel(i+1);
		}
		writer.printRow(row);
	}
	
	/**
	 * Copies the table header into the table writer.
	 * @param tableHeader table header to get content from
	 * @param writer table writer
	 * @throws Exception when an exception occurs
	 */
	public static void copyTableHeader(JTableHeader tableHeader, TableWriter writer) throws Exception {
		TableColumnModel cModel = tableHeader.getColumnModel();
		int colLength = cModel.getColumnCount();
		String row[] = new String[colLength];
		for (int i=0; i<colLength; i++) {
			Object o = cModel.getColumn(i).getHeaderValue();
			if (o != null) row[i] = o.toString();
			else row[i] = null;
		}
		writer.printRow(row);
	}
	
	/**
	 * Copies content from one reader to another writer without header row.
	 * @param reader reader to copy data from
	 * @param writer writer to write data to
	 * @throws Exception when an exception occurs
	 */
	public static void copy(TableReader reader, TableWriter writer) throws Exception {
		copy(reader, writer, false);
	}
	
	/**
	 * Copies content from one reader to another writer.
	 * The header row will only be written if the reader delivers such a row.
	 * @param reader reader to copy data from
	 * @param writer writer to write data to
	 * @param copyHeaderRow whether a header row shall be copied too (only if reader supports it)
	 * @throws Exception when an exception occurs
	 */
	public static void copy(TableReader reader, TableWriter writer, boolean copyHeaderRow) throws Exception {
		CommentCallback callback = new CopyCommentCallback(writer);
		reader.registerCommentCallBack(callback);
		if (copyHeaderRow && reader.hasHeaderRow()) {
			writer.printRow(reader.getHeaderRow());
		}
		while (reader.hasNext()) writer.printRow(reader.next());
		reader.unregisterCommentCallBack(callback);
	}

	/**
	 * Private class that copies comments.
	 * @author Ralph Schuster
	 */
	private static class CopyCommentCallback implements CommentCallback {
		private TableWriter writer = null;
		public CopyCommentCallback(TableWriter writer) {
			this.writer = writer;
		}
		@Override
		public void comment(TableReader reader, String comment, int row, int cell) {
			try {
				writer.printComment(comment, row, cell);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Copies the arrays from the collection to the stream.
	 * @param collection collection that contains rows
	 * @param writer writer to write rows to
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	public static int copy(Collection<? extends Object[]> collection, TableWriter writer) throws IOException {
		return copy(collection.iterator(), writer);
	}
	
	/**
	 * Copies the arrays from the iterator to the stream.
	 * @param i iterator that delivers rows
	 * @param writer writer to write rows to
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	public static int copy(Iterator<? extends Object[]> i, TableWriter writer) throws IOException {
		int rc = 0;
		while (i.hasNext()) {
			writer.printRow(i.next());
			rc++;
		}
		return rc;
	}
	
	/**
	 * Copies the arrays from the iterator to the stream.
	 * @param arr two-dimensional array with rows and columns
	 * @param writer writer to write rows to
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	public static int copy(Object arr[][], TableWriter writer) throws IOException {
		int rc = 0;
		for (Object row : arr) {
			writer.printRow((Object[])row);
			rc++;
		}
		return rc;
	}
	
	/**
	 * Copies the beans from the collection to the stream.
	 * The method will use {@link BeanWriter}
	 * @param <T> The type of bean in the collection
	 * @param collection collection that contains JavaBeans
	 * @param writer writer to write rows to
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	public static <T> int copyBeans(Collection<T> collection, TableWriter writer) throws IOException {
		return copyBeans(collection.iterator(), writer);
	}
	
	/**
	 * Copies the beans from the collection to the stream.
	 * The method will use {@link BeanWriter}
	 * @param <T> The type of bean in the collection
	 * @param i iterator that delivers JavaBeans
	 * @param writer writer to write rows to
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	public static <T> int copyBeans(Iterator<T> i, TableWriter writer) throws IOException {
		BeanWriter<T> w = new BeanWriter<T>(writer, true);
		int rc = w.writeBeans(i);
		w.close();
		return rc;
	}
	
	/**
	 * Copies the beans from the array to the stream.
	 * The method will use {@link BeanWriter}
	 * @param <T> The type of bean in the array
	 * @param arr array of JavaBeans
	 * @param writer writer to write rows to
	 * @return number of rows written
	 * @throws IOException when there is a problem with the writer.
	 */
	public static <T> int copyBeans(T arr[], TableWriter writer) throws IOException {
		BeanWriter<T> w = new BeanWriter<T>(writer, true);
		int rc = w.writeBeans(arr);
		w.close();
		return rc;
	}
	
    /**
     * Returns an array from the columns.
     * This function exists for convinience to take care of minimum column count.
     * @param columns columns to return
     * @param minLength minimum number of columns in return array
     * @return arrray with column values
     */
	public static String[] convertArray(Object columns[], int minLength) {
        int colcount = minLength > 0 ? minLength : 0;
        if (columns != null) colcount = columns.length;

        String rc[] = new String[Math.max(colcount, minLength)];
        if ((columns != null) && (colcount > 0)) {
            for (int i=0; i<colcount; i++) {
            	rc[i] = columns[i] != null ? columns[i].toString() : null;
            }
        }
        return rc;
    }

    /**
     * Trims array to correct length of minimum column count.
     * @param columns columns array
     * @param minLength minimum number of columns
     * @return array with at least minimum number of columns defined.
     */
	public static Object[] extendArray(Object columns[], int minLength) {
        int colcount = minLength > 0 ? minLength : 0;
        if (columns != null) colcount = columns.length;
        Object rc[] = new Object[Math.max(colcount, minLength)];
        if ((columns != null) && (colcount > 0)) {
            for (int i=0; i<colcount; i++) {
            	rc[i] = columns[i];
            }
        }
        return rc;
    }

    /**
     * Trims array to correct length of minimum column count.
     * @param columns columns array
     * @param minLength minimum number of columns
     * @return array with at least minimum number of columns defined.
     */
	public static String[] extendArray(String columns[], int minLength) {
        int colcount = minLength > 0 ? minLength : 0;
        if (columns != null) colcount = columns.length;
        String rc[] = new String[Math.max(colcount, minLength)];
        if ((columns != null) && (colcount > 0)) {
            for (int i=0; i<colcount; i++) {
            	rc[i] = columns[i];
            }
        }
        return rc;
    }

    /**
     * Trims array to correct length of minimum column count.
     * @param columns columns list
     * @param minLength minimum number of columns
     * @return array with at least minimum number of columns defined.
     */
	public static Object[] convertList(List<?> columns, int minLength) {
        int colcount = minLength > 0 ? minLength : 0;
        if (columns != null) colcount = columns.size();
        Object rc[] = new Object[Math.max(colcount, minLength)];
        if ((columns != null) && (colcount > 0)) {
            for (int i=0; i<colcount; i++) {
            	rc[i] = columns.get(i);
            }
        }
        return rc;
    }


}
