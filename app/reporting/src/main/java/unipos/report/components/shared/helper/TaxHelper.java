package unipos.report.components.shared.helper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Thomas on 04.02.2016.
 */
@Data
@Builder
public class TaxHelper {

    private int taxRate;
    private BigDecimal brutto;
    private BigDecimal netto;
    private BigDecimal mwst;
}
