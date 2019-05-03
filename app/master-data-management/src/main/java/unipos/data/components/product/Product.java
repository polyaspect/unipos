package unipos.data.components.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;
import unipos.data.components.category.Category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dominik on 23.07.2015.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "products", subTypes = Category.class)
@Document(collection = "products")
public class Product implements Syncable {

    @Id
    @ApiModelProperty(required = true, readOnly = true)
    private String id;

    @ApiModelProperty(required = true)
    private Long number;

    @ApiModelProperty(required = true, readOnly = true)
    private Long productIdentifier;

    @ApiModelProperty(required = true)
    private String name;
    private String description;

    @ApiModelProperty(required = true)
    private BigDecimal price;

    private boolean customPriceInputAllowed;

    @DBRef
    private Category category;

    private List<String> stores;

    private List<String> attributes = new ArrayList<>();

    private int stockAmount = -1;

    private int sortOrder = -1;

    /**
     * This property is just for the sync module relevant, because it makes it possible to recreate the
     */
    private String guid;

    public Product(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(String name, String description, Long number, Category category) {
        this.name = name;
        this.description = description;
        this.number = number;
        this.category = category;
    }

    public Product(String name, String description, BigDecimal price, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Product(String id, Long number, String name, String description, BigDecimal price, Category category) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Product(String id, Long number, Long productIdentifier, String name, String description, BigDecimal price, Category category) {
        this.id = id;
        this.number = number;
        this.productIdentifier = productIdentifier;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Product(String id, Long number, Long productIdentifier, String name, String description, BigDecimal price, Category category, String guid) {
        this.id = id;
        this.number = number;
        this.productIdentifier = productIdentifier;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.guid = guid;
    }

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
