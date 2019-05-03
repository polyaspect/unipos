package unipos.pos.components.order.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ggradnig on 18.01.2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTag {
    private String key;
    private String value;
}
