package unipos.pos.components.order;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.auth.model.UserList;
import unipos.pos.components.order.tag.OrderTag;
import unipos.pos.components.order.tag.OrderTagList;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.orderItem.model.ProductOrderItem;
import unipos.pos.components.orderItem.model.ReversalOrderItem;
import unipos.pos.components.shared.GSonHolder;
import unipos.pos.test.config.Fixture;
import unipos.pos.test.config.MongoTestConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author ggradnig
 */

@ContextConfiguration(classes={MongoTestConfiguration.class})
public class OrderFixture implements Fixture{

    @Autowired
    private MongoOperations mongoOperations;

    public User user1, user2, user3, user4, user5;
    public Order order1, order2, order3, order4;
    public ProductOrderItem productOrderItem;
    public ReversalOrderItem reversalOrderItem;

    Gson gson = GSonHolder.serializeDateGson();

    public void init() {
        user1 = User.builder()
                .guid("1")
                .build();
        user2 = User.builder()
                .guid("2")
                .build();
        user3 = User.builder()
                .guid("3")
                .build();
        user4 = User.builder()
                .guid("4")
                .build();
        user5 = User.builder()
                .guid("5")
                .build();

        UserList users1 = new UserList();
        users1.add(user1);
        users1.add(user2);
        users1.add(user3);

        OrderTagList orderTags1 = new OrderTagList();
        orderTags1.add(OrderTag.builder().key("Tisch").value("1").build());

        order1 = Order.builder()
                .orderNumber(1L)
                .orderId(UUID.randomUUID().toString())
                .activeUsers(users1)
                .isActive(true)
                .creatorUser(user1)
                .open(true)
                .serverReceiveDate(null)
                .orderTags(orderTags1)
                .build();

        OrderTagList orderTags2 = new OrderTagList();
        orderTags2.add(OrderTag.builder().key("Kunde").value("1").build());

        UserList users2 = new UserList();
        users2.add(user1);
        order2 = Order.builder()
                .orderNumber(2L)
                .orderId(UUID.randomUUID().toString())
                .activeUsers(users2)
                .creatorUser(user1)
                .isActive(true)
                .open(true)
                .serverReceiveDate(null)
                .orderTags(orderTags2)
                .build();

        OrderTagList orderTags3 = new OrderTagList();
        orderTags3.add(OrderTag.builder().key("Kunde").value("2").build());

        UserList users3 = new UserList();
        users3.add(user3);
        users3.add(user5);
        order3 = Order.builder()
                .orderNumber(3L)
                .orderId(UUID.randomUUID().toString())
                .activeUsers(users3)
                .creatorUser(user5)
                .open(true)
                .serverReceiveDate(null)
                .orderTags(orderTags3)
                .build();

        OrderTagList orderTags4 = new OrderTagList();
        orderTags4.add(OrderTag.builder().key("Tisch").value("2").build());

        UserList users4 = new UserList();
        users4.add(user2);
        users4.add(user4);
        order4 = Order.builder()
                .orderNumber(3L)
                .orderId(UUID.randomUUID().toString())
                .activeUsers(users4)
                .creatorUser(user3)
                .isActive(true)
                .open(true)
                .serverReceiveDate(null)
                .orderTags(orderTags4)
                .build();

        //Create some OrderItems
        productOrderItem = new ProductOrderItem();
        productOrderItem.setOrderId(order1.getOrderId());
        productOrderItem.setOrderItemId(UUID.randomUUID().toString());
        productOrderItem.setActionId("action1");
        productOrderItem.setClientCreationDate(null);
        productOrderItem.setQuantity(2);
        productOrderItem.setPosition(1);
        productOrderItem.setTurnover(new BigDecimal("19.99"));
        productOrderItem.setProductNumber("1");
        productOrderItem.generateHash();

        reversalOrderItem = new ReversalOrderItem();
        reversalOrderItem.setOrderId(order1.getOrderId());
        reversalOrderItem.setReason("Failure");
        reversalOrderItem.setOrderItemId(UUID.randomUUID().toString());
        reversalOrderItem.setPosition(2);
        reversalOrderItem.setClientCreationDate(null);
        reversalOrderItem.setActionId("action2");
        reversalOrderItem.generateHash();

        //After that add the orderItems to the orders
        order1.setOrderItems(Arrays.asList(productOrderItem, reversalOrderItem));

        //Just for JSON generating to demonstrate the JSON Layout
//        order1.setServerReceiveDate(LocalDateTime.now());
//        productOrderItem.setServerReceiveTime(LocalDateTime.now());
//        reversalOrderItem.setServerReceiveTime(LocalDateTime.now());
//        gson.toJson(order1);


    }
    public void setUp() {
        init();
        Arrays.asList(productOrderItem, reversalOrderItem).forEach((x) -> mongoOperations.insert(x, "orderItems"));
        Arrays.asList(order1,order2,order3,order4).forEach((x) -> mongoOperations.insert(x));
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(Query.query(where("_id").ne("-1")), Order.class);
        mongoOperations.findAllAndRemove(Query.query(where("_id").ne("-1")), OrderItem.class);
    }
}
