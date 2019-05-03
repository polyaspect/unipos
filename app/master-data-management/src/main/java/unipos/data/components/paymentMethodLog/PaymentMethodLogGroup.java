package unipos.data.components.paymentMethodLog;

import lombok.Data;
import org.springframework.data.annotation.Id;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.product.Product;
import unipos.data.components.productLog.LogAction;

import java.time.LocalDateTime;

/**
 * Created by dominik on 25.08.15.
 */

@Data
public class PaymentMethodLogGroup {

    @Id
    private String _id;
    private String logId;
    private LocalDateTime date;
    private LogAction Action;
    private Boolean deleted = false;
    private Boolean published = false;
    private PaymentMethod paymentMethod;
    private Boolean ignored;
}
