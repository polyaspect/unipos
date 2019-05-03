package unipos.pos.components.orderItem.model;

import org.codehaus.jackson.annotate.JsonTypeName;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;

import java.math.BigDecimal;

/**
 * Created by dominik on 27.08.15.
 */
public abstract class DiscountOrderItem extends OrderItem {

    //Client sends this to the server
    protected String discountId;

    //Client sends this to the server
    protected BigDecimal discount;
    protected String label;

    public DiscountOrderItem() {
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "DiscountOrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", position=" + position +
                ", clientCreationDate=" + clientCreationDate +
                ", serverReceiveTime=" + serverReceiveTime +
                ", discountId" + discountId +
                ", discount=" + discount +
                ", label='" + label + "'" +
                '}';
    }
}
