package unipos.auth.components.right;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Dominik on 27.05.2015.
 */

/**
 * Via the annotation @Data, all members are provided with getters/setters (lombok)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * The @Document annotation declares that this class is a no-sql document in the collection "entities"
 */
@Document(collection = "rights")

@ApiModel(value = "rights")
public class Right implements Serializable, Syncable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @Id
    private String id;

    @ApiModelProperty(required = true)
    private String viewName;
    private String name;
    private String partition;
    private String type;

    private String guid;

    public Right(String name) {
        this.name = name;
        this.guid = UUID.randomUUID().toString();
    }

    public Right(String name, String partition) {
        this.name = name;
        this.partition = partition;
        this.guid = UUID.randomUUID().toString();
    }

    public Right(String id, String name, String guid) {
        this.id = id;
        this.name = name;
        this.guid = guid;
    }
}
