package unipos.pos.components.order.moveOrder;

import lombok.Data;

import java.util.List;

/**
 * Created by ggradnig on 21.01.2017.
 */
@Data
public class MoveOrderDto {
    private String sourceOrderId;
    private String sourceTagKey;
    private String destTagValue;
}
