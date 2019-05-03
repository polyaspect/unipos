package unipos.report.components.productReport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ProductInvoiceItem;
import unipos.common.remote.pos.model.ReversalInvoice;
import unipos.report.components.Report;
import unipos.report.components.productReportDay.ProductReportDay;
import unipos.report.components.shared.helper.ProductReportHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 23.08.2016.
 */
@Component
public class ProductReport extends Report<ProductReportHelper> {

    private BigDecimal sumTurnover = new BigDecimal("0.00");

    @Autowired
    ProductReportService productReportService;

    @Override
    public List<ProductReportHelper> getDatasource(LocalDate startDate, LocalDate endDate, String storeGuid) {

//        List<ProductReportHelper> productReportHelpers = getProductReportEntries(productReportDayService.findByDateBetweenAndStoreGuid(startDate, endDate, storeGuid));

        List<ProductReportHelper> productReportHelpers = productReportService.getProductInvoiceItems(getInvoices(startDate, endDate, storeGuid));
        sumTurnover = productReportHelpers.stream().map(ProductReportHelper::getTurnover).reduce(new BigDecimal("0.00"), BigDecimal::add);
        productReportHelpers.forEach(productReportHelper -> productReportHelper.setContribution(productReportHelper.getTurnover().divide(sumTurnover, 4, RoundingMode.HALF_UP)));

        if (productReportHelpers.size() == 0) {
            productReportHelpers.add(ProductReportHelper.builder().label("").turnover(new BigDecimal("0.0")).productNumber("").contribution(new BigDecimal("0.00")).quantity(new BigDecimal("0.00")).build());
        }

        Comparator<ProductReportHelper> comparator = Comparator.comparing(ProductReportHelper::getLabel);
        productReportHelpers.sort(comparator);

        return productReportHelpers;
    }

    @Override
    public JasperReport getMainReport() throws JRException {
        return JasperCompileManager.compileReport(getClass().getResourceAsStream("/productReports/product_report.jrxml"));
    }

    @Override
    public String getFileName() {
        return "ProductReport" + new DateTime().toString().hashCode();
    }

    @Override
    public Map<String, Object> additionalParameters() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("sumTurnoverGross", sumTurnover);
        return paramMap;
    }

    public final List<ProductReportHelper> getProductReportEntries(List<ProductReportDay> productReportDays) {

        List<ProductReportHelper> productReportHelpers = new ArrayList<>();

        productReportDays.stream().filter(productReportDay -> productReportDay.getProductReportHelpers() != null).forEach(productReportDay -> productReportDay.getProductReportHelpers().stream().forEach(productReportHelper -> {
            boolean result = productReportHelpers.stream().filter(x -> x.getProductNumber().equals(productReportHelper.getProductNumber())).findFirst().map(productReportHelp -> {
                productReportHelp.setTurnover(productReportHelp.getTurnover().add(productReportHelper.getTurnover()));

                productReportHelp.setQuantity(productReportHelp.getQuantity().add(productReportHelper.getQuantity()));

                return true;
            }).isPresent();
            if (!result) {
                productReportHelpers.add(ProductReportHelper.builder().productNumber(productReportHelper.getProductNumber()).label(productReportHelper.getLabel()).quantity(productReportHelper.getQuantity()).contribution(new BigDecimal("0.00")).turnover(productReportHelper.getTurnover()).build());
            }
        }));

        BigDecimal sumTurnover = productReportHelpers.stream().map(ProductReportHelper::getTurnover).reduce(new BigDecimal("0.00"), BigDecimal::add);
        productReportHelpers.forEach(productReportHelper -> {
            productReportHelper.setContribution(productReportHelper.getTurnover().divide(sumTurnover, 6, RoundingMode.HALF_UP));
        });

        return productReportHelpers;
    }

}
