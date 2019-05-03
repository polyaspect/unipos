package unipos.report.components.productReport;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.Invoice;
import unipos.report.components.shared.helper.ProductReportHelper;
import unipos.report.components.shared.Main;
import unipos.report.components.shared.WriteToResponseStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

/**
 * Created by Thomas on 26.10.2015.
 */
@Slf4j
@RestController
@RequestMapping(value = "/productReports", produces = "application/json")
public class ProductReportController {


    @Autowired
    ServletContext servletContext;
    @Autowired
    ProductReportService productReportService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @SuppressWarnings("deprecation")
    public void testPdfGenerator(HttpServletResponse response) throws JRException, IOException {

        try {
            log.info("--------------generate TestPDF Product Report--------------");
            ArrayList<Invoice> invoices = new ArrayList<>();
            Iterator j = Main.getCollection().iterator();
            while (j.hasNext()) {
                invoices.add((Invoice) j.next());
            }

            Map<String, Object> parameterMap = new HashMap();

            List<ProductReportHelper> productReportHelpers = new ProductReportServiceImpl().getProductInvoiceItems(invoices);

            LocalDateTime startDate = LocalDateTime.of(2015, Month.SEPTEMBER, 01, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.now();

            JRDataSource JRdataSource = new JRBeanCollectionDataSource(productReportHelpers);

            JasperReport jr = JasperCompileManager.compileReport(getClass().getResourceAsStream("/productReports/product_report.jrxml"));
            Store store = dataRemoteInterface.getStoreByGuid("c9492c91-458c-4236-8c53-0455274b0a4d");
            Company company = dataRemoteInterface.getCompanyByGuid(store.getCompanyGuid());

            Locale locale = new Locale("de", "AT");

            parameterMap.put(JRParameter.REPORT_LOCALE, locale);
            parameterMap.put("StoreName", store.getName());
            parameterMap.put("CompanyName", company.getName());
            parameterMap.put("CompanyAddress", store.getAddress().getStreet());
            parameterMap.put("CompanyZIPCode", store.getAddress().getPostCode() + " " + store.getAddress().getCity());
            parameterMap.put("startDate", startDate);
            parameterMap.put("endDate", endDate);
            parameterMap.put("sumTurnoverGross", productReportHelpers.stream().map(ProductReportHelper::getTurnover).reduce(new BigDecimal("0.00"), BigDecimal::add));

            String fileName = "TestProductReport" + new DateTime().toString().hashCode();

            System.out.println(fileName);

            JasperPrint print = JasperFillManager.fillReport(jr, parameterMap, JRdataSource);
            File pdf = File.createTempFile(fileName, ".pdf");
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));

            System.out.println(pdf.getAbsolutePath());
            WriteToResponseStream.writePdfToResponseStream(response, pdf, fileName);

            pdf.delete();
        } catch (Exception e) {
            log.error("Error during TestPDF Product Report generation");
            log.error(e.getMessage());
        }

    }

    @RequestMapping(value = "/generateProductReport", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateProductReport(HttpServletResponse response, HttpServletRequest request, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid) throws JRException, IOException {

        try {
            log.info("--------------generate HTML-Report--------------");

            productReportService.generateProductReportAsHtml(startDate, endDate, storeGuid, response);

        } catch (Exception e) {
            log.error("An error occurred during HTML-ProductReport generation");
            throw e;
        }
        log.info("----DONE----");

    }

    @RequestMapping(value = "/generateProductReportAsPdf", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateProductReportToPdf(HttpServletResponse response, HttpServletRequest request, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid) throws JRException, IOException {
        try {
            log.info("--------------generate PDF-Report--------------");

            productReportService.generateProductReportAsPdf(startDate, endDate, storeGuid, response);

        } catch (Exception e) {
            log.error("An error occurred during PDF-ProductReport generation");
            throw e;
        }
        log.info("----DONE----");
    }
}
