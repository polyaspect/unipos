package unipos.licenseChecker.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by domin on 12.03.2016.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LicenseInfoEntity {

    private Long customerNumber;
    private String companyName;

    public String getEncryptionPw() {
        String encryptionSha256 = HashGeneratorUtils.generateSHA256(this.getCompanyName() + "_" + this.getCustomerNumber());

        return HashGeneratorUtils.generateMD5(this.getCompanyName() + "_" + this.getCustomerNumber() + "_" + encryptionSha256);
    }
}