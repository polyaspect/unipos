package unipos.pos.components.orderItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import unipos.pos.components.order.OrderMock;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.orderItem.model.ProductOrderItem;
import unipos.pos.components.shared.GSonHolder;
import unipos.pos.shared.AbstractRestControllerTest;

import java.math.BigDecimal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ggradnig
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(OrderMock.class)
public class ProductOrderItemControllerTest extends AbstractRestControllerTest{
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    @Qualifier("productOrderItemController")
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

        ProductOrderItem productOrderItemTestClass = new ProductOrderItem();
        productOrderItemTestClass.setOrderItemId("orderItemId");
        productOrderItemTestClass.setProductNumber("1");
        productOrderItemTestClass.setQuantity(2);
        productOrderItemTestClass.setTurnover(new BigDecimal("19.99"));
        productOrderItemTestClass.generateHash();

        when(orderItemService.saveOrderItem(any(OrderItem.class))).thenReturn(null);

        mockServer.expect(requestTo("http://localhost:8080/socket/sendToAll"))
                .andRespond(withSuccess());

        MvcResult result = mockMvc.perform(post("/productOrderItem/new").header("Content-Type", "application/json")
                .content(gson.toJson(productOrderItemTestClass)))
        .andExpect(status().isOk()).andReturn();

        int i = 0;
    }
}
