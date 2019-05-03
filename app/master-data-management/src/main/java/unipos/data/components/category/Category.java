package unipos.data.components.category;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateCategory;

import java.util.UUID;


/**
 * Created by Dominik on 23.07.2015.
 */

@Data
@Builder
@ApiModel(value = "categories", subTypes = TaxRate.class)
@Document(collection = "categories")
public class Category implements Syncable {

    @Id
    private String id;

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    @DBRef
    private TaxRate taxRate;

    private String guid;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, TaxRate taxRate) {
        this.name = name;
        this.taxRate = taxRate;
    }

    public Category(String id, String name, TaxRate taxRate) {
        this.id = id;
        this.name = name;
        this.taxRate = taxRate;
    }

    public Category(String id, String name, TaxRate taxRate, String guid) {
        this.id = id;
        this.name = name;
        this.taxRate = taxRate;
        this.guid = guid;
    }
}
