package unipos.common.remote.pos.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Dominik on 26.01.2016.
 */

@Data
@Builder
public class Discount {

    private String label;
    private String discountId;
    private BigDecimal amount;
    private Type type;

    public Discount() {}

    public Discount(String label, String discountId, BigDecimal amount, Type type) {
        this.label = label;
        this.discountId = discountId;
        this.amount = amount;
        this.type = type;
    }

    public enum Type {
        INVOICE,
        LINE
    }
}
