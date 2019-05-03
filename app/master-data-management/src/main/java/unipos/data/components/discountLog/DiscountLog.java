package unipos.data.components.discountLog;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;
import unipos.data.components.discount.Discount;
import unipos.data.components.productLog.LogAction;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by dominik on 19.08.15.
 */

@Builder
@Document(collection = "discountLogs")
@Data
public class DiscountLog implements Syncable {

    @Id
    private String _id;
    private Long discountIdentifier;
    private LocalDateTime date;
    private LogAction Action;
    private Boolean deleted = false;
    private Boolean published = false;
    private Discount discount;
    private Boolean ignored = false;
    private String guid;

    public DiscountLog() {
    }

    public DiscountLog(String _id, Long discountIdentifier, LocalDateTime date, LogAction action, Boolean deleted, Boolean published, Discount discount, Boolean ignored, String guid) {
        this._id = _id;
        this.discountIdentifier = discountIdentifier;
        this.date = date;
        this.Action = action;
        this.deleted = deleted;
        this.published = published;
        this.discount = discount;
        this.ignored = ignored;
        this.guid = guid;
    }

    @Transient
    public static DiscountLog newDiscountLogFromDiscount(Discount discount) {
        DiscountLog discountLog = new DiscountLog();

        if(discount.getDiscountIdentifier() != null) {
            discountLog.setDiscountIdentifier(discount.getDiscountIdentifier());
        }
        discountLog.setDiscount(discount);

        return discountLog;
    }

    @Transient
    public static DiscountLog newDiscountLogFromDiscountLogGround(DiscountLogGroup discountLogGroup) {
        DiscountLog productLog = new DiscountLog();

        productLog.setPublished(discountLogGroup.getPublished());
        productLog.setAction(discountLogGroup.getAction());
        productLog.setDate(discountLogGroup.getDate());
        productLog.set_id(discountLogGroup.getLogId());
        productLog.setDeleted(discountLogGroup.getDeleted());
        productLog.setDiscount(discountLogGroup.getDiscount());
        productLog.setIgnored(discountLogGroup.getIgnored());

        return productLog;
    }
}
