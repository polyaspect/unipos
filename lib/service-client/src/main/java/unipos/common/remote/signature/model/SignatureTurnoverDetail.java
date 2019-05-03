package unipos.common.remote.signature.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by domin on 22.01.2017.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignatureTurnoverDetail {

    private int taxRate;
    private BigDecimal turnoverGross;
    private BigDecimal turnoverGrossIncludingReducingDiscounts;
    private BigDecimal discount;
}
