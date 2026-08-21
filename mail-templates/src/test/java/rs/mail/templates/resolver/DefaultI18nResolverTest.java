package rs.mail.templates.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.mail.templates.I18n;
import rs.mail.templates.ResolverException;
import rs.mail.templates.TemplateContext;
import rs.mail.templates.impl.ResolverId;

/**
 * Tests the {@link DefaultI18nResolver}.
 * 
 * @author ralph
 *
 */
public class DefaultI18nResolverTest {

	private static File curDir;
	
	private DefaultI18nResolver resolver;
	private TemplateContext     context;
	
	@Test
	public void testGetPriorityPaths() throws IOException {
		context.setLocale(Locale.GERMANY);
		List<File> files = resolver.getPriorityPaths("my-translations", context);
		assertNotNull(files);
		assertEquals(4, files.size());
		assertEquals("my-translations.de-DE.properties", files.get(0).getName());
		assertEquals("my-translations.de.properties",    files.get(1).getName());
		assertEquals("my-translations.properties",       files.get(2).getName());
		assertEquals("i18n.properties",                  files.get(3).getName());
		assertEquals(resolver.getDirectory(),            files.get(0).getParentFile());
		assertEquals(resolver.getDirectory(),            files.get(1).getParentFile());
		assertEquals(resolver.getDirectory(),            files.get(2).getParentFile());
		assertEquals(resolver.getDirectory(),            files.get(3).getParentFile());
	}
	
	@Test
	public void testResolve_withNoOverride() throws ResolverException {
		context.setLocale(Locale.GERMANY);
		ResolverId id = new ResolverId("my-translations", Locale.GERMANY);
		I18n     i18n = resolver.resolve(id, id.getId(), context);
		assertNotNull(i18n);
		assertEquals(id, i18n.getId());
		assertEquals(4, i18n.size());
		assertEquals("value1-i18n", i18n.get("key1"));
		assertEquals("value2-i18n", i18n.get("key2"));
		assertEquals("value3-i18n", i18n.get("key3"));
		assertEquals("value4-i18n", i18n.get("key4"));
	}
	
	@Test
	public void testResolve_withOverride() throws ResolverException {
		context.setLocale(Locale.GERMANY);
		ResolverId id = new ResolverId("test-translations1", Locale.GERMANY);
		I18n     i18n = resolver.resolve(id, id.getId(), context);
		assertNotNull(i18n);
		assertEquals(id, i18n.getId());
		assertEquals(4, i18n.size());
		assertEquals("value1.de-DE", i18n.get("key1"));
		assertEquals("value2.de", i18n.get("key2"));
		assertEquals("value3", i18n.get("key3"));
		assertEquals("value4-i18n", i18n.get("key4"));
	}
	
	@Test
	public void testResolve_withCacheHit() throws ResolverException {
		context.setLocale(Locale.GERMANY);
		ResolverId id = new ResolverId("test-translations2", Locale.GERMANY);
		
		// Create cache entry
		I18n i18n = resolver.resolve(id, id.getId(), context);
		assertNotNull(i18n);
		assertEquals(id, i18n.getId());
		
		// Check cache
		assertTrue(resolver.getCache().containsKey(id));
		
		// Provoke a cache hit
		I18n i18n2 = resolver.resolve(id, id.getId(), context);
		assertTrue(i18n == i18n2);
	}
	
	@Test
	public void testResolve_withoutCache() throws ResolverException, IOException {
		resolver = new DefaultI18nResolver(new File(curDir, "src/test/resources/resolver"), false);
		context.setLocale(Locale.GERMANY);
		ResolverId   id = new ResolverId("test-translations1", Locale.GERMANY);
		I18n       i18n = resolver.resolve(id, id.getId(), context);
		assertNotNull(i18n);
		assertEquals(id, i18n.getId());
		
		// Provoke a reload
		I18n i18n2 = resolver.resolve(id, id.getId(), context);
		assertFalse(i18n == i18n2);
	}
	
	@Test
	public void testResolve_withCacheDifferentLanguage()  throws ResolverException, IOException {
		resolver = new DefaultI18nResolver(new File(curDir, "src/test/resources/resolver"), true);
		// Resolve with Language 1
		context.setLocale(Locale.GERMANY);
		ResolverId   id1 = new ResolverId("test-translations3", Locale.GERMANY);
		I18n       i18n1 = resolver.resolve(id1, id1.getId(), context);
		assertNotNull(i18n1);
		assertEquals(id1, i18n1.getId());
		
		// Resolve with Language 2
		context.setLocale(Locale.ENGLISH);
		ResolverId   id2 = new ResolverId("test-translations3", Locale.ENGLISH);
		I18n       i18n2 = resolver.resolve(id2, id2.getId(), context);
		assertNotNull(i18n2);
		assertEquals(id2, i18n2.getId());
		assertNotEquals(i18n1, i18n2);
	}
	

	@BeforeEach
	public void beforeEach() throws IOException {
		resolver = new DefaultI18nResolver(new File(curDir, "src/test/resources/resolver"));
		context  = new TemplateContext();
	}
	
	@BeforeAll
	public static void beforeClass() {
		curDir = Paths.get("").toAbsolutePath().toFile();
	}
}
