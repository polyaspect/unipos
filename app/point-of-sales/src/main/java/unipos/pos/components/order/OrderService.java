package unipos.pos.components.order;

import unipos.pos.components.order.transaction.OrderTransaction;

import java.util.List;

/**
 * @author ggradnig
 */
public interface OrderService {
    Order createNewOrder(OrderDto baseOrder, String userGuid, String deviceId);
    Order openOrder(String orderId, String userId, String deviceId);
    List<Order> getOpenOrdersForUser(String userId);

    void deleteByOrderId(String orderId);

    List<Order> findActiveOrdersByUserIdAndDeviceId(String userId, String deviceId);

    List<Order> findActiveOrdersByUserGuid(String guid);

    List<Order> findActiveOrdersByDeviceId(String deviceId);

    void addTagToOrder(String orderId, String tagKey, String tagValue);

    Order findOrderByTag(String tagKey, String tagValue);

    OrderTransaction splitOrder(String sourceOrderId, String destOrderId, List<String> orderItemIds);

    OrderTransaction reassignOrder(String orderId, String destUserId);

    OrderTransaction moveOrder(String orderId, String sourceTagKey, String destTagValue);

    List<Order> findActiveOrders();
}
