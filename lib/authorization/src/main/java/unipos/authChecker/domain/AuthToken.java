package unipos.authChecker.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 24.06.2015.
 */

@Data
public class AuthToken {
    private String authToken;
    private List<Permission> permissions;

    public AuthToken() {
        permissions = new ArrayList<Permission>();
    }

    public AuthToken(String authToken) {
        this.permissions = new ArrayList<Permission>();
        this.authToken = authToken;
    }

    public AuthToken(String authToken, List<Permission> permissions) {
        this.authToken = authToken;
        this.permissions = permissions;
    }

}
