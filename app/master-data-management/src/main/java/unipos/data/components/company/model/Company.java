package unipos.data.components.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dominik on 04.09.15.
 */

@Data
@Builder
@Document(collection = "companies")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company implements Syncable {

    @Id
    private String id;
    private String name;
    private String uid;
    private String commercialRegisterNumber;
    private List<Store> stores = new ArrayList<>();
    private String guid;

    public Company() {}

    public Company(String id, String name, String uid, String commercialRegisterNumber, List<Store> stores, String guid) {
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.commercialRegisterNumber = commercialRegisterNumber;
        this.stores = stores;
        this.guid = guid;
    }
}
