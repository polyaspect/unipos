package unipos.report.components.dailySalesReport;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;

/**
 * Created by Thomas on 30.12.2015.
 */
@Slf4j
@RestController
@RequestMapping(value = "/dailySalesReports", produces = "application/json")
public class DailySalesReportController {

    @Autowired
    DailySalesReportService dailySalesReportService;

    @RequestMapping(value = "/generateDailySalesReportAsPdf", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generatePdfReport(HttpServletResponse response, HttpServletRequest request, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid) {
        try {
            log.info("--------------GENERATE PDF-Report----------");

            dailySalesReportService.generateDailySalesReportAsPdf(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, request, response);

        } catch (Exception e) {
            log.error("An error occurred during Daily Sales Report PDF generation");
        }
        log.info("----DONE---");
    }

    @RequestMapping(value = "/generateDailySalesReport", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateProductReport(HttpServletResponse response, HttpServletRequest request, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid) throws JRException, IOException {

        try {
            log.info("--------------GENERATE HTML-Report----------");

            dailySalesReportService.generateDailySalesReportAsHtml(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, request, response);

        } catch (Exception e) {
            log.error("An error occurred during Daily Sales HTML generation");
            throw e;
        }
        log.info("----DONE---");

    }
}
