package unipos.data.components.company;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.data.components.discount.DiscountService;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class CompanyServiceMock {

    @Bean
    public CompanyService companyService() {
        return mock(CompanyService.class);
    }

}
