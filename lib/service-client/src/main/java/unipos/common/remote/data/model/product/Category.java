package unipos.common.remote.data.model.product;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Created by Dominik on 23.07.2015.
 */

@Data
@Builder
public class Category {

    private String id;
    private String name;
    private TaxRate taxRate;

    public Category() {}

    public Category(String id, String name, TaxRate taxRate) {
        this.id = id;
        this.name = name;
        this.taxRate = taxRate;
    }
}
