package unipos.licenseChecker.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by domin on 05.03.2016.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivationEntity {

    private String activationCode;
    private String computerId;
}
