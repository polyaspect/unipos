package unipos.pos.components.orderItem.model;

import unipos.pos.components.invoice.model.InvoiceItem;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;

/**
 * Created by dominik on 03.09.15.
 */
public class OrderItemDiscountOrderItem extends DiscountOrderItem {

    private String receiverOrderItemId;
    private OrderItemType type = OrderItemType.orderItemDiscountOrderItem;

    public String getReceiverOrderItemId() {
        return receiverOrderItemId;
    }

    public void setReceiverOrderItemId(String receiverOrderItemId) {
        this.receiverOrderItemId = receiverOrderItemId;
    }

    public OrderItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "OrderItemDiscountOrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", position=" + position +
                ", clientCreationDate=" + clientCreationDate +
                ", serverReceiveTime=" + serverReceiveTime +
                ", discountId" + discountId +
                ", discount=" + discount +
                ", receiverOrderItemId='" + receiverOrderItemId + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public InvoiceItem accept(OrderItemVisitor visitor) {
        return visitor.visit(this);
    }
}
