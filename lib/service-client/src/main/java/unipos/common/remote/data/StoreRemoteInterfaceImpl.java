package unipos.common.remote.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import unipos.common.container.UrlContainer;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.data.model.product.Product;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Joyce on 05.05.2016.
 */
@Service
public class StoreRemoteInterfaceImpl implements StoreRemoteInterface {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Store findByUserId(String userId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(UrlContainer.BASEURL + UrlContainer.DATA_GET_STORE_BY_USER_ID);
        builder.queryParam("userGuid", userId);
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<Store[]> stores = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntity,
                Store[].class);
        return Arrays.asList(stores.getBody()).stream().filter(store -> store.getName().equalsIgnoreCase("webshop")).findFirst().get();
    }
}
