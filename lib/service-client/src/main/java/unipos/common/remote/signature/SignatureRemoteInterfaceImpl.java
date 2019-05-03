package unipos.common.remote.signature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.RequestHandler;
import unipos.common.container.UrlContainer;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.Invoice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by domin on 23.12.2016.
 */

@Service
public class SignatureRemoteInterfaceImpl implements SignatureRemoteInterface {

    @Autowired
    RestTemplate restTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Invoice createStartInvoice(Invoice invoice) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<String> httpEntity = null;
        try {
            httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(invoice), headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ResponseEntity<Invoice> response = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.SIGNATURE_CREATE_START_INVOICE,
                HttpMethod.POST,
                httpEntity,
                Invoice.class
        );

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    @Override
    public Invoice signInvoice(Invoice invoice) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<String> httpEntity = null;
        try {
            httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(invoice), headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ResponseEntity<Invoice> response = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.SIGNATURE_SIGN_INVOICE,
                HttpMethod.POST,
                httpEntity,
                Invoice.class
        );

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    @Override
    public boolean isSignatureForStoreEnabled(String storeGuid) {

        HttpEntity httpEntity = new HttpEntity<>(null);

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.SIGNATURE_IS_SIGNATURE_ENABLED, HttpMethod.GET, httpEntity, Boolean.class, storeGuid);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return false;
        }
    }

    @Override
    public boolean isSignatureModuleUpAndRunning() {
        HttpEntity httpEntity = new HttpEntity(null);

        try {
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.SIGNATURE_IS_UP_AND_RUNNING, HttpMethod.GET, httpEntity, Boolean.class);
            if(responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                return false;
            }
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public boolean isSammelbelegRequired(HttpServletRequest request) {
        String authToken;
        String deviceId;

        try {
            authToken = RequestHandler.getAuthToken(request);
            deviceId = RequestHandler.getDeviceToken(request);
        } catch (Exception ex) {
            return false;
        }

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "AuthToken=" + authToken + "; DeviceToken=" + deviceId);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.SIGNATURE_IS_SAMMELBELEG_REQUIRED,
                HttpMethod.GET,
                requestEntity,
                Boolean.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return false;
        }
    }

    @Override
    public boolean isSmartCardAvailable(HttpServletRequest request) {
        String authToken;
        String deviceId;

        try {
            authToken = RequestHandler.getAuthToken(request);
            deviceId = RequestHandler.getDeviceToken(request);
        } catch (Exception ex) {
            return false;
        }

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "AuthToken=" + authToken + "; DeviceToken=" + deviceId);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.SIGNATURE_IS_SMARTCARD_AVAILABLE,
                HttpMethod.GET,
                requestEntity,
                Boolean.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return false;
        }
    }

    @Override
    public boolean isSammelbelegRequiredAndSmartCardAvailable(HttpServletRequest request, String storeGuid) {
        String authToken;
        String deviceId;

        try {
            authToken = RequestHandler.getAuthToken(request);
            deviceId = RequestHandler.getDeviceToken(request);
        } catch (Exception ex) {
            return false;
        }

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "AuthToken=" + authToken + "; DeviceToken=" + deviceId);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.SIGNATURE_IS_SAMMELBELEG_REQUIRED_AND_SMARTCARD_AVAILABLE + "?storeGuid=" + storeGuid,
                HttpMethod.GET,
                requestEntity,
                Boolean.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return false;
        }
    }

    @Override
    public Invoice createZeroInvoice(String storeGuid, Invoice.SignatureInvoiceType signatureInvoiceType) {
        ResponseEntity<Invoice> response = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.SIGNATURE_CREATE_ZERO_INVOICE + "?invoiceType=" + signatureInvoiceType.toString() + "&storeGuid=" + storeGuid,
                HttpMethod.POST,
                null,
                Invoice.class
        );

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    @Override
    public void sendDep(String storeGuid) {
        restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.SIGNATURE_SEND_DEP + "?storeGuid=" + storeGuid,
                HttpMethod.GET,
                null,
                void.class
        );
    }
}
