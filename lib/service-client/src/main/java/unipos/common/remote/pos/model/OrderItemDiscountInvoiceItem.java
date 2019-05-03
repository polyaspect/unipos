package unipos.common.remote.pos.model;


/**
 * Created by dominik on 04.09.15.
 */
public class OrderItemDiscountInvoiceItem extends DiscountInvoiceItem {

    private String receiverOrderItemId;

    public String getReceiverOrderItemId() {
        return receiverOrderItemId;
    }

    public void setReceiverOrderItemId(String receiverOrderItemId) {
        this.receiverOrderItemId = receiverOrderItemId;
    }
}
