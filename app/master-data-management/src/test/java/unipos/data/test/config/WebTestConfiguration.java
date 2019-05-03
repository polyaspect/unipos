package unipos.data.test.config;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import unipos.authChecker.domain.AuthTokenManager;
import unipos.authChecker.interceptors.AuthenticationManager;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.sync.SyncRemoteInterface;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"unipos.data.components"}, excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ANNOTATION, value=Service.class)})
public class WebTestConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public RestTemplate restTemplate(){
        return mock(RestTemplate.class);
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return mock(AuthenticationManager.class);
    }

    @Bean
    public AuthTokenManager authTokenManager(){
        return mock(AuthTokenManager.class);
    }

    @Bean
    public CoreRemoteInterface coreRemoteInterface(){
        return mock(CoreRemoteInterface.class);
    }

    @Bean
    public AuthRemoteInterface authRemoteInterface(){
        return mock(AuthRemoteInterface.class);
    }

    @Bean
    public SocketRemoteInterface socketRemoteInterface(){
        return mock(SocketRemoteInterface.class);
    }

    @Bean
    public SyncRemoteInterface syncRemoteInterface(){
        return mock(SyncRemoteInterface.class);
    }
}
