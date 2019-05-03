package unipos.common.remote.data.model.product;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 23.07.2015.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String id;
    private Long number;
    private Long productIdentifier;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean customPriceInputAllowed;
    private Category category;
    private List<String> stores;
    private String guid;
    private int stockAmount;
    private List<String> attributes  = new ArrayList<>();

    public Product(String id, Long number, Long productIdentifier, String name, String description, BigDecimal price, boolean customPriceInputAllowed, Category category, List<String> stores, String guid) {
        this.id = id;
        this.number = number;
        this.productIdentifier = productIdentifier;
        this.name = name;
        this.description = description;
        this.price = price;
        this.customPriceInputAllowed = customPriceInputAllowed;
        this.category = category;
        this.stores = stores;
        this.guid = guid;
    }
}
