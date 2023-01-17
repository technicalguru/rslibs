/**
 * 
 */
package rs.otp.secret;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Provides secret generators for OTP.
 * 
 * @author ralph
 *
 */
public enum SecretType {

	BASE32(Base32Secret.class),
	HEX(HexSecret.class);
	
	private Class<? extends ISecret> generator;
	
	private SecretType(Class<? extends ISecret> generator) {
		this.generator = generator;
	}

	/**
	 * Generates a secret of given length.
	 * @param length - the length
	 * @return the random secret of given length
	 */
	public String generate(int length) {
		try {
			Method m = generator.getDeclaredMethod("generate", Integer.TYPE);
			return (String)m.invoke(null, length);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot generate a secret with "+generator.getName(), t);
		}
	}

	/**
	 * Generates a secret of default length.
	 * @return the random secret of default length
	 */
	public String generate() {
		try {
			Method m = generator.getDeclaredMethod("generate");
			return (String)m.invoke(null);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot generate a secret with "+generator.getName(), t);
		}
	}
	
	/**
	 * Generates a secret of given length.
	 * @param length - the length
	 * @return the random secret of given length
	 */
	public ISecret generateSecret(int length) {
		try {
			Method m = generator.getDeclaredMethod("generateSecret", Integer.TYPE);
			return (ISecret)m.invoke(null, length);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot generate a secret with "+generator.getName(), t);
		}
	}

	/**
	 * Generates a secret of default length.
	 * @return the random secret of default length
	 */
	public ISecret generateSecret() {
		try {
			Method m = generator.getDeclaredMethod("generateSecret");
			return (ISecret)m.invoke(null);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot generate a secret with "+generator.getName(), t);
		}
	}
	
	/**
	 * Decode the given secret into bytes.
	 * @param s the secret to be decoded
	 * @return the decodes secret as bytes
	 */
	public byte[] decode(String s) {
		try {
			Method m = generator.getDeclaredMethod("decode", String.class);
			return (byte[])m.invoke(null, s);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot decode a secret with "+generator.getName(), t);
		}
	}
	
	/**
	 * Decode the given secret into bytes.
	 * @param bytes the bytes to be encoded
	 * @return the encoded secret
	 */
	public String encode(byte[] bytes) {
		try {
			Method m = generator.getDeclaredMethod("encode", Byte[].class);
			return (String)m.invoke(null, bytes);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot encode a secret with "+generator.getName(), t);
		}
	}
	
	/**
	 * Creates a secret from the given string.
	 * @param s - the secret
	 * @return the secret from the given string
	 */
	public ISecret from(String s) {
		try {
			Constructor<?> c = generator.getConstructor(String.class);
			return (ISecret)c.newInstance(s);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot generate a secret with "+generator.getName(), t);
		}
	}
	
	/**
	 * Creates a secret from the given bytes.
	 * @param bytes - the secret
	 * @return the secret from the given bytes
	 */
	public ISecret from(byte[] bytes) {
		try {
			Constructor<?> c = generator.getConstructor(Byte[].class);
			return (ISecret)c.newInstance(bytes);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot generate a secret with "+generator.getName(), t);
		}
	}
	

}
