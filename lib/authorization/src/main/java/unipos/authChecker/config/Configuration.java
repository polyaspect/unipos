package unipos.authChecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import unipos.authChecker.domain.AuthTokenManager;

/**
 * Created by Dominik on 19.06.2015.
 */

@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"unipos.authChecker", "unipos.common"})
public class Configuration{

    @Bean
    public AuthTokenManager authTokenManager() {
        return new AuthTokenManager();
    }
}
