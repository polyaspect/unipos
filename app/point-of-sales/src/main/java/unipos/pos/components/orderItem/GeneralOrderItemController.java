package unipos.pos.components.orderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Dominik on 23.01.2016.
 */

@RestController
@RequestMapping("/orderItems")
public class GeneralOrderItemController {

    @Autowired
    OrderItemService orderItemService;

    @RequestMapping(value = "/deleteByOrderItemId/{orderItemId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteByOrderItemId(@PathVariable("orderItemId") String orderItemId) {
        orderItemService.deleteByOrderItemId(orderItemId);
    }
}
