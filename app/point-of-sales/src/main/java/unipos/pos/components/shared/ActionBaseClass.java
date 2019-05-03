package unipos.pos.components.shared;

import com.wordnik.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Transient;
import unipos.pos.components.orderItem.model.OrderItem;

/**
 * Created by dominik on 28.08.15.
 */

@ApiModel(subTypes = {
        OrderItem.class
})
public class ActionBaseClass {

    @Transient
    protected String actionId;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
