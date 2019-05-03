package unipos.pos.components.invoice.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.pos.components.invoice.model.reversalInvoice.InvoiceItemVisitor;
import unipos.pos.components.shared.HashGeneratorUtils;

/**
 * Created by dominik on 03.09.15.
 */

@Document(collection = "invoiceItems")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = ProductInvoiceItem.class, name = "productInvoiceItem"),
        @JsonSubTypes.Type(value = PaymentInvoiceItem.class, name = "paymentInvoiceItem"),
        @JsonSubTypes.Type(value = OrderDiscountInvoiceItem.class, name = "orderDiscountInvoiceItem"),
        @JsonSubTypes.Type(value = OrderItemDiscountInvoiceItem.class, name = "orderItemDiscountInvoiceItem"),
        @JsonSubTypes.Type(value = ReversalInvoiceItem.class, name = "reversalInvoiceItem"),
        @JsonSubTypes.Type(value = TaxInvoiceItem.class, name = "taxInvoiceItem"),
        @JsonSubTypes.Type(value = ChangeInvoiceItem.class, name = "changeInvoiceItem")
})
public abstract class InvoiceItem {

    @Id
    protected String id;
    protected int position;
    protected String hash;
    protected boolean reversalApplied; //This flag indicates of this productOrderItem has been storniert.
    protected String orderItemId; //I need this orderItemId, so that I can use the old Id of the OrderItem for the Discounts.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public boolean isReversalApplied() {
        return reversalApplied;
    }

    public void setReversalApplied(boolean reversalApplied) {
        this.reversalApplied = reversalApplied;
    }

    public String getHash() {
        return hash;
    }

    public void generateHash() {
        this.hash = HashGeneratorUtils.generateMD5(toString());
    }

    public abstract InvoiceItem accept(InvoiceItemVisitor visitor);

}
