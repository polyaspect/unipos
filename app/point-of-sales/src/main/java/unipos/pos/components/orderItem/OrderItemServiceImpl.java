package unipos.pos.components.orderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.pos.components.order.Order;
import unipos.pos.components.order.OrderRepository;
import unipos.pos.components.orderItem.model.OrderItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by dominik on 27.08.15.
 */

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;


    public OrderItem saveOrderItem(OrderItem orderItem) {
        orderItem.setServerReceiveTime(LocalDateTime.now());
        orderItem.setClientCreationDate(orderItem.getClientCreationDate().plusHours(1));
        orderItem.generateHash();
        orderItemRepository.save(orderItem);

        if(orderItem.getOrderId() == null) {
            return null;
        }

        Order order = orderRepository.findFirstByOrderId(orderItem.getOrderId());

        if(order != null) {
            order.getOrderItems().add(orderItem);
            orderRepository.save(order);
            return orderItem;
        } else {
            return null;
        }
    }

    public void deleteByOrderItemId(String orderItemId) {
        OrderItem orderItem = orderItemRepository.findByOrderItemId(orderItemId);

        Order order = orderRepository.findFirstByOrderId(orderItem.getOrderId());
        order.setOrderItems(order.getOrderItems().stream().filter(orderItem1 -> !orderItem1.getOrderItemId().equals(orderItemId)).collect(Collectors.toList()));

        orderItemRepository.deleteByOrderItemId(orderItemId);
        orderRepository.save(order);
    }
}
