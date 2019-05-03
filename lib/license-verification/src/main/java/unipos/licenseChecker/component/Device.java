package unipos.licenseChecker.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by domin on 05.03.2016.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    private String computerId;
    private List<String> moduleVersions = new ArrayList<>();
    private String currentInstalledVersion;
    private String minimumAvailableVersion;
    private String maximumAvailableVersion;
    private boolean forceUpdate;

    public String getLatestVersion() {
        return moduleVersions.stream().filter(x -> x != null).sorted(new VersionComparator()).sorted(Comparator.reverseOrder()).findFirst().orElse(null);
    }
}
