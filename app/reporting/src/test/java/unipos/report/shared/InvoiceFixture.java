package unipos.report.shared;

import unipos.common.remote.data.model.PaymentMethod;
import unipos.common.remote.pos.model.*;
import unipos.report.test.config.Fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas on 12.02.2016.
 */
public class InvoiceFixture {

    private ProductInvoiceItem schnitzel, pommes, trinken, nullSteuersatz, rose, tulpe, pommes2, prodNullSteuersatz, trinken4, kinokarte, reverted;
    private List<InvoiceItem> invoiceItems, invoiceItems2;
    private PaymentInvoiceItem payment1, payment2;
    private ChangeInvoiceItem change;
    private TaxInvoiceItem taxInvoiceItem1for0, taxInvoiceItem1for10, taxInvoiceItem1for13, taxInvoiceItem1for20, taxInvoiceItem2for0, taxInvoiceItem2for10, taxInvoiceItem2for13, taxInvoiceItem2for20;

    public List<Invoice> setUp() {
        schnitzel = new ProductInvoiceItem();
        schnitzel.setLabel("Wiener Schnitzel");
        schnitzel.setTaxRate(20);
        schnitzel.setOrderItemId("orderItemId");
        schnitzel.setPrice(new BigDecimal("999.99"));
        schnitzel.setProductNumber("1");
        schnitzel.setQuantity(100);
        schnitzel.setTurnoverGross(new BigDecimal("999.99"));
        schnitzel.setTurnoverNet(new BigDecimal("833.32"));
        schnitzel.setTax(new BigDecimal("166.67"));
        schnitzel.setId("documentId");
        schnitzel.setPosition(1);

        pommes = new ProductInvoiceItem();
        pommes.setLabel("Pommes Frites Gross");
        pommes.setTaxRate(20);
        pommes.setOrderItemId("orderItemId2");
        pommes.setPrice(new BigDecimal("1991.99"));
        pommes.setProductNumber("2");
        pommes.setQuantity(1001);
        pommes.setTurnoverGross(new BigDecimal("1991.99"));
        pommes.setTurnoverNet(new BigDecimal("1659.99"));
        pommes.setTax(new BigDecimal("332.00"));
        pommes.setId("documentId2");
        pommes.setPosition(2);

        trinken = new ProductInvoiceItem();
        trinken.setLabel("Grosse Cola");
        trinken.setTaxRate(10);
        trinken.setOrderItemId("orderItemId3");
        trinken.setPrice(new BigDecimal("0.99"));
        trinken.setProductNumber("3");
        trinken.setQuantity(1);
        trinken.setTurnoverGross(new BigDecimal("0.99"));
        trinken.setTurnoverNet(new BigDecimal("0.90"));
        trinken.setTax(new BigDecimal("0.09"));
        trinken.setId("documentId3");
        trinken.setPosition(3);

        nullSteuersatz = new ProductInvoiceItem();
        nullSteuersatz.setLabel("Medizin");
        nullSteuersatz.setTaxRate(0);
        nullSteuersatz.setOrderItemId("orderItemId4");
        nullSteuersatz.setPrice(new BigDecimal("1.99"));
        nullSteuersatz.setProductNumber("40");
        nullSteuersatz.setQuantity(1);
        nullSteuersatz.setTurnoverGross(new BigDecimal("1.99"));
        nullSteuersatz.setTurnoverNet(new BigDecimal("1.99"));
        nullSteuersatz.setTax(new BigDecimal("0.0"));
        nullSteuersatz.setId("documentId4");
        nullSteuersatz.setPosition(4);

        rose = new ProductInvoiceItem();
        rose.setLabel("Rose");
        rose.setTaxRate(13);
        rose.setOrderItemId("orderItemId5");
        rose.setPrice(new BigDecimal("99.99"));
        rose.setProductNumber("1");
        rose.setQuantity(10);
        rose.setTurnoverGross(new BigDecimal("99.99"));
        rose.setTurnoverNet(new BigDecimal("88.49"));
        rose.setTax(new BigDecimal("11.50"));
        rose.setId("documentId5");
        rose.setPosition(5);

        payment1 = new PaymentInvoiceItem();
        payment1.setLabel("BAR");
        payment1.setTurnover(new BigDecimal("3100.00"));

        change = new ChangeInvoiceItem();
        change.setLabel("BAR");
        change.setTurnover(new BigDecimal("5.05"));

        taxInvoiceItem1for0 = new TaxInvoiceItem();
        taxInvoiceItem1for0.setTaxRate(0);
        taxInvoiceItem1for0.setAmountGross(new BigDecimal("1.99"));
        taxInvoiceItem1for0.setAmountNet(new BigDecimal("1.99"));
        taxInvoiceItem1for0.setAmountTax(new BigDecimal("0.00"));

        taxInvoiceItem1for10 = new TaxInvoiceItem();
        taxInvoiceItem1for10.setTaxRate(10);
        taxInvoiceItem1for10.setAmountGross(new BigDecimal("0.99"));
        taxInvoiceItem1for10.setAmountNet(new BigDecimal("0.90"));
        taxInvoiceItem1for10.setAmountTax(new BigDecimal("0.09"));

        taxInvoiceItem1for13 = new TaxInvoiceItem();
        taxInvoiceItem1for13.setTaxRate(13);
        taxInvoiceItem1for13.setAmountGross(new BigDecimal("99.99"));
        taxInvoiceItem1for13.setAmountNet(new BigDecimal("88.49"));
        taxInvoiceItem1for13.setAmountTax(new BigDecimal("11.50"));

        taxInvoiceItem1for20 = new TaxInvoiceItem();
        taxInvoiceItem1for20.setTaxRate(20);
        taxInvoiceItem1for20.setAmountGross(new BigDecimal("2991.98"));
        taxInvoiceItem1for20.setAmountNet(new BigDecimal("2493.31"));
        taxInvoiceItem1for20.setAmountTax(new BigDecimal("498.67"));

        tulpe = new ProductInvoiceItem();
        tulpe.setLabel("Tulpe");
        tulpe.setTaxRate(13);
        tulpe.setOrderItemId("orderItemId");
        tulpe.setPrice(new BigDecimal("14.95"));
        tulpe.setProductNumber("10");
        tulpe.setQuantity(5);
        tulpe.setTurnoverGross(new BigDecimal("14.99"));
        tulpe.setTurnoverNet(new BigDecimal("13.27"));
        tulpe.setTax(new BigDecimal("1.73"));
        tulpe.setId("documentId");
        tulpe.setPosition(1);

        pommes2 = new ProductInvoiceItem();
        pommes2.setLabel("Pommes Frites Gross");
        pommes2.setTaxRate(20);
        pommes2.setOrderItemId("orderItemId2");
        pommes2.setPrice(new BigDecimal("979.51"));
        pommes2.setProductNumber("2");
        pommes2.setQuantity(49);
        pommes2.setTurnoverGross(new BigDecimal("991.99"));
        pommes2.setTurnoverNet(new BigDecimal("826.66"));
        pommes2.setTax(new BigDecimal("165.33"));
        pommes2.setId("documentId2");
        pommes2.setPosition(2);

        prodNullSteuersatz = new ProductInvoiceItem();
        prodNullSteuersatz.setLabel("Klopapier");
        prodNullSteuersatz.setTaxRate(0);
        prodNullSteuersatz.setOrderItemId("orderItemId3");
        prodNullSteuersatz.setPrice(new BigDecimal("4.98"));
        prodNullSteuersatz.setProductNumber("15");
        prodNullSteuersatz.setQuantity(2);
        prodNullSteuersatz.setTurnoverGross(new BigDecimal("4.98"));
        prodNullSteuersatz.setTurnoverNet(new BigDecimal("4.98"));
        prodNullSteuersatz.setTax(new BigDecimal("0.0"));
        prodNullSteuersatz.setId("documentId3");
        prodNullSteuersatz.setPosition(3);

        trinken4 = new ProductInvoiceItem();
        trinken4.setLabel("Grosse Cola XXL");
        trinken4.setTaxRate(10);
        trinken4.setOrderItemId("orderItemId4");
        trinken4.setPrice(new BigDecimal("5.97"));
        trinken4.setProductNumber("4");
        trinken4.setQuantity(3);
        trinken4.setTurnoverGross(new BigDecimal("5.97"));
        trinken4.setTurnoverNet(new BigDecimal("5.43"));
        trinken4.setTax(new BigDecimal("0.54"));
        trinken4.setId("documentId4");
        trinken4.setPosition(4);

        kinokarte = new ProductInvoiceItem();
        kinokarte.setLabel("Kinokarte");
        kinokarte.setTaxRate(13);
        kinokarte.setOrderItemId("orderItemId5");
        kinokarte.setPrice(new BigDecimal("99.99"));
        kinokarte.setProductNumber("25");
        kinokarte.setQuantity(10);
        kinokarte.setTurnoverGross(new BigDecimal("99.99"));
        kinokarte.setTurnoverNet(new BigDecimal("88.49"));
        kinokarte.setTax(new BigDecimal("11.50"));
        kinokarte.setId("documentId5");
        kinokarte.setPosition(5);

        reverted = new ProductInvoiceItem();
        reverted.setLabel("Storniert");
        reverted.setTaxRate(10);
        reverted.setOrderItemId("orderItemId6");
        reverted.setPrice(new BigDecimal("99.99"));
        reverted.setProductNumber("100");
        reverted.setQuantity(10);
        reverted.setTurnoverGross(new BigDecimal("99.99"));
        reverted.setTurnoverNet(new BigDecimal("90.00"));
        reverted.setTax(new BigDecimal("9.99"));
        reverted.setId("documentId6");
        reverted.setPosition(6);
        reverted.setReversalApplied(true);

        payment2 = new PaymentInvoiceItem();
        payment2.setLabel("VISA");
        payment2.setTurnover(new BigDecimal("1117.92"));

        taxInvoiceItem2for0 = new TaxInvoiceItem();
        taxInvoiceItem2for0.setTaxRate(0);
        taxInvoiceItem2for0.setAmountGross(new BigDecimal("4.98"));
        taxInvoiceItem2for0.setAmountNet(new BigDecimal("4.98"));
        taxInvoiceItem2for0.setAmountTax(new BigDecimal("0.00"));

        taxInvoiceItem2for10 = new TaxInvoiceItem();
        taxInvoiceItem2for10.setTaxRate(10);
        taxInvoiceItem2for10.setAmountGross(new BigDecimal("5.97"));
        taxInvoiceItem2for10.setAmountNet(new BigDecimal("5.43"));
        taxInvoiceItem2for10.setAmountTax(new BigDecimal("0.54"));

        taxInvoiceItem2for13 = new TaxInvoiceItem();
        taxInvoiceItem2for13.setTaxRate(13);
        taxInvoiceItem2for13.setAmountGross(new BigDecimal("114.98"));
        taxInvoiceItem2for13.setAmountNet(new BigDecimal("101.76"));
        taxInvoiceItem2for13.setAmountTax(new BigDecimal("13.23"));

        taxInvoiceItem2for20 = new TaxInvoiceItem();
        taxInvoiceItem2for20.setTaxRate(20);
        taxInvoiceItem2for20.setAmountGross(new BigDecimal("991.99"));
        taxInvoiceItem2for20.setAmountNet(new BigDecimal("826.66"));
        taxInvoiceItem2for20.setAmountTax(new BigDecimal("165.33"));

        invoiceItems = new ArrayList<>(Arrays.asList(schnitzel, pommes, trinken, nullSteuersatz, rose, payment1, change, taxInvoiceItem1for0, taxInvoiceItem1for10, taxInvoiceItem1for13, taxInvoiceItem1for20));
        invoiceItems2 = new ArrayList<>(Arrays.asList(tulpe, pommes2, prodNullSteuersatz, trinken4, kinokarte, reverted, payment2, taxInvoiceItem2for0, taxInvoiceItem2for10, taxInvoiceItem2for13, taxInvoiceItem2for20));



        return Arrays.asList(Invoice.builder().invoiceItems(invoiceItems).turnoverGross(new BigDecimal("3094.95")).build(), Invoice.builder().invoiceItems(invoiceItems2).turnoverGross(new BigDecimal("1117.92")).build());
    }
}
