package unipos.pos.components.invoice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by Dominik on 26.01.2016.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    private String label;
    private String discountId;
    private BigDecimal amount;
    private String orderItemId;
    private Type type;

    public enum Type {
        INVOICE,
        LINE
    }
}
