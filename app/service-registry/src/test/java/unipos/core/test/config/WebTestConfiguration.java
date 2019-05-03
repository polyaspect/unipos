package unipos.core.test.config;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.licenseChecker.component.CertHandler;
import unipos.licenseChecker.component.LicenseChecker;
import unipos.licenseChecker.component.ModuleName;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"unipos.core.components"}, excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ANNOTATION, value=Service.class)})
public class WebTestConfiguration extends WebMvcConfigurationSupport {
    @Bean
    public LogRemoteInterface logRemoteInterface() {
        return mock(LogRemoteInterface.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public LicenseChecker licenseChecker() {
        return mock(LicenseChecker.class);
    }

    @Bean
    public CertHandler certHandler() {
        return mock(CertHandler.class);
    }

    @Bean
    public ModuleName moduleName() {
        return mock(ModuleName.class);
    }

    @Bean
    public CoreRemoteInterface coreRemoteInterface() {
        return mock(CoreRemoteInterface.class);
    }
}
