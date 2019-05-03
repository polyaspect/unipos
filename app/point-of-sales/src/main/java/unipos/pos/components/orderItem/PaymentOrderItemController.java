package unipos.pos.components.orderItem;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.pos.components.orderItem.model.DiscountOrderItem;
import unipos.pos.components.orderItem.model.PaymentOrderItem;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dominik on 28.08.15.
 */

@RestController
@RequestMapping("/paymentOrderItem")
public class PaymentOrderItemController extends OrderItemController<PaymentOrderItem> {

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public synchronized void delete(HttpServletRequest request, @RequestBody PaymentOrderItem orderItem) {

        orderItemService.deleteByOrderItemId(orderItem.getOrderItemId());

    }
}
