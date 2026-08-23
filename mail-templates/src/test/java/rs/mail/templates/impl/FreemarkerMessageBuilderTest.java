package rs.mail.templates.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.mail.templates.BuilderException;
import rs.mail.templates.BuilderResult;
import rs.mail.templates.ContentType;
import rs.mail.templates.I18nResolver;
import rs.mail.templates.TemplateContext;
import rs.mail.templates.TemplateResolver;
import rs.mail.templates.resolver.DefaultI18nResolver;
import rs.mail.templates.resolver.DefaultTemplateResolver;

/**
 * Tests the {@link FreemarkerMessageBuilder}.
 * 
 * @author ralph
 *
 */
public class FreemarkerMessageBuilderTest {
	
	private static File curDir;
	
	private TemplateResolver resolver;
	private TemplateResolver primaryResolver;
	private TemplateResolver fallbackResolver;
	private I18nResolver     i18nResolver;
	private TemplateContext  context;

	@Test
	public void testBuild_withSimpleTemplate() throws BuilderException {
		FreemarkerMessageBuilder<BuilderResult> builder = (FreemarkerMessageBuilder<BuilderResult>)new FreemarkerMessageBuilder<>(new BuilderResultCreator())
				.withContext(context)
				.withResolver(resolver)
				.withSubjectTemplate("simple-template");
		
		String result = builder.build("simple-template", ContentType.TEXT, new HashMap<>());
		assertNotNull(result);
		assertEquals("A message: my-product is available at http://example.com/product", result.trim());
	}
	
	@Test
	public void testBuild_withFallback() throws BuilderException {
		FreemarkerMessageBuilder<BuilderResult> builder = (FreemarkerMessageBuilder<BuilderResult>)new FreemarkerMessageBuilder<>(new BuilderResultCreator())
				.withContext(context)
				.withResolver(primaryResolver, fallbackResolver)
				.withSubjectTemplate("fallback-template");
		
		String result = builder.build("fallback-template", ContentType.TEXT, new HashMap<>());
		assertNotNull(result);
		assertEquals("FALLBACK: A message: my-product is available at http://example.com/product", result.trim());
	}
	
	@Test
	public void testBuild_withSubTemplate() throws BuilderException {
		FreemarkerMessageBuilder<BuilderResult> builder = (FreemarkerMessageBuilder<BuilderResult>)new FreemarkerMessageBuilder<>(new BuilderResultCreator())
				.withContext(context)
				.withResolver(primaryResolver, fallbackResolver)
				.withSubjectTemplate("main-template");
		
		String result = builder.build("main-template", ContentType.TEXT, new HashMap<>());
		assertNotNull(result);
		assertEquals("***START-OF-MAIN*** FALLBACK: A message: my-product is available at http://example.com/product ***END-OF-MAIN***", result.trim());
	}
	
	@Test
	public void testBuild_withTranslations() throws BuilderException {
		FreemarkerMessageBuilder<BuilderResult> builder = (FreemarkerMessageBuilder<BuilderResult>)new FreemarkerMessageBuilder<>(new BuilderResultCreator())
				.withContext(context)
				.withResolver(resolver)
				.withResolver(i18nResolver)
				.withSubjectTemplate("translation-template")
				.withBodyTemplate("translation-template")
				.withI18n("translation-template");
		
		String result = builder.buildBody(ContentType.TEXT);
		assertNotNull(result);
		assertEquals("Ein Template mit einer deutschen Variable und einer shared variable - und einer global variable.", result.trim());
	}
	

	@BeforeEach
	public void beforeEach() throws IOException {
		resolver = new DefaultTemplateResolver(new File(curDir, "src/test/resources/freemarker"));
		primaryResolver  = new DefaultTemplateResolver(new File(curDir, "src/test/resources/freemarker/primary"));
		fallbackResolver = new DefaultTemplateResolver(new File(curDir, "src/test/resources/freemarker/fallback"));
		i18nResolver     = new DefaultI18nResolver(new File(curDir, "src/test/resources/freemarker"));
		context  = new TemplateContext();
		context.setLocale(Locale.GERMANY);
		context.setValue("object", new Product());
		context.setValue("aMessage", "A message");
	}
	
	@BeforeAll
	public static void beforeClass() {
		curDir = Paths.get("").toAbsolutePath().toFile();
	}

	public static class Product {
		String name;
		String url;
		public Product() {
			name = "my-product";
			url  = "http://example.com/product";
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
	}

}
