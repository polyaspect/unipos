package unipos.auth.components.user;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Created by Dominik on 02.06.2015.
 */

@Data
public abstract class LoginMethod {
    @DBRef
    protected User user;
    protected boolean active;
}
