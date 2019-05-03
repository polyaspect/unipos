package unipos.common.remote.pos.model;


/**
 * Created by dominik on 04.09.15.
 */
public class OrderDiscountInvoiceItem extends DiscountInvoiceItem {

    private String receiverOrderId;

    public String getReceiverOrderId() {
        return receiverOrderId;
    }

    public void setReceiverOrderId(String receiverOrderId) {
        this.receiverOrderId = receiverOrderId;
    }
}
