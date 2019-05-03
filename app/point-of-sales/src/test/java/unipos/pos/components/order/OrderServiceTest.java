package unipos.pos.components.order;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.model.Workstation;
import unipos.pos.components.sequence.SequenceRepository;
import unipos.pos.shared.AbstractServiceTest;

import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author ggradnig
 */

public class OrderServiceTest extends AbstractServiceTest {

    OrderFixture orderFixture = new OrderFixture();

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AuthRemoteInterface authRemoteInterface;
    @Mock
    private SocketRemoteInterface socketRemoteInterface;
    @Mock
    SequenceRepository sequenceRepository;

    @Test
    public void testGetOpenOrdersForUser() {
        User mockUser = new User();
        mockUser.setGuid("1");

        when(orderRepository.findByActiveUsers_guid(mockUser.getGuid())).thenReturn(Arrays.asList(new Order(), new Order()));
        assertThat(orderService.getOpenOrdersForUser(mockUser.getGuid()).size(), is(2));
    }

    @Test
    public void testCreateNewOrder() {
        User mockUser = new User();
        mockUser.setGuid("1");

        Workstation mockDevice = new Workstation();
        mockDevice.setDeviceId("1");

        OrderDto orderBeforeSave = new OrderDto();
        orderBeforeSave.setIsActive(true);
        orderBeforeSave.setClientCreationDate(LocalDateTime.now());
        orderBeforeSave.setOpen(true);
        orderBeforeSave.setActionId("actionId");
        orderBeforeSave.setOrderId("clientGeneratedOrderId");

        Order orderAfterSave = new Order();
        orderAfterSave.setId("xxxx-xxxx-xxxx-xxxx");
        orderAfterSave.setOrderNumber(1L);
        orderAfterSave.setCreatorUser(mockUser);

        when(orderRepository.save(any(Order.class))).thenReturn(orderAfterSave);
        when(sequenceRepository.getNextSequenceId(anyString())).thenReturn(1L);

        when(authRemoteInterface.getUserByGuid("1")).thenReturn(mockUser);
        when(socketRemoteInterface.findByDeviceId("1")).thenReturn(mockDevice);

        Order newOrder = orderService.createNewOrder(orderBeforeSave, mockUser.getGuid(), "1");
        assertTrue(newOrder.getId().equals("xxxx-xxxx-xxxx-xxxx"));
        assertTrue(newOrder.getCreatorUser().getGuid().equals("1"));
        assertThat(newOrder.getOrderNumber(), is(1L));
    }

    @Test
    public void testOpenOrder() {
        Order order = new Order();
        order.setOrderId("orderId1");

        when(authRemoteInterface.getUserByGuid(any(String.class))).then(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                User user = new User();
                user.setGuid("1");
                return user;
            }
        });

        when(socketRemoteInterface.findByDeviceId(any(String.class))).then(new Answer<Workstation>() {
            @Override
            public Workstation answer(InvocationOnMock invocation) throws Throwable {
                Workstation device = new Workstation();
                device.setDeviceId("1");
                return device;
            }
        });

        when(orderRepository.save(any(Order.class))).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                return (Order) invocation.getArguments()[0];
            }
        });

        when(orderRepository.findFirstByOrderId("orderId1")).thenReturn(order);

        order = orderService.openOrder(order.getOrderId(), "1", "1");
        assertTrue(order.getActiveUsers().get(0).getGuid().equals("1"));
    }

    @Test
    public void testFindOrdersByUserId() throws Exception {
        when(orderRepository.findByActiveUsers_guidAndIsActive(anyString(), anyBoolean())).thenReturn(
                Arrays.asList(
                        orderFixture.order1,
                        orderFixture.order2,
                        orderFixture.order3
                )
        );

        List<Order> orderList = orderService.findActiveOrdersByUserGuid("userID");
        assertThat(orderList.size(), is(3));
        assertThat(orderList, is(Arrays.asList(orderFixture.order1, orderFixture.order2, orderFixture.order3)));
    }
}
