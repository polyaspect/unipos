package unipos.pos.components.orderItem.model;

import unipos.common.remote.data.model.PaymentMethod;
import unipos.pos.components.invoice.model.InvoiceItem;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;

import java.math.BigDecimal;

/**
 * Created by dominik on 27.08.15.
 */
public class PaymentOrderItem extends OrderItem {
    //Wird von frontend geschickt => MongoDbId
    private String label;
    private String paymentMethodGuid;
    private String paymentMethod;
    private boolean isBarumsatz = true; //ToDo: set this to a nonstatic value
    private BigDecimal turnover;
    private OrderItemType type = OrderItemType.paymentOrderItem;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "PaymentOrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", position=" + position +
                ", clientCreationDate=" + clientCreationDate +
                ", serverReceiveTime=" + serverReceiveTime +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentMethodGuid='" + paymentMethodGuid + '\'' +
                ", turnover=" + turnover +
                ", type=" + type +
                '}';
    }

    @Override
    public InvoiceItem accept(OrderItemVisitor visitor) {
        return visitor.visit(this);
    }

    public OrderItemType getType() {
        return type;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPaymentMethodGuid() {
        return paymentMethodGuid;
    }

    public void setPaymentMethodGuid(String paymentMethodGuid) {
        this.paymentMethodGuid = paymentMethodGuid;
    }

    public boolean isBarumsatz() {
        return isBarumsatz;
    }

    public void setBarumsatz(boolean barumsatz) {
        isBarumsatz = barumsatz;
    }
}
