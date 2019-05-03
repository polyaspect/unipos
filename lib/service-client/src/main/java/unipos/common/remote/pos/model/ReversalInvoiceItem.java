package unipos.common.remote.pos.model;


import java.math.BigDecimal;

/**
 * Created by dominik on 04.09.15.
 */
public class ReversalInvoiceItem extends InvoiceItem {

    private String reason;
    private String receiverOrderItem;
    private BigDecimal reversalAmount;

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
}
