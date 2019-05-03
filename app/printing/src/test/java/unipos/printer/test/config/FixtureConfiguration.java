package unipos.printer.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.printer.components.printer.PrinterFixture;

/**
 * @author ggradnig
 */
@Configuration
public class FixtureConfiguration {

    @Bean
    public PrinterFixture printerFixture() {
        return new PrinterFixture();
    }
}
