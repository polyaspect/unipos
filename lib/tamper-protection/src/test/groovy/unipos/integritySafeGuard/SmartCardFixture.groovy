package unipos.integritySafeGuard

import unipos.integritySafeGuard.domain.RksSuite
import unipos.integritySafeGuard.domain.SignatureInvoice
import unipos.integritySafeGuard.domain.SignatureJob

/**
 * Created by domin on 29.12.2016.
 */
class SmartCardFixture {

    SignatureJob signatureJob

    SmartCardFixture() {
        signatureJob = SignatureJob.builder()
                .pin("123456")
                .belegNr("1")
                .kassaId("1")
                .rksSuite(RksSuite.R1_AT2)
                .secretKey(SmartCardUtils.getSecretKeyByCustomPw("google"))
                .signatureDeviceAvailable(true)
                .turnOverCounterLengthInBytes(5)
                .build()
    }
}
