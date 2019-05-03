package unipos.pos.components.invoice;

import lombok.Data;

/**
 * Created by ggradnig on 18.01.2017.
 */
@Data
public class InvoiceCreationRequest {
    private String clientOrderId;
    private String cashierId;
}
