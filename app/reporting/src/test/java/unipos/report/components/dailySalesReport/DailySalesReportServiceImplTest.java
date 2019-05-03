package unipos.report.components.dailySalesReport;

import org.junit.Test;
import org.mockito.InjectMocks;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.InvoiceItem;
import unipos.common.remote.pos.model.ProductInvoiceItem;
import unipos.report.components.shared.helper.DailySalesHelper;
import unipos.report.shared.AbstractServiceTest;
import unipos.report.shared.InvoiceFixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Thomas on 12.02.2016.
 */
public class DailySalesReportServiceImplTest extends AbstractServiceTest{

    @InjectMocks
    DailySalesReportServiceImpl dailySalesReportService = new DailySalesReportServiceImpl();
    InvoiceFixture invoiceFixture = new InvoiceFixture();

    @Test
    public void testGetDailySalesLine() {
        DailySalesHelper dailySalesHelper = dailySalesReportService.getDailySales(invoiceFixture.setUp());

        assertThat(dailySalesHelper.getSum(), is(new BigDecimal("4212.87")));
        assertThat(dailySalesHelper.getNet0(), is(new BigDecimal("6.97")));
        assertThat(dailySalesHelper.getNet10(), is(new BigDecimal("6.33")));
        assertThat(dailySalesHelper.getMwst10(), is(new BigDecimal("0.63")));
        assertThat(dailySalesHelper.getNet13(), is(new BigDecimal("190.25")));
        assertThat(dailySalesHelper.getMwst13(), is(new BigDecimal("24.73")));
        assertThat(dailySalesHelper.getNet20(), is(new BigDecimal("3319.97")));
        assertThat(dailySalesHelper.getMwst20(), is(new BigDecimal("664.00")));

    }

}