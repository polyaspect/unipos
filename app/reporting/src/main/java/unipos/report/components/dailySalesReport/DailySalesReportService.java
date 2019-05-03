package unipos.report.components.dailySalesReport;

import net.sf.jasperreports.engine.JRException;
import unipos.common.remote.pos.model.Invoice;
import unipos.report.components.shared.helper.DailySalesHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 30.12.2015.
 */
public interface DailySalesReportService {

    void generateDailySalesReportAsPdf(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException;

    void generateDailySalesReportAsHtml(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException;

    DailySalesHelper getDailySales(List<Invoice> invoiceList);
}
