package unipos.pos.components.order.tag;

import lombok.Data;
import unipos.pos.components.order.tag.OrderTag;

/**
 * Created by ggradnig on 18.01.2017.
 */
@Data
public class OrderTagDto {
    private String orderId;
    private OrderTag orderTag;
}
