package unipos.pos.components.invoice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import unipos.pos.components.invoice.model.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dominik on 07.09.15.
 */
public class InvoiceItemTest {

    ProductInvoiceItem productInvoiceItem;

    @Before
    public void setUp() throws Exception {
        productInvoiceItem = new ProductInvoiceItem();
    }

    @Test
    public void testCalcTaxStuff1() throws Exception {
        productInvoiceItem.setTurnoverGross(new BigDecimal("29.99"));
        productInvoiceItem.setTaxRate(20);
        productInvoiceItem.setTax(null);
        productInvoiceItem.setTurnoverNet(null);

        productInvoiceItem.calcTaxAndGross();

        assertThat(productInvoiceItem.getTax(), is(new BigDecimal("4.9983")));
        assertThat(productInvoiceItem.getTurnoverNet(), is(new BigDecimal("24.9917")));
    }

    @Test
    public void testCalcTaxStuff2() throws Exception {
        productInvoiceItem.setTurnoverGross(new BigDecimal("99.99"));
        productInvoiceItem.setTaxRate(20);
        productInvoiceItem.setTax(null);
        productInvoiceItem.setTurnoverNet(null);

        productInvoiceItem.calcTaxAndGross();

        assertThat(productInvoiceItem.getTax(), is(new BigDecimal("16.6650")));
        assertThat(productInvoiceItem.getTurnoverNet(), is(new BigDecimal("83.3250")));
    }

    @Test
    public void testCalcTaxStuff3() throws Exception {
        productInvoiceItem.setTurnoverGross(new BigDecimal("359.99"));
        productInvoiceItem.setTaxRate(20);
        productInvoiceItem.setTax(null);
        productInvoiceItem.setTurnoverNet(null);

        productInvoiceItem.calcTaxAndGross();

        assertThat(productInvoiceItem.getTax(), is(new BigDecimal("59.9983")));
        assertThat(productInvoiceItem.getTurnoverNet(), is(new BigDecimal("299.9917")));
    }

    @Test
    public void testCalcTaxStuff4() throws Exception {
        productInvoiceItem.setTurnoverGross(new BigDecimal("319.99"));
        productInvoiceItem.setTaxRate(10);
        productInvoiceItem.setTax(null);
        productInvoiceItem.setTurnoverNet(null);

        productInvoiceItem.calcTaxAndGross();

        assertThat(productInvoiceItem.getTax(), is(new BigDecimal("29.0900")));
        assertThat(productInvoiceItem.getTurnoverNet(), is(new BigDecimal("290.9000")));
    }

    @Test
    public void testCalcTaxStuff5() throws Exception {
        productInvoiceItem.setTurnoverGross(new BigDecimal("95.90"));
        productInvoiceItem.setTaxRate(10);
        productInvoiceItem.setTax(null);
        productInvoiceItem.setTurnoverNet(null);

        productInvoiceItem.calcTaxAndGross();

        assertThat(productInvoiceItem.getTax(), is(new BigDecimal("8.7182")));
        assertThat(productInvoiceItem.getTurnoverNet(), is(new BigDecimal("87.1818")));
    }

    @Test
    public void testGenerateHashAllPropsNullProductInvoiceItem() throws Exception {
        productInvoiceItem.setOrderItemId(null);
        productInvoiceItem.setTurnoverGross(null);
        productInvoiceItem.setTax(null);
        productInvoiceItem.setPosition(0);
        productInvoiceItem.setLabel(null);
        productInvoiceItem.setOrderItemId(null);
        productInvoiceItem.setProductNumber(null);
        productInvoiceItem.setQuantity(0);
        productInvoiceItem.setTaxRate(0);
        productInvoiceItem.setTurnoverNet(null);
        productInvoiceItem.setId(null);

        assertThat(Whitebox.getInternalState(productInvoiceItem, "hash"), is(nullValue()));
        productInvoiceItem.generateHash();
        assertThat(Whitebox.getInternalState(productInvoiceItem, "hash"), is(notNullValue()));
    }

    @Test
    public void testGenerateHashAllPropsNullPaymentInvoiceItem() throws Exception {
        PaymentInvoiceItem paymentInvoiceItem = new PaymentInvoiceItem();
        for(Field field : paymentInvoiceItem.getClass().getFields()) {
            field.setAccessible(true);
            field.set(paymentInvoiceItem, null);
        }

        assertThat(Whitebox.getInternalState(paymentInvoiceItem, "hash"), is(nullValue()));
        paymentInvoiceItem.generateHash();
        assertThat(Whitebox.getInternalState(paymentInvoiceItem, "hash"), is(notNullValue()));
    }

    @Test
    public void testGenerateHashAllPropsNullReverseInvoiceItem() throws Exception {
        ReversalInvoiceItem reversalInvoiceItem = new ReversalInvoiceItem();
        for(Field field : reversalInvoiceItem.getClass().getFields()) {
            field.setAccessible(true);
            field.set(reversalInvoiceItem, null);
        }

        assertThat(Whitebox.getInternalState(reversalInvoiceItem, "hash"), is(nullValue()));
        reversalInvoiceItem.generateHash();
        assertThat(Whitebox.getInternalState(reversalInvoiceItem, "hash"), is(notNullValue()));
    }

    @Test
    public void testGenerateHashAllPropsNullOrderDiscountInvoiceItem() throws Exception {
        OrderDiscountInvoiceItem orderDiscountInvoiceItem = new OrderDiscountInvoiceItem();
        for(Field field : orderDiscountInvoiceItem.getClass().getFields()) {
            field.setAccessible(true);
            field.set(orderDiscountInvoiceItem, null);
        }

        assertThat(Whitebox.getInternalState(orderDiscountInvoiceItem, "hash"), is(nullValue()));
        orderDiscountInvoiceItem.generateHash();
        assertThat(Whitebox.getInternalState(orderDiscountInvoiceItem, "hash"), is(notNullValue()));
    }

    @Test
    public void testGenerateHashAllPropsNullOrderItemDiscountInvoiceItem() throws Exception {
        OrderItemDiscountInvoiceItem orderItemDiscountInvoiceItem = new OrderItemDiscountInvoiceItem();
        for(Field field : orderItemDiscountInvoiceItem.getClass().getFields()) {
            field.setAccessible(true);
            field.set(orderItemDiscountInvoiceItem, null);
        }

        assertThat(Whitebox.getInternalState(orderItemDiscountInvoiceItem, "hash"), is(nullValue()));
        orderItemDiscountInvoiceItem.generateHash();
        assertThat(Whitebox.getInternalState(orderItemDiscountInvoiceItem, "hash"), is(notNullValue()));
    }
}
