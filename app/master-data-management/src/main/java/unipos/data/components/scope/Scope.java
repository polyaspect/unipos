package unipos.data.components.scope;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.data.components.company.model.Store;
import unipos.data.components.product.Product;

/**
 * Created by Dominik on 27.12.2015.
 */
@Document(collection = "scopes")
@Data
@Builder
public class Scope {

    @Id
    private String id;
    @DBRef
    private Product product;
    @DBRef
    private Store store;
}
