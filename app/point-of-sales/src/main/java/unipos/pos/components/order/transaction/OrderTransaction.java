package unipos.pos.components.order.transaction;

import lombok.Builder;
import lombok.Data;

/**
 * Created by ggradnig on 18.01.2017.
 */
@Data
@Builder
public class OrderTransaction {
    private String sourceOrderId;
    private boolean success = false;

    public OrderTransaction(){

    }

    public OrderTransaction(String sourceOrderId, boolean success){
        this.sourceOrderId = sourceOrderId;
        this.success = success;
    }
}
