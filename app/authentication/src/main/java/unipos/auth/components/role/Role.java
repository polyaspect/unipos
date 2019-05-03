package unipos.auth.components.role;

/**
 * @author Dominik
 */

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.auth.components.right.Right;
import unipos.common.remote.sync.model.Syncable;

import javax.persistence.Id;
import java.util.List;

/**
 * Via the annotation @Data, all members are provided with getters/setters (lombok)
 */
@Data
@Builder

/**
 * The @Document annotation declares that this class is a no-sql document in the collection "entities"
 */
@Document(collection = "roles")

@ApiModel(value = "roles")
public class Role implements Syncable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @Id
    private String id;

    @ApiModelProperty(required = true)
    private String name;

    private List<Right> rights;

    private String guid;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public Role(String id, String name, List<Right> rights, String guid) {
        this.id = id;
        this.name = name;
        this.rights = rights;
        this.guid = guid;
    }
}
