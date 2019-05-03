package unipos.pos.components.order.reassignOrder;

import lombok.Data;

/**
 * Created by ggradnig on 21.01.2017.
 */
@Data
public class ReassignOrderDto {
    private String sourceOrderId;
    private String destUserId;
}
