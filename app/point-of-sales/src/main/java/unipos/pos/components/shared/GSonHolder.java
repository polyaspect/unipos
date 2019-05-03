package unipos.pos.components.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Dominik on 30.09.2015.
 */
public class GSonHolder {

    public static Gson serializeDateGson() {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
                    String dateValue = src.atZone(ZoneId.of("Europe/Vienna")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    JsonPrimitive jsonPrimitive = new JsonPrimitive(dateValue);
                    return jsonPrimitive;
                })
                .serializeNulls()
                .setPrettyPrinting()
                .create();

        return gson;
    }
}
