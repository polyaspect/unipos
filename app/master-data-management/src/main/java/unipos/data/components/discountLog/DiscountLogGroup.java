package unipos.data.components.discountLog;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import unipos.data.components.discount.Discount;
import unipos.data.components.productLog.LogAction;

import java.time.LocalDateTime;

/**
 * Created by dominik on 02.09.15.
 */
@Builder
@Data
public class DiscountLogGroup {
        @Id
        private String _id;
        private String logId;
        private LocalDateTime date;
        private LogAction Action;
        private Boolean deleted = false;
        private Boolean published = false;
        private Discount discount;
        private Boolean ignored;
}
