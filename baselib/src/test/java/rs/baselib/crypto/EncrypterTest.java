/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import rs.baselib.io.FileFinder;

/**
 * Tests {@link Encrypter} and {@link Decrypter}.
 * @author ralph
 *
 */
public class EncrypterTest {

	/**
	 * Test method for {@link Encrypter#encrypt(java.lang.String)}.
	 * 
	 * @throws Exception - when the encryption process fails
	 */
	@ParameterizedTest
	@MethodSource("provideTestCases")
	public void testEncrypt(int lineNo, String plainText, String saltString, String key, String encryptedText) throws Exception {
		// Encrypt test
		byte salt[] = Base64.decodeBase64(saltString);	
		Encrypter enc = new Encrypter(key, salt, 0);
		String cmp = enc.encrypt(plainText);
		assertEquals(encryptedText, cmp);

		// Decrypt test
		Decrypter dec = new Decrypter(key, salt, 0);
		String cmp2 = dec.decrypt(cmp);
		assertEquals(plainText, cmp2);
	}
	
	
	public static Stream<Arguments> provideTestCases() throws Exception {
		Collection<Arguments> data = new ArrayList<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(FileFinder.open(EncrypterTest.class, "tests.csv"), StandardCharsets.UTF_8));
		int lineNo = 0;
		while (in.ready()) {
			String line = in.readLine();
			if (line == null) break;

			lineNo++;

			// Ignore comment lines
			if (line.startsWith("#")) continue;

			// Split it
			String s[] = line.trim().split(",");
			data.add(Arguments.of(lineNo, s[0], s[1], s[2], s[3]));
		}

		return data.stream();
	}

}
