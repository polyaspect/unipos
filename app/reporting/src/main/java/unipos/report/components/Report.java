package unipos.report.components;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ReversalInvoice;
import unipos.report.components.shared.WriteToResponseStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 23.08.2016.
 */
public abstract class Report<T> {

    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    PosRemoteInterface posRemoteInterface;

    public abstract List<T> getDatasource(LocalDate startDate, LocalDate endDate, String storeGuid);

    public abstract JasperReport getMainReport() throws JRException;

    public abstract String getFileName();

    public abstract Map<String, Object> additionalParameters() throws JRException;

//    public abstract List<T> generateReportData(List<Invoice> invoiceList);

    public List<Invoice> getInvoices(LocalDate startDate, LocalDate endDate, String storeGuid) {
        List<Invoice> invoiceList = posRemoteInterface.findByCreationDateBetween(startDate.atTime(0, 0, 0, 0), endDate.atTime(23, 59, 59, 999));

        invoiceList = invoiceList.stream().filter(invoice -> invoice.getStore().getGuid().equals(storeGuid) && !invoice.isReverted() && !(invoice instanceof ReversalInvoice)).collect(Collectors.toList());

        return invoiceList;
    }

    private JasperPrint getReport(LocalDate startDate, LocalDate endDate, String storeGuid) throws JRException {
        Map<String, Object> parameterMap = new HashMap<>();

        List<T> datasource = getDatasource(startDate, endDate, storeGuid);
        JRDataSource JRdataSource = new JRBeanCollectionDataSource(datasource);

        JasperReport jr = getMainReport();

        Store store = dataRemoteInterface.getStoreByGuid(storeGuid);
        Company company = dataRemoteInterface.getCompanyByGuid(store.getCompanyGuid());

        Locale locale = new Locale("de", "AT");

        parameterMap.put(JRParameter.REPORT_LOCALE, locale);
        parameterMap.put("CompanyName", company.getName());
        parameterMap.put("CompanyAddress", store.getAddress().getStreet());
        parameterMap.put("CompanyZIPCode", store.getAddress().getPostCode() + " " + store.getAddress().getCity());
        parameterMap.put("StoreName", store.getName());
        parameterMap.put("StartDate", startDate);
        parameterMap.put("EndDate", endDate);

        Map<String, Object> additionalParameters = additionalParameters();
        if (additionalParameters != null) {
            additionalParameters.forEach(parameterMap::putIfAbsent);
        }

        JasperPrint jp = JasperFillManager.fillReport(jr, parameterMap, JRdataSource);

        if (jp.getPages().size() > datasource.size()) {
            jp.getPages().remove(jp.getPages().size() - 1);
        }

        return jp;
    }

    public final void printReportAsPdf(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException {
        JasperPrint print = getReport(startDate, endDate, storeGuid);

        String fileName = getFileName();

        System.out.println(fileName);
        File pdf = File.createTempFile(fileName, ".pdf");
        JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));

        System.out.println(pdf.getAbsolutePath());
        WriteToResponseStream.writePdfToResponseStream(response, pdf, fileName);

        pdf.delete();
    }

    public final void printReportAsHtml(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException {
        JasperPrint print = getReport(startDate, endDate, storeGuid);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HtmlExporter exporter = new HtmlExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(baos));
        exporter.exportReport();

        response.setContentType("text/html");
        response.setContentLength(baos.size());

        ServletOutputStream outputStream = response.getOutputStream();
        baos.writeTo(outputStream);
        outputStream.flush();
    }
}
