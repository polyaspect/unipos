package unipos.authChecker.annotations;

import java.lang.annotation.*;

/**
 * Created by Dominik on 19.06.2015.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RequiresPermissions {
    String[] value() default "";
}
