package unipos.pos.components.invoice.model;


import unipos.pos.components.invoice.model.reversalInvoice.InvoiceItemVisitor;

import java.math.BigDecimal;

/**
 * Created by dominik on 04.09.15.
 */
public class ReversalInvoiceItem extends InvoiceItem {

    private String reason;
    private String receiverOrderItem;
    private BigDecimal reversalAmount;
    private InvoiceItemType type = InvoiceItemType.reversalInvoiceItem;

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

    public InvoiceItemType getType() {
        return type;
    }

    public void setType(InvoiceItemType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ReversalInvoiceItem{" +
                "reason='" + reason + '\'' +
                ", receiverOrderItem='" + receiverOrderItem + '\'' +
                ", reversalAmount='" + reversalAmount + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public InvoiceItem accept(InvoiceItemVisitor visitor) {
        return visitor.visit(this);
    }
}
