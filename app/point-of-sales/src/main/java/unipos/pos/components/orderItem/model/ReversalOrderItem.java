package unipos.pos.components.orderItem.model;

import unipos.pos.components.invoice.model.InvoiceItem;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;

import java.math.BigDecimal;

/**
 * Created by dominik on 27.08.15.
 */
public class ReversalOrderItem extends OrderItem {
    private String reason;
    private String receiverOrderItem;
    private BigDecimal reversalAmount;
    protected OrderItemType type = OrderItemType.reversalOrderItem;

    public ReversalOrderItem() {
    }

    @Override
    public InvoiceItem accept(OrderItemVisitor visitor) {
        return visitor.visit(this);
    }

    public ReversalOrderItem(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReceiverOrderItem() {
        return receiverOrderItem;
    }

    public void setReceiverOrderItem(String receiverOrderItem) {
        this.receiverOrderItem = receiverOrderItem;
    }

    public BigDecimal getReversalAmount() {
        return reversalAmount;
    }

    public void setReversalAmount(BigDecimal reversalAmount) {
        this.reversalAmount = reversalAmount;
    }

    public OrderItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ReversalOrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", position=" + position +
                ", clientCreationDate=" + clientCreationDate +
                ", serverReceiveTime=" + serverReceiveTime +
                ", reason='" + reason + '\'' +
                ", reveiverOrderItem='" + receiverOrderItem + "'" +
                ", reversalAmount=" + reversalAmount +
                ", type=" + type.toString() +
                '}';
    }
}
