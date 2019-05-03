package unipos.auth.components.authentication;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import unipos.auth.components.user.User;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;

import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * Created by Dominik on 29.05.2015.
 */

@Data

/**
 * This class is used, because it's not recommended to do operations with the JSESSIONID
 * Nevertheless is the JSESSIONID the main "AuthToken" Cookie, because Spring Security
 * associates the Roles with JSESSIONID
 *
 * In a further instance, this token may be shared with the other modules, because it is possible, that the other
 * Module also need a JSESSIONID
 */
public class Token {

    private String token;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDateTime creationDate;

    @DBRef
    private User user;

    /**
     * Creates a new Token instance. sets the creationDate to the instance creation moment;
     */
    public Token() {
        creationDate = LocalDateTime.now();
    }

    /**
     * Creates a new Token instance, and sets the creation date to the instance creation moment;
     * @param token is the token you want to save
     */
    public Token(String token) {
        this.token = token;
        creationDate = LocalDateTime.now();
    }
}
