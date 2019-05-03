package unipos.pos.components.order;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Token;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.model.Workstation;
import unipos.pos.components.order.moveOrder.MoveOrderDto;
import unipos.pos.components.order.reassignOrder.ReassignOrderDto;
import unipos.pos.components.order.splitOrder.SplitOrderDto;
import unipos.pos.components.order.tag.OrderTagDto;
import unipos.pos.components.order.transaction.OrderTransaction;
import unipos.pos.components.sequence.SequenceService;
import unipos.pos.components.shared.GSonHolder;
import unipos.pos.components.shared.UrlContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ggradnig
 **/

@RestController

@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    PosRemoteInterface posRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    SequenceService sequenceService;

    Gson gson = GSonHolder.serializeDateGson();

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/openOrder", method = RequestMethod.POST)
    @ApiOperation(value = "Open an order with the current worker",
            response = Order.class)
    @ResponseStatus(HttpStatus.OK)
    public void openOrder(HttpServletRequest request, HttpServletResponse response, String orderToOpenId) {
        String userId = RequestHandler.getAuthToken(request);
        String deviceId = RequestHandler.getDeviceToken(request);
        orderService.openOrder(orderToOpenId, userId, deviceId);
    }

    @RequestMapping(value = "/findByTag", method = RequestMethod.POST)
    public Order findOrderByTag(@RequestParam String key, @RequestParam String value) {
        return orderService.findOrderByTag(key, value);
    }

    @RequestMapping(value = "/addTagToOrder", method = RequestMethod.POST)
    public ResponseEntity<Object> addTagToOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderTagDto orderTag) {

        orderService.addTagToOrder(orderTag.getOrderId(), orderTag.getOrderTag().getKey(), orderTag.getOrderTag().getValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/createNewOrder", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new order with the current worker",
            response = Order.class)
    @ResponseStatus(HttpStatus.OK)
    public void createNewOrder(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody OrderDto orderDto) throws IOException {

        // Hole die Cookies um einen Worker zu erstellen der diese Bestellung erstellt
        Token authToken = authRemoteInterface.getAuthTokenByRequest(request);

        String deviceId = RequestHandler.getDeviceToken(request);

        //ToDo: Überarbeiten! Orders können auch nach TA erstellt werden.
        //First check if its valid to create a new Order
        /*if (!posRemoteInterface.isCreationAllowed(request)) {
            response.sendError(423, "Could not create Order because DailySettlement was already executed \"today\"");
            return;
        }*/

        Order persistedOrder = orderService.createNewOrder(orderDto, authToken.getUser().getGuid(), deviceId);

        //Send the recently created Order to all clients over a socket multicast
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add("data", gson.toJson(persistedOrder));
        mvm.add("target", "/topic/updateOrders");

        //Publish the order over the Rest Template
        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.SOCKET_SEND_TO_ALL, mvm, Void.class);
    }

    @RequestMapping(value = "/getByUser", method = RequestMethod.GET)
    @ApiOperation(value = "show all open Orders by a specific User", response = Order.class)
    @ApiResponses(
            @ApiResponse(code = 401, message = "The User is not logged in --> He has not auth Token")
    )
    @ResponseStatus(HttpStatus.OK)
    public List<Order> showOpenOrdersByUser(HttpServletRequest request, HttpServletResponse response) {

        Token authToken = authRemoteInterface.getAuthTokenByRequest(request);

        if (authToken == null) {
            try {
                response.sendError(401, "NO_AUTH_TOKEN");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //Todo: check if valid user
        List<Order> orderList = orderService.findActiveOrdersByUserGuid(String.valueOf(authToken.getUser().getGuid()));

        //Todo: Active check

        return orderList;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Order> findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return orderService.findActiveOrders();
    }

    @RequestMapping(value = "/getByUserAndDevice", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Order> findByUserAndDevice(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Token authToken = authRemoteInterface.getAuthTokenByRequest(request);
        String deviceToken;
        try {
            deviceToken = RequestHandler.getDeviceToken(request);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
            return null;
        }

        if (authToken == null) {
            try {
                response.sendError(401, "No user with the given AuthToken found");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        List<Order> orderList = orderService.findActiveOrdersByUserIdAndDeviceId(String.valueOf(authToken.getUser().getGuid()), deviceToken);

        return orderList;
    }

    @RequestMapping(value = "/getByDevice", method = RequestMethod.GET)
    public List<Order> findByDevice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deviceId = RequestHandler.getDeviceToken(request);
        Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);

        List<Order> orderList = orderService.findActiveOrdersByDeviceId(deviceId);

        return orderList;
    }

    @RequestMapping(value = "/deleteByOrderId/{orderId}", method = RequestMethod.DELETE)
    public void deleteByClientOrderId(@PathVariable String orderId) {
        orderService.deleteByOrderId(orderId);
    }

    @RequestMapping(value = "/getDefaultCashierId", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getDefaultCashierId(@RequestParam CashierUsage usage, HttpServletRequest request, HttpServletResponse response) {
        if (usage.equals(CashierUsage.Webshop)) {
            return authRemoteInterface.getUserByUsername("webshop");
        } else if (usage.equals(CashierUsage.Pos)) {
            String authToken = RequestHandler.getAuthToken(request);
            return authRemoteInterface.findUserByAuthToken(authToken);
        }
        return null;
    }

    @RequestMapping(value="/splitOrder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public OrderTransaction splitOrder(@RequestBody SplitOrderDto transaction){
        return orderService.splitOrder(transaction.getSourceOrderId(), transaction.getDestOrderId(), transaction.getOrderItemIds());
    }

    @RequestMapping(value="/moveOrder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public OrderTransaction moveOrder(@RequestBody MoveOrderDto transaction){
        return orderService.moveOrder(transaction.getSourceOrderId(), transaction.getSourceTagKey(), transaction.getDestTagValue());
    }

    @RequestMapping(value="/reassignOrder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public OrderTransaction reassignOrder(@RequestBody ReassignOrderDto transaction){
        return orderService.reassignOrder(transaction.getSourceOrderId(), transaction.getDestUserId());
    }
}
