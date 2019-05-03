package unipos.auth.components.user.usernamePassword;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.auth.components.user.LoginMethod;
import unipos.auth.components.user.User;
import unipos.common.remote.sync.model.Syncable;

import javax.persistence.Id;

/**
 * @author Dominik
 */
@Document(collection = "usernamePassword")
@Data
public class UsernamePassword extends LoginMethod implements Syncable{

    @Id
    private String id;
    private String username;

    private String passwordHash;
    private String passwordSalt;

    private String password;

    private String guid;

    public UsernamePassword() {}

    public UsernamePassword(String username, String password, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }

    public UsernamePassword(String username, String password, User user, String guid) {
        this.username = username;
        this.password = password;
        this.user = user;
        this.guid = guid;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
