package unipos.pos.components.cashbook;

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
import java.util.Hashtable;

/**
 * Created by Dominik on 16.01.2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cashbook")
public class CashbookEntry implements Syncable {

    @Id
    private String id;
    //Auto-Incrementing Number
    private Long cashBookId;
    private Type type;
    private BigDecimal amount;
    private String description;
    //Geschäftlich oder Privat
    private Reference reference;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime creationDate;
    private String storeGuid;
    private String userGuid;
    private String guid;

    public CashbookEntry(String id, Long cashBookId, Type type, BigDecimal amount, String description, Reference reference, LocalDateTime creationDate, String storeGuid, String guid) {
        this.id = id;
        this.cashBookId = cashBookId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.reference = reference;
        this.creationDate = creationDate;
        this.storeGuid = storeGuid;
        this.guid = guid;
    }

    public enum Type {
        IN,
        OUT
    }

    public enum Reference {
        PRIVATE,
        COMMERCIAL
    }

    public unipos.common.remote.pos.model.CashbookEntry asDto(){
        unipos.common.remote.pos.model.CashbookEntry clone = new unipos.common.remote.pos.model.CashbookEntry();

        clone.setId(id);
        clone.setCashBookId(cashBookId);
        clone.setType(unipos.common.remote.pos.model.CashbookEntry.Type.valueOf(type.toString()));
        clone.setAmount(amount);
        clone.setDescription(description);
        clone.setCreationDate(creationDate);
        clone.setStoreGuid(storeGuid);
        clone.setUserGuid(userGuid);
        clone.setGuid(guid);

        return clone;
    }
}
