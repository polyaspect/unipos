package unipos.data.components.taxRates;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class TaxRateServiceMock {

    @Bean
    public TaxRateService taxRateService() {
        return mock(TaxRateService.class);
    }

}
