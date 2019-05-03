package unipos.pos.components.orderItem;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.orderItem.model.ReversalOrderItem;
import unipos.pos.components.shared.GSonHolder;
import unipos.pos.components.shared.UrlContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dominik on 27.08.15.
 */

@Api("/<orderItemType>")
public abstract class OrderItemController<T extends OrderItem> {

    @Autowired
    OrderItemService orderItemService;

    RestTemplate restTemplate = new RestTemplate();

    Gson gson = GSonHolder.serializeDateGson();

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ApiOperation("This method creates a new OrderItem specific to the URL you are sending it")
    @ResponseStatus(HttpStatus.OK)
    public synchronized void createNewOrderItem(HttpServletRequest request, @ApiParam @RequestBody T orderItem) {

        orderItemService.saveOrderItem(orderItem);

        String socketTarget;

        if(orderItem instanceof ReversalOrderItem) {
            socketTarget = "/topic/reversalOrderItem/add";
        } else {
            socketTarget = "/topic/productOrderItem/add";
        }

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add("target", socketTarget);
        mvm.add("data", gson.toJson(orderItem));
        restTemplate.postForObject(UrlContainer.BASEURL+UrlContainer.SOCKET_SEND_TO_ALL, mvm, Void.class);
    }
}
