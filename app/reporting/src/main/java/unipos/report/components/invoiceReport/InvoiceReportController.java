package unipos.report.components.invoiceReport;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.pos.model.ChangeInvoiceItem;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ProductInvoiceItem;
import unipos.common.remote.pos.model.ReversalInvoice;
import unipos.common.remote.printer.PrinterRemoteInterface;
import unipos.report.components.invoiceReport.model.InvoiceReportDto;
import unipos.report.components.shared.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dominik on 11.09.15.
 */

@Slf4j
@RestController
@RequestMapping(value = "/invoiceReports", produces = "application/json")
public class InvoiceReportController {

    @Autowired
    PrinterRemoteInterface printerRemoteInterface;
    @Autowired
    LogRemoteInterface logRemoteInterface;

    ReportCompiledSingleton reportCompiledSingleton = ReportCompiledSingleton.getInstance();

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void testXMLGenerator(@RequestBody Invoice invoice, HttpServletResponse response) throws JRException, IOException {
        log.info("--------------generate TestPdf report----------");

        Map<String,Object> parameterMap = new HashMap<String,Object>();
        InvoiceReportDto invoiceReportDto = generateInvoiceReportDtoFromInvoice(invoice);

        JRDataSource JRdataSource = new JRBeanCollectionDataSource(new ArrayList<>(Collections.singletonList(invoiceReportDto)));

        InputStream masterInputStream = this.getClass().getResourceAsStream("/Blank_A4.jrxml");
        InputStream subOrderItemsInputStream = this.getClass().getResourceAsStream("/sub_OrderItems.jrxml");
        InputStream subPaymentItemsInputStream = this.getClass().getResourceAsStream("/sub_Payment.jrxml");
        InputStream subTaxRatesItemsInputStream = this.getClass().getResourceAsStream("/sub_taxRates.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(masterInputStream);
        JasperReport subOrderItemsCompiled = JasperCompileManager.compileReport(subOrderItemsInputStream);
        JasperReport subPaymentCompiled = JasperCompileManager.compileReport(subPaymentItemsInputStream);
        JasperReport subTaxRatesCompiled = JasperCompileManager.compileReport(subTaxRatesItemsInputStream);

        parameterMap.put("subreportParameter", new ArrayList<>(Arrays.asList(subOrderItemsCompiled, subPaymentCompiled, subTaxRatesCompiled)));
        parameterMap.put("QR-Code", "eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ.dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk");

        JasperPrint jp = JasperFillManager.fillReport(jr, parameterMap, JRdataSource);
        jp.getPropertiesMap().setProperty("net.sf.jasperreports.text.truncate.at.char", "true");
        jp.getPropertiesMap().setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        UniposXMLExporter exporter = new UniposXMLExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

        exporter.exportReport();

        String fileName = "Receipt.xml";
        response.setHeader("Content-Disposition", "inline;filename="+ fileName);

        response.setContentType(MediaType.APPLICATION_XML_VALUE);
        response.setContentLength(baos.size());

        writeReportToResponseStream(response, baos);

    }


    @RequestMapping(value = "/generateInvoice", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void generatePdfStreamInvoiceReport(@RequestBody Invoice invoice, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {
        log.info("--------------generate report        InvoiceReportDto invoiceReportDto = generateInvoiceReportDtoFromInvoice(invoice);\n----------");

        InvoiceReportDto invoiceReportDto = generateInvoiceReportDtoFromInvoice(invoice);

        Map<String,Object> parameterMap = new HashMap<String,Object>();
        ArrayList<InvoiceReportDto> invoices = new ArrayList<>(Collections.singletonList(invoiceReportDto));

        JRDataSource JRdataSource = new JRBeanCollectionDataSource(invoices);

        try {
            JasperReport jr = reportCompiledSingleton.getJr();
            JasperReport subOrderItemsCompiled = reportCompiledSingleton.getSubOrderItemsCompiled();
            JasperReport subPaymentCompiled = reportCompiledSingleton.getSubPaymentCompiled();
            JasperReport subTaxRatesCompiled = reportCompiledSingleton.getSubTaxRatesCompiled();

            parameterMap.put("subreportParameter", new ArrayList<>(Arrays.asList(subOrderItemsCompiled, subPaymentCompiled, subTaxRatesCompiled)));
//            parameterMap.put("QR-Code", "eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ.dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk");

            JasperPrint jp = JasperFillManager.fillReport(jr, parameterMap, JRdataSource);
            jp.getPropertiesMap().setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");

            File tempFile = File.createTempFile("temp", ".xml");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

            UniposXMLExporter exporter = new UniposXMLExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fileOutputStream);

            exporter.exportReport();

            printerRemoteInterface.printUniposXmlFile(tempFile, request);

        } catch (Exception e) {
            log.error("Something went wrong during the Report generation ");
            LogDto logDto = LogDto.builder()
                    .dateTime(LocalDateTime.now())
                    .message("Something went wrong during the Report generation ")
                    .level(LogDto.Level.ERROR)
                    .source(this.getClass().getName() + "#" + "generatePdfStreamInvoiceReport")
                    .build();
            logDto.addParameter("ExceptionType", e.getClass().toString());
            logDto.addParameter("ExceptionMessage", e.getMessage());

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            logDto.addParameter("ExceptionStackTrace", errors.toString());
            logRemoteInterface.log(logDto);
            e.printStackTrace();
            throw e;
        }
        log.info("----DONE---");
    }

    private InvoiceReportDto generateInvoiceReportDtoFromInvoice(Invoice invoice) {

        List<ProductInvoiceItem> essenItems = invoice.getInvoiceItems().stream().filter(x -> x instanceof ProductInvoiceItem)
                .map(x -> (ProductInvoiceItem)x)
                .filter(x -> x.getProduct().getCategory().getName().toUpperCase().contains("Speisen".toUpperCase()) && !x.getProduct().getNumber().equals(9L) && !x.reversalApplied)
                .collect(Collectors.toList());

        List<ProductInvoiceItem> getraenkeItems = invoice.getInvoiceItems().stream().filter(x -> x instanceof ProductInvoiceItem)
                .map(x -> (ProductInvoiceItem)x)
                .filter(x -> x.getProduct().getCategory().getName().toUpperCase().contains("Getraenke".toUpperCase()) && !x.reversalApplied)
                .collect(Collectors.toList());

        List<ProductInvoiceItem> cafeItems = invoice.getInvoiceItems().stream().filter(x -> x instanceof ProductInvoiceItem)
                .map(x -> (ProductInvoiceItem)x)
                .filter(x -> x.getProduct().getCategory().getName().toUpperCase().contains("Cafe".toUpperCase()) && !x.reversalApplied)
                .collect(Collectors.toList());

        List<ProductInvoiceItem> hendlItems = invoice.getInvoiceItems().stream().filter(x -> x instanceof ProductInvoiceItem)
                .map(x -> (ProductInvoiceItem)x)
                .filter(x -> x.getProduct().getNumber().equals(9L) && !x.reversalApplied)
                .collect(Collectors.toList());

        InvoiceReportDto invoiceReportDto = InvoiceReportDto.builder()
                .cashier(invoice.getCashier())
                .company(invoice.getCompany())
                .creationDate(invoice.getCreationDate())
                .customerGuid(invoice.getCustomerGuid())
                .deviceId(invoice.getDeviceId())
                .hash(invoice.getHash())
                .invoiceId(invoice.getInvoiceId())
                .orderId(invoice.getOrderId())
                .reverted(invoice.isReverted())
                .store(invoice.getStore())
                .turnoverGross(invoice.getTurnoverGross())
                .turnoverNet(invoice.getTurnoverNet())
                .invoiceItems(invoice.getInvoiceItems())
                .invoiceItemsGetraenke(getraenkeItems)
                .invoiceItemsEssen(essenItems)
                .invoiceItemsHendl(hendlItems)
                .invoiceItemsCafe(cafeItems)
                .build();

        ChangeInvoiceItem changeInvoiceItem = invoiceReportDto.getInvoiceItems().stream()
                .filter(x -> x instanceof ChangeInvoiceItem)
                .map(x -> (ChangeInvoiceItem)x).findFirst().get();

        if(changeInvoiceItem.getTurnover().setScale(2).equals(new BigDecimal("0.00"))) {
            invoiceReportDto.getInvoiceItems().removeIf(x -> x.getPosition() == changeInvoiceItem.getPosition());
        }

        return invoiceReportDto;
    }

    @RequestMapping(value = "/reprintInvoice", method = RequestMethod.POST)
    public void reprintInvoice(@RequestBody Invoice invoice, HttpServletResponse response, HttpServletRequest request) throws JRException, IOException {
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        InvoiceReportDto invoiceReportDto = generateInvoiceReportDtoFromInvoice(invoice);

        JRDataSource JRdataSource = new JRBeanCollectionDataSource(new ArrayList<>(Collections.singletonList(invoiceReportDto)));

        try {
            JasperReport jr = reportCompiledSingleton.getJr();
            JasperReport subOrderItemsCompiled = reportCompiledSingleton.getSubOrderItemsCompiled();
            JasperReport subPaymentCompiled = reportCompiledSingleton.getSubPaymentCompiled();
            JasperReport subTaxRatesCompiled = reportCompiledSingleton.getSubTaxRatesCompiled();

            parameterMap.put("subreportParameter", new ArrayList<>(Arrays.asList(subOrderItemsCompiled, subPaymentCompiled, subTaxRatesCompiled)));

            JasperPrint jp = JasperFillManager.fillReport(jr, parameterMap, JRdataSource);
            jp.getPropertiesMap().setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");

            File tempFile = File.createTempFile("temp", ".xml");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

            UniposXMLExporter exporter = new UniposXMLExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fileOutputStream);

            exporter.exportReport();

            printerRemoteInterface.reprintInvoice(tempFile, request);

            response.setStatus(200);

        } catch (Exception e) {
            log.error("Something went wrong during the Report generation ");
            LogDto logDto = LogDto.builder()
                    .dateTime(LocalDateTime.now())
                    .message("Something went wrong during the Report generation ")
                    .level(LogDto.Level.ERROR)
                    .source(this.getClass().getName() + "#" + "generatePdfStreamInvoiceReport")
                    .build();
            logDto.addParameter("ExceptionType", e.getClass().toString());
            logDto.addParameter("ExceptionMessage", e.getMessage());

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            logDto.addParameter("ExceptionStackTrace", errors.toString());
            logRemoteInterface.log(logDto);
            e.printStackTrace();
            throw e;
        }
    }

    @RequestMapping(value = "/printRevertedInvoice", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void generatePdfStreamInvoiceReport(@RequestBody ReversalInvoice invoice, HttpServletResponse response, HttpServletRequest request) throws JRException, IOException {
        log.info("--------------generate report----------");

        Map<String,Object> parameterMap = new HashMap<String,Object>();
        InvoiceReportDto invoiceReportDto = generateInvoiceReportDtoFromInvoice(invoice);

        JRDataSource JRdataSource = new JRBeanCollectionDataSource(new ArrayList<>(Collections.singletonList(invoiceReportDto)));

        try {
            JasperReport jr = reportCompiledSingleton.getJr();
            JasperReport subOrderItemsCompiled = reportCompiledSingleton.getSubOrderItemsCompiled();
            JasperReport subPaymentCompiled = reportCompiledSingleton.getSubPaymentCompiled();
            JasperReport subTaxRatesCompiled = reportCompiledSingleton.getSubTaxRatesCompiled();

            parameterMap.put("subreportParameter", new ArrayList<>(Arrays.asList(subOrderItemsCompiled, subPaymentCompiled, subTaxRatesCompiled)));

            JasperPrint jp = JasperFillManager.fillReport(jr, parameterMap, JRdataSource);
            jp.getPropertiesMap().setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");

            File tempFile = File.createTempFile("temp", ".xml");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

            UniposXMLExporter exporter = new UniposXMLExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fileOutputStream);

            exporter.exportReport();

            printerRemoteInterface.printRevertedInvoice(tempFile, invoice.getReversedInvoiceNumber(), request);

        } catch (Exception e) {
            log.error("Something went wrong during the Report generation ");
            LogDto logDto = LogDto.builder()
                    .dateTime(LocalDateTime.now())
                    .message("Something went wrong during the Report generation ")
                    .level(LogDto.Level.ERROR)
                    .source(this.getClass().getName() + "#" + "generatePdfStreamInvoiceReport")
                    .build();
            logDto.addParameter("ExceptionType", e.getClass().toString());
            logDto.addParameter("ExceptionMessage", e.getMessage());

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            logDto.addParameter("ExceptionStackTrace", errors.toString());
            logRemoteInterface.log(logDto);
            e.printStackTrace();
            throw e;
        }
        log.info("----DONE---");
    }

    @RequestMapping(value = "/getInvoiceText", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public String generateInvoiceText(@RequestBody Invoice invoice) throws JRException, IOException {
        log.info("--------------generate text report----------");

        Map<String,Object> parameterMap = new HashMap<String,Object>();
        InvoiceReportDto invoiceReportDto = generateInvoiceReportDtoFromInvoice(invoice);

        JRDataSource JRdataSource = new JRBeanCollectionDataSource(new ArrayList<>(Collections.singletonList(invoiceReportDto)));

        try {

            JasperReport jr = reportCompiledSingleton.getJr();
            JasperReport subOrderItemsCompiled = reportCompiledSingleton.getSubOrderItemsCompiled();
            JasperReport subPaymentCompiled = reportCompiledSingleton.getSubPaymentCompiled();
            JasperReport subTaxRatesCompiled = reportCompiledSingleton.getSubTaxRatesCompiled();

            parameterMap.put("subreportParameter", new ArrayList<>(Arrays.asList(subOrderItemsCompiled, subPaymentCompiled, subTaxRatesCompiled)));

            JasperPrint jp = JasperFillManager.fillReport(jr, parameterMap, JRdataSource);
            jp.getPropertiesMap().setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");


            File tempFile = File.createTempFile("temp", ".xml");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

            UniposXMLExporter exporter = new UniposXMLExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fileOutputStream);

            exporter.exportReport();

            String invoiceAsText = printerRemoteInterface.getInvoiceAsText(tempFile);

            return invoiceAsText;

        } catch (Exception e) {
            LogDto logDto = LogDto.builder()
                    .dateTime(LocalDateTime.now())
                    .message("Something went wrong during the Report generation ")
                    .level(LogDto.Level.ERROR)
                    .source(this.getClass().getName() + "#"+"generateInvoiceText")
                    .build();
            logDto.addParameter("ExceptionType", e.getClass().toString());
            logDto.addParameter("ExceptionMessage", e.getMessage());

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            logDto.addParameter("ExceptionStackTrace", errors.toString());
            logRemoteInterface.log(logDto);
            throw e;
        }
    }



    private void writeReportToResponseStream(HttpServletResponse response,
                                             ByteArrayOutputStream baos) {

        try {
//            response.setContentType("text/html; charset=UTF-8");
//            response.setCharacterEncoding("UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            baos.writeTo(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Takes the generated report and sends it back as a response HTML Stream
     * @param response The Response the Client gets
     * @param baos The Byte array that contains the data
     */
    private void writeReportToHtmlResponseStream(HttpServletResponse response,
                                             ByteArrayOutputStream baos) {

        try {
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            baos.writeTo(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
