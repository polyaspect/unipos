package unipos.pos.components.shared;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.apache.commons.lang3.StringUtils;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by dominik on 28.08.15.
 */
public class LocalDateDeserializer extends StdScalarDeserializer<LocalDateTime> {

    private final static DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Europe/Vienna"));

    public LocalDateDeserializer() {
        super(LocalDateTime.class);
    }

    protected LocalDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, org.codehaus.jackson.JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.parse(jp.getText(), DATETIME_FORMAT);
        return localDateTime;
    }
}
