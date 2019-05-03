package unipos.report.components.shared.helper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Thomas on 30.01.2016.
 */
@Data
@Builder
public class ProductReportHelper {

    String productNumber;
    String label;
    BigDecimal quantity;
    BigDecimal contribution;
    BigDecimal turnover;

}
