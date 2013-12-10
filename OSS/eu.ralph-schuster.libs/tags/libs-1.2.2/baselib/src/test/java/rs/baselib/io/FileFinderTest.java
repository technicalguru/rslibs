/**
 * 
 */
package rs.baselib.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

import rs.baselib.configuration.ConfigurationUtilsTest;
import rs.baselib.util.CommonUtils;
/**
 * Test for {@link FileFinder}.
 * @author ralph
 *
 */
public class FileFinderTest {

	/**
	 * Test method for {@link FileFinder#find(String)}.
	 */
	@Test
	public void testFindString() {
		URL url = FileFinder.find("finder-file1.txt");
		assertNotNull("Cannot find file 1", url);
		url = FileFinder.find("finder-file2.txt");
		assertNotNull("Cannot find file 2", url);
		url = FileFinder.find("finder-file3.txt");
		assertNotNull("Cannot find file 3", url);
		url = FileFinder.find("finder-file4.txt");
		assertNotNull("Cannot find file 4", url);
	}

	/**
	 * Test method for {@link FileFinder#find(Class, String)}.
	 */
	@Test
	public void testFindClassOfQString() {
		URL url = FileFinder.find(ConfigurationUtilsTest.class, "finder-file1.txt");
		assertNull("Cannot find file 1", url);
		url = FileFinder.find(ConfigurationUtilsTest.class, "finder-file2.txt");
		assertNotNull("Cannot find file 2", url);
		url = FileFinder.find(ConfigurationUtilsTest.class, "finder-file3.txt");
		assertNotNull("Cannot find file 3", url);
		url = FileFinder.find(ConfigurationUtilsTest.class, "finder-file4.txt");
		assertNotNull("Cannot find file 4", url);
	}

	/**
	 * Test method for {@link FileFinder#open(String)}.
	 */
	@Test
	public void testOpenString() throws IOException {
		InputStream in = null;
		try {
			in = FileFinder.open("finder-file1.txt");
			String s = CommonUtils.loadContent(in);
			assertEquals("Cannot open correct stream", "finder-file1", s.trim());
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * Test method for {@link FileFinder#open(Class, String)}.
	 */
	@Test
	public void testOpenClassOfQString() throws IOException {
		InputStream in = null;
		try {
			in = FileFinder.open(ConfigurationUtilsTest.class, "finder-file2.txt");
			String s = CommonUtils.loadContent(in);
			assertEquals("Cannot open correct stream", "finder-file2", s.trim());
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * Test method for {@link FileFinder#load(String)}.
	 */
	@Test
	public void testLoadString() throws IOException {
		String s = FileFinder.load("finder-file3.txt");
		assertEquals("Cannot load correct file", "finder-file3", s.trim());
	}

	/**
	 * Test method for {@link FileFinder#load(Class, String)}.
	 */
	@Test
	public void testLoadClassOfQString() throws IOException {
		String s = FileFinder.load(ConfigurationUtilsTest.class, "finder-file4.txt");
		assertEquals("Cannot load correct file", "finder-file4", s.trim());
	}

}
