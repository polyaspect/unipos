package unipos.pos.components.dailySettlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Token;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.common.remote.report.ReportRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.model.Workstation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 20.01.2016.
 */

@RestController
@RequestMapping(value = "/dailySettlements", produces = MediaType.APPLICATION_JSON_VALUE)
public class DailySettlementController {

    @Autowired
    DailySettlementService dailySettlementService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    ReportRemoteInterface reportRemoteInterface;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DailySettlement> findAll() {
        return dailySettlementService.findAll();
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String addNewSettlement(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Token token = authRemoteInterface.getAuthTokenByRequest(request);
        String deviceId = RequestHandler.getCookieValue(request, "DeviceToken");

        if (deviceId == null || deviceId.isEmpty()) {
            response.sendError(400, "No valid DeviceToken found");
            return null;
        }

        if (token == null || token.getUser() == null || token.getUser().getCompanyGuid() == null || token.getUser().getCompanyGuid().isEmpty()) {
            response.sendError(400, "No valid User found with the given AuthToken");
            return null;
        }

        Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);
        Store store = dataRemoteInterface.getControllerPlacedStore(token.getUser().getCompanyGuid());

        if (store == null) {
            response.sendError(400, "No Store was found for the Company the user is working for");
            return null;
        }

        if (workstation == null) {
            response.sendError(400, "No Workstation was found for the given DeviceId");
            return null;
        }

        DailySettlement result = dailySettlementService.addNewSettlement(store, workstation);

        if (result == null) {
            response.sendError(400, "Not able to create new Settlement");
            return null;
        }

        return result.getGuid();
    }

    @RequestMapping(value = "/monthly", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String addNewMonthlySettlement(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Token token = authRemoteInterface.getAuthTokenByRequest(request);
        String deviceId = RequestHandler.getCookieValue(request, "DeviceToken");

        if (deviceId == null || deviceId.isEmpty()) {
            response.sendError(400, "No valid DeviceToken found");
            return null;
        }

        if (token == null || token.getUser() == null || token.getUser().getCompanyGuid() == null || token.getUser().getCompanyGuid().isEmpty()) {
            response.sendError(400, "No valid User found with the given AuthToken");
            return null;
        }

        Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);
        Store store = dataRemoteInterface.getControllerPlacedStore(token.getUser().getCompanyGuid());

        if (store == null) {
            response.sendError(400, "No Store was found for the Company the user is working for");
            return null;
        }

        if (workstation == null) {
            response.sendError(400, "No Workstation was found for the given DeviceId");
            return null;
        }

        MonthlySettlement result = dailySettlementService.addNewMonthlySettlement(store, workstation);

        if (result == null) {
            response.sendError(400, "Not able to create new Settlement");
            return null;
        }

        return result.getGuid();
    }

    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public ResponseEntity<String> closeSettlement(@RequestParam("guid") String guid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
            String authToken = RequestHandler.getAuthToken(request);

            if (deviceToken == null || deviceToken.isEmpty()) {
                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("NO_DEVICE_TOKEN");
            }

            ;

            if(dailySettlementService.closeSettlement(guid)){
                reportRemoteInterface.printFinancialReportOfCurrentDay(authToken, deviceToken);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("SUCCESS");
    }

    @RequestMapping(value = "/closeMonthly", method = RequestMethod.POST)
    public ResponseEntity<String> closeMonthlySettlement(@RequestParam("guid") String guid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
            String authToken = RequestHandler.getAuthToken(request);

            if (deviceToken == null || deviceToken.isEmpty()) {
                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("NO_DEVICE_TOKEN");
            }
            reportRemoteInterface.printMonthlySettlementReport(authToken, deviceToken);
            dailySettlementService.closeMonthlySettlement(guid);
        } catch (Exception e) {
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("SUCCESS");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSettlement(@RequestBody String guid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
            if (deviceToken == null || deviceToken.isEmpty()) {
                response.sendError(400, "No DeviceToken found");
                return;
            }
            dailySettlementService.deleteSettlement(guid);
        } catch (Exception e) {

        }
    }

    @RequestMapping(value = "/deleteMonthly", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteMonthlySettlement(@RequestBody String guid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
            if (deviceToken == null || deviceToken.isEmpty()) {
                response.sendError(400, "No DeviceToken found");
                return;
            }
            dailySettlementService.deleteMonthlySettlement(guid);
        } catch (Exception e) {

        }
    }

    @RequestMapping(value = "/isOpen", method = RequestMethod.GET)
    public boolean isOpen(@RequestParam("guid") String guid) {
        return dailySettlementService.isOpen(guid);
    }

    @RequestMapping(value = {"/isCreationAllowed", "/isActionAllowed"}, method = RequestMethod.GET)
    public boolean isCreationAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deviceId = RequestHandler.getCookieValue(request, "DeviceToken");
        if (deviceId == null || deviceId.isEmpty()) {
            response.sendError(400, "No DeviceToken was found");
        }

        Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);
        if (workstation == null) {
            response.sendError(400, "No Device was found with the given DeviceId");
            return false;
        }

        User user = authRemoteInterface.getUserByGuid(workstation.getCashierId());

        if (user == null || user.getCompanyGuid() == null || user.getCompanyGuid().isEmpty()) {
            response.sendError(400, "No valid User found with the given AuthToken");
            return false;
        }
        Store store = dataRemoteInterface.getControllerPlacedStore(user.getCompanyGuid());

        if (store == null) {
            response.sendError(400, "No Store was found for the Company the user is working for");
            return false;
        }

        return dailySettlementService.isNewDailySettlementAllowed(store, workstation);
    }

    @RequestMapping(value = "lastCreated", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DailySettlement findLastCreatedDailySettlement(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Token token = authRemoteInterface.getAuthTokenByRequest(request);

        if (token == null || token.getUser() == null || token.getUser().getCompanyGuid() == null || token.getUser().getCompanyGuid().isEmpty()) {
            response.sendError(400, "No valid User found with the given AuthToken");
            return null;
        }

        Store store = dataRemoteInterface.getControllerPlacedStore(token.getUser().getCompanyGuid());

        if (store == null) {
            response.sendError(400, "No Store was found for the Company the user is working for");
            return null;
        }

        return dailySettlementService.getLastCreatedDailySettlementByStore(store);
    }

    @RequestMapping(value = "/lastClosedByStoreGuid", method = RequestMethod.GET)
    public DailySettlement findLastClosedDailySettlmentByStoreGuid(String storeGuid) {
        return dailySettlementService.getLastClosedDailySettlementByStore(storeGuid);
    }

    @RequestMapping(value = "/lastMonthlyClosedByStoreGuid", method = RequestMethod.GET)
    public MonthlySettlement findLastMonthlyClosedDailySettlmentByStoreGuid(String storeGuid) {
        return dailySettlementService.getLastClosedMonthlySettlementByStore(storeGuid);
    }

    @RequestMapping(value = "/lastClosed", method = RequestMethod.GET)
    public DailySettlement findLastClosedDailySettlment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Token token = authRemoteInterface.getAuthTokenByRequest(request);

        if (token == null || token.getUser() == null || token.getUser().getCompanyGuid() == null || token.getUser().getCompanyGuid().isEmpty()) {
            response.sendError(400, "No valid User found with the given AuthToken");
            return null;
        }

        Store store = dataRemoteInterface.getControllerPlacedStore(token.getUser().getCompanyGuid());

        if (store == null) {
            response.sendError(400, "No Store was found for the Company the user is working for");
            return null;
        }
        return dailySettlementService.getLastClosedDailySettlementByStore(store.getGuid());
    }

    @RequestMapping(value = "/previewFinancialReport", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<ThermalPrinterLine> previewFinancialReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");
        String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");

        if (deviceToken == null || authToken == null) {
            response.sendError(400, "No Device or AuthToken found in the given Request");
            return null;
        }

        return reportRemoteInterface.previewFinancialReport(authToken, deviceToken);
    }

    @RequestMapping(value = "/findDailySettlementsByStoreGuidAndDateTimeBetween", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DailySettlement> findByStoreGuidAndDateTimeBetween(@RequestParam(required = true) String storeGuid, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Comparator<DailySettlement> sortedByDateTime = Comparator.comparing(DailySettlement::getDateTime);
        return dailySettlementService.findByStoreGuidAndDateTimeBetween(storeGuid, startDate, endDate).stream().sorted(sortedByDateTime).collect(Collectors.toList());
    }

    @RequestMapping(value = "/checkChain", method = RequestMethod.GET)
    public List<DailySettlement> checkDailySettlementChain(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return dailySettlementService.checkDailySettlementChain(from, to);
    }
}
