package unipos.printer.components.printer;

import org.springframework.dao.DataRetrievalFailureException;
import unipos.common.remote.data.model.company.Store;
import unipos.printer.components.printer.model.Printer;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by dominik on 09.09.15.
 */
public interface PrinterService {

    Printer getPrinterByDbId(String documentId);

    List<Printer> findAllPrinters();

    Printer findByIpAddress(String ipAddress);

    void deleteByDbId(String documentId);

    Printer savePrinter(Printer printer);

    Printer findByDocumentId(String documentId);

    Printer findDefaultPrinterByDevice(String deviceGuid);

    Printer findDefaultPrinterOfStore(String storeGuid);

    Printer findByPrinterId(Long printerId);

    List<Printer> findByStore(Store store);

    List<Printer> findByStoreGuid(String storeGuid);

    Printer findDefaultPrinter(HttpServletRequest request) throws IllegalArgumentException, DataRetrievalFailureException;

    Printer findDefaultPrinter();
}
