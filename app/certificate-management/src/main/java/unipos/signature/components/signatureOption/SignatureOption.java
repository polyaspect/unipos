package unipos.signature.components.signatureOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.integritySafeGuard.SmartCardUtils;
import unipos.integritySafeGuard.domain.RksSuite;
import unipos.integritySafeGuard.domain.SignatureJob;

import javax.crypto.SecretKey;

/**
 * Created by domin on 03.11.2016.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "signatureOptions")
public class SignatureOption {

    @Id
    String id;
    String storeGuid;
    String secretKeyPlainText;
    @Transient
    @JsonIgnore
    SecretKey secretKey;
    String kassaId;
    RksSuite rksSuite;
    String crtSerialNo;
    String signaturPin;
    int turnOverCounterLengthInBytes;
    boolean signatureDeviceAvailable = true;
    String SignatureCertificateDer;

    public SignatureJob signatureJob(String belegNr) {
        SignatureJob signatureJob = new SignatureJob();
        signatureJob.setBelegNr(belegNr);
        signatureJob.setKassaId(kassaId);
        signatureJob.setTurnOverCounterLengthInBytes(turnOverCounterLengthInBytes);
        signatureJob.setRksSuite(rksSuite);
        signatureJob.setSignatureDeviceAvailable(signatureDeviceAvailable);
        signatureJob.setSecretKey(SmartCardUtils.getSecretKeyByCustomPw(secretKeyPlainText));
        signatureJob.setPin(signaturPin);

        return signatureJob;
    }

    public SecretKey getSecretKey() {
        return SmartCardUtils.getSecretKeyByCustomPw(secretKeyPlainText);
    }
}
