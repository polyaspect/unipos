package unipos.authChecker.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by Dominik on 24.06.2015.
 */

@Data
public class Permission {
    private String key;
    private boolean allowed;

    public Permission() {
    }

    public Permission(String key) {
        this.key = key;
        this.allowed = false;
    }

    public Permission(String key, boolean allowed) {
        this.key = key;
        this.allowed = allowed;
    }
}
