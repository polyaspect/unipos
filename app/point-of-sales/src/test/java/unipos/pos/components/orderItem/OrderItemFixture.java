package unipos.pos.components.orderItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.orderItem.model.OrderItemDiscountOrderItem;
import unipos.pos.components.orderItem.model.ProductOrderItem;
import unipos.pos.components.shared.GSonHolder;
import unipos.pos.test.config.Fixture;
import unipos.pos.test.config.MongoTestConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author ggradnig
 */

@ContextConfiguration(classes={MongoTestConfiguration.class})
public class OrderItemFixture implements Fixture{

    @Autowired
    private MongoOperations mongoOperations;

    Gson gson = GSonHolder.serializeDateGson();

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("_id").ne("-1")), OrderItem.class);

    }
    @Override


    public void setUp() {
        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setActionId("actionId");
        productOrderItem.setQuantity(2);
        productOrderItem.setProductNumber("1");
        productOrderItem.setTurnover(new BigDecimal("19.99"));
        productOrderItem.setOrderId("orderId1");
        productOrderItem.setOrderItemId("ASDF");

        OrderItemDiscountOrderItem discountOrderItem = new OrderItemDiscountOrderItem();
        discountOrderItem.setActionId("actionId2");
        discountOrderItem.setOrderId("orderId2");
        discountOrderItem.setOrderItemId("qwer");
        discountOrderItem.setDiscount(new BigDecimal(20));
        discountOrderItem.setPosition(4);

        mongoOperations.insert(productOrderItem, "orderItems");
        mongoOperations.insert(discountOrderItem, "orderItems");
        mongoOperations.findAll(OrderItem.class);

    }
}
