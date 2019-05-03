package unipos.report.components.shared.helper;

import lombok.Builder;
import lombok.Data;
import unipos.common.remote.data.model.product.Category;

import java.math.BigDecimal;

/**
 * Created by Thomas on 19.02.2016.
 */
@Builder
@Data
public class CategoryReportHelper {

    Category category;
    BigDecimal turnover;
    BigDecimal contribution;
    BigDecimal quantity;
    BigDecimal turnoverTax;
    BigDecimal turnoverNet;
}
