package unipos.pos.components.invoice.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Dominik on 25.01.2016.
 */
public class InvoiceTest {

    Invoice invoice;
    ProductInvoiceItem fallout, blume, schnitzel, teller;
    ReversalInvoiceItem blumeStorno, tellerStorno, falloutStorno;
    OrderDiscountInvoiceItem rechnungsDiscount;
    OrderItemDiscountInvoiceItem falloutRabatt;

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
        falloutRabatt.setOrderItemId(UUID.randomUUID().toString());
        falloutRabatt.setDiscount(new BigDecimal("10.00"));
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
        tellerStorno.setReceiverOrderItem(teller.getOrderItemId());
        tellerStorno.setOrderItemId("tellerStorno");
        tellerStorno.setOrderItemId(UUID.randomUUID().toString());
        tellerStorno.setPosition(6);

        falloutStorno = new ReversalInvoiceItem();
        falloutStorno.setReason("Storno Fallout");
        falloutStorno.setType(InvoiceItemType.reversalInvoiceItem);
        falloutStorno.setReceiverOrderItem(fallout.getOrderItemId());
        falloutStorno.setPosition(7);
        falloutStorno.setOrderItemId("falloutStorno");
        falloutStorno.setOrderItemId(UUID.randomUUID().toString());
        falloutStorno.setReversalAmount(teller.getPrice());

        blumeStorno = new ReversalInvoiceItem();
        blumeStorno.setReason("Storno Blume");
        blumeStorno.setOrderItemId("blumeStorno");
        blumeStorno.setType(InvoiceItemType.reversalInvoiceItem);
        blumeStorno.setReceiverOrderItem(blume.getOrderItemId());
        blumeStorno.setPosition(4);

        invoice = Invoice.builder()
                .deviceId("dominiksDevice")
                .guid(UUID.randomUUID().toString())
                .creationDate(LocalDateTime.of(2016, 1, 25, 13, 28))
                .invoiceId(1L)
                .hash("hash")
                .invoiceItems(Arrays.asList(fallout, blume, schnitzel, blumeStorno))
                .build();
    }

    @Test
    public void testCreateInvoice() throws Exception {
        invoice.setInvoiceItems(Arrays.asList(fallout, blume, schnitzel, blumeStorno));

        invoice.calcInvoice();

        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("71.98")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("60.89")));
        assertThat(invoice.getInvoiceItems().size(), is(6));

        assertThat(((ProductInvoiceItem) invoice.getInvoiceItems().get(0)).isReversalApplied(), is(false));
        assertThat(((ProductInvoiceItem) invoice.getInvoiceItems().get(1)).isReversalApplied(), is(true));
        assertThat(((ProductInvoiceItem) invoice.getInvoiceItems().get(2)).isReversalApplied(), is(false));

        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(4)).getTaxRate(), is(10));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(4)).getAmountGross(), is(new BigDecimal("11.99")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(4)).getAmountNet(), is(new BigDecimal("10.90")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(4)).getAmountTax(), is(new BigDecimal("1.09")));

        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(5)).getTaxRate(), is(20));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(5)).getAmountGross(), is(new BigDecimal("59.99")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(5)).getAmountNet(), is(new BigDecimal("49.99")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(5)).getAmountTax(), is(new BigDecimal("10.00")));
    }

    @Test
    public void testCreateInvoice2() throws Exception {

        invoice.setInvoiceItems(Arrays.asList(fallout, blume, schnitzel, blumeStorno, teller, tellerStorno).stream().sorted((x, y) -> y.getPosition() - x.getPosition()).collect(Collectors.toList()));

        invoice.calcInvoice();

        assertThat(((ProductInvoiceItem) invoice.getInvoiceItems().get(0)).isReversalApplied(), is(false));
        assertThat(((ProductInvoiceItem) invoice.getInvoiceItems().get(1)).isReversalApplied(), is(true));
        assertThat(((ProductInvoiceItem) invoice.getInvoiceItems().get(2)).isReversalApplied(), is(false));
        assertThat(((ProductInvoiceItem) invoice.getInvoiceItems().get(4)).isReversalApplied(), is(true));

        assertThat(invoice.getInvoiceItems().size(), is(8));
        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("71.98")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("60.89")));
    }

    public void testCreateInvoice3() throws Exception {
        invoice.setInvoiceItems(Arrays.asList(fallout, blume, schnitzel, blumeStorno, teller, tellerStorno, falloutStorno).stream().sorted((x, y) -> y.getPosition() - x.getPosition()).collect(Collectors.toList()));

        invoice.calcInvoice();

        assertThat(invoice.getInvoiceItems().size(), is(8));
        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("11.99")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("10.90")));
    }

    @Test
    public void testApplyInvoiceDiscount() throws Exception {
        invoice.setInvoiceItems(Arrays.asList(fallout, blume, schnitzel, rechnungsDiscount));

        invoice.calcInvoice();

        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("71.97")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("61.23")));
        assertThat(invoice.getInvoiceItems().size(), is(7));
        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(0))).getTurnoverGross(), is(new BigDecimal("52.67")));
        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(0))).getTurnoverNet(), is(new BigDecimal("43.8917")));
        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(0))).getTax(), is(new BigDecimal("8.7783")));

        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(1))).getTurnoverGross(), is(new BigDecimal("8.77")));
        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(1))).getTurnoverNet(), is(new BigDecimal("7.7611")));
        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(1))).getTax(), is(new BigDecimal("1.0089")));

        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(2))).getTurnoverGross(), is(new BigDecimal("10.53")));
        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(2))).getTurnoverNet(), is(new BigDecimal("9.5727")));
        assertThat(((ProductInvoiceItem) (invoice.getInvoiceItems().get(2))).getTax(), is(new BigDecimal("0.9573")));
    }

    @Test
    public void testApplyInvoiceDiscountAndFalloutDiscount() throws Exception {
        invoice.setInvoiceItems(Arrays.asList(fallout, falloutRabatt, blume, schnitzel, rechnungsDiscount));

        invoice.calcInvoice();

        List<ProductInvoiceItem> productInvoiceItems = invoice.getInvoiceItems().stream().filter(x -> x instanceof ProductInvoiceItem).map(x -> (ProductInvoiceItem) x).collect(Collectors.toList());

        assertThat(productInvoiceItems.stream().map(ProductInvoiceItem::getTurnoverGross).reduce(BigDecimal::add).get().setScale(2, RoundingMode.HALF_UP), is(new BigDecimal("61.97")));
        assertThat(productInvoiceItems.stream().map(ProductInvoiceItem::getTurnoverNet).reduce(BigDecimal::add).get().setScale(2, RoundingMode.HALF_UP), is(new BigDecimal("52.87")));
        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("61.97")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("52.87")));
        assertThat(invoice.getInvoiceItems().size(), is(8));
        assertThat(productInvoiceItems.stream().flatMap(productInvoiceItem -> productInvoiceItem.getDiscounts().stream().filter(pd -> pd.getType() == Discount.Type.INVOICE).map(Discount::getAmount)).reduce(new BigDecimal("0.00"), BigDecimal::add), is(new BigDecimal("10.00")));
        assertThat(productInvoiceItems.stream().flatMap(productInvoiceItem -> productInvoiceItem.getDiscounts().stream().filter(pd -> pd.getType() == Discount.Type.LINE).map(Discount::getAmount)).reduce(new BigDecimal("0.00"), BigDecimal::add), is(new BigDecimal("10.00")));
    }

    @Test
    public void testAssertSparRechnung1() throws Exception {
        ProductInvoiceItem baguette = new ProductInvoiceItem();
        baguette.setLabel("Baguette");
        baguette.setOrderItemId("baguetteOrderItemId");
        baguette.setPrice(new BigDecimal("0.45"));
        baguette.setProductNumber("1");
        baguette.setQuantity(1);
        baguette.setPosition(1);
        baguette.setTaxRate(10);
        baguette.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem baguette2 = new ProductInvoiceItem();
        baguette2.setLabel("Baguette");
        baguette2.setOrderItemId("baguetteOrderItemId");
        baguette2.setPrice(new BigDecimal("0.45"));
        baguette2.setProductNumber("1");
        baguette2.setQuantity(1);
        baguette2.setPosition(2);
        baguette2.setTaxRate(10);
        baguette2.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem striezel = new ProductInvoiceItem();
        striezel.setLabel("Striezel");
        striezel.setOrderItemId("striezelOrderItemId");
        striezel.setPrice(new BigDecimal("1.13"));
        striezel.setProductNumber("2");
        striezel.setQuantity(1);
        striezel.setPosition(3);
        striezel.setTaxRate(10);
        striezel.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem redBull = new ProductInvoiceItem();
        redBull.setLabel("Red Bull 355ml");
        redBull.setOrderItemId("redBull355OrderItemId");
        redBull.setPrice(new BigDecimal("1.59"));
        redBull.setProductNumber("3");
        redBull.setQuantity(1);
        redBull.setPosition(4);
        redBull.setTaxRate(20);
        redBull.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem redBull2 = new ProductInvoiceItem();
        redBull2.setLabel("Red Bull 355ml");
        redBull2.setOrderItemId("redBull355OrderItemId");
        redBull2.setPrice(new BigDecimal("1.59"));
        redBull2.setProductNumber("3");
        redBull2.setQuantity(1);
        redBull2.setPosition(5);
        redBull2.setTaxRate(20);
        redBull2.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem milch = new ProductInvoiceItem();
        milch.setLabel("Milch Laktosefrei");
        milch.setOrderItemId("milchLaktosefreuOrderItemId");
        milch.setPrice(new BigDecimal("1.05"));
        milch.setProductNumber("4");
        milch.setQuantity(1);
        milch.setPosition(6);
        milch.setTaxRate(10);
        milch.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem milch2 = new ProductInvoiceItem();
        milch2.setLabel("Milch Laktosefrei");
        milch2.setOrderItemId("milchLaktosefreuOrderItemId");
        milch2.setPrice(new BigDecimal("1.05"));
        milch2.setProductNumber("4");
        milch2.setQuantity(1);
        milch2.setPosition(7);
        milch2.setTaxRate(10);
        milch2.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem feinkost = new ProductInvoiceItem();
        feinkost.setLabel("Feinkost");
        feinkost.setOrderItemId("Feinkost1OrderItemId");
        feinkost.setPrice(new BigDecimal("0.90"));
        feinkost.setProductNumber("5");
        feinkost.setQuantity(1);
        feinkost.setPosition(8);
        feinkost.setTaxRate(10);
        feinkost.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem feinkost2 = new ProductInvoiceItem();
        feinkost2.setLabel("Feinkost");
        feinkost2.setOrderItemId("Feinkost1OrderItemId");
        feinkost2.setPrice(new BigDecimal("1.82"));
        feinkost2.setProductNumber("5");
        feinkost2.setQuantity(1);
        feinkost2.setPosition(9);
        feinkost2.setTaxRate(10);
        feinkost2.setType(InvoiceItemType.productInvoiceItem);

        Invoice invoice = Invoice.builder()
                .deviceId("dominiksDevice")
                .guid(UUID.randomUUID().toString())
                .creationDate(LocalDateTime.of(2016, 1, 27, 19, 6))
                .invoiceId(1L)
                .hash("hash")
                .invoiceItems(Arrays.asList(baguette, baguette2, striezel, redBull,redBull2,milch,milch,feinkost,feinkost2))
                .build();

        invoice.calcInvoice();

        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("10.03")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("8.88")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(9)).getAmountGross(), is(new BigDecimal("6.85")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(9)).getAmountNet(), is(new BigDecimal("6.23")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(9)).getAmountTax(), is(new BigDecimal("0.62")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(9)).getTaxRate(), is(10));

        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(10)).getAmountGross(), is(new BigDecimal("3.18")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(10)).getAmountNet(), is(new BigDecimal("2.65")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(10)).getAmountTax(), is(new BigDecimal("0.53")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(10)).getTaxRate(), is(20));
    }

    @Test
    public void testProductWithMultipleQuantity() throws Exception {
        ProductInvoiceItem baguette = new ProductInvoiceItem();
        baguette.setLabel("Baguette");
        baguette.setOrderItemId("baguetteOrderItemId");
        baguette.setPrice(new BigDecimal("0.90"));
        baguette.setProductNumber("1");
        baguette.setQuantity(2);
        baguette.setPosition(1);
        baguette.setTaxRate(10);
        baguette.setType(InvoiceItemType.productInvoiceItem);

        invoice.setInvoiceItems(Arrays.asList(baguette));

        invoice.calcInvoice();

        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("0.90")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("0.82")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(1)).getTaxRate(), is(10));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(1)).getAmountGross(), is(new BigDecimal("0.90")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(1)).getAmountNet(), is(new BigDecimal("0.82")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(1)).getAmountTax(), is(new BigDecimal("0.08")));
    }

    @Test
    public void testProductsWithMultipleQuantity() throws Exception {
        ProductInvoiceItem baguette = new ProductInvoiceItem();
        baguette.setLabel("Baguette");
        baguette.setOrderItemId("baguetteOrderItemId");
        baguette.setPrice(new BigDecimal("0.90"));
        baguette.setProductNumber("1");
        baguette.setQuantity(2);
        baguette.setPosition(1);
        baguette.setTaxRate(10);
        baguette.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem milch = new ProductInvoiceItem();
        milch.setLabel("Milch");
        milch.setOrderItemId("milchOrderItemId");
        milch.setPrice(new BigDecimal("6.30"));
        milch.setProductNumber("2");
        milch.setQuantity(6);
        milch.setPosition(2);
        milch.setTaxRate(20);
        milch.setType(InvoiceItemType.productInvoiceItem);

        invoice.setInvoiceItems(Arrays.asList(baguette, milch));

        invoice.calcInvoice();

        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("7.20")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("6.07")));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(2)).getTaxRate(), is(10));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(2)).getAmountGross(), is(new BigDecimal("0.90")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(2)).getAmountNet(), is(new BigDecimal("0.82")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(2)).getAmountTax(), is(new BigDecimal("0.08")));

        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(3)).getTaxRate(), is(20));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountGross(), is(new BigDecimal("6.30")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountNet(), is(new BigDecimal("5.25")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountTax(), is(new BigDecimal("1.05")));
    }

    @Test
    public void testProductsWithMultipleQuantityAndReversalOfOne() throws Exception {
        ProductInvoiceItem baguette = new ProductInvoiceItem();
        baguette.setLabel("Baguette");
        baguette.setOrderItemId("baguetteOrderItemId");
        baguette.setPrice(new BigDecimal("0.90"));
        baguette.setProductNumber("1");
        baguette.setQuantity(2);
        baguette.setPosition(1);
        baguette.setTaxRate(10);
        baguette.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem milch = new ProductInvoiceItem();
        milch.setLabel("Milch");
        milch.setOrderItemId("milchOrderItemId");
        milch.setPrice(new BigDecimal("6.30"));
        milch.setProductNumber("2");
        milch.setQuantity(6);
        milch.setPosition(2);
        milch.setTaxRate(20);
        milch.setType(InvoiceItemType.productInvoiceItem);

        ReversalInvoiceItem milchStorno = new ReversalInvoiceItem();
        milchStorno.setOrderItemId("milchStorno");
        milchStorno.setReason("STORNO Milch");
        milchStorno.setPosition(3);
        milchStorno.setReceiverOrderItem("milchOrderItemId");
        milchStorno.setType(InvoiceItemType.reversalInvoiceItem);

        invoice.setInvoiceItems(Arrays.asList(baguette, milch, milchStorno));

        invoice.calcInvoice();

        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("0.90")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("0.82")));
        assertThat(invoice.getInvoiceItems().size(), is(4));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(3)).getTaxRate(), is(10));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountGross(), is(new BigDecimal("0.90")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountNet(), is(new BigDecimal("0.82")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountTax(), is(new BigDecimal("0.08")));
    }

    @Test
    public void testProductsWithMultipleQuantityAndInvoiceDiscount() throws Exception {
        ProductInvoiceItem baguette = new ProductInvoiceItem();
        baguette.setLabel("Baguette");
        baguette.setOrderItemId("baguetteOrderItemId");
        baguette.setPrice(new BigDecimal("45.00"));
        baguette.setProductNumber("1");
        baguette.setQuantity(100);
        baguette.setPosition(1);
        baguette.setTaxRate(10);
        baguette.setType(InvoiceItemType.productInvoiceItem);

        ProductInvoiceItem milch = new ProductInvoiceItem();
        milch.setLabel("Milch");
        milch.setOrderItemId("milchOrderItemId");
        milch.setPrice(new BigDecimal("6.30"));
        milch.setProductNumber("2");
        milch.setQuantity(6);
        milch.setPosition(2);
        milch.setTaxRate(20);
        milch.setType(InvoiceItemType.productInvoiceItem);



        invoice.setInvoiceItems(Arrays.asList(baguette, milch, rechnungsDiscount));

        invoice.calcInvoice();

        assertThat(invoice.getTurnoverGross(), is(new BigDecimal("41.30")));
        assertThat(invoice.getTurnoverNet(), is(new BigDecimal("37.16")));
        assertThat(invoice.getInvoiceItems().size(), is(5));
        assertThat(((TaxInvoiceItem) invoice.getInvoiceItems().get(3)).getTaxRate(), is(10));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountGross(), is(new BigDecimal("36.23")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountNet(), is(new BigDecimal("32.94")));
        assertThat(((TaxInvoiceItem)invoice.getInvoiceItems().get(3)).getAmountTax(), is(new BigDecimal("3.29")));
    }
}
