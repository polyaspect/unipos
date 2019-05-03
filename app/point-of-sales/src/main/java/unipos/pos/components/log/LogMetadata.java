package unipos.pos.components.log;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Created by Gradnig on 07.11.2015.
 */
@Data
public class LogMetadata {
    private String event;
    private Map<String, String> parameters;
}
