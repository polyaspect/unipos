package unipos.pos.components.shared;

import lombok.Builder;
import lombok.Data;

/**
 * Created by dominik on 27.08.15.
 */

@Builder
@Data
public class ActionWrapper {
    String actionId;
    Object data;
}
