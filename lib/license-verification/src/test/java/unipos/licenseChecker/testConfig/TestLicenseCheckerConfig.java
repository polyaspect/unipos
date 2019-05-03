package unipos.licenseChecker.testConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.licenseChecker.component.CertHandler;
import unipos.licenseChecker.component.LicenseChecker;
import unipos.licenseChecker.component.ModuleName;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 06.02.2016.
 */

@Configuration
public class TestLicenseCheckerConfig {

    @Bean
    public LicenseChecker licenseChecker() {
        return new LicenseChecker();
    }

    @Bean
    public CertHandler certHandler() {
        return new CertHandler();
    }

    @Bean
    public CoreRemoteInterface coreRemoteInterface() {
        return mock(CoreRemoteInterface.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return mock(RestTemplate.class);
    }

    @Bean
    public ModuleName moduleName() {
        return ModuleName.builder().name("unipos-pos").build();
    }
}
