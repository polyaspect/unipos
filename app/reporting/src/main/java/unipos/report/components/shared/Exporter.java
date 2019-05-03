package unipos.report.components.shared;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import java.io.ByteArrayOutputStream;

/**
 * Everything under the package org.krams.tutorial.jasper are settings imposed by Jasper (not DynamicJasper)
 * <p>



 * An exporter for exporting the report in various formats, i.e Excel, PDF, CSV. Here we declare a PDF exporter
 */
public class Exporter {

    /**
     * Exports a report to XLS (Excel) format. You can declare another export here, i.e PDF or CSV.
     * You don't really need to create a separate class or method for the exporter. You can call it
     * directly within your Service or Controller.
     *
     * @param jp the JasperPrint object
     * @param baos the ByteArrayOutputStream where the report will be written
     */
    public void export(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
// Create a JRXlsExporter instance
        JRHtmlExporter exporter = new JRHtmlExporter();
        StringBuffer sb = new StringBuffer();

// Here we assign the parameters jp and baos to the exporter
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
        exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
        exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
        exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, true);
        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
        exporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "UTF-8");


        exporter.exportReport();
    }
}
