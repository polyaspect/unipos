package unipos.common.remote.auth.model;

import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    //AutoIncrementing Number
    private Long userId;
    private String name;
    private String surname;
    private boolean enabled;
    private String companyGuid;
    private List<Right> rights;
    private String roleGuid;
    private String guid;
}
