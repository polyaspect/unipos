package unipos.printer.components.printer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.printer.components.entity.EntityService;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class PrinterServiceMock {
    @Bean
    public PrinterService printerService(){
        return mock(PrinterService.class);
    }
}
