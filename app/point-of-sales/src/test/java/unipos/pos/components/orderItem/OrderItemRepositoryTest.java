package unipos.pos.components.orderItem;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.pos.components.orderItem.model.DiscountOrderItem;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.orderItem.model.OrderItemDiscountOrderItem;
import unipos.pos.components.orderItem.model.ProductOrderItem;
import unipos.pos.test.config.MongoTestConfiguration;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by dominik on 27.08.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfiguration.class})
public class OrderItemRepositoryTest {

    @Autowired
    private OrderItemFixture orderItemFixture;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Before
    public void setUp(){
        orderItemFixture.setUp();
    }

    @Test
    public void testLoadProductOrderItem() throws Exception{

        ProductOrderItem productOrderItem = (ProductOrderItem) orderItemRepository.findAll().get(0);
        assertThat(productOrderItem.getQuantity(), is(2));
        assertThat(productOrderItem.getActionId(), is(nullValue()));
    }

    @Test
    public void testLoadDiscountOrderItem() throws Exception {
        DiscountOrderItem discountOrderItem = (DiscountOrderItem) orderItemRepository.findAll().get(1);
        assertThat(discountOrderItem.getDiscount(), is(new BigDecimal("20")));
        assertThat(discountOrderItem.getActionId(), is(nullValue()));
    }

    @Test
    public void testSaveProductOrderItem() throws Exception {
        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setQuantity(2);
        productOrderItem.setProductNumber("1");
        productOrderItem.setTurnover(new BigDecimal("19.99"));
        productOrderItem.generateHash();
        productOrderItem.setOrderId("orderId1");
        productOrderItem.setOrderItemId("ASDF");
        orderItemRepository.save(productOrderItem);

        assertThat(productOrderItem.getId(), is(notNullValue()));

        ProductOrderItem savedProductOrderItem = (ProductOrderItem) orderItemRepository.findOne(productOrderItem.getId());
        assertThat(savedProductOrderItem, is(notNullValue()));
        assertThat(savedProductOrderItem.getProductNumber(), is("1"));
        assertThat(savedProductOrderItem.getQuantity(), is(2));
        assertThat(savedProductOrderItem.getTurnover(), is(new BigDecimal("19.99")));
    }

    @Test
    public void testSaveDiscountProductOrderItem() throws Exception {
        OrderItemDiscountOrderItem discountOrderItem = new OrderItemDiscountOrderItem();
        discountOrderItem.setOrderId(null);
        discountOrderItem.setOrderItemId("qwer");
        discountOrderItem.setDiscount(new BigDecimal("20"));
        discountOrderItem.setClientCreationDate(null);
        discountOrderItem.setId(null);
        discountOrderItem.generateHash();

        orderItemRepository.save(discountOrderItem);

        assertThat(discountOrderItem.getId(), is(notNullValue()));

        OrderItemDiscountOrderItem savedDiscountOrderItem = (OrderItemDiscountOrderItem) orderItemRepository.findOne(discountOrderItem.getId());
        assertThat(savedDiscountOrderItem, is(notNullValue()));
        assertThat(savedDiscountOrderItem.getDiscount(), is(new BigDecimal("20")));
    }

    @Test
    public void testFindByOrderItemId() throws Exception {
        OrderItem orderItem = orderItemRepository.findByOrderItemId("ASDF");

        assertThat(orderItem, instanceOf(ProductOrderItem.class));

        ProductOrderItem productOrderItem = (ProductOrderItem)orderItem;

        assertThat(productOrderItem, is(notNullValue()));
        assertThat(productOrderItem.getOrderItemId(), is("ASDF"));
    }

    @After
    public void tearDown(){
        orderItemFixture.tearDown();
    }
}