package unipos.common.remote.core.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;


/**
 * Created by domin on 13.02.2016.
 */

@Data
@Builder
public class LicenseInfo {

    private String id;
    private String activationCode;
    private Long customerId;

    public LicenseInfo() {
    }

    public LicenseInfo(String id, String activationCode, Long customerId) {
        this.id = id;
        this.activationCode = activationCode;
        this.customerId = customerId;
    }
}
