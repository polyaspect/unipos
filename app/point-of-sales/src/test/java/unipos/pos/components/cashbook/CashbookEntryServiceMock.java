package unipos.pos.components.cashbook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.pos.components.invoice.InvoiceService;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class CashbookEntryServiceMock {
    @Bean
    public CashbookEntryService cashbookEntryService(){
        return mock(CashbookEntryService.class);
    }
}
