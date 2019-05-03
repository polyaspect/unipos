package unipos.authChecker.annotations;

import org.springframework.context.annotation.Import;
import unipos.authChecker.config.Configuration;

import java.lang.annotation.*;

/**
 * Created by Dominik on 19.06.2015.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(Configuration.class)
@Documented
public @interface EnableAuthenticationModule {
    String moduleName() default "";
}
