/**
 * 
 */
package rs.baselib.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation of properties that are not allowed to be copied in {@link IBean#copyTo(Object)}. 
 * @author ralph
 *
 */
@Documented
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoCopy {

}
