package unipos.pos.components.invoice.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by ggradnig on 18.01.2017.
 */
@Data
@Builder
public class InvoiceTag {
    private String key;
    private String value;
}
