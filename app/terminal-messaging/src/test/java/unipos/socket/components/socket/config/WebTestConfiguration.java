package unipos.socket.components.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.data.DataRemoteInterface;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"unipos.socket.components"}, excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ANNOTATION, value=Service.class)})
public class WebTestConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public SimpMessagingTemplate simpMessagingTemplate() {
        return mock(SimpMessagingTemplate.class);
    }

    @Bean
    public CoreRemoteInterface coreRemoteInterface() {
        return mock(CoreRemoteInterface.class);
    }

    @Bean
    public AuthRemoteInterface authRemoteInterface() {
        return mock(AuthRemoteInterface.class);
    }

    @Bean
    public Environment environment() {
        return mock(Environment.class);
    }

    @Bean
    public LogRemoteInterface logRemoteInterface() {
        return mock(LogRemoteInterface.class);
    }

    @Bean
    public DataRemoteInterface dateRemoteInterface() {
        return mock(DataRemoteInterface.class);
    }
}
