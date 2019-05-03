package unipos.data.components.company.model;

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
import unipos.common.remote.sync.model.Syncable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by dominik on 04.09.15.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stores")
public class Store implements Syncable {

    @Id
    private String id;
    private Long storeId;     //AutoIncrementing Id
    private String name;
    private Address address;
    private String companyGuid;
    private String phone;
    private String fax;
    private String email;
    private String guid;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime closeHour;
    private boolean controllerStore;
    private BigDecimal pettyCash;
    private boolean printDailySettlementReport;
    private String depEmail;
}
