package unipos.pos.components.invoice.model;



import unipos.pos.components.invoice.model.reversalInvoice.InvoiceItemVisitor;

import java.math.BigDecimal;

/**
 * Created by dominik on 04.09.15.
 */
public class OrderDiscountInvoiceItem extends DiscountInvoiceItem {

    private String receiverOrderId;
    private InvoiceItemType type = InvoiceItemType.orderDiscountInvoiceItem;


    public String getReceiverOrderId() {
        return receiverOrderId;
    }

    public void setReceiverOrderId(String receiverOrderId) {
        this.receiverOrderId = receiverOrderId;
    }

    public InvoiceItemType getType() {
        return type;
    }

    public void setType(InvoiceItemType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrderDiscountInvoiceItem{" +
                "receiverOrderId='" + receiverOrderId + '\'' +
                "type=" + type +
                '}';
    }

    @Override
    public InvoiceItem accept(InvoiceItemVisitor visitor) {
        return visitor.visit(this);
    }
}
