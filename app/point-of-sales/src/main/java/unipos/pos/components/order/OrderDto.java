package unipos.pos.components.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import unipos.common.remote.auth.model.UserList;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.socket.model.DeviceList;
import unipos.pos.components.order.tag.OrderTag;
import unipos.pos.components.order.tag.OrderTagList;
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
public class OrderDto extends ActionBaseClass implements Serializable
{
	private static final long serialVersionUID = 8464612L;

	private String orderId;
	private Long orderNumber;
	List<OrderItem> orderItems = new ArrayList<>();
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime clientCreationDate;
	private boolean isActive;
	private OrderTagList orderTags = new OrderTagList();

	private boolean open = true;

	public OrderDto()
	{
	}

	public OrderDto(String id, String orderId, Long orderNumber, List<OrderItem> orderItems, LocalDateTime clientCreationDate, LocalDateTime serverReceiveDate, boolean open, boolean isActive, Cashier cashier, Company company) {
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.orderItems = orderItems;
		this.clientCreationDate = clientCreationDate;
		this.isActive = isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Order toOrder(){
		return
				Order.builder().
						orderId(orderId).
						orderNumber(orderNumber).
						orderItems(orderItems).
						clientCreationDate(clientCreationDate).
						isActive(isActive).
						orderTags(orderTags).
						open(open).
						activeDevices(new DeviceList()).
						activeUsers(new UserList()).
						build();
	}
}
