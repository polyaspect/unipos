package unipos.report.components.cashbookEntry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.pos.model.CashbookEntry;
import unipos.common.remote.pos.model.DailySettlement;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ReversalInvoice;
import unipos.common.remote.printer.PrinterRemoteInterface;
import unipos.common.remote.report.DailySettlementHelper;
import unipos.report.components.financialReport.FinancialReportService;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.components.financialReportDay.FinancialReportDayService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 16.01.2016.
 */
@Slf4j
@RestController
@RequestMapping(value = "/cashbookEntry", produces = "application/json")
public class CashbookEntryReportController {

    @Autowired
    CashbookEntryReportService cashbookEntryReportService;
    @Autowired
    PrinterRemoteInterface printerRemoteInterface;
    @Autowired
    LogRemoteInterface logRemoteInterface;

    @RequestMapping(value = "/print", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void printCashbookEntryReport(@RequestBody(required = true) CashbookEntry cashbookEntry, HttpServletRequest request) {
        try {
            printerRemoteInterface.printTextWithEscSeq(cashbookEntryReportService.generateCashbookEntryLines(cashbookEntry), request);
        } catch (Exception ex) {
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.setLevel(LogDto.Level.ERROR);
            log.addExceptionParameters(ex);
            log.setMessage("An Error occurred while generation of CashbookEntryReport or printing via Thermalprinter");
            logRemoteInterface.log(log);
        }
    }
}
