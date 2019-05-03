package unipos.auth.components.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.auth.components.right.Right;
import unipos.common.remote.sync.model.Syncable;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dominik
 */

/**
 * Via the annotation @Data, all members are provided with getters/setters (lombok)
 */
@Data
/**
 * The @Document annotation declares that this class is a no-sql document in the collection "entities"
 */
@Document(collection = "users")

@ApiModel(value = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable, Syncable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(required = true, hidden = true)
    @Id
    private String id;
    //AutoIncrementing Number
    private Long userId;

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private String surname;

    private boolean enabled;

    private String companyGuid;

    private List<Right> rights;
    private String roleGuid;
    private String guid;

    public User() { rights = new ArrayList<>();}

    public User(String name, String surname, boolean enabled) {
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        rights = new ArrayList<>();
    }

    public User(String name, String surname, boolean enabled, Long userId) {
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.userId = userId;
        rights = new ArrayList<>();
    }

    public User(String name, String surname, boolean enabled, Long userId, String companyGuid) {
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.userId = userId;
        rights = new ArrayList<>();
        this.companyGuid = companyGuid;
    }
}
