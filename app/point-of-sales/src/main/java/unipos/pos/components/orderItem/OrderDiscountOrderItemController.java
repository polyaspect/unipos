package unipos.pos.components.orderItem;

import com.wordnik.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.pos.components.orderItem.model.DiscountOrderItem;
import unipos.pos.components.orderItem.model.OrderDiscountOrderItem;

/**
 * Created by dominik on 28.08.15.
 */

@RestController
@RequestMapping("/orderDiscountOrderItem")
public class OrderDiscountOrderItemController extends OrderItemController<OrderDiscountOrderItem> {
}
