# rest-client

## Synopsis
A implementation-agnostic structure for building REST clients.

## Important Notice
This is a replacement of the jersey-client artifact. Read the [MIGRATION.md] to understand how to migrate
your existing clients.

The implementation support both, Jackson v2 and v3. However, Jackson v2 methods are marked as deprecated
and will be removed when the Jersey backend implementation upgraded to Jackson v3.

## Maven Coordinates

```
<dependency>
	<groupId>eu.ralph-schuster</groupId>
	<artifactId>rest-client</artifactId>
	<version>${version}</version>
</dependency>
```

## Usage
You shall subclass your main client for the specific API. Derive from ``RestClient``. Here is an example.

```
public class MyApiClient extends RestClient {

	public MyApiClient(Target.Builder targetBuilder) {
		super(targetBuilder);
	}
	
	public MyApiClient(Target target) {
		super(target);
	}
	
}
```

If you have to access ressources with sub-paths, it is advisable to create separate clients
as sub clients. Again, derive from ``RestClient``. However, only one constructor is sufficient, e.g.

```
public class MySubResourceClient extends RestClient {

	public MySubResourceClient(Target target) {
		super(target.path("/sub-resource"));
	}
	
}
```

and provide access to it in the parent client:

```
public class MyApiClient extends RsRestClient {

	public MySubResourceClient subResource() {
		getClient(MySubResourceClient.class)
	}
	
}
```

You can apply this structure to sub-clients too.

## Bootstrapping

### Create the Configuration
You need a valid instance of ``RestClientConfiguration``. Do not create it directly but use the builder structure:

```
   MyConfiguration config = RestClientConfiguration.builder(MyConfiguration.class)
     .with(myUri)
     .verbose(false)          // default: false
     .with(myJsonMapper)      // default: built by Json or Json2 of jackson sub-project.
     .with(myProxyConfig)     // default: null - SpringBootImpl does not support any proxy
     .build();
```
### Create the Target.Builder
Next, you will need to create the ``Target`` via a specific builder based on your configuration:

```
   Target.Builder targetBuilder = Target.builder()
      .with(myImpl)
      .with(configuration)
      .register(new CookieInterceptor());
```

The builder offers more specific options, but you can customize it better in your main client (see later chapter).

The ``Target.Builder`` will also get the information about your underlying HTTP implementation. You can choose
``SpringBootImpl`` or ``JerseyImpl``, or you create your own implementation if required.

**Important Notice:** Be aware that Jersey depends on Jackson v2 while Spring Boot v4 is already at Jackson v3. This
affects how you write your objects to serialize and deserialize. The ``RestClient`` supports both methods. However,
you shall not mix it or use it with care.

### Create your client
Now you can use the ``RestClient.Builder`` to create an instance of your main client:

```
   MyApiClient client = RestClient.builder(MyApiClient.class)
      .with(targetBuilder)
      .authorizationStrategy(new MyAuthorizationStrategy()) // optional, see later chapter
      .build();
```

It is advisable to not create your main client directly but through the ``Target.Builder``. Future updates
might add features that are available through this builder only.

## Customizing within your main RestClient
You should encapsulate settings of your client within that client if it is not intended to configure it by users
of your client. For this specific reason, you shall override ``configureTarget(Target)``:

```
   protected Target configureTarget(Target target) {
      // Access your configuration
      MyConfiguration config = (MyConfiguration)target.configuration();
      target = super.configureTarget(target)
         .register(myAuthorizationStrategy);
         
      return target;
   }
```

Please notice that ``Target`` is an immutable object and will clone itself with your additional setting.
That's why you need to return the final target that you configured and it will become the base target
of your client.

Also be aware that ``configureTarget(Target)`` is called during construction. Some properties of your
client might not have been initialized at that point. Use the ``Target`` methods to retrieve your
configuration.

## Request Examples

Here are some examples how to implement methods in your clients:

```
	/** Requesting a paged HATEOAS result list using GET method */
	public ResultList<User> list(String search, String sort, Integer page, Integer pageSize) {
		JavaType typeRef = JacksonUtils.getListType(User.class);
		Target target = getTarget(sort, page, pageSize);
		if (search != null) target = target.queryParam("search", search);
		return target.request().get().as(typeRef);
	}

	/** Requesting a single object using GET method */
	public User get(Long id) {
		return getTarget().path(id.toString()).request().get().as(User.class);
	}
	
	/** Creating an object using POST method */
	public User create(User user) {
		return getTarget().request().post(Entity.entity(user, MediaType.APPLICATION_JSON)).as(User.class);
	}
	
	/** Saving an object using PUT method */
	public User save(User user) {
		return getTarget().path(user.getId().toString()).request().put(Entity.entity(user, MediaType.APPLICATION_JSON)).as(User.class);
	}
	
	/** Deleting an object using DELETE method */
	public User delete(long id) {
		return getTarget().path(id.toString()).request().delete().as(User.class);
	}

```

All methods will throw a ``RestResponseException`` when the request failed with 3xx/4xx/5xx status code.

## API Reference

Javadoc API for latest stable version can be accessed [here](https://www.javadoc.io/doc/eu.ralph-schuster/rest-client/latest/index.html).
 
## Contribution

 * [Project Homepage](https://github.com/technicalguru/rslibs/tree/master/rest-client)
 * [Issue Tracker](https://github.com/technicalguru/rslibs/issues)
  
## License

RS Rest Client is free software: you can redistribute it and/or modify it under the terms of version 3 of the GNU 
Lesser General Public  License as published by the Free Software Foundation.

RS Rest Client is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public 
License for more details.

You should have received a copy of the GNU Lesser General Public License along with RS Rest Client.  If not, see 
<http://www.gnu.org/licenses/lgpl-3.0.html>.

Summary:
 1. You are free to use all this code in any private or commercial project. 
 2. You must distribute license and author information along with your project.
 3. You are not required to publish your own source code.
