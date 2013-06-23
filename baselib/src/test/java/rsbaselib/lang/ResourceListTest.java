/**
 * 
 */
package rsbaselib.lang;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the resource list class
 * @author ralph
 *
 */
public class ResourceListTest {

	@Test
	public void testGetResources() {
        Pattern pattern = Pattern.compile(".*");
        Collection<URL> list = ResourceList.getResources(pattern);
        assertTrue("No resources found", list.size() > 0);
        for (URL url : list) {
            assertNotNull("Empty URL", url);
        }
	}

	@Test
	public void testGetManifests() {
        Collection<Manifest> list = ResourceList.getManifests();
        assertTrue("No manifests found", list.size() > 0);
        for (Manifest manifest : list) {
        	for (Entry<Object,Object> e : manifest.getMainAttributes().entrySet()) {
        		assertNotNull("Empty main key", e.getKey());
        		assertNotNull("Empty main value", e.getValue());
        	}
        	
        	for (Map.Entry<String, Attributes> entry : manifest.getEntries().entrySet()) {
        		for (Entry<Object,Object> e : entry.getValue().entrySet()) {
            		assertNotNull("Empty key for "+entry.getKey(), e.getKey());
            		assertNotNull("Empty value for "+entry.getKey(), e.getValue());
        		}
        	}
        }
	}
}
