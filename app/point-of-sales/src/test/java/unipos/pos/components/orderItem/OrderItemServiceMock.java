package unipos.pos.components.orderItem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class OrderItemServiceMock {
    @Bean
    public OrderItemService orderItemService(){
        return mock(OrderItemService.class);
    }
}
