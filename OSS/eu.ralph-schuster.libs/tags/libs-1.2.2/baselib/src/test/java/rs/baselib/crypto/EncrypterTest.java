/**
 * 
 */
package rs.baselib.crypto;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;

/**
 * Tests {@link Encrypter} and {@link Decrypter}.
 * @author ralph
 *
 */
@RunWith(Parameterized.class)
public class EncrypterTest {

	private static Logger log = LoggerFactory.getLogger(EncrypterTest.class);

	@Parameters
	public static Collection<Object[]> data() throws Exception {
		Collection<Object[]> data = new ArrayList<Object[]>();
		BufferedReader in = new BufferedReader(new InputStreamReader(FileFinder.open(EncrypterTest.class, "tests.csv")));
		int lineNo = 0;
		while (in.ready()) {
			String line = in.readLine();
			if (line == null) break;

			lineNo++;

			// Ignore comment lines
			if (line.startsWith("#")) continue;

			// Split it
			String s[] = line.trim().split(",");
			data.add(new Object[] { lineNo, s[0], s[1], s[2], s[3]});
		}

		return data;
	}

	private int lineNo;
	private String plainText;
	private String salt;
	private String key;
	private String encryptedText;

	/**
	 * Constructs the test with given parameters.
	 */
	public EncrypterTest(int lineNo, String plainText, String salt, String key, String encryptedText) {
		this.lineNo = lineNo;
		this.plainText = plainText;
		this.salt = salt;
		this.key = key;
		this.encryptedText = encryptedText;
	}

	/**
	 * Test method for {@link Encrypter#encrypt(java.lang.String)}.
	 */
	@Test
	public void testEncrypt() throws Exception {
		log.debug("Line "+lineNo+": "+plainText+";"+salt+";"+key+";"+encryptedText);

		// Encrypt test
		byte salt[] = Base64.decodeBase64(this.salt);	
		Encrypter enc = new Encrypter(key, salt, 0);
		String cmp = enc.encrypt(plainText);
		assertEquals(encryptedText, cmp);

		// Decrypt test
		Decrypter dec = new Decrypter(key, salt, 0);
		String cmp2 = dec.decrypt(cmp);
		assertEquals(plainText, cmp2);
	}

}
