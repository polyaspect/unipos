package unipos.common.remote.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.UrlContainer;
import unipos.common.remote.pos.model.Invoice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Thomas on 11.07.2016.
 */
@Service
public class EmailRemoteInterfaceImpl implements EmailRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.EMAIL_CURRENT_VERSION, String.class);
    }

    @Override
    public void sendConfirmationEmail(Invoice invoice, HttpServletRequest request) {
        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.EMAIL_SEND_CONFIRMATION_EMAIL, invoice, Void.class);
    }
}
