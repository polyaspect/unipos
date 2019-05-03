package unipos.common.remote.pos.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by dominik on 11.09.15.
 */

@Builder
@Data
public class Cashier {
    private String userId;
    private String name;
    private String userGuid;

    public Cashier() {}

    public Cashier(String userId, String name, String userGuid) {
        this.userId = userId;
        this.name = name;
        this.userGuid = userGuid;
    }
}
