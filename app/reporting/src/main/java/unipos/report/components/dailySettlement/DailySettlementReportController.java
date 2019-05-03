package unipos.report.components.dailySettlement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.pos.model.DailySettlement;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ReversalInvoice;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.common.remote.report.DailySettlementHelper;
import unipos.report.components.categoryReport.CategoryReportService;
import unipos.report.components.dailySalesReport.DailySalesReportServiceImpl;
import unipos.report.components.financialReport.FinancialReportService;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.components.productReport.ProductReportService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Thomas on 30.01.2016.
 */
@Slf4j
@RestController
@RequestMapping(value = "/dailySettlementReports", produces = "application/json")
public class DailySettlementReportController {

    @Autowired
    ProductReportService productReportService;
    @Autowired
    DailySalesReportServiceImpl dailySalesReportService;
    @Autowired
    FinancialReportService financialReportService;
    @Autowired
    CategoryReportService categoryReportService;
    @Autowired
    DailySettlementReportService dailySettlementReportService;
    @Autowired
    PosRemoteInterface posRemoteInterface;
    @Autowired
    LogRemoteInterface logRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;


    /**
     * Executes the calculation of dailySales to provide the Reports faster
     *
     * @param dailySettlementHelper contains the needed information to start calculation (startDate, endDate, storeGuid, userId)
     * @throws IOException
     */
    @RequestMapping(value = "/execute", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.OK)
    public synchronized void execute(@RequestBody(required = true) DailySettlementHelper dailySettlementHelper, HttpServletResponse response) throws IOException {
        try {
            generate(dailySettlementHelper);

            //ToDo: Sinnvollen Einsatz fürs Regenerate überlegen
            //regenerate(dailySettlementHelper.getEndDate().minusMonths(2L).withDayOfMonth(1), dailySettlementHelper.getEndDate().minusDays(1), dailySettlementHelper.getStoreGuid(), response);
        } catch (Exception e) {
            response.sendError(400);
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.addExceptionParameters(e);
            log.setLevel(LogDto.Level.ERROR);
            log.setMessage("Error during fetching or manipulating of invoiceList occured");
            logRemoteInterface.log(log);
        }
    }

    private void generate(DailySettlementHelper dailySettlementHelper) {
        Store store = dataRemoteInterface.getStoreByGuid(dailySettlementHelper.getStoreGuid());
        List<Invoice> invoiceList = getInvoices(dailySettlementHelper, store);

        if (invoiceList.size() > 0) {
            //set startDate to the date of the first invoice
            LocalDateTime startDate = invoiceList.stream().sorted((i1, i2) -> i1.getCreationDate().compareTo(i2.getCreationDate())).findFirst().get().getCreationDate();
            dailySettlementReportService.clearReportLinesAndDays(startDate.toLocalDate(), dailySettlementHelper.getStoreGuid());

            try{
                dailySettlementReportService.addProductReportDay(productReportService.getProductInvoiceItems(invoiceList), startDate.toLocalDate(), dailySettlementHelper.getStoreGuid(), dailySettlementHelper.getUserId());
            }
            catch(Exception ex){

            }

            try{
                dailySettlementReportService.addDailySalesLine(dailySalesReportService.getDailySales(invoiceList), startDate.toLocalDate(), dailySettlementHelper.getStoreGuid(), dailySettlementHelper.getUserId());
            }
            catch(Exception ex){

            }

            try{
                dailySettlementReportService.addFinancialReportDay(financialReportService.generateFinancialReport(invoiceList, store), startDate.toLocalDate(), dailySettlementHelper.getStoreGuid(), dailySettlementHelper.getUserId());
            }
            catch(Exception ex){

            }

            try{
                dailySettlementReportService.addCategoryReportDay(categoryReportService.getCategoryReportEntries(invoiceList), startDate.toLocalDate(), dailySettlementHelper.getStoreGuid(), dailySettlementHelper.getUserId());
            }
            catch(Exception ex){

            }
        } else {
            LocalDateTime bsDateTime = dailySettlementHelper.getEndDate();
            if (bsDateTime.toLocalDate().atTime(store.getCloseHour().toLocalTime()).isAfter(bsDateTime) && bsDateTime.isBefore(bsDateTime.toLocalDate().atTime(11, 59, 59))) {
                bsDateTime = bsDateTime.minusDays(1);
            }
            //ToDo: What's the purpose of this code?
            /* else if (bsDateTime.toLocalDate().atTime(store.getCloseHour().toLocalTime()).isAfter(bsDateTime)) {
                    bsDateTime = bsDateTime.plusDays(1);
                }*/
//                dailySettlementReportService.clearReportLinesAndDays(bsDateTime.toLocalDate(), dailySettlementHelper.getStoreGuid());
            dailySettlementReportService.addEmptyReport(bsDateTime.toLocalDate(), dailySettlementHelper.getStoreGuid(), dailySettlementHelper.getUserId());
        }
    }

    @RequestMapping(value = "/regenerateReports", method = RequestMethod.POST)
    public void regenerate(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid, HttpServletResponse response) throws IOException {
        List<DailySettlement> dailySettlements = posRemoteInterface.findByStoreGuidAndDateTimeBetween(storeGuid, startDate, endDate);
        try {
            boolean findAgain = false;
            Store store = dataRemoteInterface.getStoreByGuid(storeGuid);
            if (dailySettlements.stream().findFirst().get().getDateTime().isAfter(startDate.toLocalDate().atTime(store.getCloseHour().toLocalTime()))) {
                startDate = startDate.minusDays(1);
                findAgain = true;
            }
            Comparator<DailySettlement> byDateTimeReversed = Collections.reverseOrder(Comparator.comparing(DailySettlement::getDateTime));
            dailySettlements.sort(byDateTimeReversed);
            if (dailySettlements.stream().findFirst().get().getDateTime().isBefore(endDate.toLocalDate().atTime(store.getCloseHour().toLocalTime()))) {
                endDate = endDate.plusDays(1);
                findAgain = true;
            }
            if (findAgain) {
                dailySettlements = posRemoteInterface.findByStoreGuidAndDateTimeBetween(storeGuid, startDate, endDate);
            }
            Comparator<DailySettlement> byDateTime = Comparator.comparing(DailySettlement::getDateTime);
            dailySettlements.sort(byDateTime);
            for (int i = 0; i < dailySettlements.size() - 1; i++) {
                generate(DailySettlementHelper.builder().startDate(dailySettlements.get(i).getDateTime()).endDate(dailySettlements.get(i + 1).getDateTime()).storeGuid(storeGuid).build());
            }
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @RequestMapping(value = "/previewFinancialReport", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<ThermalPrinterLine> previewFinancialReport(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        DailySettlement lastDailySettlement = posRemoteInterface.findLastCreatedDailySettlementByStoreGuid(store.getGuid());
        LocalDateTime startDate = LocalDateTime.parse("1900-01-01T00:00:00");
        if(lastDailySettlement != null ){
            startDate = lastDailySettlement.getDateTime();
        }

        DailySettlementHelper dailySettlementHelper = DailySettlementHelper.builder().startDate(startDate).endDate(LocalDateTime.now()).storeGuid(store.getGuid()).build();

        List<Invoice> invoiceList = getInvoices(dailySettlementHelper, store);
        FinancialReportDay financialReportDay = financialReportService.generateFinancialReport(invoiceList, store);

        return financialReportService.generateFinancialReportLines(financialReportDay, false);
    }

    @RequestMapping(value = "/previewMonthlyReport", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<ThermalPrinterLine> previewMonthlyReport(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        DailySettlement lastDailySettlement = posRemoteInterface.findLastCreatedMonthlySettlementByStoreGuid(store.getGuid());
        LocalDateTime startDate = LocalDateTime.parse("1900-01-01T00:00:00");
        if(lastDailySettlement != null ){
            startDate = lastDailySettlement.getDateTime();
        }

        DailySettlementHelper dailySettlementHelper = DailySettlementHelper.builder().startDate(startDate).endDate(LocalDateTime.now()).storeGuid(store.getGuid()).build();

        List<Invoice> invoiceList = getInvoices(dailySettlementHelper, store);
        FinancialReportDay financialReportDay = financialReportService.generateFinancialReport(invoiceList, store);

        return financialReportService.generateFinancialReportLines(financialReportDay, true);
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
}
