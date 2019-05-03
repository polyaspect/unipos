package unipos.report.components.categoryReport;

import net.sf.jasperreports.engine.JRException;
import unipos.common.remote.pos.model.Invoice;
import unipos.report.components.categoryReportDay.CategoryReportDay;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 19.12.2015.
 */
public interface CategoryReportService {

    void generateCategoryReportAsPdf(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException;

    void generateCategoryReportAsHtml(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException;

    CategoryReportDay getCategoryReportEntries(List<Invoice> invoices);
}
