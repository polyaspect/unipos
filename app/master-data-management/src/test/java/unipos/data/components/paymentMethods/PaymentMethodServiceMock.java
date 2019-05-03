package unipos.data.components.paymentMethods;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.data.components.paymentMethod.PaymentMethodService;
import unipos.data.components.product.ProductService;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class PaymentMethodServiceMock {

    @Bean
    public PaymentMethodService paymentMethodService() {
        return mock(PaymentMethodService.class);
    }

}
