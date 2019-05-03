package unipos.report.components.productReport;

import net.sf.jasperreports.engine.JRException;
import unipos.common.remote.pos.model.Invoice;
import unipos.report.components.shared.helper.ProductReportHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Thomas on 01.11.2015.
 */
public interface ProductReportService {

    void generateProductReportAsHtml(LocalDateTime startDate, LocalDateTime endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException;

    void generateProductReportAsPdf(LocalDateTime startDate, LocalDateTime endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException;

    List<ProductReportHelper> getProductInvoiceItems(List<Invoice> invoiceList);
}
