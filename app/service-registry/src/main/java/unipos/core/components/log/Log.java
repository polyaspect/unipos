package unipos.core.components.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;

import java.lang.annotation.Documented;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gradnig on 07.11.2015.
 */
@Data
@Document(collection = "logs")
public class Log {
    public enum Level{
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    @Id
    private String id;
    private Level level;
    private String message;
    private String source;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime dateTime;
    private Map<String, String> parameters = new HashMap<String, String>();
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime receivedDateTime;

    public Log(){

    }

    public void addParameter(String key, String value){
        parameters.put(key, value);
    }
}
