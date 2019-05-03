package unipos.pos.components.orderItem.model;

import org.springframework.data.mongodb.core.mapping.Document;
import unipos.pos.components.invoice.model.InvoiceItem;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;

/**
 * Created by dominik on 03.09.15.
 */

public class OrderDiscountOrderItem extends DiscountOrderItem {

    //Client sends this to the server
    //This property sets the destination, the rabat is going.
    private String receiverOrderId;
    private OrderItemType type = OrderItemType.orderDiscountOrderItem;

    public String getReceiverOrderId() {
        return receiverOrderId;
    }

    public void setReceiverOrderId(String receiverOrderId) {
        this.receiverOrderId = receiverOrderId;
    }

    public OrderItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "OrderDiscountOrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", position=" + position +
                ", clientCreationDate=" + clientCreationDate +
                ", serverReceiveTime=" + serverReceiveTime +
                ", discountId" + discountId +
                ", discount=" + discount +
                ", receiverOrderId='" + receiverOrderId + "'" +
                ", type=" + type +
                "}";
    }

    @Override
    public InvoiceItem accept(OrderItemVisitor visitor) {
        return visitor.visit(this);
    }
}
