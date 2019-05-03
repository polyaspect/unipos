package unipos.data.components.discountLog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.data.components.paymentMethodLog.PaymentMethodLogService;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class DiscountLogServiceMock {

    @Bean
    public DiscountLogService discountLogService() {
        return mock(DiscountLogService.class);
    }

}
