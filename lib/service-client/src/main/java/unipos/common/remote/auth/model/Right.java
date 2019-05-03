package unipos.common.remote.auth.model;

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
 * Via the annotation @Data, all members are provided with getters/setters (lombok)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Right implements Serializable, Syncable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String viewName;
    private String name;
    private String partition;
    private String type;
    private String guid;
}
