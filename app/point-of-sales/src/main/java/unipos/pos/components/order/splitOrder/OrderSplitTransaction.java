package unipos.pos.components.order.splitOrder;

import unipos.pos.components.order.transaction.OrderTransaction;
import unipos.pos.components.orderItem.model.OrderItem;

import java.util.List;

/**
 * Created by ggradnig on 18.01.2017.
 */
public class OrderSplitTransaction extends OrderTransaction {

    private List<OrderItem> splitAwayOrderItems;
}
