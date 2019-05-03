package unipos.authChecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import unipos.authChecker.domain.AuthTokenManager;

/**
 * Created by Dominik on 25.06.2015.
 */
@org.springframework.context.annotation.Configuration
@EnableWebMvc
public class TestConfiguration {

    @Bean
    public AuthTokenManager authTokenManager() {
        return new AuthTokenManager();
    }
}
