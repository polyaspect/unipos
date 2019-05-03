package unipos.common.remote.feedback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.GSonHolder;
import unipos.common.container.UrlContainer;
import unipos.common.remote.feedback.model.Rating;
import unipos.common.remote.pos.model.Invoice;

import javax.servlet.ServletContext;
import java.nio.charset.Charset;

/**
 * Created by dominik on 01.11.15.
 */

@Service
public class FeedbackRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    Gson gson = GSonHolder.serializeDateGson();

    public void createRating(String invoiceId, String companyId) throws Exception {
        restTemplate = new RestTemplate();

        Rating rating = new Rating();
        rating.setInvoiceId(invoiceId);
        rating.setCompanyId(companyId);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(rating), headers);

        restTemplate.exchange(UrlContainer.BASEURL + "/feedback/createRating", HttpMethod.POST, entity, Void.class);
    }
}
