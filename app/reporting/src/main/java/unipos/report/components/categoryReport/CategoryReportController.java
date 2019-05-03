package unipos.report.components.categoryReport;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Address;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.report.components.shared.Main;
import unipos.report.components.shared.WriteToResponseStream;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 19.12.2015.
 */
@Slf4j
@RestController
@RequestMapping(value = "/categoryReports", produces = "application/json")
public class CategoryReportController {

    @Autowired
    CategoryReportService categoryReportService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void testPdfGenerator(HttpServletResponse response) throws JRException, IOException {

        try {
            Map<String, Object> parameterMap = new HashMap<>();

            JRDataSource JRdataSource = new JRBeanCollectionDataSource(Main.getCategoryReport());
            JasperReport jr = JasperCompileManager.compileReport(getClass().getResourceAsStream("/categoryReports/wgr_report.jrxml"));

            Store store = new Store().builder().companyGuid("517b2675-e36c-49fe-9940-f280ad0b0755").name("Meine").address(new Address().builder().city("Wien").postCode(1130).street("Lafitegasse 26C").build()).build();
            Company company = dataRemoteInterface.getCompanyByGuid(store.getCompanyGuid());

            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now();

            parameterMap.put("StoreName", store.getName());
            parameterMap.put("CompanyName", company.getName());
            parameterMap.put("CompanyAddress", store.getAddress().getStreet());
            parameterMap.put("CompanyZIPCode", store.getAddress().getPostCode() + " " + store.getAddress().getCity());
            parameterMap.put("StartDate", startDate);
            parameterMap.put("EndDate", endDate);

            String fileName = "TestCategoryReport" + new DateTime().toString().hashCode();

            JasperPrint print = JasperFillManager.fillReport(jr, parameterMap, JRdataSource);
            File pdf = File.createTempFile(fileName, ".pdf");
            JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));

            System.out.println(pdf.getAbsolutePath());
            WriteToResponseStream.writePdfToResponseStream(response, pdf, fileName);

            pdf.delete();
        } catch (Exception e) {
            log.error("Error during Test CategoryReport generation");
            log.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/generateCategoryReportAsPdf", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generatePdfReport(HttpServletResponse response, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid) throws JRException, IOException {
        try {
            log.info("--------------generate PDF-Report----------");

            categoryReportService.generateCategoryReportAsPdf(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, response);

        } catch (Exception e) {
            log.error("An Error occurred during CategoryReport PDF generation");
            throw e;
        }
        log.info("----DONE---");
    }

    @RequestMapping(value = "/generateCategoryReport", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void generateHtmlReport(HttpServletResponse response, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam(required = true) String storeGuid) throws JRException, IOException {
        try {
            log.info("--------------generate HTML-Report----------");

            categoryReportService.generateCategoryReportAsHtml(startDate.toLocalDate(), endDate.toLocalDate(), storeGuid, response);

        } catch (Exception e) {
            log.error("An Error occurred during CategoryReport HTML generation");
            throw e;
        }
        log.info("----DONE---");
    }
}
