package unipos.pos.components.orderItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.orderItem.model.OrderItemDiscountOrderItem;
import unipos.pos.components.orderItem.model.ProductOrderItem;
import unipos.pos.components.shared.GSonHolder;
import unipos.pos.shared.AbstractRestControllerTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dominik on 03.09.15.
 */
public class OrderItemDiscountOrderItemControllerTest extends AbstractRestControllerTest {

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    @Qualifier("orderItemDiscountOrderItemController")
    OrderItemController orderItemController;

    Gson gson = GSonHolder.serializeDateGson();

    MockRestServiceServer mockServer;

    @Before
    public void setUp()
    {
        reset(orderItemService);
        mockServer = MockRestServiceServer.createServer((RestTemplate) Whitebox.getInternalState(orderItemController, "restTemplate"));
    }

    @Test
    public void testSaveProductOrderItem() throws Exception {

        OrderItemDiscountOrderItem orderItemDiscountOrderItem = new OrderItemDiscountOrderItem();
        orderItemDiscountOrderItem.setOrderItemId(UUID.randomUUID().toString());
        orderItemDiscountOrderItem.setServerReceiveTime(null);
        orderItemDiscountOrderItem.setClientCreationDate(LocalDateTime.now());
        orderItemDiscountOrderItem.setPosition(1);
        orderItemDiscountOrderItem.setOrderId(UUID.randomUUID().toString());
        orderItemDiscountOrderItem.setReceiverOrderItemId(UUID.randomUUID().toString());
        orderItemDiscountOrderItem.setClientCreationDate(null);
        orderItemDiscountOrderItem.setDiscount(new BigDecimal("5.00"));
        orderItemDiscountOrderItem.setDiscountId(UUID.randomUUID().toString());

        when(orderItemService.saveOrderItem(any(OrderItem.class))).thenReturn(null);

        mockServer.expect(requestTo("http://localhost:8080/socket/sendToAll"))
                .andRespond(withSuccess());

        mockMvc.perform(post("/orderItemDiscountOrderItem/new").header("Content-Type", "application/json")
                .content(gson.toJson(orderItemDiscountOrderItem)))
                .andExpect(status().isOk()).andReturn();
    }
}