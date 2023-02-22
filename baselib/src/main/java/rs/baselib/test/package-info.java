/**
 * 	Provides classes for test-related issues.
 * <p>The package provides various builders that can be used to generate (random) test data.</p>
 * <p>Use it e.g. like this:</p>
 * <pre>
 * import static rs.baselib.test.BuilderUtils.*;
 * 
 * PersonBuilder b1        = $Person().withMinAge(21).withMaxAge(35);
 * Person        person    = b1.build;
 * String        firstName = person.firstName;
 * String        lastName  = person.lastName;
 * String        gender    = person.gender;
 * LocalDate     birthday  = person.birthday;
 * int           age       = person.age;
 * 
 * IntBuilder b2 = $Int().withRandom();
 * int random    = b2.build();
 * </pre>
 * <p>You can combine builders to produce lists or arrays:</p>
 * <pre>
 * List&lt;Integer&gt; intList     = listOf(3, $Int().withRandom());
 * Person[]      personArray = arrayOf(5, $Person());
 * </pre>
 */
package rs.baselib.test;

