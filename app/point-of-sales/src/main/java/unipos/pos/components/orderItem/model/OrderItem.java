package unipos.pos.components.orderItem.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.wordnik.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.pos.model.*;
import unipos.pos.components.invoice.model.InvoiceItem;
import unipos.pos.components.order.Order;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;
import unipos.pos.components.shared.ActionBaseClass;
import unipos.pos.components.shared.HashGeneratorUtils;
import unipos.pos.components.shared.LocalDateDeserializer;
import unipos.pos.components.shared.LocalDateSerializer;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * Created by dominik on 27.08.15.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = ProductOrderItem.class, name = "productOrderItem"),
        @JsonSubTypes.Type(value = PaymentOrderItem.class, name = "paymentOrderItem"),
        @JsonSubTypes.Type(value = OrderDiscountOrderItem.class, name = "orderDiscountOrderItem"),
        @JsonSubTypes.Type(value = OrderItemDiscountOrderItem.class, name = "orderItemDiscountOrderItem"),
        @JsonSubTypes.Type(value = ReversalOrderItem.class, name = "reversalOrderItem"),
        @JsonSubTypes.Type(value = ChangeOrderItem.class, name = "changeOrderItem")
})
@Document(collection = "orderItems")
public abstract class OrderItem extends ActionBaseClass {
    @Id
    protected String id;
    protected String orderItemId;
    protected String orderId;
    protected int position;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    protected LocalDateTime clientCreationDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    protected LocalDateTime serverReceiveTime;
    protected String hash;

    public OrderItem() {
    }

    public final void generateHash() {
        hash = HashGeneratorUtils.generateMD5(toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public LocalDateTime getClientCreationDate() {
        return clientCreationDate;
    }

    public void setClientCreationDate(LocalDateTime clientCreationDate) {
        this.clientCreationDate = clientCreationDate;
    }

    public LocalDateTime getServerReceiveTime() {
        return serverReceiveTime;
    }

    public void setServerReceiveTime(LocalDateTime serverReceiveTime) {
        this.serverReceiveTime = serverReceiveTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public abstract InvoiceItem accept(OrderItemVisitor visitor);
}
