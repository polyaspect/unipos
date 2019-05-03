package unipos.pos.components.invoice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.pos.components.orderItem.OrderItemService;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class InvoiceServiceMock {
    @Bean
    public InvoiceService invoiceService(){
        return mock(InvoiceService.class);
    }

    @Bean
    public LogRemoteInterface logRemoteInterface() {return mock(LogRemoteInterface.class);}
}
