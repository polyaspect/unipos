package unipos.report.components.largeValueReceipt;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import org.exolab.castor.types.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.pos.model.Invoice;
import unipos.report.components.shared.WriteToResponseStream;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Thomas on 25.05.2016.
 */
@Slf4j
@RestController
@RequestMapping(value = "/largeValueReceipt", produces = "application/json")
public class LargeValueReceiptController {

    @Autowired
    PosRemoteInterface posRemoteInterface;
    @Autowired
    LogRemoteInterface logRemoteInterface;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @SuppressWarnings("deprecation")
    public void printReceipt(HttpServletResponse response) throws JRException, IOException {
        try {
            List<Invoice> invoices = posRemoteInterface.findByCreationDate(LocalDateTime.of(2016, 5, 15, 0, 0));

            JasperReport main = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/largeValueReceipt/main_report.jrxml"));
            JasperReport sub_products = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/largeValueReceipt/sub_products.jrxml"));
            JasperReport sub_taxes = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/largeValueReceipt/sub_taxes.jrxml"));

            Map<String, Object> parameterMap = new HashMap<>();

            File reportsDir = new File(this.getClass().getResource("/largeValueReceipt/").getPath());

            Locale locale = new Locale("de", "AT");
            parameterMap.put(JRParameter.REPORT_LOCALE, locale);
            parameterMap.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));
            parameterMap.put("subreportParameter", new ArrayList<>(Arrays.asList(sub_products, sub_taxes)));

            JRDataSource jrDataSource = new JRBeanCollectionDataSource(Arrays.asList(invoices.stream().findFirst().get()));
            JasperPrint jp = JasperFillManager.fillReport(main, parameterMap, jrDataSource);


            log.info("--------------generate PDF-Financial-Report-Multiple-Days--------------");


            String fileName = "LargeValueReceipt" + new DateTime().toString().hashCode();
            File pdf = File.createTempFile(fileName, ".pdf");

            JasperExportManager.exportReportToPdfStream(jp, new FileOutputStream(pdf));

            System.out.println(pdf.getAbsolutePath());
            WriteToResponseStream.writePdfToResponseStream(response, pdf, fileName);

            pdf.delete();
        } catch (Exception e) {
            log.error("An Error occurred during LargeValueReceipt PDF generation");
            LogDto log = new LogDto();
            log.setDateTime(LocalDateTime.now());
            log.setLevel(LogDto.Level.ERROR);
            log.addExceptionParameters(e);
            log.setMessage("An Error occurred during LargeValueReceipt PDF generation");
            logRemoteInterface.log(log);
        }
        log.info("--------------DONE--------------");
    }
}
