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
package rs.baselib.prefs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rs.baselib.util.CommonUtils;

/**
 * Tests the {@link PreferencesService}.
 * @author ralph
 *
 */
public class PreferencesServiceTest {

	private static PreferencesService service;
	
	private static final String EXAMPLE_CONFIG = 
			"key1=value1\n"+
			"key2=value2\n"+
			"node1/key3=value3\n"+
			"node1/key4=value4\n"+
			"node1/key5=value5\n"+
			"node2/key6=value6\n"+
			"node1/node3/key7=value7\n"+
			"node1/node3/key8=value8\n"+
			"node1/node4/key9=value9\n";
	
	private static final String EXAMPLE_CONFIG2 = 
			"key1=value1\n"+
			"key2=value2\n"+
			"node1/key3=value3\n"+
			"node1/key4=value4\n"+
			"node1/key5=value5\n"+
			"node2/key6=value6\n"+
			"node1/node3/key7=value7\n"+
			"node1/node3/key8=value8\n"+
			"node1/node4/key9=value9\n"+
			"nodex\n"+ // Ignored because no = sign
			"#node5=something\n"+ // Ignored because leading #
			"   \n"; // Ignored because empty line
	
	@BeforeAll
	public static void setupClass() {
		service = (PreferencesService)PreferencesService.INSTANCE;
	}
	
	@AfterAll
	public static void cleanupClass() {
		File f = service.getUserPreferencesFile("aTestProject");
		if (f.exists()) {
			File parent = f.getParentFile();
			f.delete();
			parent.delete();
		}
		f = service.getSystemPreferencesFile("aTestProject");
		if (f.exists()) {
			File parent = f.getParentFile();
			f.delete();
			parent.delete();
		}
	}
	
	/**
	 * Test method for {@link PreferencesService#load(IPreferences, InputStream)}.
	 * 
	 * @throws BackingStoreException when the backing store fails
	 */
	@Test
	public void testLoad() throws BackingStoreException {
		ByteArrayInputStream in = new ByteArrayInputStream(EXAMPLE_CONFIG.getBytes(StandardCharsets.UTF_8));
		IPreferences prefs = new Preferences(null, null);
		service.load(prefs, in);
		testNode(prefs, new int[]{ 1, 2 }, new int[]{ 1, 2 } );
		testNode(prefs.node("node1"), new int[]{ 3, 4 }, new int[]{ 3, 4, 5 } );
		testNode(prefs.node("node2"), new int[]{ }, new int[]{ 6 } );
		testNode(prefs.node("node1/node3"), new int[]{ }, new int[]{ 7, 8 } );
		testNode(prefs.node("node1/node4"), new int[]{ }, new int[]{ 9 } );
	}

	/**
	 * Test method for {@link PreferencesService#load(IPreferences, InputStream)}.
	 * 
	 * @throws BackingStoreException when the backing store fails
	 */
	@Test
	public void testLoadIgnored() throws BackingStoreException {
		ByteArrayInputStream in = new ByteArrayInputStream(EXAMPLE_CONFIG2.getBytes(StandardCharsets.UTF_8));
		IPreferences prefs = new Preferences(null, null);
		service.load(prefs, in);
		testNode(prefs, new int[]{ 1, 2 }, new int[]{ 1, 2 } );
		testNode(prefs.node("node1"), new int[]{ 3, 4 }, new int[]{ 3, 4, 5 } );
		testNode(prefs.node("node2"), new int[]{ }, new int[]{ 6 } );
		testNode(prefs.node("node1/node3"), new int[]{ }, new int[]{ 7, 8 } );
		testNode(prefs.node("node1/node4"), new int[]{ }, new int[]{ 9 } );
	}

	/**
	 * Tests a node.
	 * @param node node to be tested
	 * @param children child indices
	 * @param keys key indices
	 * 
	 * @throws BackingStoreException when the backing store fails
	 */
	protected void testNode(IPreferences node, int children[], int keys[]) throws BackingStoreException {
		String childNames[] = node.childrenNames();
		String keyNames[]   = node.keys();
		assertEquals(children.length, childNames.length);
		for (int i : children) {
			assertTrue(node.nodeExists("node"+i));
		}
		assertEquals(keys.length, keyNames.length);
		for (int i : children) {
			assertEquals("value"+i, node.get("key"+i, ""));
		}
	}
	
	/**
	 * Test method for {@link PreferencesService#save(IPreferences, OutputStream)}.
	 * 
	 * @throws BackingStoreException - when the backing store fails
	 * @throws UnsupportedEncodingException - when string encoding fails
	 */
	@Test
	public void testSaveIPreferencesOutputStream() throws BackingStoreException, UnsupportedEncodingException {
		ByteArrayInputStream in = new ByteArrayInputStream(EXAMPLE_CONFIG.getBytes(StandardCharsets.UTF_8));
		IPreferences prefs = new Preferences(null, null);
		service.load(prefs, in);
		ByteArrayOutputStream out = new ByteArrayOutputStream(EXAMPLE_CONFIG.getBytes(StandardCharsets.UTF_8).length);
		service.save(prefs, out);
		testOutput(out.toString(StandardCharsets.UTF_8.name()));
	}

	/**
	 * Tests that the output is correct.
	 * @param output produced output
	 */
	protected void testOutput(String output) {
		String cmp[] = EXAMPLE_CONFIG.split("\\n");
		String l[]   = output.split("\\n");
		Arrays.sort(cmp);
		Arrays.sort(l);
		assertEquals(cmp.length, l.length);
		for (int i=0; i<cmp.length; i++) {
			assertEquals(cmp[i].trim(), l[i].trim());
		}
	}
	
	/**
	 * Test method for {@link PreferencesService#getUserPreferencesFile(String)}.
	 * 
	 * @throws BackingStoreException - when the backing store fails
	 * @throws IOException - when the file access fails
	 */
	@Test
	public void testGetUserPreferencesFile() throws IOException, BackingStoreException {
		File file = service.getUserPreferencesFile("aTestProject");
		assertEquals("user.prefs", file.getName());
		assertEquals(".aTestProject", file.getParentFile().getName());
		assertEquals(new File(System.getProperty("user.home")), file.getParentFile().getParentFile());
	}

	/**
	 * Test method for {@link PreferencesService#getSystemPreferencesFile(String)}.
	 */
	@Test
	public void testGetSystemPreferencesFile() {
		File file = service.getSystemPreferencesFile("aTestProject");
		assertEquals("system.prefs", file.getName());
		assertTrue(file.getParentFile().getName().endsWith("aTestProject"));
	}

	/**
	 * Test method for read and writes (high level tests)
	 * 
	 * @throws BackingStoreException - when the backing store fails
	 * @throws IOException - when the file access fails
	 */
	@Test
	public void testUserPreferences() throws BackingStoreException, IOException {
		IPreferences prefs = service.getUserPreferences("aTestProject");
		File testFile = service.getUserPreferencesFile("aTestProject");
		testReadWrite(prefs, testFile);
	}

	/**
	 * Test method for read and writes (high level tests)
	 * 
	 * @throws BackingStoreException - when the backing store fails
	 * @throws IOException - when the file access fails
	 */
	@Test
	public void testSystemPreferences() throws BackingStoreException, IOException {
		IPreferences prefs = service.getSystemPreferences("aTestProject");
		File testFile = service.getSystemPreferencesFile("aTestProject");
		testReadWrite(prefs, testFile);
	}
	
	protected void testReadWrite(IPreferences node, File f) throws BackingStoreException, IOException {
		// Create some nodes and keys
		node.put("key1", "value1");
		node.put("key2", "value2");
		IPreferences n = node.node("node1");
		n.put("key3", "value3");
		n.put("key4", "value4");
		n.put("key5", "value5");
		n = node.node("node2");
		n.put("key6", "value6");
		n = node.node("node1/node3");
		n.put("key7", "value7");
		n.put("key8", "value8");
		n = node.node("node1/node4");
		n.put("key9", "value9");
		
		// Test that all have been added correctly
		testNode(node, new int[]{ 1, 2 }, new int[]{ 1, 2 } );
		testNode(node.node("node1"), new int[]{ 3, 4 }, new int[]{ 3, 4, 5 } );
		testNode(node.node("node2"), new int[]{ }, new int[]{ 6 } );
		testNode(node.node("node1/node3"), new int[]{ }, new int[]{ 7, 8 } );
		testNode(node.node("node1/node4"), new int[]{ }, new int[]{ 9 } );
		
		// Wait for flush
		node.sync();
		
		// Check that file was produced
		assertTrue(f.exists());
		
		// Check that file contains correct values
		testOutput(CommonUtils.loadContent(f));
	}
}
