/**
 * 
 */
package rs.baselib.io;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Tests {@link ConverterUtils}.
 * @author ralph
 *
 */
public class ConverterUtilsTest {

	/**
	 * Test method for {@link ConverterUtils#toShort(byte[])}.
	 */
	@Test
	public void testToShort() {
		short value = 15;
		assertEquals("Cannot convert short", value, ConverterUtils.toShort(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toShortObject(byte[])}.
	 */
	@Test
	public void testToShortObject() {
		Short value = 16;
		assertEquals("Cannot convert Short", value, ConverterUtils.toShortObject(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toInt(byte[])}.
	 */
	@Test
	public void testToInt() {
		int value = 17;
		assertEquals("Cannot convert int", value, ConverterUtils.toInt(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toIntObject(byte[])}.
	 */
	@Test
	public void testToIntObject() {
		Integer value = 17;
		assertEquals("Cannot convert Integer", value, ConverterUtils.toIntObject(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toLong(byte[])}.
	 */
	@Test
	public void testToLong() {
		long value = 18L;
		assertEquals("Cannot convert long", value, ConverterUtils.toLong(ConverterUtils.toBytes(value)));
	}
	
	/**
	 * Test method for {@link ConverterUtils#toLongObject(byte[])}.
	 */
	@Test
	public void testToLongObject() {
		Long value = 19L;
		assertEquals("Cannot convert Long", value, ConverterUtils.toLongObject(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toFloat(byte[])}.
	 */
	@Test
	public void testToFloat() {
		float value = 20f;
		assertEquals("Cannot convert float", Float.valueOf(value), Float.valueOf(ConverterUtils.toFloat(ConverterUtils.toBytes(value))));
	}

	/**
	 * Test method for {@link ConverterUtils#toFloatObject(byte[])}.
	 */
	@Test
	public void testToFloatObject() {
		Float value = 21f;
		assertEquals("Cannot convert Float", value, ConverterUtils.toFloatObject(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toDouble(byte[])}.
	 */
	@Test
	public void testToDouble() {
		double value = 22;
		assertEquals("Cannot convert double", Double.valueOf(value), Double.valueOf(ConverterUtils.toDouble(ConverterUtils.toBytes(value))));
	}

	/**
	 * Test method for {@link ConverterUtils#toDoubleObject(byte[])}.
	 */
	@Test
	public void testToDoubleObject() {
		Double value = 23d;
		assertEquals("Cannot convert Double", value, ConverterUtils.toDoubleObject(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toChar(byte[])}.
	 */
	@Test
	public void testToChar() throws UnsupportedEncodingException {
		char value = 'c';
		assertEquals("Cannot convert char", value, ConverterUtils.toChar(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toCharObject(byte[])}.
	 */
	@Test
	public void testToCharObject() throws UnsupportedEncodingException {
		Character value = 'c';
		assertEquals("Cannot convert Character", value, ConverterUtils.toCharObject(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toCharArray(byte[])}.
	 */
	@Test
	public void testToCharArray() throws UnsupportedEncodingException {
		char value[] = new char[] { 'a', 'v', 'a', 'l', 'u', 'e' };
		assertArrayEquals("Cannot convert char array", value, ConverterUtils.toCharArray(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toByte(byte[])}.
	 */
	@Test
	public void testToByte() {
		byte value = 24;
		assertEquals("Cannot convert byte", value, ConverterUtils.toByte(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toByteObject(byte[])}.
	 */
	@Test
	public void testToByteObject() {
		Byte value = 24;
		assertEquals("Cannot convert Byte", value, ConverterUtils.toByteObject(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toBoolean(byte[])}.
	 */
	@Test
	public void testToBoolean() {
		boolean value = true;
		assertEquals("Cannot convert boolean", value, ConverterUtils.toBoolean(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toBooleanObject(byte[])}.
	 */
	@Test
	public void testToBooleanObject() {
		Boolean value = false;
		assertEquals("Cannot convert Boolean", value, ConverterUtils.toBooleanObject(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toString(byte[])}.
	 */
	@Test
	public void testToString() throws UnsupportedEncodingException {
		String value = "aValue";
		assertEquals("Cannot convert String", value, ConverterUtils.toString(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toDate(byte[])}.
	 */
	@Test
	public void testToDate() {
		Date value = new Date();
		assertEquals("Cannot convert Date", value, ConverterUtils.toDate(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toTimestamp(byte[])}.
	 */
	@Test
	public void testToTimestamp() {
		Timestamp value = new Timestamp(System.currentTimeMillis());
		assertEquals("Cannot convert Timestamp", value, ConverterUtils.toTimestamp(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toCurrency(byte[])}.
	 */
	@Test
	public void testToCurrency() throws UnsupportedEncodingException {
		Currency value = Currency.getInstance(Locale.GERMANY);
		assertEquals("Cannot convert Currency", value, ConverterUtils.toCurrency(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toTimeZone(byte[])}.
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testToTimeZone() throws UnsupportedEncodingException {
		TimeZone value = TimeZone.getDefault();
		assertEquals("Cannot convert TimeZone", value, ConverterUtils.toTimeZone(ConverterUtils.toBytes(value)));
	}

	/**
	 * Test method for {@link ConverterUtils#toURL(byte[])}.
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testToURL() throws MalformedURLException, UnsupportedEncodingException {
		URL value = new URL("http://techblog.ralph-schuster.eu/rs-library/baselib/");
		assertEquals("Cannot convert URL", value, ConverterUtils.toURL(ConverterUtils.toBytes(value)));
	}

}
