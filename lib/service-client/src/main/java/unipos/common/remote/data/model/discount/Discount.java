package unipos.common.remote.data.model.discount;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Discount {

    private String id;
    private Long discountIdentifier;
    private DiscountUsage discountUsage;
    private DiscountType discountType;
    private String name;
    private BigDecimal value;

    public Discount() {

    }

    public Discount(String id, Long discountIdentifier, DiscountUsage discountUsage, DiscountType discountType, String name, BigDecimal value) {
        this.id = id;
        this.discountIdentifier = discountIdentifier;
        this.discountUsage = discountUsage;
        this.discountType = discountType;
        this.name = name;
        this.value = value;
    }
}
