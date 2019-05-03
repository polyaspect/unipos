package unipos.pos.components.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.model.Workstation;
import unipos.pos.components.order.tag.OrderTag;
import unipos.pos.components.order.transaction.OrderTransaction;
import unipos.pos.components.orderItem.OrderItemRepository;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.sequence.SequenceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ggradnig
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    SequenceRepository sequenceRepository;

    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;

    @Override
    public Order createNewOrder(OrderDto baseOrder, String userGuid, String deviceId) {

        User creator = authRemoteInterface.getUserByGuid(userGuid);
        Workstation device = socketRemoteInterface.findByDeviceId(deviceId);

        Order order = baseOrder.toOrder();

        //Because of joyce bug i have to do this --> Is not always empty, if the user is too fast, or the server is too slow
        order.setOrderItems(new ArrayList<>());

        order.setCreatorUser(creator);
        order.getActiveUsers().add(creator);
        order.setCurrentUser(creator);

        order.getActiveDevices().add(device);
        order.setCurrentDevice(device);

        order.setOrderNumber(sequenceRepository.getNextSequenceId("ORDER"));
        order.setServerReceiveDate(LocalDateTime.now());

        Order persistedNewOrder = orderRepository.save(order);
        return persistedNewOrder;
    }

    /*ToDo: Nicht notwendig, es ist nur interessant welche Orders ein User zugewiesen hat. */

    public List<Order> getOpenOrdersForUser(String userId) {
        return orderRepository.findByActiveUsers_guid(userId);
    }

    @Override
    public Order openOrder(String orderId, String userId, String deviceId) {
        Order order = orderRepository.findFirstByOrderId(orderId);

        User editor = authRemoteInterface.getUserByGuid(userId);
        Workstation editorDevice = socketRemoteInterface.findByDeviceId(deviceId);

        changeActiveUser(order, editor);
        changeActiveDevice(order, editorDevice);

        orderRepository.save(order);

        return order;
    }

    private void changeActiveUser(Order order, User newUser){
        if (!order.getActiveUsers().containsUserGuid(newUser.getGuid())) {
            order.getActiveUsers().add(newUser);
        }
        order.setCurrentUser(newUser);
    }

    private void changeActiveDevice(Order order, Workstation newDevice){
        if (!order.getActiveDevices().containsDeviceId(newDevice.getDeviceId())) {
            order.getActiveDevices().add(newDevice);
        }
        order.setCurrentDevice(newDevice);
    }

    @Override
    public List<Order> findActiveOrdersByUserGuid(String guid) {
        return orderRepository.findByActiveUsers_guidAndIsActive(guid, true);
    }

    @Override
    public List<Order> findActiveOrdersByDeviceId(String deviceId) {
        return orderRepository.findByActiveDevices_deviceIdAndIsActive(deviceId, true);
    }

    @Override
    public void deleteByOrderId(String orderId) {
        Order order = orderRepository.findFirstByOrderId(orderId);

        order.getOrderItems().forEach(orderItem -> orderItemRepository.deleteByOrderItemId(orderItem.getOrderItemId()));

        orderRepository.deleteByOrderId(orderId);
    }

    @Override
    public List<Order> findActiveOrdersByUserIdAndDeviceId(String userId, String deviceId) {
        return orderRepository.findByActiveUsers_guidAndActiveDevices_deviceIdAndIsActive(userId, deviceId, true);
    }

    @Override
    public void addTagToOrder(String orderId, String tagKey, String tagValue) {
        Order order = orderRepository.findFirstByOrderId(orderId);

        if (order.getOrderTags().containsKey(tagKey)) {
            throw new RuntimeException("ORDER_CONTAINS_TAG");
            //alternative: order.getOrderTags().remove(order.getOrderTags().getByOriginAndKey(tagOrigin, tagKey));
        }

        OrderTag orderTag = OrderTag.builder().key(tagKey).value(tagValue).build();

        order.getOrderTags().add(orderTag);

        orderRepository.save(order);

    }

    @Override
    public Order findOrderByTag(String tagKey, String tagValue) {
        return orderRepository.findFirstByOrderTags_keyAndOrderTags_value(tagKey, tagValue);
    }

    @Override
    public OrderTransaction splitOrder(String sourceOrderId, String destOrderId, List<String> orderItemIds) {
        OrderTransaction orderTransaction = OrderTransaction.builder().sourceOrderId(sourceOrderId).build();

        Order sourceOrder = orderRepository.findFirstByOrderId(sourceOrderId);

        Order newOrder =
                Order.builder()
                        .orderId(destOrderId)
                        .orderTags(sourceOrder.getOrderTags())
                        .serverReceiveDate(LocalDateTime.now())
                        .creatorUser(sourceOrder.getCreatorUser())
                        .isActive(true)
                        .build();


        newOrder.setOrderNumber(sequenceRepository.getNextSequenceId("ORDER"));

        newOrder.setCurrentUser(sourceOrder.getCurrentUser());

        // Add OrderItems to new Order
        newOrder.setOrderItems(new ArrayList<>());
        newOrder.getOrderItems().addAll(sourceOrder.getOrderItems().stream().filter(x -> orderItemIds.contains(x.getOrderItemId())).collect(Collectors.toList()));

        // Remove OrderItems from old Order
        sourceOrder.setOrderItems(sourceOrder.getOrderItems().stream().filter(x -> !orderItemIds.contains(x.getOrderItemId())).collect(Collectors.toList()));

        orderRepository.save(sourceOrder);
        orderRepository.save(newOrder);

        return orderTransaction;
    }

    @Override
    public OrderTransaction reassignOrder(String orderId, String destUserId) {
        OrderTransaction orderTransaction = OrderTransaction.builder().sourceOrderId(orderId).build();

        Order order = orderRepository.findFirstByOrderId(orderId);
        User assignee = authRemoteInterface.getUserByGuid(destUserId);

        changeActiveUser(order, assignee);
        orderRepository.save(order);

        return orderTransaction;
    }

    @Override
    public OrderTransaction moveOrder(String orderId, String sourceTagKey, String destTagValue) {
        OrderTransaction orderTransaction = OrderTransaction.builder().sourceOrderId(orderId).build();

        Order order = orderRepository.findFirstByOrderId(orderId);

        if (order.getOrderTags().containsKey(sourceTagKey)) {
            alternative:
            order.getOrderTags().remove(order.getOrderTags().getByKey(sourceTagKey));
        }

        addTagToOrder(order.getOrderId(), sourceTagKey, destTagValue);

        return orderTransaction;
    }

    @Override
    public List<Order> findActiveOrders() {
        return orderRepository.findByIsActive(true);
    }
}
