package unipos.common.remote.core.model;

import lombok.Data;

/**
 * @author ggradnig
 */
@Data
public class Module {
    private String name;
    private String context;
    private ModuleStatus status;

    public Module(){

    }
}
