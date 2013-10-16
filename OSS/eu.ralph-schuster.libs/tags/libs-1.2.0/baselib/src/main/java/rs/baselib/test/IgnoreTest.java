/**
 * 
 */
package rs.baselib.test;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotates a Java object to be ignored in tests.
 * 
 * This annotation is useful when you want to unit-test objects and classes for specific structures
 * but these objects/classes define additional elements that would cause the test to fail 
 * although they are not relevant for your unit test.
 * <p>
 * E.g. a unit test was written to test all getters/setters in an object. However, there are setters 
 * that are intentionally defined different. This annotation could inform the unit-test that these 
 * methods intentionally do not fulfill the getter/setter requirement and hence shall not be 
 * considered in that unit test.
 * </p>
 * <p>
 * The annotation can hold strings that identify certain types within your unit test. This allows to
 * selectively ignore specific types of tests.
 * </p>
 * <b>Examples:</b><br/>
 * <code>@IgnoreTest</code> - will ignore all tests.<br/>
 * <code>@IgnoreTest("getter")</code> - will ignore only those tests that care about getters.<br/>
 * <code>@IgnoreTest({"getter", "setter"})</code> - will ignore only those tests that care about getters and setters.<br/>
 * <p>
 * Please notice that ignoring tests must be implemented in your unit tests. The annotation can be 
 * checked through {@link IgnoreChecks#ignoreTest(java.lang.reflect.AnnotatedElement, String...)}.
 * </p>
 * @author ralph
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreTest {

	/** The type of tests that the annotated object will be ignored for */
	String[] value() default {};
}
