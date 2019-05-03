package unipos.licenseChecker.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 13.02.2016.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseVerification {

    private String companyName;
    private String companyUid;
    private String companyCountry;
    private Long customerId;
    private List<Module> modules = new ArrayList<>();
    private String hash;
    private String activationCode;
}
