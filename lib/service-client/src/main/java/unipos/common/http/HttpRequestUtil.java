package unipos.common.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.UrlContainer;

/**
 * Created by ggradnig on 06.02.2017.
 */

public class HttpRequestUtil {
    public static ResponseEntity<HttpResponseObject> postUrlencoded(RestTemplate restTemplate, String url, KeyValuePair... arguments){
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        for(KeyValuePair argument : arguments){
            mvm.add(argument.getKey(), argument.getValue());
        }

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity httpEntity = new HttpEntity(mvm, headers);

        try{
            ResponseEntity<HttpResponseObject> response = restTemplate.postForObject(url, httpEntity, ResponseEntity.class);
            return response;
        }
        catch(Exception ex){
            throw new HttpResponseTypeNotAllowedException();
        }
    }
}
