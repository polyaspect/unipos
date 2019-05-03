package unipos.common.remote.data.model.product;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by dominik on 28.07.15.
 */

@Data
public class TaxRate {

    private String id;
    private String name;
    private String description;
    private int percentage;
    private TaxRateCategory taxRateCategory;

    public TaxRate() {
    }

    public TaxRate(String id, String name, int percentage, TaxRateCategory taxRateCategory, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taxRateCategory = taxRateCategory;
        this.percentage = percentage;
    }
}
