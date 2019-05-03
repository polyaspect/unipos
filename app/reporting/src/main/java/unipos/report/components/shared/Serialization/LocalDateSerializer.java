package unipos.report.components.shared.Serialization;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by dominik on 28.08.15.
 */
public class LocalDateSerializer extends StdScalarSerializer<LocalDateTime> {

    private final static DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Europe/Vienna"));

    public LocalDateSerializer() {
        super(LocalDateTime.class);
    }

    protected LocalDateSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeString(value.format(DATETIME_FORMAT));
    }
}
