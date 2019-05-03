package unipos.common.container;

import com.google.gson.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Dominik on 30.09.2015.
 */
public class GSonHolder {

    private final static DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Europe/Vienna"));

    public static Gson serializeDateGson() {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
                    String dateValue = src.atZone(ZoneId.of("Europe/Vienna")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    JsonPrimitive jsonPrimitive = new JsonPrimitive(dateValue);
                    return jsonPrimitive;
                })
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> {
                    LocalDateTime localDateTime = LocalDateTime.parse(json.getAsString(), DATETIME_FORMAT);
                    return localDateTime;
                })
                .serializeNulls()
                .setPrettyPrinting()
                .create();

        return gson;
    }
}
