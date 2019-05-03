package unipos.pos.components.orderItem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.pos.components.orderItem.model.ChangeOrderItem;
import unipos.pos.components.orderItem.model.PaymentOrderItem;

/**
 * Created by dominik on 28.08.15.
 */

@RestController
@RequestMapping("/changeOrderItem")
public class ChangeOrderItemController extends OrderItemController<ChangeOrderItem> {
}
