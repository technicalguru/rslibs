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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import csv.impl.XmlReader;
import csv.impl.XmlWriter;

/**
 * JUnit Test for JavaBeans.
 * @author RalphSchuster
 *
 */
public class BeanWriterReaderTest {
	private static final String FILE_NAME= "bean-test.xml";

	private static final TestBean TEST_BEANS[] = new TestBean[] {
		new TestBean(0, 1L, "2", 3.0, true),
		new TestBean(4, 5L, "6", 7.0, false),
		new TestBean(8, 9L, "10", 11.0, true),
		new TestBean(12, 13L, "14", 15.0, false)
	};

	private File fFile;

	/**
	 * Initializes file.
	 */
	@BeforeEach
	public void init() {
		fFile= new File(FILE_NAME);
	}

	/**
	 * Removes the file.
	 */
	@AfterEach
	public void done() {
		if ((fFile != null) && fFile.exists()) fFile.deleteOnExit();
	}
	
	@Test
	public void testPlainReadWrite() {
		testWriteFile();
		testWrittenValues();
	}

	/**
	 * The test method writes a XML file with beans.
	 * Test {@link #testWrittenValues()} will
	 * compare the results with the original values.
	 */
	public void testWriteFile() {
		BeanWriter<TestBean> out= null;
		try {
			// Write the file
			XmlWriter xmlWriter = new XmlWriter(fFile, true);
			//xmlWriter.setUseColumnNameTags(true);
			out= new BeanWriter<TestBean>(xmlWriter, true);
			for (int row= 0; row < TEST_BEANS.length; row++) {
				out.writeBean(TEST_BEANS[row]);
			}
			assertTrue(fFile.exists());
		} catch (IOException e) {
			fail("IO exception: " + e.getMessage());
		} finally {
			out.close();
		}
	}

	/**
	 * This method compares the results of read with the original values.
	 */
	public void testWrittenValues() {
		BeanReader<TestBean> in= null;
		try {
			// Reader
			XmlReader xmlReader = new XmlReader(fFile);
			in = new BeanReader<TestBean>(TestBean.class, xmlReader);

			int row= 0;
			while (in.hasNext()) {
				TestBean bean = in.next();

				// compare size of columns
				assertEquals(TEST_BEANS[row], bean);

				row++;
			}

			// compare size of rows
			assertTrue(row == TEST_BEANS.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail("May be a previous test has failed: " + e.getMessage());
		} finally {
			if (in != null) in.close();
		}
	}

	/**
	 * Internal class for testing the bean classes.
	 */
	public static class TestBean {

		private int intValue = -1;
		private long longValue = -1;
		private String stringValue = "-1";
		private double doubleValue = -1;
		private boolean booleanValue = false;

		/**
		 * Constructor used by {@link BeanReader}.
		 */
		public TestBean() {

		}

		/**
		 * Constructor used internally
		 * @param intValue integer test value
		 * @param longValue long test value
		 * @param stringValue string test value
		 * @param doubleValue double test value
		 * @param booleanValue boolean test value
		 */
		public TestBean(int intValue, long longValue, String stringValue, double doubleValue, boolean booleanValue) {
			this.intValue = intValue;
			this.longValue = longValue;
			this.stringValue = stringValue;
			this.doubleValue = doubleValue;
			this.booleanValue = booleanValue;
		}

		/**
		 * @return the intValue
		 */
		public int getIntValue() {
			return intValue;
		}

		/**
		 * @param intValue the intValue to set
		 */
		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}

		/**
		 * @return the longValue
		 */
		public long getLongValue() {
			return longValue;
		}

		/**
		 * @param longValue the longValue to set
		 */
		public void setLongValue(long longValue) {
			this.longValue = longValue;
		}

		/**
		 * @return the stringValue
		 */
		public String getStringValue() {
			return stringValue;
		}

		/**
		 * @param stringValue the stringValue to set
		 */
		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}



		/**
		 * @return the doubleValue
		 */
		public double getDoubleValue() {
			return doubleValue;
		}

		/**
		 * @param doubleValue the doubleValue to set
		 */
		public void setDoubleValue(double doubleValue) {
			this.doubleValue = doubleValue;
		}


		/**
		 * @return the booleanValue
		 */
		public boolean isBooleanValue() {
			return booleanValue;
		}

		/**
		 * @param booleanValue the booleanValue to set
		 */
		public void setBooleanValue(boolean booleanValue) {
			this.booleanValue = booleanValue;
		}

		/**
		 * For testing that beans are read back correctly.
		 * The method comapres all attributes.
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof TestBean)) return false;
			TestBean b = (TestBean)obj;
			if (getIntValue() != b.getIntValue()) return false;
			if (!getStringValue().equals(b.getStringValue())) return false;
			if (getLongValue() != b.getLongValue()) return false;
			if (getDoubleValue() != b.getDoubleValue()) return false;
			if (isBooleanValue() != b.isBooleanValue()) return false;
			return true;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return getIntValue();
		}

	}
}
