package unipos.common.remote.pos.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by dominik on 03.09.15.
 */


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

    protected String id;
    protected int position;
    protected String hash;
    protected InvoiceItemType type;

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public InvoiceItemType getType() {
        return type;
    }

    public void setType(InvoiceItemType type) {
        this.type = type;
    }
}
