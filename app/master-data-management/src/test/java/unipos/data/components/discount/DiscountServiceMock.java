package unipos.data.components.discount;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.data.components.discountLog.DiscountLogService;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class DiscountServiceMock {

    @Bean
    public DiscountService discountService() {
        return mock(DiscountService.class);
    }

}
