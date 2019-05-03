package unipos.pos.components.order;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.common.remote.auth.model.User;
import unipos.pos.components.order.tag.OrderTag;
import unipos.pos.components.orderItem.OrderItemRepository;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.test.config.MongoTestConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by ggradnig on 2015-02-05
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoTestConfiguration.class})
public class OrderRepositoryTest {

    @Autowired
    private OrderFixture orderFixture;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Before
    public void setUp() {
        orderFixture.init();
    }

    @Test
    public void testFindByOpenUsers() {
        User user1 = new User();
        user1.setGuid("1");

        assertNotNull(orderRepository.findByActiveUsers_guid(user1.getGuid()));
    }

    @Test
    public void testFindByActiveUsers_userId() throws Exception {
        orderFixture.setUp();
        List<Order> orderList = orderRepository.findByActiveUsers_guidAndIsActive("1", true);

        assertThat(orderList.size(), is(2));

        orderList = orderRepository.findByActiveUsers_guidAndIsActive("2", true);
        assertThat(orderList.size(), is(2));
    }

    @Test
    public void testSaveOrder() throws Exception {
        orderItemRepository.save(orderFixture.productOrderItem);
        orderItemRepository.save(orderFixture.reversalOrderItem);
        orderRepository.save(orderFixture.order1);
        List<Order> orderList = orderRepository.findAll();
        List<OrderItem> orderItemList = orderItemRepository.findAll();

        assertThat(orderList.size(), is(1));
        assertThat(orderList.get(0).getOrderItems().size(), is(2));
        assertThat(orderItemList.size(), is(2));
    }

    @Test
    public void testFindByTag() {
        orderRepository.save(orderFixture.order1);
        orderRepository.save(orderFixture.order2);
        orderRepository.save(orderFixture.order3);
        orderRepository.save(orderFixture.order4);

        Order order1 = orderRepository.findFirstByOrderTags_keyAndOrderTags_value("Tisch", "2");
        assertNotNull(order1);

        OrderTag orderTag1 = order1.getOrderTags().get(0);
        assertTrue(orderTag1.getKey().equals("Tisch"));
        assertTrue(orderTag1.getValue().equals("2"));
    }

    @After
    public void tearDown() {
        orderFixture.tearDown();
    }
}
