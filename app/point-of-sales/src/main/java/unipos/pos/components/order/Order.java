package unipos.pos.components.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.auth.model.UserList;
import unipos.common.remote.socket.model.DeviceList;
import unipos.common.remote.socket.model.Workstation;
import unipos.pos.components.order.tag.OrderTagList;
import unipos.pos.components.order.transaction.OrderTransaction;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.shared.ActionBaseClass;
import unipos.pos.components.shared.LocalDateDeserializer;
import unipos.pos.components.shared.LocalDateSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ggradnig
 *
 */

@Data
@Builder
@AllArgsConstructor
@Document(collection = "orders")
@ApiModel(value = "orders")
public class Order extends ActionBaseClass implements Serializable
{
	private static final long serialVersionUID = 74125842L;

    @Id
	private String id;
	@ApiModelProperty(notes = "AngularGenerated")
	private String orderId;
	private Long orderNumber;
	List<OrderItem> orderItems = new ArrayList<>();
	@ApiModelProperty(notes = "AngularGenerated")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime clientCreationDate;
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime serverReceiveDate;
	private boolean open = true;
	private boolean isActive;
	private OrderTagList orderTags = new OrderTagList();

	@ApiModelProperty(value="An order is created by a worker", required = true)
	private User creatorUser;

	@ApiModelProperty(value="Multiple workers can work on one orders", required = false)
	private UserList activeUsers = new UserList();

	private User currentUser;

	private DeviceList activeDevices = new DeviceList();
	private Workstation currentDevice;

	private List<OrderTransaction> transactions;

	public Order()
	{
	}

	public Order(String id, String orderId, Long orderNumber, List<OrderItem> orderItems, LocalDateTime clientCreationDate, LocalDateTime serverReceiveDate, boolean open, boolean isActive) {
		this.id = id;
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.orderItems = orderItems;
		this.clientCreationDate = clientCreationDate;
		this.serverReceiveDate = serverReceiveDate;
		this.open = open;
		this.isActive = isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
