/**
 * 
 */
package rs.otp.secret;

/**
 * Provides secret generators for OTP.
 * 
 * @author ralph
 *
 */
public enum OtpSecret {

	BASE32(Base32Secret.class),
	HEX(HexSecret.class);
	
	private ISecret generator;
	
	private OtpSecret(Class<? extends ISecret> generator) {
		try {
			this.generator = generator.getConstructor().newInstance();
		} catch (Throwable t) {
			throw new RuntimeException("Cannot create SecretGenerator instance of "+generator.getName(), t);
		}
	}

	/**
	 * Generates a secret of given length.
	 * @param length - the length
	 * @return the random secret of given length
	 */
	public String generate(int length) {
		return generator.generate(length);
	}

	/**
	 * Generates a secret of default length.
	 * @return the random secret of default length
	 */
	public String generate() {
		return generator.generate();
	}
	
	/**
	 * Decode the given secret into bytes.
	 * @param s the secret to be decoded
	 * @return the decodes secret as bytes
	 */
	public byte[] decode(String s) {
		return generator.decode(s);
	}
	
}
