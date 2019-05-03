package unipos.common.remote.report;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;

import java.time.LocalDateTime;

/**
 * Created by Thomas on 26.02.2016.
 */
@Data
@Builder
public class DailySettlementHelper {

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime startDate;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime endDate;
    private String deviceId;
    private String storeGuid;
    private String userId;

    public DailySettlementHelper() {

    }

    public DailySettlementHelper(LocalDateTime startDate, LocalDateTime endDate, String deviceId, String storeGuid, String userId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.deviceId = deviceId;
        this.storeGuid = storeGuid;
        this.userId = userId;
    }
}
