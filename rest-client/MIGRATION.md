Steps to migrate to RestClient

1. pom.xml / Replace jersey-client by rest-client
2. ggf. Jackson 2 -> Jackson 3 updates
3. JerseyClientConfig -> extend RestClientConfiguration
   - create a (static) build() method to return configuration from RestClientConfiguration.builder()
4. Create your own or decide on using an existing AuthorizationStrategy
5. Migrate your main client
   - remove constructors using configuration
   - add public constructors for Target.Builder and Target, just call super(builder) or super(target)
   - Override getUserAgent() if required
   - override configureTarget(Target) to adjust, e.g.
     - set AuthorizationStrategy
	 - add common headers or cookies
	 - add common query params
	 - modify the path
	 - register interceptors
	 - do not call other methods in class yet
	 - use target to retrieve your configuration or implementation
	 - replace direct calls to constructors of your sub-clients by getClient(SubClient.class)
	 - follow Sub-client migration in case request methods are provided
6. Migrate you sub clients:
   - remove constructors using configuration
   - add public constructors for Target and call super(target.path(...))
   - migrate request methods:
	 - Migrate references of jersey's Entity to RestClient's Entity class.
	 - Migrate references of jersey's MediaType to RestClient's MediaType class.
     - Use the pattern: getTarget()....<configure-basics>.request().get().as() (or alike)
7. Migrate your bootstrap to create your client configuration and main client
   - Create config using RestConfiguration.Builder
   - Create base Target using Target.Builder
   - Create client using RestClient.Builder