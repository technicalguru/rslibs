package rs.mail.templates.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.mail.templates.BuilderException;
import rs.mail.templates.BuilderResult;
import rs.mail.templates.ContentType;
import rs.mail.templates.MessageBuilderFactory;
import rs.mail.templates.TemplateContext;
import rs.mail.templates.TemplateResolver;
import rs.mail.templates.resolver.DefaultTemplateResolver;

/**
 * Tests the {@link AbstractMessageBuilder}.
 * 
 * @author ralph
 *
 */
public class AbstractMessageBuilderTest {

	private static File curDir;
	
	private TemplateResolver resolver;
	private TemplateContext  context;

	@Test
	public void testBuildSubject_withGerman() throws BuilderException {
		AbstractMessageBuilder<BuilderResult> builder = (AbstractMessageBuilder<BuilderResult>)createMessageBuilder()
				.withContext(context)
				.withResolver(resolver)
				.withSubjectTemplate("test-subject1");
		
		String subject = builder.buildSubject();
		assertNotNull(subject);
		assertEquals("Eine Nachricht", subject.trim());
	}
	
	@Test
	public void testBuildSubject_withEnglish() throws BuilderException {
		context.setLocale(Locale.ENGLISH);
		AbstractMessageBuilder<BuilderResult> builder = (AbstractMessageBuilder<BuilderResult>)createMessageBuilder()
				.withContext(context)
				.withResolver(resolver)
				.withSubjectTemplate("test-subject1");
		
		String subject = builder.buildSubject();
		assertNotNull(subject);
		assertEquals("A message", subject.trim());
	}
	
	@Test
	public void testBuildBody_withHtml() throws BuilderException {
		AbstractMessageBuilder<BuilderResult> builder = (AbstractMessageBuilder<BuilderResult>)createMessageBuilder()
				.withContext(context)
				.withResolver(resolver)
				.withBodyTemplate("test-body1");
		
		String body = builder.buildBody(ContentType.HTML);
		assertNotNull(body);
		assertEquals("<p>Dies ist ein Nachrichtentext</p>", body.trim());
	}
	
	@Test
	public void testBuildBody_withText() throws BuilderException {
		AbstractMessageBuilder<BuilderResult> builder = (AbstractMessageBuilder<BuilderResult>)createMessageBuilder()
				.withContext(context)
				.withResolver(resolver)
				.withBodyTemplate("test-body1");
		
		String body = builder.buildBody(ContentType.TEXT);
		assertNotNull(body);
		assertEquals("Dies ist ein Nachrichtentext", body.trim());
	}
	
	@Test
	public void testToText() {
		AbstractMessageBuilder<BuilderResult> builder = (AbstractMessageBuilder<BuilderResult>)createMessageBuilder();
		String html = "<p>Dies ist Zeile 1</p>\n<p>Dies ist Zeile 2</p>";
		String text = builder.toText(html);
		assertNotNull(text);
		assertEquals("Dies ist Zeile 1\nDies ist Zeile 2", text);
	}
	
	@Test
	public void testToHtml() {
		AbstractMessageBuilder<BuilderResult> builder = (AbstractMessageBuilder<BuilderResult>)createMessageBuilder();
		String text = "Dies ist Zeile 1\nDies ist Zeile 2";
		String html = builder.toHtml(text);
		assertNotNull(html);
		assertEquals("Dies ist Zeile 1<br>Dies ist Zeile 2", html);
	}
	
	@Test
	public void testBuildSubject_withResolverChainPrimary() throws BuilderException, IOException {
		TemplateResolver primaryResolver  = new DefaultTemplateResolver(new File(curDir, "src/test/resources/resolver/primary"));
		TemplateResolver fallbackResolver = new DefaultTemplateResolver(new File(curDir, "src/test/resources/resolver/fallback"));
		AbstractMessageBuilder<BuilderResult> builder = (AbstractMessageBuilder<BuilderResult>)createMessageBuilder()
				.withContext(context)
				.withResolver(primaryResolver, fallbackResolver)
				.withSubjectTemplate("test-subject2");
		
		String subject = builder.buildSubject();
		assertNotNull(subject);
		assertEquals("Eine Nachricht 2 (Primary)", subject.trim());
	}
	
	@Test
	public void testBuildSubject_withResolverChainFallback() throws BuilderException, IOException {
		TemplateResolver primaryResolver  = new DefaultTemplateResolver(new File(curDir, "src/test/resources/resolver/primary"));
		TemplateResolver fallbackResolver = new DefaultTemplateResolver(new File(curDir, "src/test/resources/resolver/fallback"));
		AbstractMessageBuilder<BuilderResult> builder = (AbstractMessageBuilder<BuilderResult>)createMessageBuilder()
				.withContext(context)
				.withResolver(primaryResolver, fallbackResolver)
				.withSubjectTemplate("test-subject3");
		
		String subject = builder.buildSubject();
		assertNotNull(subject);
		assertEquals("Eine Nachricht 3 (Fallback)", subject.trim());
	}
	
	protected AbstractMessageBuilder<BuilderResult> createMessageBuilder() {
		return (AbstractMessageBuilder<BuilderResult>)MessageBuilderFactory.newBuilder(new BuilderResultCreator());
	}
	
	@BeforeEach
	public void beforeEach() throws IOException {
		resolver = new DefaultTemplateResolver(new File(curDir, "src/test/resources/resolver"));
		context  = new TemplateContext();
		context.setLocale(Locale.GERMANY);
	}
	
	@BeforeAll
	public static void beforeClass() {
		curDir = Paths.get("").toAbsolutePath().toFile();
	}

}
