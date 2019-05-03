package unipos.design.components.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.GSonHolder;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.design.components.shared.UrlContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dominik on 19.08.15.
 */

@RestController
@RequestMapping("/socket")
public class SocketController {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    SocketRemoteInterface socketRemoteInterface;


    @RequestMapping(value = "/clientConnected", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void clientConnected(HttpServletRequest request, @RequestParam String deviceToken) {
        String response = restTemplate.getForEntity(UrlContainer.BASE_URL + UrlContainer.DESIGN_GETAREACONFIG, String.class).getBody();
        socketRemoteInterface.sendToDevice(deviceToken, "/updateScreenSets", GSonHolder.serializeDateGson().toJson(response)) ;
    }

}
