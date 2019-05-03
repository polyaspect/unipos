package unipos.pos.components.invoice;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import unipos.pos.components.invoice.model.ProductInvoiceItem;
import unipos.pos.components.orderItem.model.ProductOrderItem;
import unipos.pos.components.shared.ModelMapperGetter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dominik on 04.09.15.
 */

public class InvoiceItemMappingTest {

    ProductOrderItem productOrderItem;
    ModelMapper modelMapper = ModelMapperGetter.getModelMapperWithoutIdMapping();

    @Before
    public void setUp() throws Exception {


        productOrderItem = new ProductOrderItem();
        productOrderItem.setQuantity(1);
        productOrderItem.setOrderId("orderId");
        productOrderItem.setOrderItemId("orderItemId");
        productOrderItem.setTurnover(new BigDecimal("19.99"));
        productOrderItem.setClientCreationDate(LocalDateTime.now());
        productOrderItem.setProductNumber("productNumber");
        productOrderItem.setLabel("Schnitzel groß");
        productOrderItem.setActionId("actionId");
        productOrderItem.setId("id");
        productOrderItem.setServerReceiveTime(LocalDateTime.now());
    }

    @Test
    public void testProductInvoiceItemMapper() throws Exception {
        ProductInvoiceItem productInvoiceItem = modelMapper.map(productOrderItem, ProductInvoiceItem.class);
        assertThat(productInvoiceItem.getId(), is(nullValue()));
    }
}
