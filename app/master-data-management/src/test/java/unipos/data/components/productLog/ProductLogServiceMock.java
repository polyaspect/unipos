package unipos.data.components.productLog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.data.components.product.ProductService;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class ProductLogServiceMock {

    @Bean
    public ProductLogService productLogService() {
        return mock(ProductLogService.class);
    }

}
