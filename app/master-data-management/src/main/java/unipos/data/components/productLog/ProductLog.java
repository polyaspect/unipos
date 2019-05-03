package unipos.data.components.productLog;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;
import unipos.data.components.product.Product;
import unipos.data.components.shared.Serialization.LocalDateDeserializer;
import unipos.data.components.shared.Serialization.LocalDateSerializer;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by dominik on 19.08.15.
 */

@Builder
@Document(collection = "productLogs")
@Data
public class ProductLog implements Syncable {

    @Id
    private String _id;
    private Long productIdentifier;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime date;
    private LogAction action;
    private Boolean deleted = false;
    private Boolean published = false;
    private Product product;
    private Boolean ignored = false;
    private String guid;

    public ProductLog() {
    }

    public ProductLog(String _id, Long productIdentifier, LocalDateTime date, LogAction action, Boolean deleted, Boolean published, Product product, Boolean ignored, String guid) {
        this._id = _id;
        this.productIdentifier = productIdentifier;
        this.date = date;
        this.action = action;
        this.deleted = deleted;
        this.published = published;
        this.product = product;
        this.ignored = ignored;
        this.guid = guid;
    }

    @Transient
    public static ProductLog newProductLogFromProduct(Product product) {
        ProductLog productLog = new ProductLog();

        if (product.getProductIdentifier() != null) {
            productLog.setProductIdentifier(product.getProductIdentifier());
        }
        productLog.product = product;


        return productLog;
    }

    @Transient
    public static ProductLog newProductLogFromProductLogGroup(ProductLogGroup productLogGroup) {
        ProductLog productLog = new ProductLog();

        productLog.setProductIdentifier(productLogGroup.getProductIdentifier());
        productLog.setProduct(productLogGroup.getProduct());
        productLog.setPublished(productLogGroup.getPublished());
        productLog.setAction(productLogGroup.getAction());
        productLog.setDate(productLogGroup.getDate());
        productLog.set_id(productLogGroup.getLogId());
        productLog.setDeleted(productLogGroup.getDeleted());
        productLog.setIgnored(productLogGroup.getIgnored());

        return productLog;
    }
}
