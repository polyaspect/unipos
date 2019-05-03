package unipos.auth.test.config;

import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import unipos.auth.components.user.UserServiceMock;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiterPinServiceMock;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinService;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinServiceImpl;
import unipos.auth.components.user.usernamePassword.UsernamePasswordServiceMock;
import unipos.auth.config.web.security.RestAuthenticationEntryPoint;
import unipos.common.remote.sync.SyncRemoteInterface;

import static org.mockito.Mockito.mock;


/**
 * @author ggradnig
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"unipos.auth.components"})
@EnableMongoRepositories(basePackages = "unipos.auth.components")
@Import({UserServiceMock.class, UsernamePasswordServiceMock.class, MitarbeiterPinServiceMock.class, MongoTestConfiguration.class})
public class WebTestConfiguration extends WebMvcConfigurationSupport {
        @Bean
        public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
                return new RestAuthenticationEntryPoint();
        }

        @Bean
        public SyncRemoteInterface syncRemoteInterface(){
                return mock(SyncRemoteInterface.class);
        }
}
