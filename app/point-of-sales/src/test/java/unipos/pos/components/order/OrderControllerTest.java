package unipos.pos.components.order;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Token;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.pos.components.shared.GSonHolder;
import unipos.pos.components.shared.UrlContainer;
import unipos.pos.shared.AbstractRestControllerTest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ggradnig
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(OrderMock.class)
public class OrderControllerTest extends AbstractRestControllerTest{

    OrderFixture orderFixture = new OrderFixture();

    @Autowired
    private OrderService orderService;

    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    PosRemoteInterface posRemoteInterface;

    @Autowired
    OrderController orderController;

    MockRestServiceServer mockServer;
    private User mockUser;

    Gson gson = GSonHolder.serializeDateGson();

    @Before
    public void setUp()
    {
        orderFixture.init();
        super.setup();
        reset(orderService, authRemoteInterface);
        mockUser = new User();
        mockUser.setGuid("1");

//        when(workerRemoteInterface.getWorker(argThat(new IsValidWorkerHttpServletRequest()))).thenReturn(mockWorker);
    }

    //TODO: Fix Tests
    /*@Test
    public void testCreateNewOrder() throws Exception{
        Worker worker = new Worker();
        worker.setDeviceID("deviceID");
        worker.setStoreID("storeID");
        worker.setUserID("userID");

        Order orderBeforeSave = new Order();
        orderBeforeSave.setIsActive(true);
        orderBeforeSave.setClientCreationDate(LocalDateTime.now());
        orderBeforeSave.setOpen(true);
        orderBeforeSave.setActionId("actionId");
        orderBeforeSave.setOrderId("clientGeneratedOrderId");

        MockRestServiceServer restServiceServer = MockRestServiceServer.createServer((RestTemplate) Whitebox.getInternalState(orderController, "restTemplate"));
        restServiceServer.expect(requestTo("http://localhost:8080/socket/sendToAll")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        when(orderService.createNewOrder(any(Worker.class), any(Order.class), any(), any())).thenReturn(new Order());
        when(workerRemoteInterface.getWorker(any(HttpServletRequest.class))).thenReturn(worker);
        when(authRemoteInterface.getAuthTokenByRequest(any(HttpServletRequest.class))).thenReturn(Token.builder().user(User.builder().userId(1L).name("Dominik").build()).token("XXXX-XXXX-XXXX-XXXX").build());
        when(posRemoteInterface.isCreationAllowed(any(HttpServletRequest.class))).thenReturn(true);

        Cookie cookie = new Cookie("AuthToken", "xxxx-xxxx-xxxx-xxxx");

        mockMvc.perform(post(UrlContainer.ORDERS_CREATE_NEW_ORDER)
                .cookie(cookie)
                .header("Content-Type", "application/json")
                .content(
                        gson.toJson(orderBeforeSave)
                ))
                .andExpect(status().isOk());

        verify(workerRemoteInterface, times(1)).getWorker(any(HttpServletRequest.class));
//        verify(orderService, times(1)).createNewOrder(worker, "2015.08.27 17:41:47", anyString());
    }*/

    @Test
    public void testOpenOrder() throws Exception{
        Order order1 = new Order();

        when(posRemoteInterface.isCreationAllowed(any(HttpServletRequest.class))).thenReturn(true);

//        when(orderService.openOrder(anyObject(), argThat(new IsValidWorker()))).then(new Answer<Order>() {
//            @Override
//            public Order answer(InvocationOnMock invocation) throws Throwable {
//                Order order = (Order) invocation.getArguments()[0];
//                order.getActiveWorkers().add(mockWorker);
//                return order;
//            }
//        });


        Cookie cookie1 = new Cookie("AuthToken", "xxxx-xxxx-xxxx-xxxx");
        Cookie cookie2 = new Cookie("DeviceToken", "xxxx-xxxx-xxxx-xxxx");

        mockMvc.perform(post("/orders/openOrder").cookie(cookie1, cookie2).requestAttr("order", order1)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testShowOpenOrdersByUser() throws Exception {
        when(orderService.findActiveOrdersByUserGuid("1")).thenReturn(
                Arrays.asList(
                        orderFixture.order1,
                        orderFixture.order3,
                        orderFixture.order4
                )
        );
        when(authRemoteInterface.getAuthTokenByRequest(any(HttpServletRequest.class))).thenReturn(Token.builder().token("XXXXXXXXXXXXXXXXXX").user(User.builder().userId(1L).build()).build());
        when(posRemoteInterface.isCreationAllowed(any(HttpServletRequest.class))).thenReturn(true);

        MvcResult result = mockMvc.perform(get("/orders/getByUser").cookie(new Cookie("AuthToken", "XXXXXXXXXXXXXXXXXX")))
                .andExpect(status().isOk()).andReturn();

//        List<Order> orderListResult = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Order>>() {}.getType());
//        assertThat(orderListResult.size(), is(3));
    }

    @Test
    public void testShowOpenOrdersByUserWithoutCookie() throws Exception {
        when(posRemoteInterface.isCreationAllowed(any(HttpServletRequest.class))).thenReturn(true);
        mockMvc.perform(get("/orders/getByUser"))
                .andExpect(status().is(401));
    }


}
