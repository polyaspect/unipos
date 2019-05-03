package unipos.common.remote.socket.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by dominik on 26.08.15.
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workstation {

    String id;
    //Fortlaufende Nummer
    Long deviceNumber;
    //GUID
    String deviceId;
    String deviceName;
    String ipAdress;
    String authToken;
    String currentWorkingUser;
    String currentStoreId;
    String cashierId;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDateTime creationDate;
    //guids of the Stores the Device is assigned to.
    List<String> stores;

    public Workstation(String id, Long deviceNumber, String deviceId, String deviceName, String ipAdress, String authToken, String currentWorkingUser, LocalDateTime creationDate, List<String> stores) {
        this.id = id;
        this.deviceNumber = deviceNumber;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.ipAdress = ipAdress;
        this.authToken = authToken;
        this.currentWorkingUser = currentWorkingUser;
        this.creationDate = creationDate;
        this.stores = stores;
    }
}
