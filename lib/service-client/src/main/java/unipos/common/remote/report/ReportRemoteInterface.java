package unipos.common.remote.report;

import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.CashbookEntry;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ReversalInvoice;
import unipos.common.remote.printer.model.ThermalPrinterLine;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created by Dominik on 03.12.2015.
 */
public interface ReportRemoteInterface {

    void printInvoice(Invoice invoice, HttpServletRequest request);

    void revertInvoice(ReversalInvoice remoteReversalInvoice, HttpServletRequest request);
//    void executeDailySettlement(LocalDateTime startDateTime, LocalDateTime endDateTime, String storeGuid);
    void executeDailySettlement(DailySettlementHelper dailySettlementHelper);
    List<ThermalPrinterLine> previewFinancialReport(String authToken, String deviceToken);
    boolean printFinancialReportOfCurrentDay(String authToken, String deviceToken);
    String getCurrentVersion();

    boolean printMonthlySettlementReport(String authToken, String deviceToken);
    void printCashbookEntry(CashbookEntry cashbookEntry, String authToken, String deviceToken);
}
