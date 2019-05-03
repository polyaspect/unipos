package unipos.report.components.categoryReport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unipos.common.remote.data.model.product.Category;
import unipos.common.remote.data.model.product.TaxRate;
import unipos.common.remote.pos.model.Invoice;
import unipos.report.components.Report;
import unipos.report.components.categoryReportDay.CategoryReportDay;
import unipos.report.components.categoryReportDay.CategoryReportDayService;
import unipos.report.components.shared.helper.CategoryReportHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 29.08.2016.
 */
@Component
public class CategoryReport extends Report<CategoryReportHelper> {

    @Autowired
    CategoryReportDayService categoryReportDayService;
    @Autowired
    CategoryReportService categoryReportService;

    @Override
    public List<CategoryReportHelper> getDatasource(LocalDate startDate, LocalDate endDate, String storeGuid) {

        List<CategoryReportDay> categoryReportDays = generateReportData(startDate, endDate, storeGuid);
        List<CategoryReportHelper> categoryReportHelpers = new ArrayList<>();
        categoryReportDays.forEach(categoryReportDay -> categoryReportDay.getCategories().forEach(categoryReportHelper -> {
            boolean result = categoryReportHelpers.stream().filter(x -> x.getCategory().equals(categoryReportHelper.getCategory())).findFirst().map(categoryRepHelper -> {
                categoryRepHelper.setTurnover(categoryRepHelper.getTurnover().add(categoryReportHelper.getTurnover()));
                categoryRepHelper.setTurnoverNet(categoryRepHelper.getTurnoverNet().add(categoryReportHelper.getTurnoverNet()));
                categoryRepHelper.setTurnoverTax(categoryRepHelper.getTurnoverTax().add(categoryReportHelper.getTurnoverTax()));
                categoryRepHelper.setQuantity(categoryRepHelper.getQuantity().add(categoryReportHelper.getQuantity()));

                return true;
            }).isPresent();
            if (!result) {
                categoryReportHelpers.add(CategoryReportHelper.builder().turnover(categoryReportHelper.getTurnover()).turnoverNet(categoryReportHelper.getTurnoverNet()).turnoverTax(categoryReportHelper.getTurnoverTax()).quantity(categoryReportHelper.getQuantity()).category(categoryReportHelper.getCategory()).build());
            }
        }));

        BigDecimal sum = categoryReportHelpers.stream().map(CategoryReportHelper::getTurnover).reduce(new BigDecimal("0.0"), BigDecimal::add);
        categoryReportHelpers.forEach(categoryReportHelper -> {
            if (!sum.equals(BigDecimal.ZERO)) {
                categoryReportHelper.setContribution(categoryReportHelper.getTurnover().divide(sum, 6, BigDecimal.ROUND_HALF_UP));
            } else {
                categoryReportHelper.setContribution(new BigDecimal("0.0"));
            }
        });

        if (categoryReportHelpers.size() == 0) {
            categoryReportHelpers.add(CategoryReportHelper.builder().category(Category.builder().name("").taxRate(new TaxRate("", "", 0, null, "")).build()).quantity(new BigDecimal("0.0")).turnover(new BigDecimal("0.0")).turnoverTax(new BigDecimal("0.0")).turnoverNet(new BigDecimal("0.0")).build());
        }

        return categoryReportHelpers.stream().sorted(Comparator.comparing(c -> c.getCategory().getName())).collect(Collectors.toList());
    }

    private List<CategoryReportDay> generateReportData(LocalDate startDate, LocalDate endDate, String storeGuid) {
        List<CategoryReportDay> categoryReportDays = new ArrayList<>();
        while (Period.between(startDate, endDate).getDays() >= 0) {
            categoryReportDays.add(categoryReportService.getCategoryReportEntries(getInvoices(startDate, startDate, storeGuid)));

            startDate = startDate.plusDays(1L);
        }
        return categoryReportDays;
    }

    @Override
    public JasperReport getMainReport() throws JRException {
        return JasperCompileManager.compileReport(getClass().getResourceAsStream("/categoryReports/wgr_report.jrxml"));
    }

    @Override
    public String getFileName() {
        return "CategoryReport" + new DateTime().toString().hashCode();
    }

    @Override
    public Map<String, Object> additionalParameters() throws JRException {
        return null;
    }

    private List<CategoryReportHelper> generateReportData(List<Invoice> invoiceList) {
        return null;
    }
}
