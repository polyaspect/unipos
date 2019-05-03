package unipos.report.components.productReport;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ProductInvoiceItem;
import unipos.report.components.productReportDay.ProductReportDayService;
import unipos.report.components.shared.helper.ProductReportHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Thomas on 01.11.2015.
 */
@Service
public class ProductReportServiceImpl implements ProductReportService {

    @Autowired
    ProductReportDayService productReportDayService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    ProductReport productReport;

    private List<ProductReportHelper> productReportHelpers;

    @Override
    public void generateProductReportAsHtml(LocalDateTime startDate, LocalDateTime endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException {

        productReport.printReportAsHtml(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, response);
    }

    @Override
    public void generateProductReportAsPdf(LocalDateTime startDate, LocalDateTime endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException {
        productReport.printReportAsPdf(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, response);
    }


    public List<ProductReportHelper> getProductInvoiceItems(List<Invoice> invoiceList) {
        productReportHelpers = new ArrayList<>();
        if (!invoiceList.isEmpty()) {
            invoiceList.stream().filter(invoice -> invoice.getInvoiceItems() != null).forEach(invoice -> invoice.getInvoiceItems().stream().filter(x -> x instanceof ProductInvoiceItem && !((ProductInvoiceItem) x).isReversalApplied()).forEach(invoiceItem -> {
                boolean result = productReportHelpers.stream().filter(x -> x.getProductNumber().equals(((ProductInvoiceItem) invoiceItem).getProductNumber())).findFirst().map(productReportHelper -> {
                    productReportHelper.setTurnover(productReportHelper.getTurnover().add(((ProductInvoiceItem) invoiceItem).getTurnoverGross()));

                    productReportHelper.setQuantity(productReportHelper.getQuantity().add(new BigDecimal(((ProductInvoiceItem) invoiceItem).getQuantity())));

                    return true;
                }).isPresent();
                if (!result) {
                    productReportHelpers.add(ProductReportHelper.builder().productNumber(((ProductInvoiceItem) invoiceItem).getProductNumber()).label(((ProductInvoiceItem) invoiceItem).getLabel()).quantity(new BigDecimal(((ProductInvoiceItem) invoiceItem).getQuantity())).contribution(new BigDecimal("0.00")).turnover(((ProductInvoiceItem) invoiceItem).getTurnoverGross()).build());
                }
            }));
            BigDecimal sumTurnover = productReportHelpers.stream().map(ProductReportHelper::getTurnover).reduce(new BigDecimal("0.00"), BigDecimal::add);
            productReportHelpers.stream().forEach(productReportHelper -> {
                productReportHelper.setContribution(productReportHelper.getTurnover().divide(sumTurnover, 6, RoundingMode.HALF_UP));
            });

        }

        return productReportHelpers;
    }
}
