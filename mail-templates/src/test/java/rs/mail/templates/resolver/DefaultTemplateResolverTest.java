package rs.mail.templates.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.mail.templates.ContentType;
import rs.mail.templates.ResolverException;
import rs.mail.templates.Template;
import rs.mail.templates.TemplateContext;
import rs.mail.templates.impl.ResolverId;

/**
 * Tests the {@link DefaultTemplateResolver}.
 * 
 * @author ralph
 *
 */
public class DefaultTemplateResolverTest {

	private static File curDir;
	
	private DefaultTemplateResolver resolver;
	private TemplateContext         context;
	
	@Test
	public void testGetPriorityPaths_withHtml() throws IOException {
		context.setLocale(Locale.GERMANY);
		List<File> files = resolver.getPriorityPaths("my-template", context, ".html");
		assertNotNull(files);
		assertEquals(3, files.size());
		assertEquals("my-template.de-DE.html", files.get(0).getName());
		assertEquals("my-template.de.html",    files.get(1).getName());
		assertEquals("my-template.html",       files.get(2).getName());
		assertEquals(resolver.getDirectory(),  files.get(0).getParentFile());
		assertEquals(resolver.getDirectory(),  files.get(1).getParentFile());
		assertEquals(resolver.getDirectory(),  files.get(2).getParentFile());
	}
	
	@Test
	public void testGetPriorityPaths_withText() throws IOException {
		context.setLocale(Locale.GERMANY);
		List<File> files = resolver.getPriorityPaths("my-template", context, ".txt");
		assertNotNull(files);
		assertEquals(3, files.size());
		assertEquals("my-template.de-DE.txt", files.get(0).getName());
		assertEquals("my-template.de.txt",    files.get(1).getName());
		assertEquals("my-template.txt",       files.get(2).getName());
		assertEquals(resolver.getDirectory(), files.get(0).getParentFile());
		assertEquals(resolver.getDirectory(), files.get(1).getParentFile());
		assertEquals(resolver.getDirectory(), files.get(2).getParentFile());
	}
	
	@Test
	public void testGetPriorityPaths_withHtmlAndFtl() throws IOException {
		context.setLocale(Locale.GERMANY);
		List<File> files = resolver.getPriorityPaths("my-template.ftl", context, ".html");
		assertNotNull(files);
		assertEquals(4, files.size());
		assertEquals("my-template.ftl.de-DE.html", files.get(0).getName());
		assertEquals("my-template.ftl.de.html",    files.get(1).getName());
		assertEquals("my-template.ftl.html",       files.get(2).getName());
		assertEquals("my-template.ftl",            files.get(3).getName());
		assertEquals(resolver.getDirectory(),      files.get(0).getParentFile());
		assertEquals(resolver.getDirectory(),      files.get(1).getParentFile());
		assertEquals(resolver.getDirectory(),      files.get(2).getParentFile());
		assertEquals(resolver.getDirectory(),      files.get(3).getParentFile());
	}
	
	@Test
	public void testGetPriorityPaths_withTextAndFtl() throws IOException {
		context.setLocale(Locale.GERMANY);
		List<File> files = resolver.getPriorityPaths("my-template.ftl", context, ".txt");
		assertNotNull(files);
		assertEquals(4, files.size());
		assertEquals("my-template.ftl.de-DE.txt", files.get(0).getName());
		assertEquals("my-template.ftl.de.txt",    files.get(1).getName());
		assertEquals("my-template.ftl.txt",       files.get(2).getName());
		assertEquals("my-template.ftl",           files.get(3).getName());
		assertEquals(resolver.getDirectory(),     files.get(0).getParentFile());
		assertEquals(resolver.getDirectory(),     files.get(1).getParentFile());
		assertEquals(resolver.getDirectory(),     files.get(2).getParentFile());
		assertEquals(resolver.getDirectory(),     files.get(3).getParentFile());
	}
	
	@Test
	public void testFindFile_withNoFiles() {
		context.setLocale(Locale.GERMANY);
		File file = resolver.findFile(resolver.getPriorityPaths("my-template", context, ".html"));
		assertNull(file);
	}
	
	@Test
	public void testFindFile_withFirstFile() {
		context.setLocale(Locale.GERMANY);
		File file = resolver.findFile(resolver.getPriorityPaths("test-template1", context, ".html"));
		assertNotNull(file);
		assertEquals("test-template1.de-DE.html", file.getName());
		assertEquals(resolver.getDirectory(), file.getParentFile());
	}
	
	@Test
	public void testFindFile_withSecondFile() {
		context.setLocale(Locale.GERMANY);
		File file = resolver.findFile(resolver.getPriorityPaths("test-template2", context, ".html"));
		assertNotNull(file);
		assertEquals("test-template2.de.html", file.getName());
		assertEquals(resolver.getDirectory(), file.getParentFile());
	}
	
	@Test
	public void testResolve_withNoTemplate() throws ResolverException {
		context.setLocale(Locale.GERMANY);
		ResolverId     id = new ResolverId("my-template", Locale.GERMANY);
		Template template = resolver.resolve(id, id.getId(), context);
		assertNull(template);
	}
	
	@Test
	public void testResolve_withTemplate() throws ResolverException {
		context.setLocale(Locale.GERMANY);
		ResolverId     id = new ResolverId("test-template1", Locale.GERMANY);
		Template template = resolver.resolve(id, id.getId(), context);
		assertNotNull(template);
		assertEquals(id, template.getId());
	}
	
	@Test
	public void testResolve_withCacheHit() throws ResolverException {
		context.setLocale(Locale.GERMANY);
		ResolverId     id = new ResolverId("test-template2", Locale.GERMANY);
		
		// Create cache entry
		Template template = resolver.resolve(id, id.getId(), context);
		assertNotNull(template);
		assertEquals(id, template.getId());
		
		// Check cache
		assertTrue(resolver.getCache().containsKey(id));
		
		// Provoke a cache hit
		Template template2 = resolver.resolve(id, id.getId(), context);
		assertEquals(template, template2);
	}
	
	@Test
	public void testResolve_withoutCache() throws ResolverException, IOException {
		resolver = new DefaultTemplateResolver(new File(curDir, "src/test/resources/resolver"), false);
		context.setLocale(Locale.GERMANY);
		ResolverId     id = new ResolverId("test-template1", Locale.GERMANY);
		Template template = resolver.resolve(id, id.getId(), context);
		assertNotNull(template);
		assertEquals(id, template.getId());
		
		// Provoke a reload
		Template template2 = resolver.resolve(id, id.getId(), context);
		assertNotEquals(template, template2);
	}
	
	@Test
	public void testResolve_withCacheDifferentLanguage()  throws ResolverException, IOException {
		resolver = new DefaultTemplateResolver(new File(curDir, "src/test/resources/resolver"), true);
		// Resolve with Language 1
		context.setLocale(Locale.GERMANY);
		ResolverId     id1 = new ResolverId("test-template3", Locale.GERMANY);
		Template template1 = resolver.resolve(id1, id1.getId(), context);
		assertNotNull(template1);
		assertEquals(id1, template1.getId());
		
		// Resolve with Language 2
		context.setLocale(Locale.ENGLISH);
		ResolverId     id2 = new ResolverId("test-template3", Locale.ENGLISH);
		Template template2 = resolver.resolve(id2, id2.getId(), context);
		assertNotNull(template2);
		assertEquals(id2, template2.getId());
		assertNotEquals(template1.getTemplate(ContentType.HTML), template2.getTemplate(ContentType.HTML));
		assertNotEquals(template1.getTemplate(ContentType.TEXT), template2.getTemplate(ContentType.TEXT));
	}
	

	@BeforeEach
	public void beforeEach() throws IOException {
		resolver = new DefaultTemplateResolver(new File(curDir, "src/test/resources/resolver"));
		context  = new TemplateContext();
	}
	
	@BeforeAll
	public static void beforeClass() {
		curDir = Paths.get("").toAbsolutePath().toFile();
	}
}
