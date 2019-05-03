package unipos.data.components.paymentMethodLog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.data.components.paymentMethod.PaymentMethodService;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class PaymentMethodLogServiceMock {

    @Bean
    public PaymentMethodLogService paymentMethodLogService() {
        return mock(PaymentMethodLogService.class);
    }

}
