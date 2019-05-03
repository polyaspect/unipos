package unipos.core.remote.artifactory;

/**
 * @author ggradnig
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderInfo {
    String repo;
    String path;
    List<Folder> children;
}
