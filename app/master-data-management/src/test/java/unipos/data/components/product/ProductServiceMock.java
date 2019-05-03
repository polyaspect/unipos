package unipos.data.components.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class ProductServiceMock {

    @Bean
    public ProductService productService() {
        return mock(ProductService.class);
    }

}
