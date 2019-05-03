package unipos.common.remote.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * Created by jolly on 21.05.2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePassword {
    @Id
    private String id;
    private String username;
    private String password;
    private String guid;

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
