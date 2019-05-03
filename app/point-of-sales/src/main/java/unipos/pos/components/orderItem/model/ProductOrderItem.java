package unipos.pos.components.orderItem.model;

import unipos.pos.components.invoice.model.InvoiceItem;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;

import java.math.BigDecimal;

/**
 * Created by dominik on 27.08.15.
 */


public class ProductOrderItem extends OrderItem {

    public String productNumber;
    public BigDecimal turnover;
    public int quantity;
    public String label;
    protected OrderItemType type = OrderItemType.productOrderItem;

    public ProductOrderItem() {super();}

    @Override
    public InvoiceItem accept(OrderItemVisitor visitor) {
        return visitor.visit(this);
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public OrderItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ProductOrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", position=" + position +
                ", clientCreationDate=" + clientCreationDate +
                ", serverReceiveTime=" + serverReceiveTime +
                ", productNumber='" + productNumber + "'" +
                ", turnover=" + turnover +
                ", quantity=" + quantity +
                ", label='" + label + '\'' +
                ", type=" + type.toString() +
                '}';
    }
}
