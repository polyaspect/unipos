package unipos.printer.components.printer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import unipos.common.container.RequestHandler;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.printer.components.printer.model.Printer;
import unipos.printer.components.sequence.ProductLogSequenceRepository;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * Created by dominik on 09.09.15.
 */

@Service
public class PrinterServiceImpl implements PrinterService {

    @Autowired
    PrinterRepository printerRepository;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    ProductLogSequenceRepository sequenceRepository;

    @Override
    public Printer getPrinterByDbId(String documentId) {
        return printerRepository.findOne(documentId);
    }

    @Override
    public List<Printer> findAllPrinters() {
        return printerRepository.findAll();
    }

    @Override
    public Printer findByIpAddress(String ipAddress) {
        return null;/*printerRepository.findFirstByIpAddress(ipAddress);*/
    }

    @Override
    public void deleteByDbId(String documentId) {
        printerRepository.delete(documentId);
    }

    @Override
    public Printer savePrinter(Printer printer) {
        if(printer.getId() == null || printer.getId().isEmpty()) {
            printer.setPrinterId(sequenceRepository.getNextSequenceId("PRINTER"));
            printer.setGuid(UUID.randomUUID().toString());
        }
        if(printer.isDefaultPrinter()) {
            Printer oldDefaultPrinter = printerRepository.findFirstByDefaultPrinter(true);
            if(oldDefaultPrinter != null) {
                oldDefaultPrinter.setDefaultPrinter(false);
                printerRepository.save(oldDefaultPrinter);
            }
        }
        printerRepository.save(printer);
        return printer;
    }

    @Override
    public Printer findByDocumentId(String documentId) {
        return printerRepository.findOne(documentId);
    }

    public Printer findDefaultPrinterByDevice(String deviceGuid) {
        unipos.common.remote.socket.model.Printer devicePrinterEntity = socketRemoteInterface.findDeviceDefaultPrinter(deviceGuid);

        if(devicePrinterEntity != null) {
            return printerRepository.findByGuid(devicePrinterEntity.getPrinterGuid());
        } else {
            return null;
        }
    }

    public Printer findDefaultPrinterOfStore(String storeGuid) {
        return printerRepository.findFirstByDefaultPrinterAndStoresIn(true, storeGuid);
    }

    @Override
    public Printer findByPrinterId(Long printerId) {
        return printerRepository.findByPrinterId(printerId);
    }

    @Override
    public List<Printer> findByStore(Store store) {
        if(store == null || store.getGuid() == null || store.getGuid().isEmpty()) {
            return null;
        }
        return printerRepository.findByStoresIn(store.getGuid());
    }

    @Override
    public List<Printer> findByStoreGuid(String storeGuid) {
        if(storeGuid == null || storeGuid.isEmpty()) {
            return null;
        }
        return printerRepository.findByStoresIn(storeGuid);
    }

    @Override
    public Printer findDefaultPrinter(HttpServletRequest request) throws IllegalArgumentException, DataRetrievalFailureException {
        String deviceToken = RequestHandler.getDeviceToken(request);

        Printer printer = findDefaultPrinterByDevice(deviceToken);

        if(printer == null) {
            Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
            if(store == null) {
                throw new DataRetrievalFailureException("For the given AuthToken And DeviceToken is no Store available");
            }

            printer = findDefaultPrinterOfStore(store.getGuid());
        }

        if(printer == null) {
            throw new DataRetrievalFailureException("No default Printer found for the given Device, AND the Store you are working in");
        }

        return printer;
    }

    @Override
    public Printer findDefaultPrinter() throws IllegalArgumentException, DataRetrievalFailureException {
        return printerRepository.findFirstByDefaultPrinter(true);
    }
}
