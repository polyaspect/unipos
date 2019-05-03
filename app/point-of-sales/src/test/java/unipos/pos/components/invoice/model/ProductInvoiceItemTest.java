package unipos.pos.components.invoice.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Dominik on 26.01.2016.
 */
public class ProductInvoiceItemTest {

    ProductInvoiceItem burger, schnitzel;
    OrderItemDiscountInvoiceItem einEuroRabatt, zweiEuroRabatt, zuVielRabatt;

    @Before
    public void setUp() throws Exception {
        burger = new ProductInvoiceItem();
        burger.setType(InvoiceItemType.productInvoiceItem);
        burger.setTaxRate(20);
        burger.setQuantity(1);
        burger.setProductNumber("1");
        burger.setPosition(1);
        burger.setLabel("Burger");
        burger.setOrderItemId("burgerOrderItemId");
        burger.setPrice(new BigDecimal("12.99"));

        schnitzel = new ProductInvoiceItem();
        schnitzel.setType(InvoiceItemType.productInvoiceItem);
        schnitzel.setTaxRate(20);
        schnitzel.setQuantity(1);
        schnitzel.setProductNumber("2");
        schnitzel.setPosition(2);
        schnitzel.setLabel("Schnitzel");
        schnitzel.setOrderItemId("schnitzelOrderItemId");
        schnitzel.setPrice(new BigDecimal("9.99"));

        einEuroRabatt = new OrderItemDiscountInvoiceItem();
        einEuroRabatt.setLabel("-1 Euro");
        einEuroRabatt.setPosition(3);
        einEuroRabatt.setReceiverOrderItemId("burgerOrderItemId");
        einEuroRabatt.setType(InvoiceItemType.orderItemDiscountInvoiceItem);
        einEuroRabatt.setDiscount(new BigDecimal("1.00"));
        einEuroRabatt.setDiscountId("einEuroRabatt");

        zweiEuroRabatt = new OrderItemDiscountInvoiceItem();
        zweiEuroRabatt.setLabel("-2 Euro");
        zweiEuroRabatt.setPosition(4);
        zweiEuroRabatt.setReceiverOrderItemId("burgerOrderItemId");
        zweiEuroRabatt.setType(InvoiceItemType.orderItemDiscountInvoiceItem);
        zweiEuroRabatt.setDiscount(new BigDecimal("2.00"));
        zweiEuroRabatt.setDiscountId("zweiEuroRabatt");

        zuVielRabatt = new OrderItemDiscountInvoiceItem();
        zuVielRabatt.setLabel("-50 Euro");
        zuVielRabatt.setPosition(4);
        zuVielRabatt.setReceiverOrderItemId("burgerOrderItemId");
        zuVielRabatt.setType(InvoiceItemType.orderItemDiscountInvoiceItem);
        zuVielRabatt.setDiscount(new BigDecimal("50.00"));
        zuVielRabatt.setDiscountId("zuVielRabatt");

        burger.calcTaxAndGross();
        schnitzel.calcTaxAndGross();
    }

    @Test
    public void testApplyOneDiscount() {
        burger.applyDiscount(einEuroRabatt);

        assertThat(burger.getPrice(), is(new BigDecimal("12.99")));
        assertThat(burger.getTurnoverGross(), is(new BigDecimal("11.99")));
        assertThat(burger.getTurnoverNet(), is(new BigDecimal("9.9917")));
        assertThat(burger.getTax(), is(new BigDecimal("1.9983")));
        assertThat(burger.getDiscounts().size(), is(1));
        assertThat(burger.getDiscounts().get(0), is(Discount.builder().discountId("einEuroRabatt").label("-1 Euro").amount(new BigDecimal("1.00")).type(Discount.Type.LINE).build()));
    }

    @Test
    public void testApplyTwoDiscounts() {
        burger.applyDiscount(einEuroRabatt);

        assertThat(burger.getPrice(), is(new BigDecimal("12.99")));
        assertThat(burger.getTurnoverGross(), is(new BigDecimal("11.99")));
        assertThat(burger.getTurnoverNet(), is(new BigDecimal("9.9917")));
        assertThat(burger.getTax(), is(new BigDecimal("1.9983")));
        assertThat(burger.getDiscounts().size(), is(1));
        assertThat(burger.getDiscounts().get(0), is(Discount.builder().label("-1 Euro").discountId("einEuroRabatt").amount(new BigDecimal("1.00")).type(Discount.Type.LINE).build()));

        burger.applyDiscount(zweiEuroRabatt);

        assertThat(burger.getPrice(), is(new BigDecimal("12.99")));
        assertThat(burger.getTurnoverGross(), is(new BigDecimal("9.99")));
        assertThat(burger.getTurnoverNet(), is(new BigDecimal("8.3250")));
        assertThat(burger.getTax(), is(new BigDecimal("1.6650")));
        assertThat(burger.getDiscounts().size(), is(2));
        assertThat(burger.getDiscounts().get(0), is(Discount.builder().label("-1 Euro").discountId("einEuroRabatt").amount(new BigDecimal("1.00")).type(Discount.Type.LINE).build()));
        assertThat(burger.getDiscounts().get(1), is(Discount.builder().label("-2 Euro").discountId("zweiEuroRabatt").amount(new BigDecimal("2.00")).type(Discount.Type.LINE).build()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyZuVielRabbat() {
        burger.applyDiscount(zuVielRabatt);
    }
}
