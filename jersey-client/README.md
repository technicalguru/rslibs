# jersey-client
A basic structure for building clients with jersey.

## Maven Coordinates

```
<dependency>
	<groupId>eu.ralph-schuster</groupId>
	<artifactId>jersey-client</artifactId>
	<version>2.1.3</version>
</dependency>
```

## Usage
You shall subclass your main client for the specific API. Derive from ``JerseyClient``. Here is an example.

```
public class MyApiClient extends JerseyClient {

	public MyApiClient(JerseyClientConfig config) {
		super(config);
	}
	
}
```

If you have to access ressources with sub-paths, it is advisable to to create separate clients
as sub clients. Always derive from ``AbstractClient``.  e.g.

```
public class MySubResourceClient extends AbstractClient {

	public MySubResourceClient(WebTarget target) {
		super(target.path("/sub-resource"));
	}
	
}
```

and provide access to it in the parent client:

```
public class MyApiClient extends JerseyClient {

	public MySubResourceClient subResource() {
		get(MySubResourceClient.class)
	}
	
}
```

You can apply this structure to sub-clients too.

## Configuration
You are free to subclass ``JerseyClientConfig`` to provide more configuration specific to your use case.
Override the ``configure()`` method to do your configuration:

```
public class MyApiClient extends JerseyClient {

	public MyApiClient(MyDerivedConfig config) {
		super(config);
	}

	@Override
	protected void configure(MyDerivedConfig config, ClientFilter clientFilter) {
		super.configure(config, clientFilter);
		
		// ... do your stuff here
	}
	
}
```
 
## Request Examples

Here are some examples how to implement methods in your clients:

```
	/** Requesting a paged HATEOAS result list using GET method */
	public ResultList<User> list(String search, String sort, Integer page, Integer pageSize) {
		GenericType<HateOasPagedList<User>> typeRef = new GenericType<HateOasPagedList<User>>() {};
		WebTarget target = getTarget(sort, page, pageSize);
		if (search != null) target = target.queryParam("search", search);
		return getResults(target.request().get(typeRef));
	}

	/** Requesting a single object using GET method */
	public User get(Long id) {
		return getTarget().path(id.toString()).request().get(User.class);
	}
	
	/** Creating an object using POST method */
	public User create(User user) {
		return getTarget().request().post(Entity.entity(user, MediaType.APPLICATION_JSON), User.class);
	}
	
	/** Saving an object using PUT method */
	public User save(User user) {
		return getTarget().path(user.getId().toString()).request().put(Entity.entity(user, MediaType.APPLICATION_JSON), User.class);
	}
	
	/** Deleting an object using DELETE method */
	public User delete(long id) {
		return getTarget().path(id.toString()).request().delete(User.class);
	}

```

## API Reference

Javadoc API for latest stable version can be accessed [here](https://www.javadoc.io/doc/eu.ralph-schuster/jersey-client/latest//index.html).

## Important Changes

 * v2.1 requires Java 21 now
 * v2 is using Jakarta libraries - however, v2.0.0 was a broken release in that respect.
 
## Contribution

 * [Project Homepage](https://github.com/technicalguru/jersey-client)
 * [Issue Tracker](https://github.com/technicalguru/jersey-client/issues)
  
## License

Jersey Client is free software: you can redistribute it and/or modify it under the terms of version 3 of the GNU 
Lesser General Public  License as published by the Free Software Foundation.

Jersey Client is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public 
License for more details.

You should have received a copy of the GNU Lesser General Public License along with Jersey Client.  If not, see 
<http://www.gnu.org/licenses/lgpl-3.0.html>.

Summary:
 1. You are free to use all this code in any private or commercial project. 
 2. You must distribute license and author information along with your project.
 3. You are not required to publish your own source code.
