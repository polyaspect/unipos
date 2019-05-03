package unipos.signature.components.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by domin on 14.01.2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncryptionDetailsDto {

    private String pwPlainText;
    private String encodedPassword;
    private String verificationValue;
    private String certificateSerialNrDec;
    private String certificateSerialNrHex;
}
