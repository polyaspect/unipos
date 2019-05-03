package unipos.licenseChecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import unipos.licenseChecker.component.CertHandler;
import unipos.licenseChecker.component.LicenseChecker;

/**
 * Created by Dominik on 06.02.2016.
 */

@Configuration
@ComponentScan(basePackages = "unipos.common")
@Import(LicenceCheckerWebConfiguration.class)
public class LicenseCheckerConfig {

    @Bean
    public LicenseChecker licenseChecker() {
        return new LicenseChecker();
    }

    @Bean
    public CertHandler certHandler() {
        return new CertHandler();
    }

}
