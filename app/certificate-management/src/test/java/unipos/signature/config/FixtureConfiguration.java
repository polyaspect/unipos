package unipos.signature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.signature.components.signature.SignatureFixture;
import unipos.signature.components.signatureOption.SignatureOptionFixture;
import unipos.signature.components.signatureResult.SignatureResultFixture;
import unipos.signature.components.umsatzZaehler.UmsatzZaehlerFixture;

/**
 * @author ggradnig
 */
@Configuration
public class FixtureConfiguration {

    @Bean
    public SignatureResultFixture signatureResultFixture() {
        return new SignatureResultFixture();
    }

    @Bean
    public SignatureFixture signatureFixture() {
        return new SignatureFixture();
    }

    @Bean
    public UmsatzZaehlerFixture umsatzZaehlerFixture() {
        return new UmsatzZaehlerFixture();
    }

    @Bean
    public SignatureOptionFixture signatureOptionFixture() {
        return new SignatureOptionFixture();
    }
}
