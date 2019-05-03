package unipos.common.remote.signature.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by domin on 14.01.2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "signatureResults")
public class SignatureResult {

    @Id
    String id;
    String headerBase64Url;
    String payloadBase64Url;
    String signatureBase64Url;
    Type invoiceSignatureType;
    LocalDateTime creationDate;
    String storeGuid;
    String umsatzZaehlerGuid;

    public enum Type {
        STANDARD(false, "Standardbeleg"),
        STORNO(false, "Stornobeleg"),
        TRAINING(false, "Trainingsbeleg"),
        NULL(false, "Nullbeleg"),
        START(true, "Startbeleg"),
        SAMMEL(true, "Sammelbeleg"),
        SCHLUSS(false, "Schlussbeleg"),
        MONATS(false, "Monatsbeleg"),
        JAHRES(true, "Jahresbeleg");

        boolean signatureRequired;
        String name;

        Type(boolean signatureRequired, String name) {
            this.signatureRequired = signatureRequired;
            this.name = name;
        }

        public boolean isSignatureRequired() {
            return signatureRequired;
        }

        public String getName() {
            return name;
        }
    }
}
