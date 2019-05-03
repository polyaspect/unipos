package unipos.core.components.licenseSetup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


/**
 * Created by domin on 13.02.2016.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LicenseInfo {

    @Id
    private String id;
    private String activationCode;
    private Long customerId;
    private String companyName;

    @JsonIgnore
    public String getActivationCode() {
        return activationCode;
    }

    @JsonProperty
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
