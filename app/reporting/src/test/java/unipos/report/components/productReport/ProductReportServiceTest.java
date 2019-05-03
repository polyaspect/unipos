package unipos.report.components.productReport;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.InvoiceItem;
import unipos.common.remote.pos.model.ProductInvoiceItem;
import unipos.report.components.productReportDay.ProductReportDay;
import unipos.report.components.productReportDay.ProductReportDayServiceImpl;
import unipos.report.components.shared.helper.ProductReportHelper;
import unipos.report.shared.AbstractServiceTest;
import unipos.report.shared.InvoiceFixture;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Thomas on 01.11.2015.
 */
public class ProductReportServiceTest extends AbstractServiceTest {

    @InjectMocks
    ProductReportServiceImpl productReportService = new ProductReportServiceImpl();
    @Mock
    PosRemoteInterface posRemoteInterface;
    @Mock
    ProductReportDayServiceImpl productReportDayService;
    @Mock
            ProductReport productReport;

    InvoiceFixture invoiceFixture = new InvoiceFixture();


    @Test
    public void testProductReportDayWithNoInvoiceItems() throws Exception {

        when(posRemoteInterface.findByCreationDateBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(
                Arrays.asList(Invoice.builder().build())
        );

        List<ProductReportHelper> productReportHelpers = productReportService.getProductInvoiceItems(posRemoteInterface.findByCreationDateBetween(null,null));

        verify(posRemoteInterface, times(1)).findByCreationDateBetween(any(), any());
        assertThat(productReportHelpers.size(), is(0));

    }

    @Test
    public void testProductReportDayWithInvoiceItems() throws Exception {

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

        ProductInvoiceItem schnitzel2 = new ProductInvoiceItem();
        schnitzel2.setLabel("Wiener Schnitzel");
        schnitzel2.setTaxRate(20);
        schnitzel2.setOrderItemId("orderItemId5");
        schnitzel2.setPrice(new BigDecimal("9.99"));
        schnitzel2.setProductNumber("1");
        schnitzel2.setQuantity(1);
        schnitzel2.setTurnoverGross(new BigDecimal("999.99"));
        schnitzel2.setTurnoverNet(new BigDecimal("8.33"));
        schnitzel2.setTax(new BigDecimal("1.66"));
        schnitzel2.setId("documentId5");
        schnitzel2.setPosition(5);

        List<InvoiceItem> invoiceItems = Arrays.asList(schnitzel, pommes, trinken, trinken2, schnitzel2);

        when(posRemoteInterface.findByCreationDateBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(new ArrayList<>(
                Arrays.asList(Invoice.builder().invoiceItems(invoiceItems).build())
        ));

        List<ProductReportHelper> productReportHelpers = productReportService.getProductInvoiceItems(posRemoteInterface.findByCreationDateBetween(null,null));

        verify(posRemoteInterface, times(1)).findByCreationDateBetween(any(), any());
        assertThat(productReportHelpers.size(), is(4));
        assertThat(productReportHelpers.stream().map(ProductReportHelper::getTurnover).reduce(new BigDecimal("0.0"), BigDecimal::add), is(new BigDecimal("3994.95")));
        assertThat(productReportHelpers.stream().filter(x -> x.getProductNumber().equals("1")).findFirst().get().getTurnover(), is(new BigDecimal("1999.98")));

    }

    @Test
    public void testProductReportDayWithMultipleInvoices() throws Exception {
        when(posRemoteInterface.findByCreationDateBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(invoiceFixture.setUp());

        List<ProductReportHelper> productReportHelpers = productReportService.getProductInvoiceItems(posRemoteInterface.findByCreationDateBetween(null,null));

        verify(posRemoteInterface, times(1)).findByCreationDateBetween(any(), any());
        assertThat(productReportHelpers.size(), is(8));
        assertThat(productReportHelpers.stream().map(ProductReportHelper::getTurnover).reduce(new BigDecimal("0.00"), BigDecimal::add), is(new BigDecimal("4212.87")));

    }

    @Test
    public void testGetProductReportEntries() {
        LocalDate date = LocalDate.now();

        ProductReportHelper productReportHelper1 = ProductReportHelper.builder().productNumber("1").label("asdf").contribution(new BigDecimal("1.0")).quantity(new BigDecimal("2.0")).turnover(new BigDecimal("123.45")).build();
        ProductReportHelper productReportHelper2 = ProductReportHelper.builder().productNumber("2").label("qwer").contribution(new BigDecimal("0.3333")).quantity(new BigDecimal("3.0")).turnover(new BigDecimal("456.78")).build();
        ProductReportHelper productReportHelper3 = ProductReportHelper.builder().productNumber("3").label("yxcv").contribution(new BigDecimal("1")).quantity(new BigDecimal("1.0")).turnover(new BigDecimal("123.45")).build();
        ProductReportHelper productReportHelper4 = ProductReportHelper.builder().productNumber("3").label("yxcv").contribution(new BigDecimal("0.3333")).quantity(new BigDecimal("4.0")).turnover(new BigDecimal("489.80")).build();
        ProductReportHelper productReportHelper5 = ProductReportHelper.builder().productNumber("4").label("jkl").contribution(new BigDecimal("0.3333")).quantity(new BigDecimal("1.0")).turnover(new BigDecimal("288.11")).build();

        ProductReportDay productReportDay1 = new ProductReportDay(date, new ArrayList<>(Arrays.asList(productReportHelper1)), new BigDecimal("123.45"), "storeGuid", "userId");
        ProductReportDay productReportDay2 = new ProductReportDay(date.minusMonths(1), new ArrayList<>(Arrays.asList(productReportHelper3)), new BigDecimal("123.45"), "storeGuid", "userId");
        ProductReportDay productReportDay3 = new ProductReportDay(date.plusMonths(2), new ArrayList<>(Arrays.asList(productReportHelper2, productReportHelper4, productReportHelper5)), new BigDecimal("1234.69"), "storeGuid", "userId");

        when(productReportDayService.findByDateBetweenAndStoreGuid(any(LocalDate.class), any(LocalDate.class), any(String.class))).thenReturn(new ArrayList<>(
                Arrays.asList(productReportDay1, productReportDay2, productReportDay3)
        ));

        List<ProductReportHelper> productReportHelpers = productReport.getProductReportEntries(productReportDayService.findByDateBetweenAndStoreGuid(null, null, null));

        verify(productReportDayService, times(1)).findByDateBetweenAndStoreGuid(null, null, null);
        assertThat(productReportHelpers.size(), is(4));
        assertThat(productReportHelpers.stream().map(ProductReportHelper::getTurnover).reduce(new BigDecimal("0.0"), BigDecimal::add), is(new BigDecimal("1481.59")));
        assertThat(productReportHelpers.stream().filter(x -> x.getProductNumber().equals("3")).findFirst().get().getTurnover(), is(new BigDecimal("613.25")));
        assertThat(productReportHelpers.stream().filter(x -> x.getProductNumber().equals("3")).findFirst().get().getQuantity(), is(new BigDecimal("5.0")));
        assertThat(productReportHelpers.stream().map(ProductReportHelper::getContribution).reduce(new BigDecimal("0.00"), BigDecimal::add), is(new BigDecimal("1.000000")));
    }
}