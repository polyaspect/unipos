package unipos.pos.components.orderItem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.pos.components.orderItem.model.DiscountOrderItem;
import unipos.pos.components.orderItem.model.OrderItemDiscountOrderItem;

/**
 * Created by dominik on 28.08.15.
 */

@RestController
@RequestMapping("/orderItemDiscountOrderItem")
public class OrderItemDiscountOrderItemController extends OrderItemController<OrderItemDiscountOrderItem> {
}
