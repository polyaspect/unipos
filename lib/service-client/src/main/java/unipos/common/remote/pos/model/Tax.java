package unipos.common.remote.pos.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by dominik on 16.09.15.
 */
@Data
public class Tax {
    private int taxRate;
    private BigDecimal taxAmount;
    private BigDecimal turnoverGross;
    private BigDecimal turnoverNet;
}
