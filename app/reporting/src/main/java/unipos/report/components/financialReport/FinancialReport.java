package unipos.report.components.financialReport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.report.components.Report;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.components.financialReportDay.FinancialReportDayService;
import unipos.report.components.shared.helper.TaxHelper;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * Created by Thomas on 29.08.2016.
 */
@Component
public class FinancialReport extends Report<FinancialReportDay> {

    @Autowired
    FinancialReportDayService financialReportDayService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    FinancialReportService financialReportService;

    private boolean sum;

    public void setSum(boolean sum) {
        this.sum = sum;
    }

    public boolean isSum() {
        return sum;
    }

    @Override
    public List<FinancialReportDay> getDatasource(LocalDate startDate, LocalDate endDate, String storeGuid) {

        FinancialReportDay financialReportDay;
        List<FinancialReportDay> financialReportDays = generateReportData(startDate, endDate, storeGuid);

        Comparator<TaxHelper> comparator = Comparator.comparing(TaxHelper::getTaxRate);

        if (sum) {
            financialReportDay = sumDays(financialReportDays, storeGuid, startDate);
            financialReportDay.getTaxList().sort(comparator);
            financialReportDays = new ArrayList<>(Arrays.asList(financialReportDay));
            return financialReportDays;
        } else {
            if (financialReportDays.size() == 0) {
                financialReportDays.add(FinancialReportDay.builder().date(startDate.atTime(12, 0)).cashStatus(new BigDecimal("0.00")).dailySales(new BigDecimal("0.00")).sumOfSales(new BigDecimal("0.00")).paymentsList(new ArrayList<>()).taxList(new ArrayList<>()).build());
            }
            financialReportDays.forEach(x -> x.getTaxList().sort(comparator));
            return financialReportDays;
        }
    }

    @Override
    public JasperReport getMainReport() throws JRException {
        return JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/financialReports/main_financialReport.jrxml"));
    }

    @Override
    public String getFileName() {
        return "FinancialReport" + new DateTime().toString().hashCode();
    }

    @Override
    @SuppressWarnings("deprecation")
    public Map<String, Object> additionalParameters() throws JRException {
        Map<String, Object> paramMap = new HashMap<>();

        File reportsDir = new File(this.getClass().getResource("/financialReports/").getPath());
        paramMap.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

        JasperReport sub_payments = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/financialReports/sub_paymentsReport.jrxml"));
        JasperReport sub_taxes = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/financialReports/sub_taxReport.jrxml"));
        paramMap.put("subreportParameter", new ArrayList<>(Arrays.asList(sub_payments, sub_taxes)));

        paramMap.put("sum", sum);
        return paramMap;
    }

    private List<FinancialReportDay> generateReportData(LocalDate startDate, LocalDate endDate, String storeGuid) {
        List<FinancialReportDay> reportDays = new ArrayList<>();

        while (Period.between(startDate, endDate).getDays() >= 0) {
            FinancialReportDay financialReportDay = financialReportService.generateFinancialReport(getInvoices(startDate, startDate, storeGuid), dataRemoteInterface.getStoreByGuid(storeGuid));
            financialReportDay.setDate(startDate.atTime(12,0));
            reportDays.add(financialReportDay);
            startDate = startDate.plusDays(1L);
        }
        return reportDays;
    }

    private FinancialReportDay sumDays(List<FinancialReportDay> financialReportDays, String storeGuid, LocalDate startDate) {
        FinancialReportDay financialReportDay;
        if (financialReportDays.size() > 0) {
            FinancialReportDay tmpDay = new FinancialReportDay();
            tmpDay.setStoreGuid(storeGuid);
            tmpDay.setDate(financialReportDays.stream().findFirst().get().getDate());
            tmpDay.setSumOfSales(financialReportDays.stream().map(FinancialReportDay::getSumOfSales).reduce(new BigDecimal("0.00"), BigDecimal::add));
            tmpDay.setDailySales(financialReportDays.stream().map(FinancialReportDay::getDailySales).reduce(new BigDecimal("0.00"), BigDecimal::add));

            financialReportDays.forEach(financialReportDay1 -> {
                financialReportDay1.getPaymentsList().forEach(paymentInvoiceItem -> {
                    boolean result = tmpDay.getPaymentsList().stream().filter(x -> x.getLabel().equals(paymentInvoiceItem.getLabel())).findFirst().map(paymentInvoiceItem1 -> {
                        paymentInvoiceItem1.setTurnover(paymentInvoiceItem1.getTurnover().add(paymentInvoiceItem.getTurnover()));

                        return true;
                    }).isPresent();
                    if (!result) {
                        tmpDay.getPaymentsList().add(paymentInvoiceItem);
                    }
                });

                financialReportDay1.getTaxList().forEach(taxHelper -> {
                    boolean result = tmpDay.getTaxList().stream().filter(x -> x.getTaxRate() == taxHelper.getTaxRate()).findFirst().map(taxHelper1 -> {
                        taxHelper1.setBrutto(taxHelper1.getBrutto().add(taxHelper.getBrutto()));
                        taxHelper1.setNetto(taxHelper1.getNetto().add(taxHelper.getNetto()));
                        taxHelper1.setMwst(taxHelper1.getMwst().add(taxHelper.getMwst()));

                        return true;
                    }).isPresent();
                    if (!result) {
                        tmpDay.getTaxList().add(taxHelper);
                    }
                });

                return;
            });
            financialReportDay = tmpDay;
        } else {
            financialReportDay = FinancialReportDay.builder()
                    .dailySales(new BigDecimal("0.00"))
                    .sumOfSales(new BigDecimal("0.00"))
                    .paymentsList(new ArrayList<>())
                    .taxList(new ArrayList<>())
                    .storeGuid(storeGuid)
                    .date(startDate.atTime(12, 0))
                    .build();
        }
        return financialReportDay;
    }
}