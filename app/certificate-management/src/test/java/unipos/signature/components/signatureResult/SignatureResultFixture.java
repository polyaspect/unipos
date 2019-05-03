package unipos.signature.components.signatureResult;

import unipos.integritySafeGuard.domain.SignatureResult;
import unipos.integritySafeGuard.domain.Type;
import unipos.signature.config.Fixture;

import java.time.LocalDateTime;

/**
 * Created by domin on 22.12.2016.
 */
public class SignatureResultFixture implements Fixture {

    public SignatureResult base1;
    public SignatureResult base2;
    public SignatureResult base3;
    public SignatureResult base4;
    public SignatureResult toInsert1;

    @Override
    public void setUp() {
        base1 = SignatureResult.builder()
                .creationDate(LocalDateTime.of(1994, 11, 5, 0, 35, 0))
                .headerBase64Url("header")
                .invoiceSignatureType(Type.STANDARD)
                .payloadBase64Url("payload")
                .signatureBase64Url("signature")
                .storeGuid("storeGuid")
                .build();

        base2 = SignatureResult.builder()
                .creationDate(LocalDateTime.of(1996, 5, 18, 12, 28, 0))
                .headerBase64Url("header2")
                .invoiceSignatureType(Type.STANDARD)
                .payloadBase64Url("payload2")
                .signatureBase64Url("signature2")
                .storeGuid("storeGuid")
                .build();

        base3 = SignatureResult.builder()
                .creationDate(LocalDateTime.of(1996, 5, 21, 12, 34, 56))
                .headerBase64Url("header3")
                .invoiceSignatureType(Type.STANDARD)
                .payloadBase64Url("payload3")
                .signatureBase64Url("signature3")
                .storeGuid("storeGuid")
                .build();

        base4 = SignatureResult.builder()
                .creationDate(LocalDateTime.of(1995, 1, 1, 12, 34, 56))
                .headerBase64Url("header4")
                .invoiceSignatureType(Type.STANDARD)
                .payloadBase64Url("payload4")
                .signatureBase64Url("signature4")
                .storeGuid("storeGuid")
                .build();

        toInsert1 = SignatureResult.builder()
                .creationDate(LocalDateTime.of(1995, 10, 2, 9, 35, 0))
                .headerBase64Url("header4")
                .invoiceSignatureType(Type.STANDARD)
                .payloadBase64Url("payload4")
                .signatureBase64Url("signature4")
                .storeGuid("storeGuid2")
                .build();
    }

    @Override
    public void tearDown() {

    }
}
