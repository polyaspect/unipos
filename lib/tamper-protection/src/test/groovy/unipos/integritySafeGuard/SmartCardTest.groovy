package unipos.integritySafeGuard

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import unipos.integritySafeGuard.config.TestConfiguration
import unipos.integritySafeGuard.domain.SignatureInvoice
import unipos.integritySafeGuard.smartCard.SmartCardHandlerTest
import unipos.integritySafeGuard.smartcard.SmartCard
import unipos.integritySafeGuard.smartcard.SmartCardHandler
import unipos.integritySafeGuard.smartcard.SmartCardImpl
import unipos.integritySafeGuard.smartcard.SmartCardMock

import java.time.LocalDateTime

/**
 * Created by domin on 28.12.2016.
 */
@ContextConfiguration(classes = TestConfiguration.class)
class SmartCardTest extends Specification {

    SmartCard smartCard

    @Autowired
    SmartCardFixture smartCardFixture
    @Autowired
    SmartCardHandler smartCardHandler

    def "setup"() {
//        smartCard = SmartCardHandler.attachedSmartCards
        smartCard = smartCardHandler.getSmartCardBySerialNo("670776051039")
    }

    def "Test selectMasterFile"() {
        when:
        smartCard.selectMasterFile()

        then:
        notThrown Exception
    }

    def "Test selectSigApp"() {
        when:
        smartCard.selectDfSigApp()

        then:
        notThrown Exception
    }

    def "Test verifyPin"(){
        when:
        smartCard.verifyPin(smartCardFixture.signatureJob.pin)

        then:
        notThrown Exception
    }

    def "Test signData UngültigeSerialNo"() {
        given:
        String data = "DasHierSindTestDaten"

        when:
        def result = smartCard.signData(data, smartCardFixture.signatureJob)

        then:
        if (smartCard instanceof SmartCardMock)
            result.size() == 0
        if (smartCard instanceof SmartCardImpl)
            result.size() > 0
    }

    def "Test signInvoice"() {
        given:
        def signInvoice = SignatureInvoice.getStartInvoice(smartCardFixture.signatureJob, LocalDateTime.now())

        when:
        def result = smartCard.signInvoice(signInvoice, smartCardFixture.signatureJob)

        then:
        result
        result.signatureBase64Url
        result.signatureBase64Url.size() > 0
    }

    def "Test signInvoice NoSmartCardAvailable"() {
        given:
        def signInvoice = SignatureInvoice.getStartInvoice(smartCardFixture.signatureJob, LocalDateTime.now())
        def sc = smartCardHandler.getSmartCardBySerialNo("test")

        when:
        def result = sc.signInvoice(signInvoice, smartCardFixture.signatureJob)

        then:
        result
        result.signatureBase64Url
        result.signatureBase64Url.size() > 0
        result.signatureBase64Url.equals(Base64.getUrlEncoder().withoutPadding().encodeToString("Sicherheitseinrichtung ausgefallen".getBytes("UTF-8")))
    }

    def "Test signInvoice SmartCardRemoved"() {
        given:
        def signInvoice = SignatureInvoice.getStartInvoice(smartCardFixture.signatureJob, LocalDateTime.now())
        def sc = new SmartCardMock("serialNo")

        when:
        def result = sc.signInvoice(signInvoice, smartCardFixture.signatureJob)

        then:
        result.signatureBase64Url.equals(Base64.getUrlEncoder().withoutPadding().encodeToString("Sicherheitseinrichtung ausgefallen".getBytes("UTF-8")))
    }

    def "Test getCertifiate"() {
        when:
        def certificate = smartCard.getCertificate()

        then:
        if (smartCard instanceof SmartCardImpl) {
            certificate != null
            println certificate.toString()
        }


    }

    def "Test getCertificateSerialDecimal"() {
        when:
        def result = smartCard.getCertificateSerialDecimal()
        println result

        then:
        if (smartCard instanceof SmartCardImpl)
            result
        if(smartCard instanceof SmartCardMock)
            result == null
    }

    def "Test getCertificateSerialHex"() {
        when:
        def result = smartCard.getCertificateSerialHex()
        println result

        then:
        if (smartCard instanceof SmartCardImpl)
            result
        if(smartCard instanceof  SmartCardMock)
            result == null
    }

    def "Test getCertificateSerial2 Valid"() {
        when:
        if(smartCard instanceof SmartCardImpl)
            smartCard = (SmartCardImpl)smartCard
        else
            return

        def result = smartCard.getCertificateSerial2()
        println result

        then:
        result
        result.length() > 0
    }
}
