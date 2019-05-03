package unipos.socket.config.socket;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.model.Token;
import unipos.socket.components.shared.UrlContainer;
import unipos.socket.components.workstation.Workstation;
import unipos.socket.components.workstation.WorkstationService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Dominik on 13.08.2015.
 */
@Log(topic = "SocketHandshakeInterceptor")
public class MyHandshakeInterceptor implements HandshakeInterceptor
{
    @Autowired
    WorkstationService workstationService;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public synchronized boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String ipAdress = request.getRemoteAddress().getAddress().toString();
        String authTokenString = "";
        String deviceToken = "";
        if(!request.getHeaders().containsKey("Cookie")) {
            return false;
        }
        for(String authString : request.getHeaders().get("Cookie")) {
            for(String part : authString.split(";"))
            {
                List<String> parts = Arrays.asList(part.split("="));
                if((parts.get(0).equalsIgnoreCase("AuthToken") || parts.get(0).equalsIgnoreCase(" AuthToken")) && !parts.get(1).isEmpty()) {
                    authTokenString = parts.get(1);
                }
                if((parts.get(0).equalsIgnoreCase("DeviceToken") || parts.get(0).equalsIgnoreCase(" DeviceToken")) && !parts.get(1).isEmpty()) {
                    deviceToken = parts.get(1);
                }
            }
        }

        if(authTokenString.isEmpty()) {
            log.info("Socket ||| Unauthenticated attempt to connect to the server from IP-Address: " + ipAdress.replace("/", ""));
            return false;
        }

        //Optain the Information about the current logged in User from the Auth-Module so that just a already logged in user is able to use the socket
        Token userToken = restTemplate.getForObject(unipos.common.container.UrlContainer.BASEURL + unipos.common.container.UrlContainer.AUTH_GET_USERNAME_BY_AUTH_TOKEN + "/" + authTokenString, Token.class );
        String userId = "";

        if(userToken != null) {
            userId = String.valueOf(userToken.getUser().getUserId());
        } else {
                log.info("Socket ||| Tried to establish connection with an invalid AuthToken from: " + ipAdress.replace("/", "") + " with authToken " + authTokenString);
            return false;
        }
        if(userId == null || userId.isEmpty()) {
            log.info("Socket ||| Tried to establish connection with an invalid AuthToken from: " + ipAdress.replace("/", "") + " with authToken " + authTokenString);
            return false;
        }

        if(deviceToken == null ||deviceToken.isEmpty()) {
            log.info("No Device Token Found");
            return false;
        }

        Workstation workstation = workstationService.findByDeviceId(deviceToken);
        if(workstation == null) {
            workstation = Workstation.builder()
                    .ipAdress(ipAdress)
                    .deviceId(UUID.randomUUID().toString())
                    .authToken(authTokenString)
                    .creationDate(LocalDateTime.now())
                    .currentWorkingUser(userId)
                    .build();
            workstationService.saveWorkstation(workstation);
            log.info("Socket ||| A newly created Workstation connected: " + workstation.toString());
        } else {
            if(workstation.getAuthToken() == null || !workstation.getAuthToken().equals(authTokenString)) {
                workstation.setAuthToken(authTokenString);
                workstationService.saveWorkstation(workstation);
                log.info("Socket ||| Restarted Connection from newly logged in workstation: " + workstation.toString());
            } else {
                log.info("Socket ||| Restarted connection from already logged in workstation: " + workstation.toString());
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
