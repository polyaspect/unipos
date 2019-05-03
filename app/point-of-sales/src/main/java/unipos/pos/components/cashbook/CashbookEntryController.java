package unipos.pos.components.cashbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Token;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.report.ReportRemoteInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Dominik on 16.01.2016.
 */

@RestController
@RequestMapping("/cashbook")
public class CashbookEntryController {

    @Autowired
    CashbookEntryService cashbookEntryService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    ReportRemoteInterface reportRemoteInterface;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CashbookEntry> findAll(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        Assert.notNull(store, "The store must not be null");

        return cashbookEntryService.findAllByStore(store);
    }

    @RequestMapping(value = "/sinceLastDailySettlement", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CashbookEntry> findAllSinceLastDailySettlement(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        Assert.notNull(store, "The store must not be null");

        return cashbookEntryService.findAllByStoreSinceLastDailySettlement(store);
    }

    @RequestMapping(value = "/guid", method = RequestMethod.GET)
    public CashbookEntry findByGuid(@RequestParam("guid") String guid, HttpServletResponse response) throws IOException {
        if (guid == null || guid.isEmpty()) {
            response.sendError(400, "The guid Param was not set");
        } else {
            return cashbookEntryService.findByGuid(guid);
        }
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createCashbookEntry(@RequestBody CashbookEntry cashbookEntry, HttpServletRequest request) {
        if (cashbookEntry.getId() == null || cashbookEntry.getId().isEmpty()) {
            Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
            Token authToken = authRemoteInterface.getAuthTokenByRequest(request);
            cashbookEntryService.addCashbookEntry(cashbookEntry, store, authToken.getUser());
        } else {
            cashbookEntryService.updateCashbookEntry(cashbookEntry);
        }
        reportRemoteInterface.printCashbookEntry(cashbookEntry.asDto(), RequestHandler.getAuthToken(request), RequestHandler.getDeviceToken(request));
    }

    @RequestMapping(value = "/guid", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteByGuid(@RequestParam("guid") String guid, HttpServletResponse response) throws IOException {
        if (guid == null || guid.isEmpty()) {
            response.sendError(400, "The guid Param was not set");
        } else {
            cashbookEntryService.deleteByGuid(guid);
        }
    }

    @RequestMapping(value = "/getCurrentCashStatus", method = RequestMethod.GET)
    public BigDecimal getCurrentCashStatus(@RequestParam(required = false) String storeGuid, HttpServletRequest request) {
        Store store = null;
        if (storeGuid == null) {
            store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        } else {
            store = dataRemoteInterface.getStoreByGuid(storeGuid);
        }

        return cashbookEntryService.getCurrentCashStatus(store);
    }

    @RequestMapping(value = "/adjustCashStatus", method = RequestMethod.POST)
    public CashbookEntry adjustCashStatus(@RequestParam String storeGuid, HttpServletRequest request){
        User user = authRemoteInterface.findUserByAuthToken(RequestHandler.getAuthToken(request));
        Store store = dataRemoteInterface.getStoreByGuid(storeGuid);

        if(store != null && user != null) {
            return cashbookEntryService.adjustCashStatus(store, user.getGuid());
        }else{
            return null;
        }
    }

    @RequestMapping(value = "/isAdjustmentNecessary", method = RequestMethod.GET)
    public CashbookEntry isAdjustmentNecessary(@RequestParam String storeGuid){
        Store store = dataRemoteInterface.getStoreByGuid(storeGuid);
        if(store != null){
            CashbookEntry entry = cashbookEntryService.getAdjustmentCashbookEntry(store);
            return entry;
        }else{
            return null;
        }
    }
}
