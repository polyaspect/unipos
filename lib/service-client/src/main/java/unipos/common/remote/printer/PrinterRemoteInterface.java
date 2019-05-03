package unipos.common.remote.printer;

import unipos.common.remote.printer.model.ThermalPrinterLine;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * Created by Dominik on 03.12.2015.
 */
public interface PrinterRemoteInterface {
    void printUniposXmlFile(File uniposXmlFile, HttpServletRequest request);
    String getInvoiceAsText(File uniposXmlFile);
    void reprintInvoice(File uniposXmlFile, HttpServletRequest request) throws UniposRemoteInterfaceException;
    void printText(List<String> lines, HttpServletRequest request);
    void printTextWithEscSeq(List<ThermalPrinterLine> escapeSequences, HttpServletRequest request);

    void printRevertedInvoice(File uniposXmlFile, Long invoiceNumber, HttpServletRequest request);

    String getCurrentVersion();
}
