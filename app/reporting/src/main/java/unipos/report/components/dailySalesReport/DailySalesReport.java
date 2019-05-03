package unipos.report.components.dailySalesReport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.collections.map.HashedMap;
import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import unipos.common.remote.pos.model.Invoice;
import unipos.report.components.Report;
import unipos.report.components.dailySalesLine.DailySalesLine;
import unipos.report.components.dailySalesLine.DailySalesLineService;
import unipos.report.components.shared.helper.DailySalesHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas on 23.08.2016.
 */
@Component
public class DailySalesReport extends Report<DailySalesHelper> {

    @Autowired
    DailySalesLineService dailySalesLineService;
    @Autowired
    DailySalesReportService dailySalesReportService;

    private DailySalesHelper sumSales = new DailySalesHelper();

    @Override
    public List<DailySalesHelper> getDatasource(LocalDate startDate, LocalDate endDate, String storeGuid) {
//        List<DailySalesHelper> dailySalesHelpers = new ArrayList<>();

        List<DailySalesHelper> dailySalesHelpers = generateReportDate(startDate, endDate, storeGuid);
        sumSales.setSum(dailySalesHelpers.stream().map(DailySalesHelper::getSum).reduce(new BigDecimal("0.00"), BigDecimal::add));
        sumSales.setMwst0(dailySalesHelpers.stream().map(DailySalesHelper::getMwst0).reduce(new BigDecimal("0.00"), BigDecimal::add));
        sumSales.setMwst10(dailySalesHelpers.stream().map(DailySalesHelper::getMwst10).reduce(new BigDecimal("0.00"), BigDecimal::add));
        sumSales.setMwst13(dailySalesHelpers.stream().map(DailySalesHelper::getMwst13).reduce(new BigDecimal("0.00"), BigDecimal::add));
        sumSales.setMwst20(dailySalesHelpers.stream().map(DailySalesHelper::getMwst20).reduce(new BigDecimal("0.00"), BigDecimal::add));
        sumSales.setNet0(dailySalesHelpers.stream().map(DailySalesHelper::getNet0).reduce(new BigDecimal("0.00"), BigDecimal::add));
        sumSales.setNet10(dailySalesHelpers.stream().map(DailySalesHelper::getNet10).reduce(new BigDecimal("0.00"), BigDecimal::add));
        sumSales.setNet13(dailySalesHelpers.stream().map(DailySalesHelper::getNet13).reduce(new BigDecimal("0.00"), BigDecimal::add));
        sumSales.setNet20(dailySalesHelpers.stream().map(DailySalesHelper::getNet20).reduce(new BigDecimal("0.00"), BigDecimal::add));

        /*List<DailySalesLine> dailySalesLines = dailySalesLineService.findByStoreGuidAndDateBetween(storeGuid, startDate, endDate);


        dailySalesLines.forEach(dailySalesLine -> {
            DailySalesHelper dailySalesHelper = new DailySalesHelper();
            dailySalesHelper.setSum(dailySalesLine.getSum());
            dailySalesHelper.setDate(dailySalesLine.getDate().toLocalDate());
            dailySalesLine.getTaxes().forEach(taxHelper -> {
                if (taxHelper.getTaxRate() == 0) {
                    dailySalesHelper.setNet0(taxHelper.getNetto());
                }
                if (taxHelper.getTaxRate() == 10) {
                    dailySalesHelper.setNet10(taxHelper.getNetto());
                    dailySalesHelper.setMwst10(taxHelper.getMwst());
                }
                if (taxHelper.getTaxRate() == 13) {
                    dailySalesHelper.setNet13(taxHelper.getNetto());
                    dailySalesHelper.setMwst13(taxHelper.getMwst());
                }
                if (taxHelper.getTaxRate() == 20) {
                    dailySalesHelper.setNet20(taxHelper.getNetto());
                    dailySalesHelper.setMwst20(taxHelper.getMwst());
                }
            });
            dailySalesHelpers.add(dailySalesHelper);
        });*/

        if (dailySalesHelpers.size() == 0) {
            dailySalesHelpers.add(DailySalesHelper.builder().date(LocalDate.now()).mwst0(new BigDecimal("0.00")).mwst10(new BigDecimal("0.00")).mwst13(new BigDecimal("0.00")).mwst20(new BigDecimal("0.00")).net0(new BigDecimal("0.00")).net10(new BigDecimal("0.00")).net13(new BigDecimal("0.00")).net20(new BigDecimal("0.00")).sum(new BigDecimal("0.00")).build());
            sumSales = DailySalesHelper.builder().date(LocalDate.now()).mwst0(new BigDecimal("0.00")).mwst10(new BigDecimal("0.00")).mwst13(new BigDecimal("0.00")).mwst20(new BigDecimal("0.00")).net0(new BigDecimal("0.00")).net10(new BigDecimal("0.00")).net13(new BigDecimal("0.00")).net20(new BigDecimal("0.00")).sum(new BigDecimal("0.00")).build();
        }

        return dailySalesHelpers;
    }

    private List<DailySalesHelper> generateReportDate(LocalDate startDate, LocalDate endDate, String storeGuid) {
        List<DailySalesHelper> dailySalesHelpers = new ArrayList<>();
        while(Period.between(startDate, endDate).getDays() >= 0) {
            DailySalesHelper dailySalesHelper = dailySalesReportService.getDailySales(getInvoices(startDate, startDate, storeGuid));
            dailySalesHelper.setDate(startDate);
            dailySalesHelpers.add(dailySalesHelper);
            startDate = startDate.plusDays(1L);
        }
        return dailySalesHelpers;
    }

    @Override
    public JasperReport getMainReport() throws JRException {
        return JasperCompileManager.compileReport(getClass().getResourceAsStream("/dailySalesReports/dailySalesReport.jrxml"));
    }

    @Override
    public String getFileName() {
        return "DailySalesReport" + new DateTime().toString().hashCode();
    }

    @Override
    public Map<String, Object> additionalParameters() {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("summedValues", sumSales);
        return parameterMap;
    }

    private List<DailySalesHelper> generateReportData(List<Invoice> invoiceList) {
        return null;
    }
}
