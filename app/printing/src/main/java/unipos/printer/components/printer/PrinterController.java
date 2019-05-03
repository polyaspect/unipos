package unipos.printer.components.printer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.printer.components.printer.model.Printer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by dominik on 09.09.15.
 */

@RestController
@RequestMapping(value = "/printers", produces = "application/json")
public class PrinterController {

    @Autowired
    PrinterService printerService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Printer> findAllPrinters() {
        List<Printer> printers = printerService.findAllPrinters();

        return printers;
    }

    @RequestMapping(value = "/dbId/{documentId}", method = RequestMethod.GET)
    public Printer findPrinterByDbId(@PathVariable("documentId") String documentId) {
        return printerService.findByDocumentId(documentId);
    }

    @RequestMapping(value = "/ipAddress/{ipAddress}")
    public Printer findPrinterByIpAddress(@PathVariable("ipAddress") String ipAddress) {
        return printerService.findByIpAddress(ipAddress);
    }

    @RequestMapping(value = "/dbId", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePrinterByDbId(@RequestParam("documentId") String documentId) {
        printerService.deleteByDbId(documentId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Printer savePrinter(@RequestBody Printer printer, HttpServletResponse response) throws IOException {
        Printer printer1 = printerService.savePrinter(printer);

        if(printer1 == null) {
            response.sendError(500, "Something went wrong during the Printer creation");
            return null;
        }

        return printer1;
    }

    @RequestMapping(value = "/findPrinterByUsersStore", method = RequestMethod.GET)
    public List<Printer> findPrinterByUsersStore(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authToken;
        String deviceToken;

        //Just to check if theres an Auth- and Device Token
        try {
            RequestHandler.getAuthToken(request);
            RequestHandler.getDeviceToken(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return null;
        }

        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        if(store == null) {
            response.sendError(400, "There was no store associated for the given AuthToken and DeviceToken");
            return null;
        }

        return printerService.findByStore(store);
    }

}
