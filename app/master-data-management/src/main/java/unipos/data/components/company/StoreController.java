package unipos.data.components.company;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Token;
import unipos.data.components.company.model.Store;
import unipos.data.components.exception.DataNotFoundException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Created by dominik on 28.07.15.
 */

@RestController
@Api("/stores")
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    StoreService storeService;
    @Autowired
    AuthRemoteInterface authRemoteInterface;

    //List all Stores

    @ApiOperation(value = "Return all stored Stores",
        response = Store.class,
        responseContainer = "List")
    @ApiResponse(code = 400, message = "There is not Element Stored inside the Database")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Store> findAllStores(HttpServletResponse response) {
        List<Store> stores = storeService.findAll();
        if(stores != null) {
            return stores;
        } else {
            response.setStatus(400);
            return null;
        }
    }

    @ApiOperation(value = "Return all stored Stores",
            response = Store.class,
            responseContainer = "List")
    @ApiResponse(code = 400, message = "There is not Element Stored inside the Database")
    @RequestMapping(value = "/findByUser", method = RequestMethod.GET)
    public List<Store> findStoresByUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authToken = "";

        authToken = RequestHandler.getCookieValue(request, "AuthToken");

        if(authToken == null || authToken.isEmpty()) {
            response.sendError(400, "No Cookie 'AuthToken' was sent");
            return null;
        }


        List<Store> stores = storeService.findByUser(authToken);
        if(stores != null) {
            return stores;
        } else {
            response.setStatus(400);
            return null;
        }
    }

    @RequestMapping(value = "/findByUserId", method = RequestMethod.GET)
    public List<Store> findStoresByUser(@RequestParam String userGuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Store> stores = storeService.findByUserId(userGuid);
        if(stores != null) {
            return stores;
        } else {
            response.setStatus(400);
            return null;
        }
    }
    @RequestMapping(value = "/guid", method = RequestMethod.GET)
    public List<String> findAllStoreStrings() {
        return storeService.findAll().parallelStream().map(Store::getGuid).collect(toList());
    }

    @RequestMapping(value = "/companyGuid/{companyGuid}", method = RequestMethod.GET)
    public List<String> findAllStoreStringsByCompanyGuid(@PathVariable("companyGuid") String companyGuid, HttpServletResponse response) throws IOException {

        if(companyGuid == null || companyGuid.isEmpty()) {
            response.sendError(400, "No companyGuid found");
            return null;
        }

        return storeService.findByCompanyGuid(companyGuid).stream().map(Store::getGuid).collect(Collectors.toList());
    }

    //Save a new Store

    @ApiOperation("Save a new Store")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void saveStore(@ApiParam @RequestBody Store store) {
        if(store.getId() != null && !store.getId().isEmpty())
            storeService.updateStore(store);
        else
            storeService.saveStore(store);
    }

    //Delete a Store

    @ApiOperation("Delete a Store by its Mongo-DB ID")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ApiResponse(code = 404, message = "Unable to Delete Store with the given Mongo-DB ID")
    public void deleteStoreByMongoId(@RequestParam("id") String dbId, HttpServletResponse response) {
        try {
            storeService.deleteByDocumentId(dbId);
        } catch (IllegalArgumentException e) {
            response.setStatus(404);
        }
    }

    @RequestMapping(value = "/guid", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteByGuid(@RequestParam("guid") String guid) {
        storeService.deleteByGuid(guid);
    }

    @RequestMapping(value = "/addStoreToCompany", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addStoreToCompany(@RequestParam("storeGuid") String storeGuid, @RequestParam("companyGuid") String companyGuid) {
        storeService.addStoreToCompany(storeGuid, companyGuid);
    }

    @RequestMapping(value = "/getByUserIdAndDeviceId", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Store getStoreByUserAndDevice(@RequestParam("userId") String userId, @RequestParam("deviceId") String deviceId) throws IOException {
        return storeService.getStoreByUserIdAndDeviceId(userId, deviceId);
    }

    @RequestMapping(value = "/getByUserAndDevice", method = RequestMethod.GET)
    public Store getStoreByUserAndDevice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deviceId = "";
        String authToken = "";

        if(request.getCookies() != null && request.getCookies().length >= 2) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("AuthToken")) {
                    authToken = cookie.getValue();
                }
                if(cookie.getName().equals("DeviceToken")) {
                    deviceId = cookie.getValue();
                }
            }
        }

        if(deviceId.isEmpty() || authToken.isEmpty()) {
            response.sendError(400, "You didn't have a AuthToken Or DeviceToken as Cookies in this Request");
            return null;
        }

        return storeService.getStoreByUserAndDevice(authToken, deviceId);
    }

    @RequestMapping(value = "/getByGuid/{guid}", method = RequestMethod.GET)
    public Store getStoreByGuid(@PathVariable("guid") String guid) throws IOException {
        return storeService.findByGuid(guid);
    }

    @RequestMapping(value = "/controllerPlacedStore/{companyGuid}", method = RequestMethod.GET)
    public Store getControllerPlacedStore(@PathVariable("companyGuid") String companyGuid) throws IOException {
        return storeService.getControllerPlacedStore(companyGuid);
    }

    @RequestMapping(value = "/getByControllerPlaced", method = RequestMethod.GET)
    public List<Store> findAllControllerPlacedStores() {
        return storeService.getControllerPlacedStores();
    }

}
