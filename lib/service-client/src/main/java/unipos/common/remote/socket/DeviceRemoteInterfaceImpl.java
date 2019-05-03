package unipos.common.remote.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import unipos.common.container.UrlContainer;
import unipos.common.remote.data.StoreRemoteInterface;
import unipos.common.remote.data.model.company.Store;

import javax.servlet.http.Cookie;
import java.util.List;

/**
 * Created by Joyce on 05.05.2016.
 */
@Service
public class DeviceRemoteInterfaceImpl implements DeviceRemoteInterface {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    StoreRemoteInterface storeRemoteInterface;

    @Override
    public Cookie addStoreToDevice(String userGuid) {
        Store store = storeRemoteInterface.findByUserId(userGuid);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(UrlContainer.BASEURL + UrlContainer.SOCKET_DEVICE_ADDSTORETODEVICE);
        builder.queryParam("storeGuid", store.getGuid());
        builder.queryParam("deviceName", "webshopDevice");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.POST,
                requestEntity,
                String.class);
        List<String> headers = response.getHeaders().get("Set-Cookie");

        Cookie cookie = new Cookie("DeviceToken", headers.stream().filter(s -> s.startsWith("DeviceToken")).findFirst().orElseThrow(() -> new RuntimeException("Kein deviceToken gefunden")).replace("DeviceToken=", "").split(";")[0]);
        cookie.setPath("/");
        cookie.setMaxAge(315360000);
        return cookie;
    }

    @Override
    public void setCashierForWorkstation(String cashierId, String deviceId) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "DeviceToken=" + deviceId);
        HttpEntity requestEntity = new HttpEntity(cashierId, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.SOCKET_SET_CASHIER_FOR_WORKSTATION,
                HttpMethod.POST,
                requestEntity,
                String.class);
    }
}
