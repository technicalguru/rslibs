# Java Mail Templates
This project aims at creating e-mails from templates. The idea was created when dealing with a few
applications that need different mail templates, with different languages and even different clients
at the same time, using different content types (HTML and TEXT).

The project delivers a default implementation using [Freemarker](https://freemarker.apache.org) but
has been designed openly to use other template engines if needed.

# Installation

Maven Coordinates:

**Attention:** Due to a dependency you will need JDK 17 and above.

```
	<dependency>
		<groupId>eu.ralph-schuster</groupId>
		<artifactId>mail-templates</artifactId>
		<version>1.2.3</version>
	</dependency>
```

# Documentation

Javadoc is available from: [javadoc.io](https://www.javadoc.io/doc/eu.ralph-schuster/mail-templates)

# Getting Started

You need a `MessageBuilder` that you configure:

```
MessageBuilder builder = MessageBuilderFactory.newBuilder()
	.withResolver(new DefaultTemplateResolver(new File("/path-to-templates")))
	.withSubjectTemplate("my-subject-template-name")
	.withBodyTemplate("my-body-template-name")
	.withMessageProducer(new DefaultMessageProducer(mailSession));
```

- The resolver is responsible to find and load your named templates. See Resolvers section below for 
more information.
- The subject and body templates define what templates will be used respectively. You can use names 
  as above or set your `Template` objects directly. Latter is not recommended as it is hardcoded.
- Finally, you need a `MessageProducer`. It is responsible to create a `Message` that
  will be filled with the message body and the subject. Usually, this is a `MimeMessage`.
  The default implementation can be used with an existing `javax.mail.Session` object
  or a `Properties` object. 
   
Now you are ready to add named values to your `MessageBuilder`:

```
builder.withValue('user', userObject);
builder.withValue('product', productObject);
```

(Remember to follow the Freemarker rules for the [Data Model](https://freemarker.apache.org/docs/pgui_datamodel.html).)

Once you have configured the builder, create the `Message`:

```
Message email = builder.build();
```

Set your TO, CC and BCC address fields and whatever other customization is required and send it.

## The Template Context
The `Context` object holds most of the information that is required to build a message. Actually it 
is the backing store for the `MessageBuilder`. So instead of above initialization code, you can also
write:

```
TemplateContext context = new TemplateContext();
context.addResolver(new DefaultTemplateResolver(new File("/path-to-templates")));
context.setSubjectTemplate("my-subject-template-name");
context.setBodyTemplate("my-body-template-name");

MessageBuilder builder = MessageBuilderFactory.newBuilder()
	.withContext(context)
	.withMessageProducer(new DefaultMessageProducer(mailSession));
	
context.setValue('user', userObject);
context.setValue('product', productObject);
```

## Different Languages
The `Template` object is language-agnostic. It means it does not know anything
about languages. That requires that individual templates are bound to specific languages.
You will need a template for German and another one for English. However, you can provide
the information about your requirement within the `TemplateContext` object:

```
context.setLocale(Locale.GERMANY);
```

It will be up for the resolvers to find your specific language template.

## Resolvers
The task of a `Resolver` is to deliver a `Template` object based on the name and the context. 
You can implement your own resolvers but the default implementation `DefaultTemplateResolver` 
should be sufficient for the most use cases. It will look for a file in a defined directory on your 
system, taking into account the locale information within the context.

We create the object with:

```
TemplateResolver resolver = new DefaultTemplateResolver(new File('/my/template/directory'));
```

Assumed that you defined `Locale.GERMANY` (`de-DE`), it will look for HTML and TEXT template files 
when you ask for an template named `my-body-template`:

1. `/my/template/directory/my-body-template.de-DE.html`
1. `/my/template/directory/my-body-template.de.html`
1. `/my/template/directory/my-body-template.html`

TEXT template files are respectively searched for in this order: 
1. `/my/template/directory/my-body-template.de-DE.txt`
1. `/my/template/directory/my-body-template.de.txt`
1. `/my/template/directory/my-body-template.txt`

A `Template` should always provide HTML and TEXT templates. However, this is not required. The
default builder implementation will derive one from the other as best as possible.

## Resolver Priority
Now, maybe you need to use a different template for specific clients. But the majority of
your templates is the same. There is no need to copy all shared files. `MessageBuilder` 
can use multiple `TemplateResolver` objects. If the first resolver cannot find the
requested template, the second one is asked, then the third and so on. It is is only until
the last resolver cannot find the template before the message building fails:

```
MessageBuilder builder = MessageBuilderFactory.newBuilder()
	.withResolver(
		new DefaultTemplateResolver(new File("/client-1-templates")),
		new DefaultTemplateResolver(new File("/shared-templates")),
	);
```

Please note that resolvers cannot be changed once the message building started. You will
need another `MessageBuilder` when you require different priorities.

## Freemarker Templates
The `FreemarkerMessageBuilder` provides all features of the Freemarker library. You
can use directives, expressions, interpolations and include other templates. Especially latter
will be found using the resolver process described above. This can be useful to extract
shared template parts or use shared sub-templates.

The easiest way to include a sub-template is:

```
<#include "sub-template">
```

However, check whether the `<#import "sub-template>` is more suitable for you as the
Freemarker documentation describes.

Remember that each sub-template will be resolved from scratch, means it can be a more language
specific template or a primary or fallback template regardless where the including template
was loaded from.

# Freemarker Configuration
The default builder will include various configurations such as support for JavaTime classes
(see [java.time support for FreeMarker](https://github.com/lazee/freemarker-java-8)).

Here is the snippet from `FreemarkerMessageBuilder` that configures Freemarker.

```
Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
cfg.setTemplateLoader(getFreemarkerTemplateLoader());
cfg.setDefaultEncoding("UTF-8");
cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
cfg.setLogTemplateExceptions(false);
cfg.setWrapUncheckedExceptions(true);
cfg.setFallbackOnNullLoopVariable(false);
cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
cfg.setCacheStorage(new NullCacheStorage());
cfg.setObjectWrapper(new Java8ObjectWrapper(Configuration.VERSION_2_3_31));
```
