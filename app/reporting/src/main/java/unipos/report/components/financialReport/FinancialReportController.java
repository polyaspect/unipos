package unipos.report.components.financialReport;

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
import unipos.common.remote.pos.model.DailySettlement;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ReversalInvoice;
import unipos.common.remote.printer.PrinterRemoteInterface;
import unipos.common.remote.report.DailySettlementHelper;
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
@RequestMapping(value = "/financialReports", produces = "application/json")
public class FinancialReportController {

    @Autowired
    FinancialReportDayService financialReportDayService;
    @Autowired
    PosRemoteInterface posRemoteInterface;
    @Autowired
    FinancialReportService financialReportService;
    @Autowired
    PrinterRemoteInterface printerRemoteInterface;
    @Autowired
    LogRemoteInterface logRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    @RequestMapping(value = "/printFinancialReport", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void printFinancialReport(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date, @RequestParam(required = true) String storeGuid, HttpServletRequest request) {
        try {
            FinancialReportDay financialReportDay = financialReportDayService.findFinancialReportDayByDateAndStoreGuid(date.toLocalDate(), storeGuid);

            printerRemoteInterface.printTextWithEscSeq(financialReportService.generateFinancialReportLines(financialReportDay, false), request);

        } catch (Exception ex) {
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.setLevel(LogDto.Level.ERROR);
            log.addExceptionParameters(ex);
            log.setMessage("An Error occurred while generation of FinancialReport or printing via Thermalprinter");
            logRemoteInterface.log(log);
        }
    }

    @RequestMapping(value = "/printMonthlyReport", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void printMonthlyReport(HttpServletRequest request) {
        try {
            Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

            DailySettlement lastDailySettlement = posRemoteInterface.findLastCreatedMonthlySettlementByStoreGuid(store.getGuid());
            LocalDateTime startDate = LocalDateTime.parse("1900-01-01T00:00:00");
            if(lastDailySettlement != null ){
                startDate = lastDailySettlement.getDateTime();
            }

            DailySettlementHelper dailySettlementHelper = DailySettlementHelper.builder().startDate(startDate).endDate(LocalDateTime.now()).storeGuid(store.getGuid()).build();

            List<Invoice> invoiceList = getInvoices(dailySettlementHelper, store);
            FinancialReportDay financialReportDay = financialReportService.generateFinancialReport(invoiceList, store);

            printerRemoteInterface.printTextWithEscSeq(financialReportService.generateFinancialReportLines(financialReportDay, true), request);

        } catch (Exception ex) {
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.setLevel(LogDto.Level.ERROR);
            log.addExceptionParameters(ex);
            log.setMessage("An Error occurred while generation of FinancialReport or printing via Thermalprinter");
            logRemoteInterface.log(log);
        }
    }

    private List<Invoice> getInvoices(DailySettlementHelper dailySettlementHelper, Store store) {
        List<Invoice> invoiceList = posRemoteInterface.findByCreationDateBetween(dailySettlementHelper.getStartDate(), dailySettlementHelper.getEndDate());

        //Filter for StoreGuid and reverted Invoices
        invoiceList = invoiceList.stream().filter(invoice -> invoice.getStore().equals(store) && invoice.getStore().getGuid().equals(dailySettlementHelper.getStoreGuid()) && !invoice.isReverted() && !(invoice instanceof ReversalInvoice)).collect(Collectors.toList());

        //Filter for DeviceId if exists
        if (dailySettlementHelper.getDeviceId() != null && !dailySettlementHelper.getDeviceId().equals("null")) {
            invoiceList = invoiceList.stream().filter(invoice -> invoice.getDeviceId().equals(dailySettlementHelper.getDeviceId())).collect(Collectors.toList());
        }

        //Filter for UserId if exists
        if (dailySettlementHelper.getUserId() != null && !dailySettlementHelper.getUserId().equals("0")) {
            invoiceList = invoiceList.stream().filter(invoice -> invoice.getCashier().getUserId().equals(dailySettlementHelper.getUserId())).collect(Collectors.toList());
        }

        return invoiceList;
    }

    @RequestMapping(value = "/generateFinancialReportAsPdf", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateSummedFinancialReportAsPdf(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid, HttpServletResponse response) {
        try {
            log.info("--------------generate PDF-Financial-Report--------------");

            financialReportService.generateFinancialJasperReportAsPdf(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, response, true);

        } catch (Exception e) {
            log.error("Error while PDF-Report generation");
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.setLevel(LogDto.Level.ERROR);
            log.addExceptionParameters(e);
            log.setMessage("An Error occurred during SummedFinancialReport-PDF generation");
            logRemoteInterface.log(log);
        }
        log.info("--------------DONE--------------");
    }

    @RequestMapping(value = "/generateFinancialReport", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateSummedFinancialReport(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid, HttpServletResponse response) {
        try {
            log.info("--------------generate HTML-Financial-Report--------------");

            financialReportService.generateFinancialJasperReportAsHtml(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, response, true);

        } catch (Exception e) {
            log.error("Error while HTML-SummedFinancialReport generation");
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.setLevel(LogDto.Level.ERROR);
            log.addExceptionParameters(e);
            log.setMessage("An Error occurred during FinancialReport HTML generation with JasperReport");
            logRemoteInterface.log(log);
        }
        log.info("--------------DONE--------------");
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/generateFinancialReportPerDaysAsPdf", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateFinancialReportPerDayAsPdf(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid, HttpServletResponse response) {

        try {
            log.info("--------------generate PDF-Financial-Report-Multiple-Days--------------");

            financialReportService.generateFinancialJasperReportAsPdf(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, response, false);

        } catch (Exception e) {
            log.error("An Error occurred during FinancialReportPerDayPDF generation");
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.setLevel(LogDto.Level.ERROR);
            log.addExceptionParameters(e);
            log.setMessage("An Error occurred during FinancialReport PDF generation with JasperReport");
            logRemoteInterface.log(log);
        }
        log.info("--------------DONE--------------");
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/generateFinancialReportPerDays", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateFinancialReportPerDay(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid, HttpServletResponse response) {
        try {
            log.info("--------------generate HTML-Financial-Report-Per-Day--------------");

            financialReportService.generateFinancialJasperReportAsHtml(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, response, false);

        } catch (Exception e) {
            log.error("Error while HTML-FinancialReportPerDay generation");
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.setLevel(LogDto.Level.ERROR);
            log.addExceptionParameters(e);
            log.setMessage("An Error occurred during FinancialReport HTML generation with JasperReport");
            logRemoteInterface.log(log);
        }
        log.info("--------------DONE--------------");
    }
}
