package unipos.pos.components.invoice.model;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import unipos.pos.components.invoice.model.reversalInvoice.ReversalInvoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by domin on 25.02.2016.
 */
public class ReversalInvoiceTest {

    Invoice invoice;
    ProductInvoiceItem fallout, blume, schnitzel, teller;
    ReversalInvoiceItem blumeStorno, tellerStorno, falloutStorno;
    OrderDiscountInvoiceItem rechnungsDiscount;
    OrderItemDiscountInvoiceItem falloutRabatt;

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {

        //Products
        fallout = new ProductInvoiceItem();
        fallout.setLabel("Fallout");
        fallout.setOrderItemId("falloutOrderItemId");
        fallout.setPrice(new BigDecimal("59.99"));
        fallout.setProductNumber("1");
        fallout.setQuantity(1);
        fallout.setPosition(1);
        fallout.setTaxRate(20);
        fallout.setType(InvoiceItemType.productInvoiceItem);

        blume = new ProductInvoiceItem();
        blume.setLabel("Blume");
        blume.setOrderItemId("blumenOrderId");
        blume.setPrice(new BigDecimal("9.99"));
        blume.setProductNumber("2");
        blume.setQuantity(1);
        blume.setPosition(2);
        blume.setTaxRate(13);
        blume.setType(InvoiceItemType.productInvoiceItem);

        schnitzel = new ProductInvoiceItem();
        schnitzel.setLabel("Schnitzel");
        schnitzel.setOrderItemId("schnitzelnOrderId");
        schnitzel.setPrice(new BigDecimal("11.99"));
        schnitzel.setProductNumber("3");
        schnitzel.setQuantity(1);
        schnitzel.setPosition(3);
        schnitzel.setTaxRate(10);
        schnitzel.setType(InvoiceItemType.productInvoiceItem);

        //Rechnungsrabatt
        rechnungsDiscount = new OrderDiscountInvoiceItem();
        rechnungsDiscount.setDiscountId("rechnungsStornoId");
        rechnungsDiscount.setDiscount(new BigDecimal("10.00"));
        rechnungsDiscount.setType(InvoiceItemType.orderDiscountInvoiceItem);
        rechnungsDiscount.setReceiverOrderId("orderId");
        rechnungsDiscount.setPosition(8);
        rechnungsDiscount.setOrderItemId(UUID.randomUUID().toString());
        rechnungsDiscount.setLabel("10 Euro Rabatt auf Rechnung");

        //Einzelrabatt
        falloutRabatt = new OrderItemDiscountInvoiceItem();
        falloutRabatt.setType(InvoiceItemType.orderItemDiscountInvoiceItem);
        falloutRabatt.setReceiverOrderItemId("falloutOrderItemId");
        falloutRabatt.setPosition(9);
        falloutRabatt.setLabel("Fallout Rabatt 10 Euro");
        falloutRabatt.setDiscount(new BigDecimal("10.00"));
        falloutRabatt.setOrderItemId(UUID.randomUUID().toString());
        falloutRabatt.setDiscountId("falloutRabattDiscountId");

        teller = new ProductInvoiceItem();
        teller.setLabel("Teller");
        teller.setOrderItemId("tellerOrderId");
        teller.setPrice(new BigDecimal("7.99"));
        teller.setProductNumber("4");
        teller.setQuantity(1);
        teller.setPosition(5);
        teller.setTaxRate(20);
        teller.setType(InvoiceItemType.productInvoiceItem);

        //BonzeilenStorno
        tellerStorno = new ReversalInvoiceItem();
        tellerStorno.setReason("Storno Teller");
        tellerStorno.setType(InvoiceItemType.reversalInvoiceItem);
        tellerStorno.setOrderItemId("tellerStorno");
        tellerStorno.setReversalAmount(new BigDecimal("7.99"));
        tellerStorno.setReceiverOrderItem(teller.getOrderItemId());
        tellerStorno.setPosition(6);

        falloutStorno = new ReversalInvoiceItem();
        falloutStorno.setReason("Storno Fallout");
        falloutStorno.setType(InvoiceItemType.reversalInvoiceItem);
        falloutStorno.setReceiverOrderItem(fallout.getOrderItemId());
        falloutStorno.setOrderItemId("falloutStorno");
        falloutStorno.setReversalAmount(new BigDecimal("59.99"));
        falloutStorno.setPosition(7);
        falloutStorno.setReversalAmount(teller.getPrice());

        blumeStorno = new ReversalInvoiceItem();
        blumeStorno.setReason("Storno Blume");
        blumeStorno.setOrderItemId("blumeStorno");
        blumeStorno.setReversalAmount(new BigDecimal("9.99"));
        blumeStorno.setType(InvoiceItemType.reversalInvoiceItem);
        blumeStorno.setReceiverOrderItem(blume.getOrderItemId());
        blumeStorno.setPosition(4);

        invoice = Invoice.builder()
                .deviceId("dominiksDevice")
                .guid(UUID.randomUUID().toString())
                .creationDate(LocalDateTime.of(2016, 1, 25, 13, 28))
                .invoiceId(1L)
                .hash("hash")
                .orderId("orderId")
                .invoiceItems(Arrays.asList(fallout, blume, schnitzel, blumeStorno))
                .build();

        invoice.calcInvoice();

        String serializedJson = mapper.writeValueAsString(invoice);
    }

    @Test
    public void testCreateReversalInvoiceFromInvoice1() throws Exception {

        ReversalInvoice reversalInvoice = new ReversalInvoice();

        reversalInvoice.createReversalInvoiceFromInvoice(invoice);

        //Assert Invoice
        assertThat(reversalInvoice.getTurnoverGross(), is(new BigDecimal("-71.98")));
        assertThat(reversalInvoice.getTurnoverNet(), is(new BigDecimal("-60.89")));
        assertThat(reversalInvoice.getGuid(), is(notNullValue()));
        assertThat(reversalInvoice.getCashier(), is(invoice.getCashier()));
        assertThat(reversalInvoice.getCompany(), is(invoice.getCompany()));
        assertThat(reversalInvoice.getReversedInvoiceGuid(), is(invoice.getGuid()));
        assertThat(reversalInvoice.getOrderId(), is(invoice.getOrderId()));
        assertThat(reversalInvoice.getInvoiceItems().size(), is(invoice.getInvoiceItems().size()));

        //Cast InvoiceItems
        ProductInvoiceItem first = ((ProductInvoiceItem)reversalInvoice.getInvoiceItems().get(0));
        ProductInvoiceItem second = ((ProductInvoiceItem)reversalInvoice.getInvoiceItems().get(1));
        ProductInvoiceItem third = ((ProductInvoiceItem)reversalInvoice.getInvoiceItems().get(2));
        ReversalInvoiceItem fourth = ((ReversalInvoiceItem) reversalInvoice.getInvoiceItems().get(3));
        TaxInvoiceItem fifth = ((TaxInvoiceItem) reversalInvoice.getInvoiceItems().get(4));
        TaxInvoiceItem sixth = ((TaxInvoiceItem) reversalInvoice.getInvoiceItems().get(5));

        //Assert Invoice Items
        assertThat(first.getTurnoverNet(), is(new BigDecimal("-49.9917")));
        assertThat(first.getTurnoverGross(), is(new BigDecimal("-59.99")));
        assertThat(first.getPrice(), is(new BigDecimal("-59.99")));
        assertThat(first.getTaxRate(), is(20));
        assertThat(first.getTax(), is(new BigDecimal("-9.9983")));
        assertThat(first.getLabel(), is("Fallout"));
        assertThat(first.getDiscounts().size(), is(0));
        assertThat(first.getPosition(), is(1));
        assertThat(first.getQuantity(), is(-1));

        assertThat(second.getTurnoverNet(), is(new BigDecimal("-8.8407")));
        assertThat(second.getTurnoverGross(), is(new BigDecimal("-9.99")));
        assertThat(second.getPrice(), is(new BigDecimal("-9.99")));
        assertThat(second.getTaxRate(), is(13));
        assertThat(second.getTax(), is(new BigDecimal("-1.1493")));
        assertThat(second.getLabel(), is("Blume"));
        assertThat(second.getDiscounts().size(), is(0));
        assertThat(second.getPosition(), is(2));
        assertThat(first.getQuantity(), is(-1));

        assertThat(third.getTurnoverNet(), is(new BigDecimal("-10.9000")));
        assertThat(third.getTurnoverGross(), is(new BigDecimal("-11.99")));
        assertThat(third.getPrice(), is(new BigDecimal("-11.99")));
        assertThat(third.getTaxRate(), is(10));
        assertThat(third.getTax(), is(new BigDecimal("-1.0900")));
        assertThat(third.getLabel(), is("Schnitzel"));
        assertThat(third.getDiscounts().size(), is(0));
        assertThat(third.getPosition(), is(3));
        assertThat(first.getQuantity(), is(-1));

        assertThat(fourth.getPosition(), is(4));
        assertThat(fourth.getReason(), is("Storno Blume"));
        assertThat(fourth.getReceiverOrderItem(), is("blumenOrderId"));
        assertThat(fourth.getReversalAmount(), is(new BigDecimal("-9.99")));

        assertThat(fifth.getPosition(), is(5));
        assertThat(fifth.getTaxRate(), is(10));
        assertThat(fifth.getAmountGross(), is(new BigDecimal("-11.99")));
        assertThat(fifth.getAmountNet(), is(new BigDecimal("-10.90")));
        assertThat(fifth.getAmountTax(), is(new BigDecimal("-1.09")));

        assertThat(sixth.getPosition(), is(6));
        assertThat(sixth.getTaxRate(), is(20));
        assertThat(sixth.getAmountGross(), is(new BigDecimal("-59.99")));
        assertThat(sixth.getAmountNet(), is(new BigDecimal("-49.99")));
        assertThat(sixth.getAmountTax(), is(new BigDecimal("-10.00")));
    }

    @Test
    public void testCreateReversalInvoiceFromInvoiceWithDiscounts() throws Exception {

        invoice.setInvoiceItems(Arrays.asList(fallout, falloutRabatt, schnitzel, rechnungsDiscount));

        invoice.calcInvoice();

        ReversalInvoice reversalInvoice = new ReversalInvoice();

        reversalInvoice.createReversalInvoiceFromInvoice(invoice);

        //Assert Invoice
        assertThat(reversalInvoice.getTurnoverGross(), is(new BigDecimal("-51.98")));
        assertThat(reversalInvoice.getTurnoverNet(), is(new BigDecimal("-44.08")));
        assertThat(reversalInvoice.getGuid(), is(notNullValue()));
        assertThat(reversalInvoice.getCashier(), is(invoice.getCashier()));
        assertThat(reversalInvoice.getCompany(), is(invoice.getCompany()));
        assertThat(reversalInvoice.getReversedInvoiceGuid(), is(invoice.getGuid()));
        assertThat(reversalInvoice.getOrderId(), is(invoice.getOrderId()));
        assertThat(reversalInvoice.getInvoiceItems().size(), is(invoice.getInvoiceItems().size()));

        //Cast InvoiceItems
        ProductInvoiceItem first = ((ProductInvoiceItem)reversalInvoice.getInvoiceItems().get(0));
        ProductInvoiceItem second = ((ProductInvoiceItem)reversalInvoice.getInvoiceItems().get(1));
        OrderDiscountInvoiceItem third = ((OrderDiscountInvoiceItem)reversalInvoice.getInvoiceItems().get(2));
        OrderItemDiscountInvoiceItem fourth = ((OrderItemDiscountInvoiceItem) reversalInvoice.getInvoiceItems().get(3));
        TaxInvoiceItem fifth = ((TaxInvoiceItem) reversalInvoice.getInvoiceItems().get(4));
        TaxInvoiceItem sixth = ((TaxInvoiceItem) reversalInvoice.getInvoiceItems().get(5));

        //Assert Invoice Items
        assertThat(first.getTurnoverNet(), is(new BigDecimal("-34.9333")));
        assertThat(first.getTurnoverGross(), is(new BigDecimal("-41.92")));
        assertThat(first.getPrice(), is(new BigDecimal("-59.99")));
        assertThat(first.getTaxRate(), is(20));
        assertThat(first.getTax(), is(new BigDecimal("-6.9867")));
        assertThat(first.getLabel(), is("Fallout"));
        assertThat(first.getDiscounts().size(), is(2));
        assertThat(first.getDiscounts().get(0).getAmount(), is(new BigDecimal("-10.00")));
        assertThat(first.getDiscounts().get(1).getAmount(), is(new BigDecimal("-8.07")));
        assertThat(first.getPosition(), is(1));
        assertThat(first.getQuantity(), is(-1));

        assertThat(second.getTurnoverNet(), is(new BigDecimal("-9.1455")));
        assertThat(second.getTurnoverGross(), is(new BigDecimal("-10.06")));
        assertThat(second.getPrice(), is(new BigDecimal("-11.99")));
        assertThat(second.getTaxRate(), is(10));
        assertThat(second.getTax(), is(new BigDecimal("-0.9145")));
        assertThat(second.getLabel(), is("Schnitzel"));
        assertThat(second.getDiscounts().size(), is(1));
        assertThat(second.getDiscounts().get(0).getAmount(), is(new BigDecimal("-1.93")));
        assertThat(second.getPosition(), is(3));
        assertThat(second.getQuantity(), is(-1));

        assertThat(third.getPosition(), is(8));
        assertThat(third.getReceiverOrderId(), is("orderId"));
        assertThat(third.getDiscountId(), is("rechnungsStornoId"));
        assertThat(third.getDiscount(), is(new BigDecimal("-10.00")));
        assertThat(third.getLabel(), is(any(String.class)));

        assertThat(fourth.getPosition(), is(9));
        assertThat(fourth.getReceiverOrderItemId(), is("falloutOrderItemId"));
        assertThat(fourth.getDiscountId(), is("falloutRabattDiscountId"));
        assertThat(fourth.getDiscount(), is(new BigDecimal("-10.00")));
        assertThat(fourth.getLabel(), is(any(String.class)));

        assertThat(fifth.getPosition(), is(5));
        assertThat(fifth.getTaxRate(), is(10));
        assertThat(fifth.getAmountGross(), is(new BigDecimal("-10.06")));
        assertThat(fifth.getAmountNet(), is(new BigDecimal("-9.15")));
        assertThat(fifth.getAmountTax(), is(new BigDecimal("-0.91")));

        assertThat(sixth.getPosition(), is(6));
        assertThat(sixth.getTaxRate(), is(20));
        assertThat(sixth.getAmountGross(), is(new BigDecimal("-41.92")));
        assertThat(sixth.getAmountNet(), is(new BigDecimal("-34.93")));
        assertThat(sixth.getAmountTax(), is(new BigDecimal("-6.99")));
    }
}
