package unipos.pos.components.order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author ggradnig
 *
 * Spring repository for CRUD operations on Entity
 *
 */

public interface OrderRepository extends MongoRepository<Order, String>
{
    List<Order> findByIsActive(boolean isActive);

    List<Order> findByActiveUsers_guid(String userId);

    List<Order> findByActiveUsers_guidAndIsActive(String guid, boolean isActive);

    List<Order> findByActiveDevices_deviceIdAndIsActive(String deviceId, boolean isActive);

    Order findFirstByOrderId(String id);

    Long deleteByOrderId(String orderId);

    Order findFirstByOrderTags_keyAndOrderTags_value(String tagKey, String tagValue);

    List<Order> findByActiveUsers_guidAndActiveDevices_deviceIdAndIsActive(String guid, String deviceId, boolean isActive);
}
