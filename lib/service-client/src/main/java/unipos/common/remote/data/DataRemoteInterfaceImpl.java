package unipos.common.remote.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import unipos.common.container.UrlContainer;
import unipos.common.remote.data.model.PaymentMethod;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.data.model.discount.Discount;
import unipos.common.remote.data.model.product.Product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by dominik on 04.09.15.
 */

@Service
public class DataRemoteInterfaceImpl implements DataRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Product getProductByDocumentId(String documentId) {
        Product product = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_PRODUCT_BY_DOCUMENTID + "/" + documentId, Product.class);
        return product;
    }

    @Override
    public Product getProductByProductIdentifier(String productIdentifier) {
        Product product = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_PRODUCT_BY_PRODUCTIDENTIFIER + "/" + productIdentifier, Product.class);
        return product;
    }

    @Override
    public Discount getDiscountByDocumentId(String documentId) {
        Discount discount = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_DISCOUT_BY_DOCUMENT_ID + "/" + documentId, Discount.class);
        return discount;
    }

    @Override
    public Store getStoreByUserIdAndDeviceId(String userId, String deviceId) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_STORE_BY_USERID_AND_DEVICEID + "?userId=" + userId + "&deviceId=" + deviceId, Store.class);
    }

    @Override
    public Store getStoreByAuthtokenAndDeviceid(String authToken, String deviceId) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "AuthToken=" + authToken + "; DeviceToken=" + deviceId);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<Store> store = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.DATA_GET_STORE_BY_AUTHTOKEN_AND_DEVICEID,
                HttpMethod.GET,
                requestEntity,
                Store.class);
        return store.getBody();
    }

    @Override
    public Store getStoreByAuthtokenAndDeviceid(HttpServletRequest request) throws IllegalArgumentException {

        String deviceId = "";
        String authToken = "";

        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            switch (cookie.getName()) {
                case "AuthToken":
                    authToken = cookie.getValue();
                    break;
                case "DeviceToken":
                    deviceId = cookie.getValue();
                    break;
            }
        }

        if (deviceId.isEmpty() || authToken.isEmpty()) {
            throw new IllegalArgumentException("No AuthToken or DeviceToken found in the given Request");
        }

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "AuthToken=" + authToken + "; DeviceToken=" + deviceId);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<Store> store = restTemplate.exchange(
                UrlContainer.BASEURL + UrlContainer.DATA_GET_STORE_BY_AUTHTOKEN_AND_DEVICEID,
                HttpMethod.GET,
                requestEntity,
                Store.class);
        return store.getBody();
    }

    @Override
    public Store getStoreByGuid(String guid) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_STORE_BY_GUID + "/" + guid, Store.class);
    }

    public Company getCompanyByGuid(String guid) {
        if (guid.isEmpty()) {
            return null;
        }

        Company company = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_COMPANY_BY_GUID + "/" + guid, Company.class);
        return company;
    }

    public PaymentMethod getPaymentMethodByGuid(String guid) {
        if (guid.isEmpty()) {
            return null;
        }

        PaymentMethod paymentMethod = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_PAYMENTMETHOD_BY_GUID + "/" + guid, PaymentMethod.class);
        return paymentMethod;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        HttpEntity httpEntity = new HttpEntity(null);

        ResponseEntity<PaymentMethod[]> responseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.DATA_GET_PAYMENTMETHODS, HttpMethod.GET, httpEntity, PaymentMethod[].class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Arrays.asList(responseEntity.getBody());
        }
        return Collections.emptyList();
    }

    public Store getControllerPlacedStore(String companyGuid) {
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();

        Store store = restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_CONTROLLER_PLACED_STORE + "/" + companyGuid, Store.class);

        return store;
    }

    @Override
    public List<Store> getStoresByPlacedController() {
        return Arrays.asList(restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_GET_STORE_BY_CONTROLLER_PLACED, Store[].class));
    }

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DATA_CURRENT_VERSION, String.class);
    }

    @Override
    public List<Product> getByAtrributes(List<String> attributes, HttpServletRequest request) {
        /*if (request.getCookies() == null) {
            return null;
        }
        String deviceId = RequestHandler.getDeviceToken(request);
        String authToken = RequestHandler.getAuthToken(request);

        if (deviceId.isEmpty() || authToken.isEmpty()) {
            throw new IllegalArgumentException("No AuthToken or DeviceToken found in the given Request");
        }

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Cookie", "AuthToken=" + authToken + "; DeviceToken=" + deviceId);
*/
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(UrlContainer.BASEURL + UrlContainer.DATA_GET_PRODUCT_BY_ATTRIBUTES);
        attributes.stream().forEach(s -> builder.queryParam("attributes", s));

        HttpEntity requestEntity = new HttpEntity(null,  new HttpHeaders());
        ResponseEntity<Product[]> products = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntity,
                Product[].class);
        if (products.getStatusCode().is2xxSuccessful()) {
            return Arrays.asList(products.getBody());
        } else {
            return new ArrayList<Product>();
        }
    }

    @Override
    public Company addCompany(Company company) {
        ResponseEntity<Company> responseEntity = restTemplate.postForEntity(UrlContainer.BASEURL + UrlContainer.DATA_ADD_COMPANY, company, Company.class);
        return responseEntity.getBody();
    }

    @Override
    public void reduceStockAmountForProductGuid(Product product) {
        ObjectMapper objectMapper = new ObjectMapper();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<String> entity = null;
        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(product), headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResponseEntity<Product> responseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.DATA_POST_REDUCT_STOCK_AMOUNT, HttpMethod.POST, entity, Product.class);
    }
}
