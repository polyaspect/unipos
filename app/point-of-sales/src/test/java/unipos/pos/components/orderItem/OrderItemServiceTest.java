package unipos.pos.components.orderItem;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import unipos.pos.components.order.Order;
import unipos.pos.components.order.OrderRepository;
import unipos.pos.components.order.OrderService;
import unipos.pos.components.order.OrderServiceImpl;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.orderItem.model.ProductOrderItem;
import unipos.pos.components.sequence.SequenceRepository;
import unipos.pos.components.sequence.SequenceTable;
import unipos.pos.shared.AbstractServiceTest;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author ggradnig
 */

public class OrderItemServiceTest extends AbstractServiceTest {

    @InjectMocks
    private OrderItemService orderService = new OrderItemServiceImpl();

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderItemRepository orderItemRepository;
    @Mock
    SequenceRepository sequenceRepository;

    @Test
    public void testSaveOrderItem() throws Exception {
        Order order = new Order();
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(null);
        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);

        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setActionId("asdfdfs");
        productOrderItem.setOrderId("asdf");
        productOrderItem.setClientCreationDate(LocalDateTime.of(2015,10,31,10,15,15));
        orderService.saveOrderItem(productOrderItem);
        assertThat(order.getOrderItems().get(0).getActionId(), is("asdfdfs"));
    }

    @Test
    public void testDeleteByOrderItemId() throws Exception {
        Order order = new Order();

        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setActionId("asdfdfs");
        productOrderItem.setOrderId("asdf");
        productOrderItem.setOrderItemId("notDelete");
        productOrderItem.setClientCreationDate(LocalDateTime.of(2015,10,31,10,15,15));
        order.getOrderItems().add(productOrderItem);

        ProductOrderItem productOrderItem2 = new ProductOrderItem();
        productOrderItem2.setActionId("asdfdfs2");
        productOrderItem2.setOrderId("asdf");
        productOrderItem2.setOrderItemId("delete");
        productOrderItem2.setClientCreationDate(LocalDateTime.of(2015,10,31,10,15,15));
        order.getOrderItems().add(productOrderItem2);

        when(orderItemRepository.findByOrderItemId(anyString())).thenReturn(productOrderItem2);
        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);
        when(orderItemRepository.deleteByOrderItemId(anyString())).thenReturn(1L);
        when(orderRepository.save(any(Order.class))).thenReturn(null);

        orderService.deleteByOrderItemId("delete");

        assertThat(order.getOrderItems().size(), is(1));
        assertThat(order.getOrderItems().get(0).getOrderItemId(), is("notDelete"));
    }
}
