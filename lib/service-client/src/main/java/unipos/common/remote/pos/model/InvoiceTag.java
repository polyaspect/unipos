package unipos.common.remote.pos.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by ggradnig on 29.01.2017.
 */
@Data
@Builder
public class InvoiceTag {
    private String key;
    private String value;
}
