package unipos.data.components.paymentMethodLog;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.product.Product;
import unipos.data.components.productLog.LogAction;
import unipos.data.components.productLog.ProductLogGroup;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by dominik on 19.08.15.
 */

@Builder
@Document(collection = "paymentMethodLogs")
@Data
public class PaymentMethodLog implements Syncable {

    @Id
    private String _id;
    private Long paymentMethodIdentifier;
    private LocalDateTime date;
    private LogAction Action;
    private Boolean deleted = false;
    private Boolean published = false;
    private PaymentMethod paymentMethod;
    private Boolean ignored = false;
    private String guid;

    public PaymentMethodLog() {
    }

    public PaymentMethodLog(String _id, Long paymentMethodIdentifier, LocalDateTime date, LogAction action, Boolean deleted, Boolean published, PaymentMethod paymentMethod, Boolean ignored, String guid) {
        this._id = _id;
        this.paymentMethodIdentifier = paymentMethodIdentifier;
        this.date = date;
        this.Action = action;
        this.deleted = deleted;
        this.published = published;
        this.paymentMethod = paymentMethod;
        this.ignored = ignored;
        this.guid = guid;
    }

    @Transient
    public static PaymentMethodLog newPaymentMethodLogFromPaymentMethod(PaymentMethod paymentMethod) {
        PaymentMethodLog paymentMethodLog = new PaymentMethodLog();

        if(paymentMethod.getPaymentMethodIdentifier() != null) {
            paymentMethodLog.setPaymentMethodIdentifier(paymentMethod.getPaymentMethodIdentifier());
        }
        paymentMethodLog.setPaymentMethod(paymentMethod);

        return paymentMethodLog;
    }

    @Transient
    public static PaymentMethodLog newProductLogFromProductLogGroup(PaymentMethodLogGroup paymentMethodLogGroup) {
        PaymentMethodLog productLog = new PaymentMethodLog();

        productLog.setPublished(paymentMethodLogGroup.getPublished());
        productLog.setAction(paymentMethodLogGroup.getAction());
        productLog.setDate(paymentMethodLogGroup.getDate());
        productLog.set_id(paymentMethodLogGroup.getLogId());
        productLog.setDeleted(paymentMethodLogGroup.getDeleted());
        productLog.setPaymentMethod(paymentMethodLogGroup.getPaymentMethod());
        productLog.setIgnored(paymentMethodLogGroup.getIgnored());

        return productLog;
    }
}
