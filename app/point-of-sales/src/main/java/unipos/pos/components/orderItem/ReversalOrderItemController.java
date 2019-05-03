package unipos.pos.components.orderItem;

import com.wordnik.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.pos.components.orderItem.model.PaymentOrderItem;
import unipos.pos.components.orderItem.model.ReversalOrderItem;

/**
 * Created by dominik on 28.08.15.
 */

@RestController
@RequestMapping("/reversalOrderItem")
public class ReversalOrderItemController extends OrderItemController<ReversalOrderItem> {
}
