package unipos.auth.authentication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import unipos.auth.components.authentication.RestClient;
import unipos.auth.components.user.UserServiceMock;

/**
 * Created by dominik on 27.07.15.
 */

@Configuration
@ComponentScan(basePackageClasses = {RestClient.class})
@Import({UserServiceMock.class})
public class RestClientConfiguration {
}
