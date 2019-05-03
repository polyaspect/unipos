package unipos.pos.components.order.splitOrder;

import lombok.Data;

import java.util.List;

/**
 * Created by ggradnig on 21.01.2017.
 */
@Data
public class SplitOrderDto {
    private String sourceOrderId;
    private String destOrderId;
    private List<String> orderItemIds;
}
