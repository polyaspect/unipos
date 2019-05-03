package unipos.pos.components.invoice.model;



import unipos.pos.components.invoice.model.reversalInvoice.InvoiceItemVisitor;

import java.math.BigDecimal;

/**
 * Created by dominik on 04.09.15.
 */
public class OrderItemDiscountInvoiceItem extends DiscountInvoiceItem {

    private String receiverOrderItemId;
    private InvoiceItemType type = InvoiceItemType.orderItemDiscountInvoiceItem;


    public String getReceiverOrderItemId() {
        return receiverOrderItemId;
    }

    public void setReceiverOrderItemId(String receiverOrderItemId) {
        this.receiverOrderItemId = receiverOrderItemId;
    }

    public InvoiceItemType getType() {
        return type;
    }

    public void setType(InvoiceItemType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrderItemDiscountInvoiceItem{" +
                "receiverOrderItemId='" + receiverOrderItemId + '\'' +
                "type=" + type +
                '}';
    }

    @Override
    public InvoiceItem accept(InvoiceItemVisitor visitor) {
        return visitor.visit(this);
    }
}
