/**
 * 
 */
package rs.baselib.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Currency;
import java.util.Date;
import java.util.TimeZone;

import rs.baselib.util.RsDate;
import rs.baselib.util.RsDay;
import rs.baselib.util.RsMonth;
import rs.baselib.util.RsYear;

/**
 * Provides simple methods vor converting bytes into objects and back.
 * @author ralph
 *
 */
public class ConverterUtils {

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(short param) {
		ByteBuffer b = ByteBuffer.allocate(Short.SIZE / 8);
		b.putShort(param);
		return b.array();		
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Short param) {
		if (param == null) return null;
		return toBytes(param.shortValue());
	}

	/**
	 * Converts the given bytes to short.
	 * @param bytes bytes
	 * @return short value
	 */
	public static short toShort(byte bytes[]) {
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getShort();
	}

	/**
	 * Converts the given bytes to Short.
	 * @param bytes bytes
	 * @return Short object
	 */
	public static Short toShortObject(byte bytes[]) {
		if (bytes == null) return null;
		return Short.valueOf(toShort(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(int param) {
		ByteBuffer b = ByteBuffer.allocate(Integer.SIZE / 8);
		b.putInt(param);
		return b.array();
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Integer param) {
		if (param == null) return null;
		return toBytes(param.intValue());
	}

	/**
	 * Converts the given bytes to int.
	 * @param bytes bytes
	 * @return int value
	 */
	public static int toInt(byte bytes[]) {
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getInt();
	}

	/**
	 * Converts the given bytes to Integer.
	 * @param bytes bytes
	 * @return Integer object
	 */
	public static Integer toIntObject(byte bytes[]) {
		if (bytes == null) return null;
		return Integer.valueOf(toInt(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(long param) {
		ByteBuffer b = ByteBuffer.allocate(Long.SIZE / 8);
		b.putLong(param);
		return b.array();
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Long param) {
		if (param == null) return null;
		return toBytes(param.longValue());
	}

	/**
	 * Converts the given bytes to long.
	 * @param bytes bytes
	 * @return long value
	 */
	public static long toLong(byte bytes[]) {
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getLong();
	}

	/**
	 * Converts the given bytes to Integer.
	 * @param bytes bytes
	 * @return Integer object
	 */
	public static Long toLongObject(byte bytes[]) {
		if (bytes == null) return null;
		return Long.valueOf(toLong(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(float param) {
		ByteBuffer b = ByteBuffer.allocate(Float.SIZE / 8);
		b.putFloat(param);
		return b.array();
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Float param) {
		if (param == null) return null;
		return toBytes(param.floatValue());
	}

	/**
	 * Converts the given bytes to float.
	 * @param bytes bytes
	 * @return float value
	 */
	public static float toFloat(byte bytes[]) {
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getFloat();
	}

	/**
	 * Converts the given bytes to Float.
	 * @param bytes bytes
	 * @return Float object
	 */
	public static Float toFloatObject(byte bytes[]) {
		if (bytes == null) return null;
		return Float.valueOf(toFloat(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(double param) {
		ByteBuffer b = ByteBuffer.allocate(Double.SIZE / 8);
		b.putDouble(param);
		return b.array();
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Double param) {
		if (param == null) return null;
		return toBytes(param.doubleValue());
	}

	/**
	 * Converts the given bytes to double.
	 * @param bytes bytes
	 * @return double value
	 */
	public static double toDouble(byte bytes[]) {
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getDouble();
	}

	/**
	 * Converts the given bytes to Double.
	 * @param bytes bytes
	 * @return Double object
	 */
	public static Double toDoubleObject(byte bytes[]) {
		if (bytes == null) return null;
		return Double.valueOf(toDouble(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(char param) {
		ByteBuffer b = ByteBuffer.allocate(Character.SIZE / 8);
		b.putChar(param);
		return b.array();
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Character param) throws UnsupportedEncodingException {
		if (param == null) return null;
		return toBytes(param.charValue());
	}

	/**
	 * Converts the given bytes to char.
	 * @param bytes bytes
	 * @return char value
	 */
	public static char toChar(byte bytes[]) throws UnsupportedEncodingException {
		ByteBuffer b = ByteBuffer.wrap(bytes);
		return b.getChar();
	}

	/**
	 * Converts the given bytes to Character.
	 * @param bytes bytes
	 * @return Character object
	 */
	public static Character toCharObject(byte bytes[]) throws UnsupportedEncodingException {
		if (bytes == null) return null;
		return Character.valueOf(toChar(bytes));
	}


	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(char param[]) throws UnsupportedEncodingException {
		return toBytes(new String(param));
	}

	/**
	 * Converts the given bytes to float.
	 * @param bytes bytes
	 * @return float value
	 */
	public static char[] toCharArray(byte bytes[]) throws UnsupportedEncodingException {
		return toString(bytes).toCharArray();
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(byte param) {
		return new byte[] { param };
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Byte param) {
		if (param == null) return null;
		return toBytes(param.byteValue());
	}

	/**
	 * Converts the given bytes to byte.
	 * @param bytes bytes
	 * @return byte value
	 */
	public static byte toByte(byte bytes[]) {
		return bytes[0];
	}

	/**
	 * Converts the given bytes to Byte.
	 * @param bytes bytes
	 * @return Byte object
	 */
	public static Byte toByteObject(byte bytes[]) {
		if (bytes == null) return null;
		return Byte.valueOf(toByte(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(boolean param) {
		return new byte[] { param ? (byte)1 : (byte)0 };
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Boolean param) {
		if (param == null) return null;
		return toBytes(param.booleanValue());
	}

	/**
	 * Converts the given bytes to boolean.
	 * @param bytes bytes
	 * @return boolean value
	 */
	public static boolean toBoolean(byte bytes[]) {
		if ((bytes == null) || (bytes.length == 0)) return false;
		return bytes[0] > 0;
	}

	/**
	 * Converts the given bytes to Boolean.
	 * @param bytes bytes
	 * @return Boolean object
	 */
	public static Boolean toBooleanObject(byte bytes[]) {
		if (bytes == null) return null;
		return Boolean.valueOf(toBoolean(bytes));
	}

	/**
	 * Converts the given parameter (UTF8) to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(String param) throws UnsupportedEncodingException {
		if (param == null) return null;
		return param.getBytes("UTF8");
	}

	/**
	 * Converts the given bytes to string (UTF8).
	 * @param bytes bytes
	 * @return string value
	 */
	public static String toString(byte bytes[]) throws UnsupportedEncodingException {
		return new String(bytes, "UTF8");
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Date param) {
		if (param == null) return null;
		return toBytes(param.getTime());
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(RsDate param) {
		if (param == null) return null;
		return toBytes(param.getTime());
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(RsMonth param) {
		if (param == null) return null;
		return toBytes(param.getTime());
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(RsYear param) {
		if (param == null) return null;
		return toBytes(param.getTime());
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(RsDay param) {
		if (param == null) return null;
		return toBytes(param.getTime());
	}

	/**
	 * Converts the given bytes to Date.
	 * @param bytes bytes
	 * @return Date value
	 */
	public static Date toDate(byte bytes[]) {
		if (bytes == null) return null;
		return new Date(toLong(bytes));
	}

	/**
	 * Converts the given bytes to Day.
	 * @param bytes bytes
	 * @return Day value
	 */
	public static RsDate toRsDay(byte bytes[]) {
		if (bytes == null) return null;
		return new RsDay(toLong(bytes));
	}

	/**
	 * Converts the given bytes to Date.
	 * @param bytes bytes
	 * @return Date value
	 */
	public static RsDate toRsDate(byte bytes[]) {
		if (bytes == null) return null;
		return new RsDate(toLong(bytes));
	}

	/**
	 * Converts the given bytes to Month.
	 * @param bytes bytes
	 * @return Month value
	 */
	public static RsMonth toRsMonth(byte bytes[]) {
		if (bytes == null) return null;
		return new RsMonth(toLong(bytes));
	}

	/**
	 * Converts the given bytes to Month.
	 * @param bytes bytes
	 * @return Month value
	 */
	public static RsYear toRsYear(byte bytes[]) {
		if (bytes == null) return null;
		return new RsYear(toLong(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Timestamp param) {
		if (param == null) return null;
		return toBytes(param.getTime());
	}

	/**
	 * Converts the given bytes to Date.
	 * @param bytes bytes
	 * @return Timestamp value
	 */
	public static Timestamp toTimestamp(byte bytes[]) {
		if (bytes == null) return null;
		return new Timestamp(toLong(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Currency param) throws UnsupportedEncodingException {
		if (param == null) return null;
		return toBytes(param.getCurrencyCode());
	}

	/**
	 * Converts the given bytes to Currency.
	 * @param bytes bytes
	 * @return Currency value
	 */
	public static Currency toCurrency(byte bytes[]) throws UnsupportedEncodingException {
		if (bytes == null) return null;
		return Currency.getInstance(toString(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 *
	public static byte[] toBytes(Locale param) throws UnsupportedEncodingException {
		if (param == null) return null;
		return toBytes(param.toLanguageTag());
	}

	/**
	 * Converts the given bytes to Locale.
	 * @param bytes bytes
	 * @return Locale value
	 *
	public static Locale toLocale(byte bytes[]) throws UnsupportedEncodingException {
		if (bytes == null) return null;
		return Locale.forLanguageTag(toString(bytes));
	}
	 */

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(TimeZone param) throws UnsupportedEncodingException {
		if (param == null) return null;
		return toBytes(param.getID());
	}

	/**
	 * Converts the given bytes to TimeZone.
	 * @param bytes bytes
	 * @return TimeZone value
	 */
	public static TimeZone toTimeZone(byte bytes[]) throws UnsupportedEncodingException {
		if (bytes == null) return null;
		return TimeZone.getTimeZone(toString(bytes));
	}

	/**
	 * Converts the given parameter to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(URL param) throws UnsupportedEncodingException {
		if (param == null) return null;
		return toBytes(param.toExternalForm());
	}

	/**
	 * Converts the given bytes to URL.
	 * @param bytes bytes
	 * @return URL value
	 */
	public static URL toURL(byte bytes[]) throws UnsupportedEncodingException, MalformedURLException {
		if (bytes == null) return null;
		return new URL(toString(bytes));
	}

	/**
	 * Converts the given Serializable to its byte representation.
	 * @param param parameter
	 * @return byte reresentation
	 */
	public static byte[] toBytes(Serializable o) throws IOException {
		if (o == null) return null;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream(1000);
		ObjectOutputStream out = new ObjectOutputStream(bOut);
		out.writeObject(o);
		out.close();
		return bOut.toByteArray();
	}

	/**
	 * Converts a byte array to a {@link Serializable}.
	 * @param bytes the bytes from a serializable
	 * @return the {@link Serializable}
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Object toObject(byte bytes[]) throws ClassNotFoundException, IOException {
		ByteArrayInputStream bIn = new ByteArrayInputStream(bytes);

		// Read object using ObjectInputStream
		ObjectInputStream in =  new ObjectInputStream (bIn);

		// Read an object
		return  in.readObject();
	}
}
