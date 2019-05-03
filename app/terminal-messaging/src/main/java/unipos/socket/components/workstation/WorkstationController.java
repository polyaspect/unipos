package unipos.socket.components.workstation;

import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 28.12.2015.
 */
@RestController
@RequestMapping("/device")
public class WorkstationController {

    @Autowired
    WorkstationService workstationService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Workstation> findAll(HttpServletRequest request, HttpServletResponse response) {
        return workstationService.findAll();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Workstation findAndRemember(HttpServletRequest request) {
        String deviceToken = RequestHandler.getDeviceToken(request);

        Workstation ws = workstationService.findByDeviceId(deviceToken);

        // Check if Device still has a store. If not, force a reload of the app.
        try{
            Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
            if(store == null){
                ws.forceReload = true;
            }
        }
        catch(Exception ex){
            ws.forceReload = true;
        }

        ws.setLastKeepAlive(LocalDateTime.now());
        workstationService.updateWorkstation(ws);
        return ws;
    }

    @RequestMapping(value = "/guid", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deletebyGuid(@RequestParam("guid") String guid) {
        workstationService.deleteByGuid(guid);
    }

    @ApiOperation("Save a new Device")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void saveTaxRate(@RequestBody Workstation workstation) {
        if (workstation.getId() != null && !workstation.getId().isEmpty())
            workstationService.updateWorkstation(workstation);
        else
            workstationService.saveWorkstation(workstation);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void createNewDevice(@RequestParam String storeGuid, @RequestParam String deviceName, HttpServletResponse response, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        Workstation workstation = workstationService.createNewDevice(storeGuid, deviceName, ipAddress);

        if (workstation.getDeviceId() != null && !workstation.getDeviceId().isEmpty()) {
            Cookie cookie = new Cookie("DeviceToken", workstation.getDeviceId());
            cookie.setPath("/");
            cookie.setMaxAge(315360000);
            response.addCookie(cookie);
        }
    }

    @RequestMapping(value = "/set", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setDevice(@RequestBody String deviceId, HttpServletResponse response, HttpServletRequest request) {
        Workstation workstation = workstationService.findByDeviceId(deviceId);

        if (workstation.getDeviceId() != null && !workstation.getDeviceId().isEmpty()) {
            Cookie cookie = new Cookie("DeviceToken", workstation.getDeviceId());
            cookie.setPath("/");
            cookie.setMaxAge(315360000);
            response.addCookie(cookie);
        }
    }

    @RequestMapping(value = "/replace", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void replaceDevice(@RequestParam(required = false) String deviceId, @RequestParam String storeGuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ipAddress = request.getRemoteAddr();

        //If theres no deviceId try to use the DeviceToken Cookie
        if (deviceId == null || deviceId.isEmpty()) {
            if (request.getCookies() == null) {
                response.sendError(400, "You have to specify a deviceId or have a valid Device Token to replace a Device");
                return;
            }
            List<Cookie> cookies = Arrays.asList(request.getCookies()).stream().filter(cookie -> cookie.getName().equals("DeviceToken")).collect(Collectors.toList());
            if (cookies.size() > 0) {
                deviceId = cookies.get(0).getValue();
            }
        }

        if (workstationService.findByDeviceId(deviceId) == null) {
            response.sendError(400, "For the DeviceId you provided is no Device stored inside the Database. Please provide a valid DeviceId. ");
            return;
        }

        Workstation workstation = workstationService.replaceDevice(deviceId, ipAddress, storeGuid);

        Cookie cookie = new Cookie("DeviceToken", workstation.getDeviceId());
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setPath("/");
        cookie.setMaxAge(315360000);
        response.addCookie(cookie);
    }

    @RequestMapping(value = "/addStoreToDevice", method = RequestMethod.POST)
    public void addStoreToDevice(@RequestParam(required = false) String deviceName, @RequestParam String storeGuid, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String deviceId = "";

        try {
            deviceId = RequestHandler.getDeviceToken(request);
        } catch (Exception ex) {

        }

        Workstation workstation = workstationService.findByDeviceId(deviceId);

        if (workstation == null) {
            String ipAddress = request.getRemoteAddr();
            workstation = workstationService.createNewDevice(storeGuid, deviceName, ipAddress);

            if (workstation.getDeviceId() != null && !workstation.getDeviceId().isEmpty()) {
//                Cookie cookie = new Cookie("DeviceToken", workstation.getDeviceId());
//                cookie.setPath("/");
//                cookie.setMaxAge(Integer.MAX_VALUE);
//
//                response.addCookie(cookie);
                RequestHandler.addCookie(response, "DeviceToken", workstation.getDeviceId());
            }
        } else {
            if (workstation.getStores().stream().noneMatch(guid -> guid.equals(storeGuid))) {
                workstation.getStores().add(storeGuid);
                workstationService.saveWorkstation(workstation);
            }
        }
    }

    @RequestMapping(value = "/addStoreAndPrinterToNewDevice", method = RequestMethod.POST)
    public void addStoreAndPrinterToDevice(@RequestParam(required = false) String deviceId, @RequestParam(required = false) String deviceName, @RequestParam String storeGuid,  @RequestParam String printerGuid, HttpServletResponse response, HttpServletRequest request) throws IOException {

        Printer printer = Printer.builder().defaultPrinter(true).printerGuid(printerGuid).build();

        if (deviceId == null || workstationService.findByDeviceId(deviceId) == null) {
            String ipAddress = request.getRemoteAddr();
            Workstation workstation = workstationService.createNewDevice(storeGuid, deviceName, ipAddress);
            deviceId = workstation.getDeviceId();
            workstationService.addPrinterToDevice(printer, deviceId);
            return;
        }

        workstationService.addStoreToDevice(storeGuid, deviceId);
        workstationService.addPrinterToDevice(printer, deviceId);
        return;
    }

    @RequestMapping(value = "/deviceId/{deviceId}", method = RequestMethod.GET)
    public Workstation findByDeviceId(@PathVariable String deviceId) {
        return workstationService.findByDeviceId(deviceId);
    }

    @RequestMapping(value = "/storeGuid/{storeGuid}", method = RequestMethod.GET)
    public List<Workstation> findByStoreGuid(@PathVariable("storeGuid") String storeGuid) {
        return workstationService.findByStoreGuid(storeGuid);
    }

    @RequestMapping(value = "/setCashierForWorkstation", method = RequestMethod.POST)
    public void saveCashierTo(@RequestBody String cashierId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deviceId = RequestHandler.getDeviceToken(request);
        String authToken = RequestHandler.getAuthToken(request);
        if (deviceId == null || deviceId.equals("") || authToken == null || authToken.isEmpty()) {
            response.sendError(400, "You have to specify a deviceId or have a valid Device Token to replace a Device");
            return;
        }
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        if(store == null) {
            return;
        }

        workstationService.setCashierForWorkstation(cashierId, deviceId, store.getGuid());
    }


    @RequestMapping(value = "/addPrinterToDevice", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addPrinterToDevice(@RequestBody Printer printer, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deviceToken;
        try {
            deviceToken = RequestHandler.getDeviceToken(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return;
        }

        try {
            workstationService.addPrinterToDevice(printer, deviceToken);
        } catch (Exception e) {
            response.sendError(500, e.getMessage());
            return;
        }
    }

    @RequestMapping(value = "/findDefaultPrinterOfDevice", method = RequestMethod.GET)
    public Printer findDefaultPrinterOfDevice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deviceToken;
        try {
            deviceToken = RequestHandler.getDeviceToken(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return null;
        }

        return workstationService.findDefaultPrinterOfDevice(deviceToken);
    }

    @RequestMapping(value = "/findDefaultPrinterOfDeviceId", method = RequestMethod.GET)
    public Printer findDefaultPrinterOfDeviceId(@RequestParam String deviceId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return workstationService.findDefaultPrinterOfDevice(deviceId);
    }

    @RequestMapping(value = "/hasDefaultPrinter", method = RequestMethod.GET)
    public boolean hasDefaultPrinter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deviceToken;

        try {
            deviceToken = RequestHandler.getDeviceToken(request);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return workstationService.hasDefaultPrinter(deviceToken);
    }

    @RequestMapping(value = "/getStoreByDevice", method = RequestMethod.GET)
    public Store getStoreByDevice(HttpServletRequest request, HttpServletResponse response) {
        String deviceToken = RequestHandler.getDeviceToken(request);
        String storeId = workstationService.findByDeviceId(deviceToken).getCurrentStoreId();
        return dataRemoteInterface.getStoreByGuid(storeId);
    }

    @RequestMapping(value = "/getStoreByDeviceId", method = RequestMethod.GET)
    public Store getStoreByDeviceId(@RequestParam String deviceId, HttpServletRequest request, HttpServletResponse response) {
        String storeId = workstationService.findByDeviceId(deviceId).getCurrentStoreId();
        return dataRemoteInterface.getStoreByGuid(storeId);
    }

    @RequestMapping(value = "/reloaded", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void reloaded(HttpServletRequest request, HttpServletResponse response) {
        String deviceToken = RequestHandler.getDeviceToken(request);
        Workstation workstation = workstationService.findByDeviceId(deviceToken);
        workstation.setReloadRequired(false);
        workstation.setForceReload(false);
        workstation.setReloadWhenPossible(false);
        workstation.setLastReload(LocalDateTime.now());
        workstationService.updateWorkstation(workstation);
    }
}