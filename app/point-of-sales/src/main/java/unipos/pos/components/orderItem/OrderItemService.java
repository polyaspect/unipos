package unipos.pos.components.orderItem;

import unipos.pos.components.orderItem.model.OrderItem;

/**
 * Created by dominik on 27.08.15.
 */
public interface OrderItemService {

    OrderItem saveOrderItem(OrderItem orderItem);
    public void deleteByOrderItemId(String orderItemId);
}
