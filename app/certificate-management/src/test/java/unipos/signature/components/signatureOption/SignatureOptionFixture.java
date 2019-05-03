package unipos.signature.components.signatureOption;

import unipos.integritySafeGuard.SmartCardUtils;
import unipos.integritySafeGuard.domain.RksSuite;
import unipos.signature.config.Fixture;

/**
 * Created by domin on 22.12.2016.
 */
public class SignatureOptionFixture implements Fixture {

    public SignatureOption signatureOption1;
    public SignatureOption signatureOption2;

    @Override
    public void setUp() {
        signatureOption1 = SignatureOption.builder()
                .storeGuid("storeGuid")
                .secretKeyPlainText("test")
                .secretKey(SmartCardUtils.getSecretKeyByCustomPw("test"))
                .rksSuite(RksSuite.R1_AT2)
                .kassaId("1")
                .crtSerialNo("12-34-56-78-90-12")
                .signatureDeviceAvailable(true)
                .turnOverCounterLengthInBytes(5)
                .build();

        signatureOption2 = SignatureOption.builder()
                .storeGuid("storeGuid2")
                .secretKeyPlainText("test2")
                .secretKey(SmartCardUtils.getSecretKeyByCustomPw("test2"))
                .rksSuite(RksSuite.R1_AT1)
                .kassaId("2")
                .crtSerialNo("09-87-65-43-21-09")
                .signatureDeviceAvailable(true)
                .turnOverCounterLengthInBytes(5)
                .build();
    }

    @Override
    public void tearDown() {

    }
}
