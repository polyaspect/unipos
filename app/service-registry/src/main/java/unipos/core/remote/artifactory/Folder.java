package unipos.core.remote.artifactory;

/**
 * @author ggradnig
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Folder {
    String uri;
    boolean folder;
}
