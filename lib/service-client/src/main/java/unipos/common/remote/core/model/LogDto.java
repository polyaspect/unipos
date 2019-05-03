package unipos.common.remote.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gradnig on 07.11.2015.
 */
@Data
@Builder
public class LogDto {
    public enum Level{
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }
    private Level level;
    private String message;
    private String source;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime dateTime;
    private Map<String, String> parameters = new HashMap<String, String>();

    public LogDto() {}

    public LogDto(Level level, String message, String source, LocalDateTime dateTime, Map<String,String> parameters) {
        this.level = level;
        this.message = message;
        this.source = source;
        this.dateTime = dateTime;
        this.parameters = parameters;
    }

    public void addParameter(String key, String value){
        if(parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put(key, value);
    }

    public void addExceptionParameters(Exception e){
        addParameter("ExceptionType", e.getClass().toString());
        addParameter("ExceptionMessage", e.getMessage());

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        addParameter("ExceptionStackTrace", errors.toString());
    }
}
