# Migration Guide
Congratulation! You decided to migrate from the old jersey-client (v6.0 and below) to the new rest-client implementation.
The advantages are yours: You client code will become independant of any HTTP client implementation and you can
even write your own implementation when Jersey or Spring Boot is not suitable for you.

## A Few Words
The migration process can be tricky. This guide tries to point you to all possible tasks and pitfalls you
might fall into. If you have further remarks, please feel free to contact me and make this framework better.

## 1. Upgrade your pom.xml
Replace the jersey-client dependency by:

```
<dependency>
	<groupId>eu.ralph-schuster</groupId>
	<artifactId>rest-client</artifactId>
	<version>6.1.0</version>
</dependency>
```

You shall also remove the Jackson v2 dependencies. ``rest-client`` will still reference them 
but the are deprecated and will be removed in future versions. See the chapter about Jackson migration
to cleanly prepare for that change.

## 2. Change you Configuration class
Extend now from ``RestClientConfiguration`` and fix any compiler issues.

## 3. Build your AuthorizationStrategy
Encapsulate all authorization issues within an implementation of ``AuthorizationStrategy``.
The ``AbstractAuthorizationStrategy`` implements a default way how to deal with
login, renewal and token lifetimes. You would just need to override specific methods
for your needs.

## 4. Migrate your Main Client

* Remove constructors that use configuration objects or ``WebTarget``
* Add public constructors for ``Target.Builder`` and ``Target``, calling their
  respective super constructors.
* Override ``getUserAgent()`` - this will add the User-Agent header in each request
* Override configureTarget(Target) to further customize your base target, e.g.
  * register your AuthorizationStrategy
  * add common headers or cookies
  * add common query params
  * modify the path
  * register interceptors
  Do not call other methods in your class yet. Use the ``Target`` argument to retrieve your 
  configuration or implementation.
* Replace direct calls to constructors of your sub-clients by ``getClient(SubClient.class)``
* Apply all migration steps from next chapter too.

## 5. Migrate you Sub Clients:
* Remove constructors that use configuration objects
* Add a public constructor for ``Target`` and call e.g. ``super(target.path("sub-path"))``
* Migrate request methods:
  * Replace references of Jersey's Entity by references to RestClient's Entity class.
  * Migrate references of Jersey's MediaType by references to RestClient's MediaType class.
  * Use the pattern: ``getTarget()...<configure-basics>...request().get().as(response-type.class)`` (or alike)
* Your client should  be free of any Jersey-specific class now.
  
## 6. Migrate your Bootstrap 
* Create your configuration using ``RestConfiguration.Builder``
* Create the base ``Target`` using ``Target.Builder``
* Create your main client using ``RestClient.Builder``.

Here is an example:

```
	MyConfiguration configuration = RestClientConfiguration.builder(MyConfiguration.class)
      .with(myUri)
      .verbose(true)
      .build();
   Target.Builder targetBuilder = Target.builder()
      .with(myImpl)
      .with(configuration)
      .register(new CookieInterceptor());
   MyApiClient client = RestClient.builder(MyApiClient.class)
      .with(targetBuilder)
      .build();
```

## 7. Jackson 3 Migration
Migrate to Jackson 3 by searching for "com.fasterxml.jackson" imports. Typical usages are:
* JsonSerialize
* JsonDeserialize
* JsonNaming
* StdSerializer

Usage of com.fasterxml.jackson.annotation is valid for both, Jackson v2 and v3.

## 8. Miscellaneous
* Try to avoid Jackson's ``GenericType`` or ``TypeReference`` for Lists and Sets or other parametrized classes when 
  declaring response types. Use JacksonUtils' ``getListType()``, ``getSetType()``  or ``getMapType()``. For
  more complex types, make use of Jackson's ``jsonMapper.getTypeFactory().constructParametricType()`` method.
* Replace ``Entity.json()`` by ``Json.JSON.toJson()``
* Intensively test your client requests
* Notice that a ``RestResponseException`` is thrown for other than 1xx/2xx responses. Replace occurrences
  of ``NotFoundException`` or ``WebClientException`` and catch the ``RestResponseException`` instead.

   