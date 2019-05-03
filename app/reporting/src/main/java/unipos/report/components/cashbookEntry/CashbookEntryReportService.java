package unipos.report.components.cashbookEntry;

import net.sf.jasperreports.engine.JRException;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.CashbookEntry;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.report.components.financialReportDay.FinancialReportDay;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 16.01.2016.
 */
public interface CashbookEntryReportService {

    List<ThermalPrinterLine> generateCashbookEntryLines(CashbookEntry cashbookEntry);
}
