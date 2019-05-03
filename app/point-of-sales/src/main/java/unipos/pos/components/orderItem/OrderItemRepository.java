package unipos.pos.components.orderItem;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.pos.components.orderItem.model.OrderItem;

/**
 * Created by dominik on 27.08.15.
 */
public interface OrderItemRepository extends MongoRepository<OrderItem, String> {


    Long deleteByOrderItemId(String orderItemId);

    OrderItem findByOrderItemId(String orderItemId);
}
