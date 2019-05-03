package unipos.data.components.discount;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by dominik on 02.09.15.
 */

@Document(collection = "discounts")
@Builder
@Data
public class Discount implements Syncable {

    @Id
    private String id;
    private Long discountIdentifier;
    private DiscountUsage discountUsage;
    private DiscountType discountType;
    private String name;
    private BigDecimal value;
    private String guid;

    public Discount() {

    }

    public Discount(String id, Long discountIdentifier, DiscountUsage discountUsage, DiscountType discountType, String name, BigDecimal value, String guid) {
        this.id = id;
        this.discountIdentifier = discountIdentifier;
        this.discountUsage = discountUsage;
        this.discountType = discountType;
        this.name = name;
        this.value = value;
        this.guid = guid;
    }
}
