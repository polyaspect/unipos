package unipos.report.components.shared;

import unipos.common.remote.data.model.company.Address;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.data.model.product.Category;
import unipos.common.remote.data.model.product.TaxRate;
import unipos.common.remote.data.model.product.TaxRateCategory;
import unipos.common.remote.pos.model.*;
import unipos.report.components.dailySalesReport.DailySalesReportServiceImpl;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.components.productReport.ProductReportService;
import unipos.report.components.productReport.ProductReportServiceImpl;
import unipos.report.components.shared.helper.CategoryReportHelper;
import unipos.report.components.shared.helper.DailySalesHelper;
import unipos.report.components.shared.helper.ProductReportHelper;
import unipos.report.components.shared.helper.TaxHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by dominik on 14.09.15.
 */
public class Main {

    public static Collection<Invoice> getCollection() {
        ProductInvoiceItem schnitzel = new ProductInvoiceItem();
        schnitzel.setLabel("Wiener Schnitzel");
        schnitzel.setTaxRate(20);
        schnitzel.setOrderItemId("orderItemId");
        schnitzel.setPrice(new BigDecimal("9.99"));
        schnitzel.setProductNumber("1");
        schnitzel.setQuantity(1);
        schnitzel.setTurnoverGross(new BigDecimal("999.99"));
        schnitzel.setTurnoverNet(new BigDecimal("8.33"));
        schnitzel.setTax(new BigDecimal("1.66"));
        schnitzel.setId("documentId");
        schnitzel.setPosition(1);

        ProductInvoiceItem pommes = new ProductInvoiceItem();
        pommes.setLabel("Pommes Frites Gross");
        pommes.setTaxRate(20);
        pommes.setOrderItemId("orderItemId2");
        pommes.setPrice(new BigDecimal("1.99"));
        pommes.setProductNumber("2");
        pommes.setQuantity(1);
        pommes.setTurnoverGross(new BigDecimal("1991.99"));
        pommes.setTurnoverNet(new BigDecimal("1.66"));
        pommes.setTax(new BigDecimal("0.33"));
        pommes.setId("documentId2");
        pommes.setPosition(2);

        ProductInvoiceItem trinken = new ProductInvoiceItem();
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

        ProductInvoiceItem trinken2 = new ProductInvoiceItem();
        trinken2.setLabel("Grosse Cola XXL");
        trinken2.setTaxRate(10);
        trinken2.setOrderItemId("orderItemId4");
        trinken2.setPrice(new BigDecimal("1.99"));
        trinken2.setProductNumber("4");
        trinken2.setQuantity(1);
        trinken2.setTurnoverGross(new BigDecimal("1.99"));
        trinken2.setTurnoverNet(new BigDecimal("1.81"));
        trinken2.setTax(new BigDecimal("0.18"));
        trinken2.setId("documentId4");
        trinken2.setPosition(4);

        PaymentInvoiceItem barzahlung = new PaymentInvoiceItem();
        barzahlung.setPosition(5);
        barzahlung.setLabel("BAR");
        barzahlung.setId("documentId3");
        barzahlung.setPaymentMethod("Barzahlung");
        barzahlung.setTurnover(new BigDecimal("14.96"));

        TaxInvoiceItem taxInvoiceItem = new TaxInvoiceItem();
        taxInvoiceItem.setAmountGross(new BigDecimal("5.80"));
        taxInvoiceItem.setAmountNet(new BigDecimal("5.00"));
        taxInvoiceItem.setTaxRate(20);
        taxInvoiceItem.setPosition(6);
        taxInvoiceItem.setAmountTax(new BigDecimal("0.80"));

        TaxInvoiceItem taxInvoiceItem2 = new TaxInvoiceItem();
        taxInvoiceItem2.setAmountGross(new BigDecimal("2.20"));
        taxInvoiceItem2.setAmountNet(new BigDecimal("2.00"));
        taxInvoiceItem2.setTaxRate(10);
        taxInvoiceItem2.setPosition(7);
        taxInvoiceItem2.setAmountTax(new BigDecimal("0.20"));

        List<InvoiceItem> invoiceItemList = new ArrayList<InvoiceItem>(Arrays.asList(schnitzel, pommes, trinken, trinken2, barzahlung,taxInvoiceItem, taxInvoiceItem2));

        Invoice invoice = Invoice.builder()
                .invoiceId(1L)
                .hash("hash")
                .creationDate(LocalDateTime.now())
                .cashier(Cashier.builder().userId("1").name("Dominik").build())
                .company(Company.builder()
                        .name("Unipos IT Solutions")
                        .commercialRegisterNumber("434303g")
                        .uid("ATU69608506")
                        .stores(null)
                        .build())
                .store(Store.builder()
                        .name("Hauptsitz")
                        .storeId(1L)
                        .email("office@unipos.at")
                        .phone("+43 660 638 29 83")
                        .address(Address.builder()
                                .city("Wien")
                                .country("Austria")
                                .postCode(1100)
                                .street("Lafitegasse 26C")
                                .build())
                        .build())
                .turnoverGross(new BigDecimal("14.96"))
                .turnoverNet(new BigDecimal("12.70"))
                .id("1001")
                .hash("hash")
                .invoiceItems(invoiceItemList)
                .build();

        ProductInvoiceItem trinken3 = new ProductInvoiceItem();
        trinken3.setLabel("Wasser");
        trinken3.setTaxRate(10);
        trinken3.setOrderItemId("orderItemId6");
        trinken3.setPrice(new BigDecimal("1.99"));
        trinken3.setProductNumber("6");
        trinken3.setQuantity(1);
        trinken3.setTurnoverGross(new BigDecimal("1.99"));
        trinken3.setTurnoverNet(new BigDecimal("1.81"));
        trinken3.setTax(new BigDecimal("0.18"));
        trinken3.setId("documentId4");
        trinken3.setPosition(1);

        ProductInvoiceItem essen = new ProductInvoiceItem();
        essen.setLabel("Nudeln");
        essen.setTaxRate(10);
        essen.setOrderItemId("orderItemId5");
        essen.setPrice(new BigDecimal("5.00"));
        essen.setProductNumber("5");
        essen.setQuantity(3);
        essen.setTurnoverGross(new BigDecimal("5.00"));
        essen.setTurnoverNet(new BigDecimal("4.00"));
        essen.setTax(new BigDecimal("1.00"));
        essen.setId("documentId1");
        essen.setPosition(2);

        ProductInvoiceItem trinken4 = new ProductInvoiceItem();
        trinken4.setLabel("Grosse Cola XXL");
        trinken4.setTaxRate(10);
        trinken4.setOrderItemId("orderItemId4");
        trinken4.setPrice(new BigDecimal("1.99"));
        trinken4.setProductNumber("4");
        trinken4.setQuantity(1);
        trinken4.setTurnoverGross(new BigDecimal("1.99"));
        trinken4.setTurnoverNet(new BigDecimal("1.81"));
        trinken4.setTax(new BigDecimal("0.18"));
        trinken4.setId("documentId3");
        trinken4.setPosition(3);

        ProductInvoiceItem trinken5 = new ProductInvoiceItem();
        trinken5.setLabel("Grosse Cola XXL");
        trinken5.setTaxRate(10);
        trinken5.setOrderItemId("orderItemId4");
        trinken5.setPrice(new BigDecimal("1.99"));
        trinken5.setProductNumber("4");
        trinken5.setQuantity(1);
        trinken5.setTurnoverGross(new BigDecimal("1.99"));
        trinken5.setTurnoverNet(new BigDecimal("1.81"));
        trinken5.setTax(new BigDecimal("0.18"));
        trinken5.setId("documentId4");
        trinken5.setPosition(4);

        PaymentInvoiceItem barzahlung2 = new PaymentInvoiceItem();
        barzahlung2.setPosition(5);
        barzahlung2.setId("documentId3");
        barzahlung2.setPaymentMethod("Barzahlung");
        barzahlung2.setTurnover(new BigDecimal("10.97"));

        List<InvoiceItem> invoice2ItemList = new ArrayList<>(Arrays.asList(trinken3, essen, trinken4, trinken5, barzahlung2));

        Invoice invoice2 = Invoice.builder()
                .invoiceId(2L)
                .hash("invoice2")
                .creationDate(LocalDateTime.now())
                .cashier(Cashier.builder().userId("2").name("Thomas").build())
                .company(Company.builder()
                        .name("Unipos IT Solutions")
                        .commercialRegisterNumber("AT0001")
                        .uid("UID")
                        .stores(Arrays.asList(Store.builder()
                                .name("Hauptsitz")
                                .storeId(1L)
                                .address(Address.builder()
                                        .city("Vienna")
                                        .country("Austria")
                                        .postCode(1100)
                                        .street("Lafitegasse 26C")
                                        .build())
                                .build()))
                        .build())
                .turnoverGross(new BigDecimal("10.97"))
                .turnoverNet(new BigDecimal("9.57"))
                .id("docId2")
                .invoiceItems(invoice2ItemList)
                .build();

        return new ArrayList<>(Arrays.asList(invoice));
    }

    public static List<DailySalesHelper> getDailySales() {

        ArrayList<Invoice> invoices = new ArrayList<>();
        Iterator j = getCollection().iterator();
        while(j.hasNext()) {
            invoices.add((Invoice)j.next());
        }

        return Arrays.asList(new DailySalesReportServiceImpl().getDailySales(invoices));
    }

    public static List<ProductInvoiceItem> getProductCollection() {

        ArrayList<Invoice> invoices = new ArrayList<>();
        Iterator j = getCollection().iterator();
        while(j.hasNext()) {
            invoices.add((Invoice)j.next());
        }

        List<ProductInvoiceItem> products = new ArrayList<>();
        List<ProductReportHelper> productReportHelpers = new ProductReportServiceImpl().getProductInvoiceItems(invoices);

//        Set set = productMap.entrySet();
//        Iterator i = set.iterator();
//        while (i.hasNext()) {
//            Map.Entry me = (Map.Entry) i.next();
//            if (me.getKey() != "Sum")
//                products.add((ProductInvoiceItem) me.getValue());
//        }

        return products;
    }

    public static Collection<Invoice> getInvoiceItems() {
        ProductInvoiceItem schnitzel = new ProductInvoiceItem();
        schnitzel.setLabel("Wiener Schnitzel");
        schnitzel.setTaxRate(20);
        schnitzel.setOrderItemId("orderItemId");
        schnitzel.setPrice(new BigDecimal("9.99"));
        schnitzel.setProductNumber("1");
        schnitzel.setQuantity(1);
        schnitzel.setTurnoverGross(new BigDecimal("999.99"));
        schnitzel.setTurnoverNet(new BigDecimal("8.33"));
        schnitzel.setTax(new BigDecimal("1.66"));
        schnitzel.setId("documentId");
        schnitzel.setPosition(1);

        ProductInvoiceItem pommes = new ProductInvoiceItem();
        pommes.setLabel("Pommes Frites Gross");
        pommes.setTaxRate(20);
        pommes.setOrderItemId("orderItemId2");
        pommes.setPrice(new BigDecimal("1.99"));
        pommes.setProductNumber("2");
        pommes.setQuantity(1);
        pommes.setTurnoverGross(new BigDecimal("1991.99"));
        pommes.setTurnoverNet(new BigDecimal("1.66"));
        pommes.setTax(new BigDecimal("0.33"));
        pommes.setId("documentId2");
        pommes.setPosition(2);

        ProductInvoiceItem trinken = new ProductInvoiceItem();
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

        ProductInvoiceItem trinken2 = new ProductInvoiceItem();
        trinken2.setLabel("Grosse Cola XXL");
        trinken2.setTaxRate(10);
        trinken2.setOrderItemId("orderItemId4");
        trinken2.setPrice(new BigDecimal("1.99"));
        trinken2.setProductNumber("4");
        trinken2.setQuantity(1);
        trinken2.setTurnoverGross(new BigDecimal("1.99"));
        trinken2.setTurnoverNet(new BigDecimal("1.81"));
        trinken2.setTax(new BigDecimal("0.18"));
        trinken2.setId("documentId4");
        trinken2.setPosition(4);

        PaymentInvoiceItem barzahlung = new PaymentInvoiceItem();
        barzahlung.setPosition(5);
        barzahlung.setLabel("BAR");
        barzahlung.setId("documentId3");
        barzahlung.setPaymentMethod("Barzahlung");
        barzahlung.setTurnover(new BigDecimal("14.96"));

        TaxInvoiceItem taxInvoiceItem = new TaxInvoiceItem();
        taxInvoiceItem.setAmountGross(new BigDecimal("5.80"));
        taxInvoiceItem.setAmountNet(new BigDecimal("5.00"));
        taxInvoiceItem.setTaxRate(20);
        taxInvoiceItem.setPosition(6);
        taxInvoiceItem.setAmountTax(new BigDecimal("0.80"));

        TaxInvoiceItem taxInvoiceItem2 = new TaxInvoiceItem();
        taxInvoiceItem2.setAmountGross(new BigDecimal("2.20"));
        taxInvoiceItem2.setAmountNet(new BigDecimal("2.00"));
        taxInvoiceItem2.setTaxRate(10);
        taxInvoiceItem2.setPosition(7);
        taxInvoiceItem2.setAmountTax(new BigDecimal("0.20"));

        List<InvoiceItem> invoiceItemList = new ArrayList<InvoiceItem>(Arrays.asList(schnitzel, pommes, trinken, trinken2, barzahlung,taxInvoiceItem, taxInvoiceItem2));

        Invoice invoice = Invoice.builder()
                .invoiceId(1L)
                .hash("hash")
                .creationDate(LocalDateTime.now())
                .cashier(Cashier.builder().userId("1").name("Dominik").build())
                .company(Company.builder()
                        .name("Unipos IT Solutions")
                        .commercialRegisterNumber("AT0001")
                        .uid("UID")
                        .stores(null)
                        .build())
                .store(Store.builder()
                        .name("Hauptsitz")
                        .storeId(1L)
                        .address(Address.builder()
                                .city("Vienna")
                                .country("Austria")
                                .postCode(1100)
                                .street("Lafitegasse 26C")
                                .build())
                        .build())
                .turnoverGross(new BigDecimal("14.96"))
                .turnoverNet(new BigDecimal("12.70"))
                .id("documentId")
                .hash("hash")
                .invoiceItems(invoiceItemList)
                .build();

        return new ArrayList<>(Arrays.asList(invoice));
    }

    public static List<ProductReportHelper> getProductReport() {
//        ProductReportService productReportService = new ProductReportServiceImpl();
        return new ArrayList(Arrays.asList(ProductReportHelper.builder().label("Kaese").productNumber("asdf1234").quantity(new BigDecimal("10.00")).contribution(new BigDecimal("0.123456")).build()));
    }

    public static List<CategoryReportHelper> getCategoryReport() {
//        CategoryReportServiceImpl categoryReportService = new CategoryReportServiceImpl();
//        CategoryReportDay categoryReportDay = categoryReportService.getCategoryReportEntries((List)getCollection());
        CategoryReportHelper catHelp1 = CategoryReportHelper.builder().category(new Category().builder().name("Kaese").taxRate(new TaxRate("", "20", 20, TaxRateCategory.NORMAL, "")).build()).quantity(new BigDecimal("5.00")).turnover(new BigDecimal("120.00")).turnoverNet(new BigDecimal("100.00")).turnoverTax(new BigDecimal("20.00")).contribution(new BigDecimal("0.25")).build();
        CategoryReportHelper catHelp2 = CategoryReportHelper.builder().category(new Category().builder().name("Trinken").taxRate(new TaxRate("", "10", 10, TaxRateCategory.NORMAL, "")).build()).quantity(new BigDecimal("4.00")).turnover(new BigDecimal("110.00")).turnoverNet(new BigDecimal("100.00")).turnoverTax(new BigDecimal("10.00")).contribution(new BigDecimal("0.25")).build();
        CategoryReportHelper catHelp3 = CategoryReportHelper.builder().category(new Category().builder().name("Medizin").taxRate(new TaxRate("", "0", 0, TaxRateCategory.NORMAL, "")).build()).quantity(new BigDecimal("21.00")).turnover(new BigDecimal("120.00")).turnoverNet(new BigDecimal("120.00")).turnoverTax(new BigDecimal("0.00")).contribution(new BigDecimal("0.25")).build();
        CategoryReportHelper catHelp4 = CategoryReportHelper.builder().category(new Category().builder().name("Kinokarten").taxRate(new TaxRate("", "13", 13, TaxRateCategory.NORMAL, "")).build()).quantity(new BigDecimal("67.00")).turnover(new BigDecimal("113.00")).turnoverNet(new BigDecimal("100.00")).turnoverTax(new BigDecimal("13.00")).contribution(new BigDecimal("0.25")).build();

        return Arrays.asList(catHelp1, catHelp2, catHelp3, catHelp4);
    }

    public static List<FinancialReportDay> getFinancialReport() {
        PaymentInvoiceItem paymentInvoiceItem = new PaymentInvoiceItem();
        paymentInvoiceItem.setTurnover(new BigDecimal("120.00"));
        paymentInvoiceItem.setLabel("BAR");
        PaymentInvoiceItem paymentInvoiceItem2 = new PaymentInvoiceItem();
        paymentInvoiceItem2.setTurnover(new BigDecimal("110.00"));
        paymentInvoiceItem2.setLabel("VISA");
        return Arrays.asList(FinancialReportDay.builder()
                .date(LocalDate.now().atTime(12,0))
                .sumOfSales(new BigDecimal("200.00"))
                .dailySales(new BigDecimal("200.00"))
                .paymentsMap(new HashMap<>())
                .paymentsList(Arrays.asList(paymentInvoiceItem, paymentInvoiceItem2))
                .taxList(Arrays.asList(TaxHelper.builder()
                        .brutto(new BigDecimal("120.00"))
                        .netto(new BigDecimal("100.00"))
                        .taxRate(20)
                        .mwst(new BigDecimal("20.00"))
                        .build(),
                        TaxHelper.builder()
                        .brutto(new BigDecimal("110.00"))
                        .netto(new BigDecimal("100.00"))
                        .taxRate(10)
                        .mwst(new BigDecimal("10.00"))
                        .build()))
                .build());
    }

//    public static List<ThermalPrinterLine> getLines() {
//        List<ThermalPrinterLine> lines = new ArrayList<>();
//        ThermalPrinterLineBuilder builder = new ThermalPrinterLineBuilder(48);
//
//        builder.setAlignment(0, Alignment.CENTER);
//        builder.setContent("ich will das es funktioniert");
//
//        lines.add(builder.getLine());
//
//        builder.addColumn(1, 2);
//        builder.setColumnContent(0, "ich glaube");
//        builder.setAlignment(0, Alignment.LEFT);
//        builder.setColumnContent(1, "dass es geht");
//        builder.setAlignment(1, Alignment.RIGHT);
//
//        lines.add(builder.getLine());
//        return lines;
//    }

}
