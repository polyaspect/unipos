package unipos.socket.components.workstation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;
import unipos.common.remote.data.model.company.Store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 13.08.2015.
 */

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Workstation {

    @Id
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
    List<String> stores = new ArrayList<>();
    List<Printer> printers = new ArrayList<>();
    boolean reloadRequired;
    boolean reloadWhenPossible;
    boolean forceReload;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDateTime lastReload;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDateTime lastKeepAlive;
}
