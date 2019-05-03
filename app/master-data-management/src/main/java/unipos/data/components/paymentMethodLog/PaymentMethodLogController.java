package unipos.data.components.paymentMethodLog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.paymentMethod.PaymentMethodService;
import unipos.data.components.product.Product;
import unipos.data.components.shared.UrlContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */

@RestController
@RequestMapping("/adminPaymentMethods")
public class PaymentMethodLogController {

    @Autowired
    PaymentMethodLogService paymentMethodLogService;
    @Autowired
    PaymentMethodService paymentMethodService;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;

    RestTemplate restTemplate = new RestTemplate();
    Gson gson = new GsonBuilder().serializeNulls().create();

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PaymentMethod> listAll() {
        return paymentMethodLogService.adminListPaymentMethods();
    }

    /**
     * Write the Changes that the Admin made to the Log Table to the "public" visible PaymentMethods Collection
     */
    @RequestMapping(value = "/publishChanges", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void publishChanges(HttpServletRequest request) {
        paymentMethodLogService.publishChanges();

        //Send the new content of the published List over the socket to the clients
        socketRemoteInterface.sendToAll("/topic/updatedPublish", gson.toJson(paymentMethodService.findAll()));
    }

    @RequestMapping(value = "/resetChanges", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void resetChanges() {
        paymentMethodLogService.resetChanges();
    }
}
