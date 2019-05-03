package unipos.signature.components.signatureOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unipos.integritySafeGuard.domain.RksSuite;

/**
 * Created by domin on 10.11.2016.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class SignatureOptionDto {

    String id;
    String storeGuid;
    String secretKeyPlainText;
    String kassaId;
    String signaturPin;
    String rksSuite;
    String crtSerialNo;
}
