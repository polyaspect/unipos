package unipos.pos.components.orderItem.visitor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.product.Category;
import unipos.common.remote.data.model.product.Product;
import unipos.common.remote.data.model.product.TaxRate;
import unipos.common.remote.data.model.product.TaxRateCategory;
import unipos.pos.components.invoice.model.*;
import unipos.pos.components.orderItem.model.*;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemToInvoiceMapperVisitor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by dominik on 07.09.15.
 */
public class OrderItemVisitorTest {

    @InjectMocks
    OrderItemToInvoiceMapperVisitor orderItemVisitor = new OrderItemToInvoiceMapperVisitor();
    @Mock
    DataRemoteInterface dataRemoteInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testvisitPaymentOrderItem() throws Exception {
        PaymentOrderItem paymentOrderItem = new PaymentOrderItem();
        paymentOrderItem.setPaymentMethod("paymentMethodDocumentId");
        paymentOrderItem.setId("documentId");
        paymentOrderItem.setOrderItemId("orderItemIdDocumentId");
        paymentOrderItem.setTurnover(new BigDecimal("19.99"));
        paymentOrderItem.setActionId("actionId");
        paymentOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(5));
        paymentOrderItem.setOrderId("orderIdDocumentId");
        paymentOrderItem.setPosition(1);
        paymentOrderItem.setServerReceiveTime(LocalDateTime.now());

        PaymentInvoiceItem paymentInvoiceItem = orderItemVisitor.visit(paymentOrderItem);
        assertThat(paymentInvoiceItem.getId(), is(nullValue()));
        assertThat(paymentInvoiceItem.getPaymentMethod(), is("paymentMethodDocumentId"));
        assertThat(paymentInvoiceItem.getTurnover(), is(new BigDecimal("19.99")));
        assertThat(paymentInvoiceItem.getPosition(), is(1));
    }

    @Test
    public void testVisitProductOrderItem() throws Exception {
        when(dataRemoteInterface.getProductByProductIdentifier(anyString())).thenReturn(Product.builder()
                .category(Category.builder()
                        .id("documentId")
                        .name("Essen")
                        .taxRate(new TaxRate(null,"Essen/Trinken", 20, TaxRateCategory.NORMAL, ""))
                        .build())
                .name("Apfel")
                .description("Apfel Rot Mittel")
                .number(1L)
                .price(new BigDecimal("9.99"))
                .productIdentifier(1L)
                .build());

        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setId("documentId");
        productOrderItem.setQuantity(1);
        productOrderItem.setOrderItemId("orderItemDocumentId");
        productOrderItem.setTurnover(new BigDecimal("9.99"));
        productOrderItem.setServerReceiveTime(LocalDateTime.now());
        productOrderItem.setOrderId("orderDocumentId");
        productOrderItem.setProductNumber("productNumber");
        productOrderItem.setLabel("label");
        productOrderItem.setActionId("actionId");
        productOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(4));
        productOrderItem.setPosition(2);

        ProductInvoiceItem productInvoiceItem = orderItemVisitor.visit(productOrderItem);

        assertThat(productInvoiceItem.getId(), is(nullValue()));
        assertThat(productInvoiceItem.getPosition(), is(2));
        assertThat(productInvoiceItem.getLabel(), is("label"));
        assertThat(productInvoiceItem.getProductNumber(), is("productNumber"));
        assertThat(productInvoiceItem.getQuantity(), is(1));
        assertThat(productInvoiceItem.getTurnoverNet(), is(new BigDecimal("8.3250")));
        assertThat(productInvoiceItem.getTurnoverGross(), is(new BigDecimal("9.99")));
        assertThat(productInvoiceItem.getTax(), is(new BigDecimal("1.6650")));
        assertThat(productInvoiceItem.getTaxRate(), is(20));
    }

    @Test
    public void testVisitProductOrderItem2() throws Exception {
        when(dataRemoteInterface.getProductByProductIdentifier(anyString())).thenReturn(Product.builder()
                .category(Category.builder()
                        .id("documentId")
                        .name("Essen")
                        .taxRate(new TaxRate(null, "Essen/Trinken", 10, TaxRateCategory.NORMAL, ""))
                        .build())
                .name("Apfel")
                .description("Apfel Rot Mittel")
                .number(1L)
                .price(new BigDecimal("15.99"))
                .productIdentifier(1L)
                .build());

        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setId("documentId");
        productOrderItem.setQuantity(1);
        productOrderItem.setOrderItemId("orderItemDocumentId");
        productOrderItem.setTurnover(new BigDecimal("15.99"));
        productOrderItem.setServerReceiveTime(LocalDateTime.now());
        productOrderItem.setOrderId("orderDocumentId");
        productOrderItem.setProductNumber("productNumber");
        productOrderItem.setLabel("label");
        productOrderItem.setActionId("actionId");
        productOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(4));
        productOrderItem.setPosition(2);

        ProductInvoiceItem productInvoiceItem = orderItemVisitor.visit(productOrderItem);

        assertThat(productInvoiceItem.getId(), is(nullValue()));
        assertThat(productInvoiceItem.getPosition(), is(2));
        assertThat(productInvoiceItem.getLabel(), is("label"));
        assertThat(productInvoiceItem.getProductNumber(), is("productNumber"));
        assertThat(productInvoiceItem.getQuantity(), is(1));
        assertThat(productInvoiceItem.getTurnoverNet(), is(new BigDecimal("14.5364")));
        assertThat(productInvoiceItem.getTurnoverGross(), is(new BigDecimal("15.99")));
        assertThat(productInvoiceItem.getTax(), is(new BigDecimal("1.4536")));
        assertThat(productInvoiceItem.getTaxRate(), is(10));
    }

    @Test
    public void testVisitReversalOrderItem() throws Exception {
        ReversalOrderItem reversalOrderItem = new ReversalOrderItem();
        reversalOrderItem.setReason("becauseICan");
        reversalOrderItem.setReceiverOrderItem("orderItemId");
        reversalOrderItem.setOrderItemId("orderItemDocumentId");
        reversalOrderItem.setPosition(3);
        reversalOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(4));
        reversalOrderItem.setActionId("actionId");
        reversalOrderItem.setId("documentId");
        reversalOrderItem.setOrderId("orderDocumentId");
        reversalOrderItem.setServerReceiveTime(LocalDateTime.now());

        ReversalInvoiceItem reversalInvoiceItem = orderItemVisitor.visit(reversalOrderItem);

        assertThat(reversalInvoiceItem.getId(), is(nullValue()));
        assertThat(reversalInvoiceItem.getPosition(), is(3));
        assertThat(reversalInvoiceItem.getReason(), is("becauseICan"));
        assertThat(reversalInvoiceItem.getReceiverOrderItem(), is(notNullValue()));
    }

    @Test
    public void testVisitOrderItemDiscountOrderItem() throws Exception {
        OrderItemDiscountOrderItem orderItemDiscountOrderItem = new OrderItemDiscountOrderItem();
        orderItemDiscountOrderItem.setReceiverOrderItemId("receiverOrderItemId");
        orderItemDiscountOrderItem.setDiscountId("discountDocumentId");
        orderItemDiscountOrderItem.setDiscount(new BigDecimal("10.00"));
        orderItemDiscountOrderItem.setOrderItemId("orderItemId");
        orderItemDiscountOrderItem.setServerReceiveTime(LocalDateTime.now());
        orderItemDiscountOrderItem.setOrderId("orderId");
        orderItemDiscountOrderItem.setActionId("action");
        orderItemDiscountOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(3));
        orderItemDiscountOrderItem.setPosition(1);
        orderItemDiscountOrderItem.setId("documentId");

        OrderItemDiscountInvoiceItem orderItemDiscountInvoiceItem = orderItemVisitor.visit(orderItemDiscountOrderItem);

        assertThat(orderItemDiscountInvoiceItem.getPosition(), is(1));
        assertThat(orderItemDiscountInvoiceItem.getId(), is(nullValue()));
        assertThat(orderItemDiscountInvoiceItem.getDiscount(), is(new BigDecimal("10.00")));
        assertThat(orderItemDiscountInvoiceItem.getReceiverOrderItemId(), is("receiverOrderItemId"));
        assertThat(orderItemDiscountInvoiceItem.getDiscountId(), is("discountDocumentId"));
    }

    @Test
    public void testVisitOrderDiscountInvoiceItem() throws Exception {
        OrderDiscountOrderItem orderDiscountOrderItem = new OrderDiscountOrderItem();
        orderDiscountOrderItem.setReceiverOrderId("receiverOrderId");
        orderDiscountOrderItem.setDiscountId("discountDocumentId");
        orderDiscountOrderItem.setDiscount(new BigDecimal("10.00"));
        orderDiscountOrderItem.setId("documentId");
        orderDiscountOrderItem.setOrderItemId("orderItemId");
        orderDiscountOrderItem.setPosition(3);
        orderDiscountOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(4));
        orderDiscountOrderItem.setServerReceiveTime(LocalDateTime.now());
        orderDiscountOrderItem.setOrderId("orderId");
        orderDiscountOrderItem.setActionId("actionId");

        OrderDiscountInvoiceItem orderDiscountInvoiceItem = orderItemVisitor.visit(orderDiscountOrderItem);

        assertThat(orderDiscountInvoiceItem.getDiscountId(), is("discountDocumentId"));
        assertThat(orderDiscountInvoiceItem.getDiscount(), is(new BigDecimal("10.00")));
        assertThat(orderDiscountInvoiceItem.getPosition(), is(3));
        assertThat(orderDiscountInvoiceItem.getReceiverOrderId(), is("receiverOrderId"));
        assertThat(orderDiscountInvoiceItem.getId(), is(nullValue()));

    }
}
