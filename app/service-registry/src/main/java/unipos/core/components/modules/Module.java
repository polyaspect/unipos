package unipos.core.components.modules;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author ggradnig
 */
@Data
@Builder
public class Module {
    private String context;
    private String name;
    private ModuleStatus status;
    private String installedVersion;

}
