package unipos.integritySafeGuard.system

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.IgnoreIf
import spock.lang.Specification
import unipos.integritySafeGuard.smartcard.SmartCardHandler
import unipos.integritySafeGuard.SmartCardUtils
import unipos.integritySafeGuard.config.TestConfiguration
import unipos.integritySafeGuard.domain.RksSuite
import unipos.integritySafeGuard.domain.SignatureInvoice
import unipos.integritySafeGuard.domain.SignatureJob
import unipos.integritySafeGuard.domain.SignatureResult

import java.time.LocalDateTime

/**
 * Created by domin on 03.10.2016.
 */
@IgnoreIf({!Boolean.valueOf(env['SignerConnected'] as String)})
@ContextConfiguration(classes = TestConfiguration.class)
class SystemTest extends Specification {

    @Autowired
    SmartCardHandler smartCardHandler

    String signaturePin = "308544"
    String puk = "59184812"

    def "Test InvoiceChain"() {
        given:
        def smartCard = smartCardHandler.connectToCardReaderByRksSerialNr("02-04-04-de-00-10-1c-12")

        SignatureJob signatureJob = SignatureJob.builder().
                secretKey(SmartCardUtils.getSecretKeyByCustomPw("MyCustomPw")).
                belegNr("1").
                kassaId("AT12345345").
                rksSuite(RksSuite.R1_AT2).
                signatureDeviceAvailable(true).
                build()

        def now = LocalDateTime.now()

        SignatureInvoice signatureInvoice = SignatureInvoice.getStartInvoice(signatureJob, now)

        when:
        smartCard.setSignaturPin(signaturePin)
        def result = smartCard.signInvoice(signatureInvoice, signatureJob)

        then:
        result.toJwsCompactRepresentation()
        println result.toJwsCompactRepresentation()
    }

    def "Test SignatureChain"() {
        given:
        def smartCard = smartCardHandler.connectToCardReaderByRksSerialNr("02-04-04-de-00-10-1c-12")

        SignatureResult signatureResult = SignatureResult.builder()
                .headerBase64Url("eyJhbGciOiJFUzI1NiJ9")
                .payloadBase64Url("X1IxLUFUMl9BVDEyMzQ1MzQ1XzFfMjAxNi0xMC0wM1QxMjoyMjozNC4zOTdfMCwwMF8wLDAwXzAsMDBfMCwwMF8wLDAwX3hyanpzeE09XzAxYzI3ZWU2MjYxMDNjZGJkNmJhNDczNDU3XzZuTHNEcWpSa0tFPQ==")
                .signatureBase64Url("fNg31iZw39SpOi1iSRKoZEwVmwXc-8cXQ5QYgi19zELR-eiXkwNoV8c29hzt2V58qhvm_EyvqVIs1LMI3aOC8g==")
                .build()

        println signatureResult.toJwsCompactRepresentation()

        SignatureJob signatureJob = SignatureJob.builder().
                secretKey(SmartCardUtils.getSecretKeyByCustomPw("MyCustomPw")).
                belegNr("2").
                kassaId("AT12345345").
                rksSuite(RksSuite.R1_AT2).
                signatureDeviceAvailable(true).
                build()

        SignatureInvoice signatureInvoice = SignatureInvoice.getInvoice(
                signatureJob,
                LocalDateTime.now(),
                new BigDecimal("19.99"),
                new BigDecimal("0"),
                new BigDecimal("0"),
                new BigDecimal("0"),
                new BigDecimal("0"),
                1999 as long,
                signatureResult
        )

        smartCard.setSignaturPin(signaturePin)

        when:
        def result = smartCard.signInvoice(signatureInvoice, signatureJob)

        then:
        result
        result.toJwsCompactRepresentation()
        println result.toJwsCompactRepresentation()
    }
}
