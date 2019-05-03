package unipos.printer.components.printer;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.printer.components.printer.model.Printer;

import java.util.List;

/**
 * Created by dominik on 09.09.15.
 */
public interface PrinterRepository extends MongoRepository<Printer, String> {
//    Printer findFirstByIpAddress(String ipAddress);

    Printer findFirstByDefaultPrinterAndStoresIn(boolean isDefaultPrinter, String storeGuid);

    Printer findByPrinterId(Long printerId);

    List<Printer> findByStoresIn(String guid);

    Printer findByGuid(String printerGuid);

    Printer findFirstByDefaultPrinter(boolean isDefault);
}
