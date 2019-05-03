package unipos.socket.components.socket;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.Module;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.socket.components.shared.UrlContainer;
import unipos.socket.components.workstation.Workstation;
import unipos.socket.components.workstation.WorkstationService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Dominik on 12.08.2015.
 */

@RestController
@RequestMapping(value = "/", produces = "application/json")
@Api
public class SocketController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    WorkstationService workstationService;

    @Autowired
    CoreRemoteInterface coreRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    LogRemoteInterface logRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    RestTemplate restTemplate = new RestTemplate();

    @MessageMapping("/action")
    public OutputMessage sendMessage(String message) {
        return new OutputMessage(new Message(1, "LOL"), new Date());
    }

    /**
     * Use this method to send data to all registered subscribers
     *
     * @param target is the name of the subscription, f.i. /topic/action
     */
    @RequestMapping(value = "/sendToAll", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Send an information String (@param data) to the given @target")
    public void sendToAll(@RequestParam("target") String target, @RequestParam("data") String data) {
        this.template.convertAndSend(target, new TextMessage(data));
    }

    @RequestMapping(value = "/sendToUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Send an information to ONE client instance (@param target). The payload that the client receives will be @param data")
    public void sendToUser(@RequestParam("userId") String userId, @RequestParam("target") String target, @RequestParam("data") String data) {
        template.setUserDestinationPrefix("/user/");
        template.convertAndSendToUser(userId, target, new TextMessage(data));
    }

    @RequestMapping(value = "/sendToDevice", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Send an information to ONE client instance (@param target). The payload that the client receives will be @param data")
    public void sendToDevice(@RequestParam("deviceToken") String deviceToken, @RequestParam("target") String target, @RequestParam("data") String data) {
        template.setUserDestinationPrefix("/device/");

        template.convertAndSendToUser(deviceToken, target, new TextMessage(data));
    }

    @RequestMapping(value = "/clientInformation/{authToken}", method = RequestMethod.GET)
    public Workstation getClientInformationByAuthToken(@PathVariable("authToken") String authToken, HttpServletResponse response) {
        if (authToken != null) {
            Workstation lastWorkstation = workstationService.findByAuthToken(authToken);
            if (lastWorkstation != null) {
                return lastWorkstation;
            } else {
                response.setStatus(404);
                return null;
            }
        } else {
            response.setStatus(404);
            return null;
        }
    }

    @RequestMapping(value = "/getDeviceId", method = RequestMethod.GET)
    public String getDeviceId(HttpServletRequest request) {
        ArrayList<Cookie> cookies = new ArrayList<>(Arrays.asList(request.getCookies()));
        if (cookies.stream().anyMatch(x -> x.getName().equals("AuthToken"))) {
            String authToken = cookies.stream().filter(x -> x.getName().equals("AuthToken")).findFirst().get().getValue();

            Workstation workstation = workstationService.findByAuthToken(authToken);
            String deviceId = workstation.getDeviceId();

            return deviceId;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/clientStarted", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> clientStarted(HttpServletRequest request) throws RestClientException {
        try {
            Module module = coreRemoteInterface.startedModules().stream().filter(x -> x.getName().equalsIgnoreCase("design")).findFirst().orElseThrow(() -> new RestClientException("No Design Module has been started!!!"));
            String url = UrlContainer.BASE_URL + "/" + module.getName() + UrlContainer.CLIENT_CONNECTED + "?deviceToken=" + RequestHandler.getCookieValue(request, "DeviceToken");
            restTemplate.getForObject(url, Void.class);
        } catch (Exception ex) {
            throw new RestClientException("Could not access list of installed modules. Check tomcat credentials and make sure tomcat-manager is up and running.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/checkDeviceToken", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void checkDeviceToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String deviceId = RequestHandler.getCookieValue(request, "DeviceToken");
            if (workstationService.findByDeviceId(deviceId) == null) {
                RequestHandler.deleteCookie(request, response, "DeviceToken");
            }
        } catch (Exception ex) {
            throw new RestClientException("An Error occured during the DeviceToken-Checking progress.");
        }
    }

    @RequestMapping(value = "/isStoreWithTokenAvailable", method = RequestMethod.GET)
    public boolean isStoreWithTokenAvailable(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        return store != null;
    }

    @RequestMapping(value ="/isAlive", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean isAlive(){
        return true;
    }
}
