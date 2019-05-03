package unipos.data.components.productLog;

import lombok.Data;
import org.springframework.data.annotation.Id;
import unipos.data.components.product.Product;

import java.time.LocalDateTime;

/**
 * Created by dominik on 25.08.15.
 */

@Data
public class ProductLogGroup {

    @Id
    private String _id;
    private String logId;
    private Long productIdentifier;
    private LocalDateTime date;
    private LogAction action;
    private Boolean deleted = false;
    private Boolean published = false;
    private Product product;
    private Boolean ignored;
}
