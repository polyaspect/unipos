package unipos.common.remote.auth.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;

import java.time.LocalDateTime;

/**
 * Created by Dominik on 29.05.2015.
 */

@Data
@Builder
public class Token {

    private String token;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDateTime creationDate;

    private User user;

    public Token() {
    }

    public Token(String token, LocalDateTime creationDate, User user) {
        this.token = token;
        this.creationDate = creationDate;
        this.user = user;
    }
}
