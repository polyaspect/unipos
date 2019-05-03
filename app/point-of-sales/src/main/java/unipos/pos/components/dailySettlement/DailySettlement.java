package unipos.pos.components.dailySettlement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;
import unipos.common.remote.sync.model.Syncable;

import java.time.LocalDateTime;

/**
 * Created by Dominik on 19.01.2016.
 */

@Document(collection = "dailySettlements")
@Data
@Builder
public class DailySettlement implements Syncable {

    @Id
    private String id;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime dateTime;
    private boolean closed;
    private String storeGuid; //the storeGuid of the store the DailySettlement is destined
    private String deviceId; //the deviceId of the Device that created the DailySettlement
    private String guid;

    public DailySettlement() {
    }

    public DailySettlement(String id, LocalDateTime dateTime, boolean closed, String storeGuid, String deviceId, String guid) {
        this.id = id;
        this.dateTime = dateTime;
        this.closed = closed;
        this.storeGuid = storeGuid;
        this.deviceId = deviceId;
        this.guid = guid;
    }

    public enum Mode {
        DEVICE,
        GLOBAL
    }
}
