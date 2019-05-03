package unipos.pos.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.pos.components.invoice.InvoiceFixture;
import unipos.pos.components.order.OrderFixture;
import unipos.pos.components.orderItem.OrderItemFixture;
import unipos.pos.components.sequence.SequenceFixture;

/**
 * @author ggradnig
 */
@Configuration
public class FixtureConfiguration {

    @Bean
    public OrderFixture orderFixture(){
        return new OrderFixture();
    }

    @Bean
    public OrderItemFixture orderItemFixture() {
        return new OrderItemFixture();
    }

    @Bean
    public InvoiceFixture invoiceFixture() {
        return new InvoiceFixture();
    }

    @Bean
    public SequenceFixture sequenceFixture() {
        return new SequenceFixture();
    }
}
