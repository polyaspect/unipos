package unipos.common.remote.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.RequestHandler;
import unipos.common.container.UrlContainer;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.socket.model.Printer;
import unipos.common.remote.socket.model.Workstation;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gradnig on 07.11.2015.
 */
@Service
public class SocketRemoteInterfaceImpl implements  SocketRemoteInterface{

    @Autowired
    private RestTemplate restTemplate;

    public void sendToUser(String userId, String target, Object data)
    {
        MultiValueMap mvm = new LinkedMultiValueMap<>();
        mvm.add("target", target);
        mvm.add("userId", userId);
        mvm.add("data", data);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity httpEntity = new HttpEntity(mvm, headers);

        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.SOCKET_SEND_TO_USER, httpEntity, Void.class);
    }

    public void sendToDevice(String deviceToken, String target, Object data)
    {
        MultiValueMap mvm = new LinkedMultiValueMap<>();
        mvm.add("target", target);
        mvm.add("deviceToken", deviceToken);
        mvm.add("data", data);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity httpEntity = new HttpEntity(mvm, headers);

        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.SOCKET_SEND_TO_DEVICE, httpEntity, Void.class);
    }

    @Override
    public void sendToAll(String target, Object data) {
        MultiValueMap mvm = new LinkedMultiValueMap<>();
        mvm.add("target", target);
        mvm.add("data", data);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity httpEntity = new HttpEntity(mvm, headers);

        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.SOCKET_SEND_TO_ALL, httpEntity, Void.class);
    }

    @Override
    public Workstation getWorkstationByAuthtoken(String authToken) {
        Workstation workstation = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.SOCKET_CLIENT_INFORMATION + "/" + authToken, Workstation.class);
        return workstation;
    }

    @Override
    public Workstation findByDeviceId(String deviceId) {
        Workstation workstation = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.SOCKET_DEVICE_DEVICEID + "/" + deviceId, Workstation.class);
        return workstation;
    }

    public List<Workstation> findByStoreGuid(String storeGuid) {
        List<Workstation> workstations = Arrays.asList(restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.SOCKET_FIND_BY_STOREGUID + "/" + storeGuid, Workstation[].class));
        return workstations;
    }

    @Override
    public Printer findDeviceDefaultPrinter(String deviceId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "DeviceToken="+deviceId);

        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Printer> printerResponseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.SOCKET_DEVICE_DEFAULT_PRINTER, HttpMethod.GET, httpEntity, Printer.class);

        if(printerResponseEntity.getStatusCode().is2xxSuccessful()) {
            return printerResponseEntity.getBody();
        } else {
            return null;
        }
    }

    @Override
    public Printer findDeviceDefaultPrinter(HttpServletRequest request) {

        String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");

        if(deviceToken == null || deviceToken.isEmpty()) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "DeviceToken="+deviceToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Printer> printerResponseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.SOCKET_DEVICE_DEFAULT_PRINTER, HttpMethod.GET, httpEntity, Printer.class);

        if(printerResponseEntity.getStatusCode().is2xxSuccessful()) {
            return printerResponseEntity.getBody();
        } else {
            return null;
        }
    }

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.SOCKET_CURRENT_VERSION, String.class);
    }
    @Override
    public Store getStoreByDevice(String deviceToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "DeviceToken="+deviceToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Store> storeEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.SOCKET_GET_STORE_BY_DEVICE, HttpMethod.GET, httpEntity, Store.class);

        if(storeEntity.getStatusCode().is2xxSuccessful()) {
            return storeEntity.getBody();
        } else {
            return null;
        }
    }
}
