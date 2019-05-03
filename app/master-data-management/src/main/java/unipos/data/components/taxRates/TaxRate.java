package unipos.data.components.taxRates;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;

/**
 * Created by dominik on 28.07.15.
 */

@Data
@Builder
@ApiModel
@Document(collection = "taxRates")
public class TaxRate implements Syncable {

    @Id
    private String id;

    @ApiModelProperty(required = true)
    private String name;

    private String description;

    @ApiModelProperty(required = true)
    private int percentage;

    @ApiModelProperty(required = true)
    private TaxRateCategory taxRateCategory;

    private String guid;

    public TaxRate() {
    }

    public TaxRate(String id, String name, String description, int percentage, TaxRateCategory taxRateCategory, String guid) {
        this.name = name;
        this.percentage = percentage;
        this.taxRateCategory = taxRateCategory;
        this.description = description;
        this.guid = guid;
    }

    public TaxRate(String name, int percentage, TaxRateCategory taxRateCategory, String description, String guid) {
        this.name = name;
        this.percentage = percentage;
        this.taxRateCategory = taxRateCategory;
        this.description = description;
        this.guid = guid;
    }

    public TaxRate(String name, int percentage, TaxRateCategory taxRateCategory, String description) {
        this.name = name;
        this.percentage = percentage;
        this.taxRateCategory = taxRateCategory;
        this.description = description;
    }

    public TaxRate(String name, int percentage, TaxRateCategory taxRateCategory) {
        this.name = name;
        this.percentage = percentage;
        this.taxRateCategory = taxRateCategory;
    }
}
