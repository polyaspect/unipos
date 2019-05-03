package unipos.report.components.financialReport;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.shared.AbstractServiceTest;
import unipos.report.shared.InvoiceFixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Thomas on 12.02.2016.
 */
public class FinancialReportServiceImplTest extends AbstractServiceTest {

    @InjectMocks
    FinancialReportServiceImpl financialReportService = new FinancialReportServiceImpl();
    @Mock
    PosRemoteInterface posRemoteInterface;

    InvoiceFixture invoiceFixture = new InvoiceFixture();

    @Test
    public void testGenerateFinancialReport() {

        FinancialReportDay financialReportDay = financialReportService.generateFinancialReport(invoiceFixture.setUp(), null);

        assertThat(financialReportDay, is(notNullValue()));
        assertThat(financialReportDay.getDailySales(), is(new BigDecimal("4212.87")));
        assertThat(financialReportDay.getSumOfSales(), is(new BigDecimal("4200.35")));
        assertThat(financialReportDay.getPaymentsList().size(), is(2));
        assertThat(financialReportDay.getPaymentsList().stream().filter(payment -> payment.getLabel().equals("BAR")).findFirst().get().getTurnover(), is(new BigDecimal("3094.95")));
        assertThat(financialReportDay.getPaymentsList().stream().filter(payment -> payment.getLabel().equals("VISA")).findFirst().get().getTurnover(), is(new BigDecimal("1117.92")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 0).findFirst().get().getBrutto(), is(new BigDecimal("6.97")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 10).findFirst().get().getBrutto(), is(new BigDecimal("6.96")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 10).findFirst().get().getNetto(), is(new BigDecimal("6.33")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 10).findFirst().get().getMwst(), is(new BigDecimal("0.63")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 13).findFirst().get().getBrutto(), is(new BigDecimal("214.97")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 13).findFirst().get().getNetto(), is(new BigDecimal("190.25")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 13).findFirst().get().getMwst(), is(new BigDecimal("24.73")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 20).findFirst().get().getBrutto(), is(new BigDecimal("3983.97")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 20).findFirst().get().getNetto(), is(new BigDecimal("3319.97")));
        assertThat(financialReportDay.getTaxList().stream().filter(taxHelper -> taxHelper.getTaxRate() == 20).findFirst().get().getMwst(), is(new BigDecimal("664.00")));
    }

}