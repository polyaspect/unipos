package unipos.authChecker.annotations;

import java.lang.annotation.*;

/**
 * Created by domin on 07.06.2016.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RequiresAuthentication {
}
