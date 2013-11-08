/**
 * 
 */
package rs.baselib.prefs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;

import org.junit.BeforeClass;
import org.junit.Test;

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
	
	@BeforeClass
	public static void setupClass() {
		service = (PreferencesService)PreferencesService.INSTANCE;
	}
	
	/**
	 * Test method for {@link PreferencesService#load(IPreferences, InputStream)}.
	 */
	@Test
	public void testLoad() throws BackingStoreException {
		ByteArrayInputStream in = new ByteArrayInputStream(EXAMPLE_CONFIG.getBytes());
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
	 * @throws BackingStoreException
	 */
	protected void testNode(IPreferences node, int children[], int keys[]) throws BackingStoreException {
		String childNames[] = node.childrenNames();
		String keyNames[]   = node.keys();
		assertEquals("Node "+node.absolutePath()+" has not enough children", children.length, childNames.length);
		for (int i : children) {
			assertTrue("Cannot find node"+i, node.nodeExists("node"+i));
		}
		assertEquals("Node "+node.absolutePath()+" has not enough keys", keys.length, keyNames.length);
		for (int i : children) {
			assertEquals("Cannot find correct key"+i, "value"+i, node.get("key"+i, ""));
		}
	}
	
	/**
	 * Test method for {@link PreferencesService#save(IPreferences, OutputStream)}.
	 */
	@Test
	public void testSaveIPreferencesOutputStream() throws BackingStoreException {
		ByteArrayInputStream in = new ByteArrayInputStream(EXAMPLE_CONFIG.getBytes());
		IPreferences prefs = new Preferences(null, null);
		service.load(prefs, in);
		ByteArrayOutputStream out = new ByteArrayOutputStream(EXAMPLE_CONFIG.getBytes().length);
		service.save(prefs, out);
		String cmp[] = EXAMPLE_CONFIG.split("\\n");
		String l[]   = out.toString().split("\\n");
		Arrays.sort(cmp);
		Arrays.sort(l);
		assertEquals("Produced output has incorrect number of lines", cmp.length, l.length);
		for (int i=0; i<cmp.length; i++) {
			assertEquals("Produced output differs in line "+(i+1), cmp[i].trim(), l[i].trim());
		}
	}

	/**
	 * Test method for {@link PreferencesService#getUserPreferencesFile(String)}.
	 */
	@Test
	public void testGetUserPreferencesFile() throws IOException, BackingStoreException {
		Path path = service.getUserPreferencesFile("aTestProject").toPath();
		int cnt = path.getNameCount();
		assertEquals("Not the correct user preferences filename", "user.prefs", path.getName(cnt-1).toFile().getName());
		assertEquals("Not the correct user preferences application directory", ".aTestProject", path.getName(cnt-2).toFile().getName());
		assertEquals("Not the correct user preferences home directory", new File(System.getProperty("user.home")), path.getParent().getParent().toFile());
	}

	/**
	 * Test method for {@link PreferencesService#getSystemPreferencesFile(String)}.
	 */
	@Test
	public void testGetSystemPreferencesFile() {
		Path path = service.getSystemPreferencesFile("aTestProject").toPath();
		System.out.println(path.toFile().getAbsolutePath());
		int cnt = path.getNameCount();
		assertEquals("Not the correct user preferences filename", "system.prefs", path.getName(cnt-1).toFile().getName());
		assertEquals("Not the correct user preferences application directory", "aTestProject", path.getName(cnt-2).toFile().getName());
	}

	
}
